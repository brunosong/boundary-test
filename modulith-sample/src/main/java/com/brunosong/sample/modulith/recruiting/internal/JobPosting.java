package com.brunosong.sample.modulith.recruiting.internal;

import java.time.LocalDateTime;

/**
 * 공고 애그리거트. internal 이라 다른 모듈이 못 본다.
 *
 * hideChat 같은 상태 변경 메서드가 있다는 것이 이 타입을 공개하면 안 되는 이유다.
 * 밖에는 JobPostingBasicInfo 로 바꿔서 내준다.
 */
public class JobPosting {

    private final String jobPostingUuid;
    private final String title;
    private final LocalDateTime createdAt;
    private final String agencyUuid;
    private boolean chatHidden;

    public JobPosting(String jobPostingUuid, String title, LocalDateTime createdAt, String agencyUuid) {
        this.jobPostingUuid = jobPostingUuid;
        this.title = title;
        this.createdAt = createdAt;
        this.agencyUuid = agencyUuid;
    }

    public void hideChat() {
        this.chatHidden = true;
    }

    public String jobPostingUuid() {
        return jobPostingUuid;
    }

    public String title() {
        return title;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public String agencyUuid() {
        return agencyUuid;
    }

    public boolean isChatHidden() {
        return chatHidden;
    }
}
