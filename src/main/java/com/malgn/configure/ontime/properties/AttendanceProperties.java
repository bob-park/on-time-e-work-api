package com.malgn.configure.ontime.properties;

import java.time.Duration;

import jakarta.ws.rs.DefaultValue;

public record AttendanceProperties(@DefaultValue("10m") Duration expiredCheck) {
}
