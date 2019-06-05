package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public class Recipient implements Serializable {

    private String accountId;
    private String gatewayId;

}
