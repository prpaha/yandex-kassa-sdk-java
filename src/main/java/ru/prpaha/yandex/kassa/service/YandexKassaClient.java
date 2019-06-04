package ru.prpaha.yandex.kassa.service;

import com.google.gson.Gson;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import ru.prpaha.yandex.kassa.domain.Currency;
import ru.prpaha.yandex.kassa.domain.Error;
import ru.prpaha.yandex.kassa.domain.Payment;
import ru.prpaha.yandex.kassa.exception.YandexKassaException;
import ru.prpaha.yandex.kassa.request.Amount;
import ru.prpaha.yandex.kassa.request.CreatePaymentRequest;
import ru.prpaha.yandex.kassa.request.IConfirmation;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.logging.Logger;

public class YandexKassaClient {

    private Logger log = Logger.getLogger(YandexKassaClient.class.getName());

    private static final MediaType JSON = MediaType.get("application/json");

    private OkHttpClient client;

    private YandexKassaClient(final String shopId, final String secretKey) {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(logging)
                .authenticator((route, response) -> {
                    String credentials = Credentials.basic(shopId, secretKey);
                    return response.request().newBuilder()
                            .header("Authorization", credentials)
                            .build();
                })
                .build();
    }

    public Payment createPayment(BigDecimal amount, Currency currency, boolean capture, String description,
                                 IConfirmation confirmation, String paymentToken, String idempotenceKey) throws YandexKassaException {
        if (StringUtils.isNotBlank(description) && description.length() > 128) {
            throw new RuntimeException("description must be not biggest 128 characters");
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new RuntimeException("invalid amount");
        }
        if (currency == null) {
            throw new RuntimeException("invalid currency");
        }

        Amount amountObj = new Amount(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), currency);
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest(amountObj, capture, confirmation, description, paymentToken);

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

        public Builder shopId(final String shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder secretKey(final String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public YandexKassaClient build() {
            if (StringUtils.isBlank(shopId)) {
                throw new RuntimeException("invalid shopId");
            }
            if (StringUtils.isBlank(secretKey)) {
                throw new RuntimeException("invalid secretKey");
            }

            return new YandexKassaClient(shopId, secretKey);
        }

    }

}
