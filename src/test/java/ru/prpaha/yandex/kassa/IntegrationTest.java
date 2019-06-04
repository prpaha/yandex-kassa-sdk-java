package ru.prpaha.yandex.kassa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.prpaha.yandex.kassa.domain.Currency;
import ru.prpaha.yandex.kassa.domain.Payment;
import ru.prpaha.yandex.kassa.exception.YandexKassaException;
import ru.prpaha.yandex.kassa.request.ConfirmationRedirect;
import ru.prpaha.yandex.kassa.request.ConfirmationType;
import ru.prpaha.yandex.kassa.request.IConfirmation;
import ru.prpaha.yandex.kassa.service.YandexKassaClient;

import java.math.BigDecimal;

class IntegrationTest {

    private static final String YANDEX_API_SECRET_KEY = "test_fp7UGpWToO9-3NUfHlGNZtxOgNRUkrHYdGYQzLPGGOQ";
    private static final String YANDEX_SHOP_ID = "610039";

    @Test
    void createKassaClientFail() {
        try {
            new YandexKassaClient.Builder().build();
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }
    }

    @Test
    void makeRequestFail() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        boolean capture = true;
        String description = "Описание платежа";
        IConfirmation confirmation = new ConfirmationRedirect(ConfirmationType.redirect, "http://test.ts");
        String paymentToken = "paymentToken";

        try {
            kassaClient.createPayment(null, currency, capture, description, confirmation, paymentToken, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        try {
            kassaClient.createPayment(new BigDecimal(0), currency, capture, description, confirmation, paymentToken, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        try {
            kassaClient.createPayment(new BigDecimal(-1), currency, capture, description, confirmation, paymentToken, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        try {
            kassaClient.createPayment(amount, null, capture, description, confirmation, paymentToken, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        StringBuilder descriptionBuilder = new StringBuilder();
        for (int i = 0; i < 129; i++) {
            descriptionBuilder.append("a");
        }

        description = descriptionBuilder.toString();
        try {
            kassaClient.createPayment(amount, currency, capture, description, confirmation, paymentToken, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }
    }

    @Test
    void makeRequest() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        boolean capture = true;
        String description = "Описание платежа";
        IConfirmation confirmation = new ConfirmationRedirect(ConfirmationType.redirect, "https://www.merchant-website.com/return_url");
        Payment payment = null;
        try {
            payment = kassaClient.createPayment(amount, currency, capture, description, confirmation, null, null);
        } catch (YandexKassaException e) {
            e.printStackTrace();
        }

        Assertions.assertNotNull(payment);
    }

}
