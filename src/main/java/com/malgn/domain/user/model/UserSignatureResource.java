package com.malgn.domain.user.model;

import org.springframework.core.io.ByteArrayResource;

public class UserSignatureResource extends ByteArrayResource {

    private final String filename;

    public UserSignatureResource(byte[] byteArray, String filename) {
        super(byteArray);

        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }
}
