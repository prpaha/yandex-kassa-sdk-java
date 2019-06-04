package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class Confirmation implements Serializable {

    private String type;
    private String confirmationUrl;
    private Map<String, Object> additionalProperties;

}
