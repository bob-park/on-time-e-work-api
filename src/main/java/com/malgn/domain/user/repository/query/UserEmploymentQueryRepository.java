package com.malgn.domain.user.repository.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.model.SearchUserEmploymentRequest;

public interface UserEmploymentQueryRepository {

    List<UserEmployment> getActiveAll();

    Page<UserEmployment> search(SearchUserEmploymentRequest request, Pageable pageable);

}
