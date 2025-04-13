package com.malgn.configure.proerties;

import org.springframework.core.io.Resource;

public record GoogleProperties(String redirectUrl,
                               String calendarId,
                               Resource authLocation,
                               Resource storeLocation) {
}
