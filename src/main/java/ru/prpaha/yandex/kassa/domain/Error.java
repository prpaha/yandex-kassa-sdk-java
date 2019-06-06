package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
public class Error implements Serializable {

    private String type;
    private String id;
    private String code;
    private String description;
    private String parameter;

    public void setDescription(String description) {
        this.description = description;
    }

}
