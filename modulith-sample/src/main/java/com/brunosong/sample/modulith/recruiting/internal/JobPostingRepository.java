package com.brunosong.sample.modulith.recruiting.internal;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** 예제라 DB 대신 메모리에 담는다. internal 이라 다른 모듈이 못 본다. */
@Repository
public class JobPostingRepository {

    private final Map<String, JobPosting> store = new ConcurrentHashMap<>();

    public JobPostingRepository() {
        save(new JobPosting("JP-001", "영업사원 모집", LocalDateTime.now(), "AG-001"));
        save(new JobPosting("JP-002", "설계사 모집", LocalDateTime.now(), "AG-002"));
    }

    public void save(JobPosting jobPosting) {
        store.put(jobPosting.jobPostingUuid(), jobPosting);
    }

    public Optional<JobPosting> findByUuid(String jobPostingUuid) {
        return Optional.ofNullable(store.get(jobPostingUuid));
    }

    public List<JobPosting> findByUuidIn(List<String> jobPostingUuids) {
        return jobPostingUuids.stream().map(store::get).filter(java.util.Objects::nonNull).toList();
    }

    public List<JobPosting> findByTitleContaining(String keyword) {
        return store.values().stream().filter(it -> it.title().contains(keyword)).toList();
    }
}
