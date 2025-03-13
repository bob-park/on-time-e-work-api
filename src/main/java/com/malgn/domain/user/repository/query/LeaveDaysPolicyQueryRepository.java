package com.malgn.domain.user.repository.query;

import java.util.List;

import com.malgn.domain.user.entity.LeaveDaysPolicy;

public interface LeaveDaysPolicyQueryRepository {

    List<LeaveDaysPolicy> getPolicyAll();

}
