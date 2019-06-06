package ru.prpaha.yandex.kassa.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class PaymentNotification implements Serializable {

    private String type;
    private String event;
    private Payment object;

}
