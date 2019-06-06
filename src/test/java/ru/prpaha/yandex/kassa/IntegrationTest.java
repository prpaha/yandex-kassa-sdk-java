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
import java.util.HashMap;
import java.util.Map;

class IntegrationTest {

    private static final String YANDEX_API_SECRET_KEY = "test_fp7UGpWToO9-3NUfHlGNZtxOgNRUkrHYdGYQzLPGGOQ";
    private static final String YANDEX_SHOP_ID = "610039";

    @Test
    void createKassaClientFailTest() {
        try {
            new YandexKassaClient.Builder().build();
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }
    }

    @Test
    void makeRequestFailTest() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .logEnabled(true)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        boolean capture = true;
        String description = "Описание платежа";
        IConfirmation confirmation = new ConfirmationRedirect(ConfirmationType.redirect, "http://test.ts");
        String paymentToken = "paymentToken";

        try {
            kassaClient.createPayment(null, currency, capture, description, confirmation, paymentToken, null, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        try {
            kassaClient.createPayment(new BigDecimal(0), currency, capture, description, confirmation, paymentToken, null, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        try {
            kassaClient.createPayment(new BigDecimal(-1), currency, capture, description, confirmation, paymentToken, null, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        try {
            kassaClient.createPayment(amount, null, capture, description, confirmation, paymentToken, null, null);
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
            kassaClient.createPayment(amount, currency, capture, description, confirmation, paymentToken, null, null);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }

        Map<String, Object> metadata = new HashMap<>(17);
        for (int i = 0; i < 17; i++) {
            metadata.put(String.valueOf(i), i);
        }

        try {
            kassaClient.createPayment(amount, currency, capture, description, confirmation, paymentToken, null, metadata);
            Assertions.fail();
        } catch (YandexKassaException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            // Success case
        }
    }

//    @Test
    void makeSimpleRequestSuccessTest() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .logEnabled(true)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        String paymentToken = "test_payment_token";
        Payment payment = null;
        try {
            payment = kassaClient.createPayment(amount, currency, paymentToken);
        } catch (YandexKassaException e) {
            e.printStackTrace();
        }

        Assertions.assertNotNull(payment);
        Assertions.assertEquals(payment.getAmount().getCurrency(), currency);
        Assertions.assertTrue(new BigDecimal(payment.getAmount().getValue()).compareTo(amount) == 0);
    }

    @Test
    void makeFullRequestSuccessTest() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .logEnabled(true)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        boolean capture = true;
        String description = "Описание платежа";
        IConfirmation confirmation = new ConfirmationRedirect(ConfirmationType.redirect, "https://www.merchant-website.com/return_url");
        Payment payment = null;
        try {
            payment = kassaClient.createPayment(amount, currency, capture, description, confirmation, null, null, null);
        } catch (YandexKassaException e) {
            e.printStackTrace();
        }

        Assertions.assertNotNull(payment);
        Assertions.assertEquals(payment.getAmount().getCurrency(), currency);
        Assertions.assertTrue(new BigDecimal(payment.getAmount().getValue()).compareTo(amount) == 0);
        Assertions.assertEquals(payment.getDescription(), description);
    }

    @Test
    void getPaymentTest() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .logEnabled(true)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        boolean capture = true;
        String description = "Описание платежа";
        IConfirmation confirmation = new ConfirmationRedirect(ConfirmationType.redirect, "https://www.merchant-website.com/return_url");
        Payment payment = null;
        try {
            payment = kassaClient.createPayment(amount, currency, capture, description, confirmation, null, null, null);
        } catch (YandexKassaException e) {
            e.printStackTrace();
        }

        Payment checkPayment = null;
        try {
            checkPayment = kassaClient.getPayment(payment.getId());
        } catch (YandexKassaException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(checkPayment);
        Assertions.assertEquals(checkPayment.getId(), payment.getId());
        Assertions.assertEquals(checkPayment.getAmount().getCurrency(), currency);
        Assertions.assertTrue(new BigDecimal(checkPayment.getAmount().getValue()).compareTo(amount) == 0);
        Assertions.assertEquals(checkPayment.getDescription(), description);
    }

    @Test
    void getPaymentFailTest() {
        YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .logEnabled(true)
                .build();

        BigDecimal amount = new BigDecimal("100");
        Currency currency = Currency.RUB;
        boolean capture = true;
        String description = "Описание платежа";
        IConfirmation confirmation = new ConfirmationRedirect(ConfirmationType.redirect, "https://www.merchant-website.com/return_url");
        Payment payment = null;
        try {
            payment = kassaClient.createPayment(amount, currency, capture, description, confirmation, null, null, null);
        } catch (YandexKassaException e) {
            e.printStackTrace();
        }

        Payment checkPayment = null;
        try {
            checkPayment = kassaClient.getPayment(payment.getId() + "0");
            Assertions.fail();
        } catch (YandexKassaException e) {
            e.printStackTrace();
            // Success case
        }
    }

}
