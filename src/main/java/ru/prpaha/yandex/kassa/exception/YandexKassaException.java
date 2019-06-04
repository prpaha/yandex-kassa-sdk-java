package ru.prpaha.yandex.kassa.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.prpaha.yandex.kassa.domain.Error;

@Getter
@AllArgsConstructor
public class YandexKassaException extends Exception {

    private Error error;

}
