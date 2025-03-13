package com.malgn.domain.user.repository.query;

import java.util.List;

import com.malgn.domain.user.entity.UserEmployment;

public interface UserEmploymentQueryRepository {

    List<UserEmployment> getActiveAll();

}
