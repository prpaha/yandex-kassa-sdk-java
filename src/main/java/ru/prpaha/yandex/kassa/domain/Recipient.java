package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class Recipient implements Serializable {

    private String accountId;
    private String gatewayId;
    private Map<String, Object> additionalProperties;

}
