package ru.prpaha.yandex.kassa.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@EqualsAndHashCode
public class Metadata extends HashMap<String, Object> implements Serializable {

}
