package ru.prpaha.yandex.kassa.service;

import com.google.gson.Gson;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import ru.prpaha.yandex.kassa.domain.Currency;
import ru.prpaha.yandex.kassa.domain.Error;
import ru.prpaha.yandex.kassa.domain.Payment;
import ru.prpaha.yandex.kassa.exception.YandexKassaException;
import ru.prpaha.yandex.kassa.request.Amount;
import ru.prpaha.yandex.kassa.request.CreatePaymentRequest;
import ru.prpaha.yandex.kassa.request.IConfirmation;
import ru.prpaha.yandex.kassa.utils.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class YandexKassaClient {

    private Logger log = Logger.getLogger(YandexKassaClient.class.getName());

    private static final String USER_AGENT_YANDEX_KASSA_SDK = "yandex-kassa-sdk";
    private static final MediaType JSON = MediaType.get("application/json");

    private OkHttpClient client;

    private YandexKassaClient(final String shopId, final String secretKey, boolean logEnabled) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    String credentials = Credentials.basic(shopId, secretKey);
                    return response.request().newBuilder()
                            .header("Authorization", credentials)
                            .header("User-Agent", USER_AGENT_YANDEX_KASSA_SDK)
                            .build();
                });

        if (logEnabled) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addNetworkInterceptor(logging);
        }

        client = clientBuilder.build();
    }

    public Payment createPayment(BigDecimal amount, Currency currency, String paymentToken)
            throws YandexKassaException {
        return createPayment(amount, currency, null, null, null, paymentToken,
                null, null);
    }

    public Payment getPayment(String paymentId) throws YandexKassaException {
        if (StringUtils.isBlank(paymentId)) {
            throw new RuntimeException("invalid paymentId");
        }

        Request request = new Request.Builder()
                .url(String.format(Constants.GET_PAYMENT_URL, paymentId))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String getPaymentResult = response.body().string();
            log.info("Request from Yandex.Kassa: " + getPaymentResult);
            Gson gson = new Gson();
            if (response.isSuccessful()) {
                return gson.fromJson(getPaymentResult, Payment.class);
            } else {
                Error error = gson.fromJson(getPaymentResult, Error.class);
                throw new YandexKassaException(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Payment createPayment(BigDecimal amount, Currency currency, Boolean capture, String description,
                                 IConfirmation confirmation, String paymentToken, String idempotenceKey,
                                 Map<String, Object> metaData)
            throws YandexKassaException {
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new RuntimeException("invalid amount");
        }
        if (currency == null) {
            throw new RuntimeException("invalid currency");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 128) {
            throw new RuntimeException("description length must be max 128 characters");
        }
        if (metaData != null && metaData.size() > 16) {
            throw  new RuntimeException("invalid metadata, length must be max 16");
        }

        Amount amountObj = new Amount(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), currency);
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest(amountObj, capture, confirmation, description,
                paymentToken, metaData);

        Gson gson = new Gson();
        String json = gson.toJson(paymentRequest);

        log.info("Create Yandex.Kassa payment request: " + json);

        if (StringUtils.isBlank(idempotenceKey)) {
            idempotenceKey = UUID.randomUUID().toString();
        }

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Constants.CREATE_PAYMENT_URL)
                .header("Idempotence-Key", idempotenceKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String createPaymentResult = response.body().string();
            log.info("Request from Yandex.Kassa: " + createPaymentResult);
            if (response.isSuccessful()) {
                return gson.fromJson(createPaymentResult, Payment.class);
            } else {
                Error error = gson.fromJson(createPaymentResult, Error.class);
                throw new YandexKassaException(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class Builder {

        private String shopId;
        private String secretKey;
        private boolean logEnabled = false;

        public Builder shopId(final String shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder secretKey(final String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder logEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        public YandexKassaClient build() {
            if (StringUtils.isBlank(shopId)) {
                throw new RuntimeException("invalid shopId");
            }
            if (StringUtils.isBlank(secretKey)) {
                throw new RuntimeException("invalid secretKey");
            }

            return new YandexKassaClient(shopId, secretKey, logEnabled);
        }

    }

}
