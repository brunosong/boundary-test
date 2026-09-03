package com.brunosong.sample.modulith.product.dataaccess;

import com.brunosong.sample.modulith.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** 예제라 DB 대신 메모리에 담는다. internal 이라 다른 모듈이 못 본다. */
@Repository
public class ProductRepository {

    private final Map<String, Product> store = new ConcurrentHashMap<>();

    public ProductRepository() {
        save(new Product("SKU-1", "샘플 상품", 12000, LocalDateTime.now(), true));
        save(new Product("SKU-2", "단종 상품", 9900, LocalDateTime.now(), false));
    }

    public void save(Product product) {
        store.put(product.productCode(), product);
    }

    public Optional<Product> findByCode(String productCode) {
        return Optional.ofNullable(store.get(productCode));
    }

    public List<Product> findSellable() {
        return store.values().stream().filter(Product::isSellable).toList();
    }
}
