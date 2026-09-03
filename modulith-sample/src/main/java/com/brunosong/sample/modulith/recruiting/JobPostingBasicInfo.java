package com.brunosong.sample.modulith.recruiting;

import java.time.LocalDateTime;

/**
 * 다른 모듈이 공고 식별자만으로 화면에 필요한 최소 정보를 받아가기 위한 경량 결과.
 * 공고 내부 식별자(seq)는 노출하지 않는다.
 *
 * 이 record 가 모듈 base 패키지에 있는 이유는 JobPostingReferenceUseCase 의 반환 타입이기
 * 때문이다. 계약과 계약이 주고받는 타입은 같이 다녀야 한다. internal 로 내리면 부르는 쪽이
 * 반환값을 받는 순간 위반이 된다.
 */
public record JobPostingBasicInfo(
        String jobPostingUuid,
        String title,
        LocalDateTime createdAt,
        String agencyUuid
) {
}
