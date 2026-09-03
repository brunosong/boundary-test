package com.brunosong.sample.archunit.target.dataaccess.product.persistence;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ProductJpaRepository extends Repository<ProductJpaEntity, String> {

    Optional<ProductJpaEntity> findBySku(String sku);
}
