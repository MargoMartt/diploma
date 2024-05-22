package bppp.practice.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CART("В корзине"),

    WAIT_PAYMENT("Ожидается оплата"),

    WAITING("Заказ в листе ожидания"),

    MANUFACTURING("В процессе производства"),

    DELIVERING("Заказ доставляется"),

    DELIVERED("Заказ доставлен");

    private String status;

    private OrderStatus(String status) {
        this.status = status;
    }

}
