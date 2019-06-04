package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class Metadata implements Serializable {

    private Map<String, Object> additionalProperties;

}
