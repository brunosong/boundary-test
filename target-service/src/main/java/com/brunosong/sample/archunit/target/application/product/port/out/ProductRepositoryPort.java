package com.brunosong.sample.archunit.target.application.product.port.out;

import com.brunosong.sample.archunit.target.domain.product.entity.Product;

import java.util.Optional;

public interface ProductRepositoryPort {

    Optional<Product> findBySku(String sku);
}
