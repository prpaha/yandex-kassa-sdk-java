package ru.prpaha.yandex.kassa.service;

public class Constants {

    private static final String YANDEX_KASSA_URL = "https://payment.yandex.net/api/v3/";
    public static final String CREATE_PAYMENT_URL = YANDEX_KASSA_URL + "payments";
    public static final String GET_PAYMENT_URL = YANDEX_KASSA_URL + "payments/%s";

}
