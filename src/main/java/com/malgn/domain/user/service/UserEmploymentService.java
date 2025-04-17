package com.malgn.domain.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.user.model.SearchUserEmployRequest;
import com.malgn.domain.user.model.UserEmploymentResponse;

public interface UserEmploymentService {

    Page<UserEmploymentResponse> search(SearchUserEmployRequest searchRequest, Pageable pageable);

}
