package com.brunosong.sample.modulith.recruiting;

import java.util.List;
import java.util.Map;

/**
 * recruiting 모듈이 밖에 내주는 공개 계약.
 *
 * 모듈 base 패키지에 있으므로 다른 모듈이 쓸 수 있다. 이 모듈의 나머지(internal 아래)는
 * 전부 닫혀 있다.
 *
 * 도메인 엔티티(JobPosting)를 반환하지 않는다는 점이 중요하다. 엔티티를 내주면 받는 쪽이
 * 상태 변경 메서드를 부를 수 있다. 제목 하나 읽으려고 공고를 바꿀 수 있는 물건을
 * 건네받아서는 안 된다.
 */
public interface JobPostingReferenceUseCase {

    Map<String, JobPostingBasicInfo> getBasicInfoByUuids(List<String> jobPostingUuids);

    List<String> findUuidsByTitleContaining(String keyword);

    void recordChatHideLog(String jobPostingUuid, String beforeValue, String afterValue, String actor);
}
