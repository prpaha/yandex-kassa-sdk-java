package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public class Error implements Serializable {

    private String type;
    private String id;
    private String code;
    private String description;
    private String parameter;

}
