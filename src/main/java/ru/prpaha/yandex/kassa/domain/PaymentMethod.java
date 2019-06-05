package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentMethod implements Serializable {

    private String type;
    private String id;

}
