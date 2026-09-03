package com.brunosong.sample.modulith.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * product 모듈이 밖에 내주는 공개 계약.
 *
 * recruiting 과 달리 결과 타입을 인터페이스 안에 중첩해 두었다. 중첩 타입의 패키지는
 * 바깥 클래스의 패키지와 같으므로, 인터페이스가 공개면 record 도 자동으로 공개다.
 *
 * 이렇게 하면 계약이 파일 하나로 닫힌다. 모듈 base 패키지는 공개 표면이라 평평한데,
 * 계약마다 DTO 를 파일로 흩으면 금방 지저분해진다. 중첩하면 계약 수 = 파일 수가 된다.
 *
 * 대신 이 DTO 를 다른 계약과 공유해야 하는 순간 별도 파일로 빼야 한다.
 * 한쪽 인터페이스 안에 두고 다른 쪽이 참조하면 이상한 결합이 생긴다.
 */
public interface ProductLookupUseCase {

    /** 목록 화면용. 필요한 것만 담는다. */
    List<Summary> findSellable();

    /** 상세 화면용. 같은 상품이라도 화면이 다르면 모양이 다르다. */
    Optional<Detail> findByCode(String productCode);

    record Summary(String productCode, String name, long price) {
    }

    record Detail(String productCode, String name, long price,
                  boolean sellable, LocalDateTime registeredAt) {
    }
}
