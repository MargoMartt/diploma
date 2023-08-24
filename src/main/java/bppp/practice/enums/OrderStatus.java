package bppp.practice.enums;

public enum OrderStatus {
    CART("In cart"),

    WAITING("Order in waiting list"),

    MANUFACTURING("In the manufacturing process"),

    DELIVERING("Order is delivering"),

    DELIVERED("Order delivered");

    private String status;

    private OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
