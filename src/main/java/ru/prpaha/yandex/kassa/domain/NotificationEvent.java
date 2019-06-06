package ru.prpaha.yandex.kassa.domain;

public class NotificationEvent {

    public static final String PAYMENT_SUCCESS = "payment.succeeded";
    public static final String PAYMENT_WAITNIG = "payment.waiting_for_capture";
    public static final String REFUND_SUCCESS = "refund.succeeded";
    public static final String PAYMENT_CANCELED = "payment.canceled";

}
