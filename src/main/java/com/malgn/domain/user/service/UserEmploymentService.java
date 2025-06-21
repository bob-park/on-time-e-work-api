package com.malgn.domain.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.user.model.SearchUserEmploymentRequest;
import com.malgn.domain.user.model.UserEmploymentResponse;

public interface UserEmploymentService {

    Page<UserEmploymentResponse> search(SearchUserEmploymentRequest searchRequest, Pageable pageable);

    UserEmploymentResponse getEmploymentByUserId(String userUniqueId);

}
