package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public class Amount implements Serializable {

    private String value;
    private Currency currency;

}
