package com.brunosong.sample.archunit.target.application.product.port.in;

/**
 * product 서브도메인이 밖으로 내보내는 공개 계약.
 * 도메인 엔티티(Product)를 그대로 넘기지 않으려고 in 포트 패키지에 둔다.
 * 다른 서브도메인은 이 패키지까지만 볼 수 있다(SubdomainRules).
 */
public record ProductSnapshot(String sku, String name, long price) {
}
