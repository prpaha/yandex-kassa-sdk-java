package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public class Confirmation implements Serializable {

    private String type;
    private String confirmationUrl;

}
