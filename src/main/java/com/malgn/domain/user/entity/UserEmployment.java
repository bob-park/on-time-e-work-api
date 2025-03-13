package com.malgn.domain.user.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseEntity;
import com.malgn.domain.user.entity.type.EmployStatus;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_employments")
public class UserEmployment extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userUniqueId;

    private LocalDateTime effectiveDate;

    @Enumerated(EnumType.STRING)
    private EmployStatus status;

    @Builder
    private UserEmployment(Long id, String userUniqueId, LocalDateTime effectiveDate, EmployStatus status) {

        checkArgument(StringUtils.isNotBlank(userUniqueId), "userId must be provided.");
        checkArgument(isNotEmpty(effectiveDate), "effectiveDate must be provided.");

        this.id = id;
        this.userUniqueId = userUniqueId;
        this.effectiveDate = effectiveDate;
        this.status = defaultIfNull(status, EmployStatus.ACTIVE);
    }
}
