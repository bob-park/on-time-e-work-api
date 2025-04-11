package com.malgn.domain.user.entity;

import static com.google.common.base.Preconditions.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_signatures")
public class UserSignature extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userUniqueId;

    @Column(columnDefinition = "bytea")
    private byte[] signatureImage;

    private String fileExtension;

    @Builder
    private UserSignature(Long id, String userUniqueId, byte[] signatureImage, String fileExtension) {

        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");

        this.id = id;
        this.userUniqueId = userUniqueId;
        this.signatureImage = signatureImage;
        this.fileExtension = fileExtension;
    }

    public void updateSignature(byte[] image, String fileExtension) {
        this.signatureImage = image;
        this.fileExtension = fileExtension;
    }

    public void resetSignature() {
        this.signatureImage = null;
        this.fileExtension = null;
    }
}
