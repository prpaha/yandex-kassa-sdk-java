package ru.prpaha.yandex.kassa.request;

import java.io.Serializable;

public interface IConfirmation extends Serializable {

    ConfirmationType getType();

}
