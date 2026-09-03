package com.brunosong.sample.modulith.recruiting.internal;

import com.brunosong.sample.modulith.recruiting.JobPostingBasicInfo;
import com.brunosong.sample.modulith.recruiting.JobPostingReferenceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 공개 계약의 구현. 구현은 internal 에 둔다.
 *
 * 다른 모듈은 JobPostingReferenceUseCase 인터페이스만 주입받고 이 클래스는 못 본다.
 * internal 에서 base 패키지의 타입을 쓰는 것은 자유롭다. 막히는 것은 그 반대 방향뿐이다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingReferenceService implements JobPostingReferenceUseCase {

    private final JobPostingRepository jobPostingRepository;

    @Override
    public Map<String, JobPostingBasicInfo> getBasicInfoByUuids(List<String> jobPostingUuids) {
        return jobPostingRepository.findByUuidIn(jobPostingUuids).stream()
                .map(JobPostingReferenceService::toBasicInfo)
                .collect(Collectors.toMap(JobPostingBasicInfo::jobPostingUuid, Function.identity()));
    }

    @Override
    public List<String> findUuidsByTitleContaining(String keyword) {
        return jobPostingRepository.findByTitleContaining(keyword).stream()
                .map(JobPosting::jobPostingUuid)
                .toList();
    }

    @Override
    public void recordChatHideLog(String jobPostingUuid, String beforeValue, String afterValue, String actor) {
        jobPostingRepository.findByUuid(jobPostingUuid).ifPresent(JobPosting::hideChat);
        log.info("채팅 숨김 변경 {} {} -> {} by {}", jobPostingUuid, beforeValue, afterValue, actor);
    }

    /** 도메인을 밖에 나갈 모양으로 바꾼다. 이 한 줄이 애그리거트가 새는 것을 막는다. */
    private static JobPostingBasicInfo toBasicInfo(JobPosting jobPosting) {
        return new JobPostingBasicInfo(
                jobPosting.jobPostingUuid(), jobPosting.title(),
                jobPosting.createdAt(), jobPosting.agencyUuid());
    }
}
