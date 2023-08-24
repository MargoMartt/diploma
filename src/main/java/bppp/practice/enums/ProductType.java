package bppp.practice.enums;

public enum ProductType {
    PLASTIC("Plastic products"),

    POLYMER("Polymer products"),

    FILM("Film products"),

    HOUSEHOLD("Household chemicals"),

    REGRANULATE("Regranulate"),

    SHAPES("Press shapes");

    private String code;

    private ProductType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
