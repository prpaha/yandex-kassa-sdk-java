package ru.prpaha.yandex.kassa.service;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import ru.prpaha.yandex.kassa.domain.Currency;
import ru.prpaha.yandex.kassa.domain.Error;
import ru.prpaha.yandex.kassa.domain.Payment;
import ru.prpaha.yandex.kassa.exception.YandexKassaException;
import ru.prpaha.yandex.kassa.request.Amount;
import ru.prpaha.yandex.kassa.request.CreatePaymentRequest;
import ru.prpaha.yandex.kassa.request.IConfirmation;
import ru.prpaha.yandex.kassa.utils.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class YandexKassaClient {

    private Logger log = Logger.getLogger(YandexKassaClient.class.getName());

    private static final String USER_AGENT_YANDEX_KASSA_SDK = "yandex-kassa-sdk";

    private CloseableHttpClient client;

    private YandexKassaClient(final String shopId, final String secretKey, boolean logEnabled) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(shopId, secretKey);
        provider.setCredentials(AuthScope.ANY, credentials);

        client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    public Payment createPayment(BigDecimal amount, Currency currency, String paymentToken)
            throws YandexKassaException {
        try {
            return createPayment(amount, currency, null, null, null, paymentToken,
                    null, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Error error = new Error();
            error.setDescription("Ошибка отправки запроса");
            throw new YandexKassaException(error);
        }
    }

    public Payment getPayment(String paymentId) throws YandexKassaException {
        if (StringUtils.isBlank(paymentId)) {
            throw new RuntimeException("invalid paymentId");
        }

        HttpGet get = new HttpGet(String.format(Constants.GET_PAYMENT_URL, paymentId));

        try (CloseableHttpResponse response = client.execute(get)) {
            String getPaymentResult = IOUtils.toString(response.getEntity().getContent());
            log.info("Request from Yandex.Kassa: " + getPaymentResult);
            Gson gson = new Gson();
            if (response.getStatusLine().getStatusCode() == 200) {
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
            throws YandexKassaException, UnsupportedEncodingException {
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

        HttpPost post = new HttpPost(Constants.CREATE_PAYMENT_URL);
        post.setHeader("Idempotence-Key", idempotenceKey);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        post.setEntity(entity);

        try (CloseableHttpResponse response = client.execute(post)) {
            String createPaymentResult = IOUtils.toString(response.getEntity().getContent());
            log.info("Request from Yandex.Kassa: " + createPaymentResult);
            if (response.getStatusLine().getStatusCode() == 200) {
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
