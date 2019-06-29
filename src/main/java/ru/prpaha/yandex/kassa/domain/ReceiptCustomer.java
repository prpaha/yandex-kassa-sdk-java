package ru.prpaha.yandex.kassa.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import ru.prpaha.yandex.kassa.utils.StringUtils;

@Getter
public class ReceiptCustomer {

    private String phone;
    private String email;
    @SerializedName("full_name")
    private String fullName;
    private String inn;

    public ReceiptCustomer(String phone, String email, String fullName, String inn) {
        if (StringUtils.isBlank(phone) && StringUtils.isBlank(email)) {
            new RuntimeException("phone or email can be fill");
        }
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(email)) {
            new RuntimeException("only one param can be not null");
        }

        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
        this.inn = inn;
    }

}
