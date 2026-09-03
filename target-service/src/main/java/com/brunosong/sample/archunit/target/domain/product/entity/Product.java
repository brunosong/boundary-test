package com.brunosong.sample.archunit.target.domain.product.entity;

public class Product {

    private final String sku;
    private final String name;
    private final long price;
    private final boolean sellable;

    public Product(String sku, String name, long price, boolean sellable) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.sellable = sellable;
    }

    public boolean isSellable() {
        return sellable;
    }

    public String sku() {
        return sku;
    }

    public String name() {
        return name;
    }

    public long price() {
        return price;
    }
}
