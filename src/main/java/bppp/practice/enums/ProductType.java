package bppp.practice.enums;

import lombok.Getter;

@Getter
public enum ProductType {
    PLASTIC("Пластмассовая продукция"),

    POLYMER("Полимерная продукция"),

    FILM("Пленочная продукция"),

    HOUSEHOLD("Бытовая химия"),

    REGRANULATE("Регранулят"),

    SHAPES("Пресс-форма");

    private String code;

     ProductType(String code) {
        this.code = code;
    }

}
