package ru.prpaha.yandex.kassa.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.prpaha.yandex.kassa.domain.Currency;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Amount implements Serializable {

    private String value;
    private Currency currency;

}
