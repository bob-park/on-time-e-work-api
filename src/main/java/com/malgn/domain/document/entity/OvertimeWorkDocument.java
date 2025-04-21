package com.malgn.domain.document.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "documents_overtime_works")
@DiscriminatorValue("OVERTIME_WORK")
public class OvertimeWorkDocument extends Document {

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    private List<OvertimeWorkTime> times = new ArrayList<>();

    @Builder
    private OvertimeWorkDocument(Long id, DocumentStatus status, String userUniqueId) {
        super(id, DocumentType.OVERTIME_WORK, status, userUniqueId);
    }

    /*
     * 편의 메서드
     */
    public void addTime(OvertimeWorkTime overtimeWorkTime) {
        overtimeWorkTime.updateDocument(this);

        getTimes().add(overtimeWorkTime);
    }
}
