package com.brunosong.sample.modulith.product.domain;

import java.time.LocalDateTime;

/**
 * 상품 애그리거트. internal 이라 다른 모듈이 못 본다.
 *
 * stopSelling 같은 상태 변경 메서드가 있다는 것이 이 타입을 공개하면 안 되는 이유다.
 * 밖에는 Summary 나 Detail 로 바꿔서 내준다.
 */
public class Product {

    private final String productCode;
    private final String name;
    private final long price;
    private final LocalDateTime registeredAt;
    private boolean sellable;

    public Product(String productCode, String name, long price, LocalDateTime registeredAt, boolean sellable) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.registeredAt = registeredAt;
        this.sellable = sellable;
    }

    public void stopSelling() {
        this.sellable = false;
    }

    public String productCode() {
        return productCode;
    }

    public String name() {
        return name;
    }

    public long price() {
        return price;
    }

    public LocalDateTime registeredAt() {
        return registeredAt;
    }

    public boolean isSellable() {
        return sellable;
    }
}
