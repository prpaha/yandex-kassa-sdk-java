package ru.prpaha.yandex.kassa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Receipt {

    private ReceiptCustomer customer;
    private List<ReceiptItem> items;

}
