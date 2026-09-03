package com.brunosong.sample.modulith.product.application;

import com.brunosong.sample.modulith.product.ProductLookupUseCase;
import com.brunosong.sample.modulith.product.domain.Product;
import com.brunosong.sample.modulith.product.dataaccess.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 공개 계약의 구현. 구현은 internal 에 둔다.
 *
 * 다른 모듈은 ProductLookupUseCase 인터페이스만 주입받고 이 클래스는 못 본다.
 * 중첩 타입은 ProductLookupUseCase.Summary 처럼 바깥 클래스를 거쳐 부른다.
 */
@Service
@RequiredArgsConstructor
public class ProductLookupService implements ProductLookupUseCase {

    private final ProductRepository productRepository;

    @Override
    public List<Summary> findSellable() {
        return productRepository.findSellable().stream()
                .map(ProductLookupService::toSummary)
                .toList();
    }

    @Override
    public Optional<Detail> findByCode(String productCode) {
        return productRepository.findByCode(productCode).map(ProductLookupService::toDetail);
    }

    /** 도메인을 밖에 나갈 모양으로 바꾼다. 이 한 줄이 애그리거트가 새는 것을 막는다. */
    private static Summary toSummary(Product product) {
        return new Summary(product.productCode(), product.name(), product.price());
    }

    private static Detail toDetail(Product product) {
        return new Detail(product.productCode(), product.name(), product.price(),
                product.isSellable(), product.registeredAt());
    }
}
