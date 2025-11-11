package com.malgn.domain.user.batch;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.domain.user.entity.LeaveDaysPolicy;
import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.repository.LeaveDaysPolicyRepository;
import com.malgn.domain.user.repository.UserEmploymentRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class UserLeaveDayBatch {

    private static final int DEFAULT_TOTAL_LEAVE_DAYS = 15;

    private final LeaveDaysPolicyRepository leaveDaysPolicyRepository;
    private final UserEmploymentRepository userEmploymentRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;

    @Transactional
    @Scheduled(cron = "${e-work.user.cron.generate-users-leave-days}")
    public void generateUserLeaveDays() {

        List<LeaveDaysPolicy> policies = leaveDaysPolicyRepository.getPolicyAll();
        List<UserEmployment> userEmployments = userEmploymentRepository.getActiveAll();

        LocalDate now = LocalDate.now();

        // 내년 까지 계산하여 추가
        List<Integer> years = List.of(now.getYear(), now.getYear() + 1);

        for (Integer year : years) {
            for (UserEmployment userEmployment : userEmployments) {

                UserLeaveEntry userLeaveEntry =
                    userLeaveEntryRepository.getLeaveEntry(userEmployment.getUserUniqueId(), year)
                        .orElseGet(() -> {

                            // 작년 대체 휴가(Compensatory Leave) 가 있는 지 확인
                            BigDecimal prevCompLeaveBalance = BigDecimal.ZERO;

                            UserLeaveEntry prevUserLeaveEntry =
                                userLeaveEntryRepository.getLeaveEntry(userEmployment.getUserUniqueId(), year - 1)
                                    .orElse(null);

                            if (isNotEmpty(prevUserLeaveEntry)) {
                                BigDecimal total = prevUserLeaveEntry.getTotalCompLeaveDays();
                                BigDecimal used = prevUserLeaveEntry.getUsedCompLeaveDays();

                                prevCompLeaveBalance = total.subtract(used);
                            }

                            // 금년 휴가 정보 생성
                            Period between = Period.between(userEmployment.getEffectiveDate().toLocalDate(), LocalDate.of(year, 1,1));
                            int userContinuousYears = between.getYears();

                            int totalLeaveDays = decideTotalLeaveDays(policies, userContinuousYears);

                            UserLeaveEntry createdLeaveEntry =
                                UserLeaveEntry.builder()
                                    .userUniqueId(userEmployment.getUserUniqueId())
                                    .year(year)
                                    .totalLeaveDays(BigDecimal.valueOf(totalLeaveDays))
                                    .totalCompLeaveDays(prevCompLeaveBalance)
                                    .build();

                            createdLeaveEntry = userLeaveEntryRepository.save(createdLeaveEntry);

                            log.debug("created user leave entry: {}", createdLeaveEntry);

                            return createdLeaveEntry;
                        });

                // 내년인 경우에만 prev comp leave days 갱신
                if (year == now.getYear() + 1) {

                    // 작년 대체 휴가(Compensatory Leave) 가 있는 지 확인
                    BigDecimal prevCompLeaveBalance = BigDecimal.ZERO;

                    UserLeaveEntry prevUserLeaveEntry =
                        userLeaveEntryRepository.getLeaveEntry(userEmployment.getUserUniqueId(), year - 1)
                            .orElse(null);

                    if (isNotEmpty(prevUserLeaveEntry)) {
                        BigDecimal total = prevUserLeaveEntry.getTotalCompLeaveDays();
                        BigDecimal used = prevUserLeaveEntry.getUsedCompLeaveDays();

                        prevCompLeaveBalance = total.subtract(used);
                    }

                    userLeaveEntry.updateTotalCompLeaveDays(prevCompLeaveBalance);

                    log.debug("updated user leave entry: {}", userLeaveEntry);
                }

                log.debug("batched user leave entry: {}", userLeaveEntry);

            }
        }

    }

    private int decideTotalLeaveDays(List<LeaveDaysPolicy> policies, int userContinuousYears) {

        int totalLeaveDays = DEFAULT_TOTAL_LEAVE_DAYS;

        for (LeaveDaysPolicy policy : policies) {
            if (policy.getContinuousYears() > userContinuousYears) {
                break;
            }

            totalLeaveDays = policy.getTotalLeaveDays();

        }

        return totalLeaveDays;
    }

}
