package ru.prpaha.yandex.kassa.domain;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class PaymentNotification implements Serializable {

    private String type;
    private String event;
    private Payment object;

}
