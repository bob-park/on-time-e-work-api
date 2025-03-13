package com.malgn.configure.ontime;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.malgn.ontimeapi.configure.ontime.properties.OnTimeProperties;
import com.malgn.ontimeapi.domain.attendance.provider.AttendanceClockInProvider;
import com.malgn.ontimeapi.domain.attendance.provider.AttendanceClockOutProvider;
import com.malgn.ontimeapi.domain.attendance.provider.DelegatingAttendanceProvider;
import com.malgn.ontimeapi.domain.attendance.repository.AttendanceCheckRepository;
import com.malgn.ontimeapi.domain.attendance.repository.AttendanceRecordRepository;
import com.malgn.ontimeapi.domain.team.repository.TeamUserRepository;
import com.malgn.ontimeapi.domain.user.feign.UserFeignClient;
import com.malgn.ontimeapi.domain.user.repository.UserPositionRepository;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(OnTimeProperties.class)
public class OnTimeConfiguration {

    private final OnTimeProperties properties;

    private final ApplicationEventPublisher publisher;

    private final UserFeignClient userClient;

    private final AttendanceCheckRepository checkRepository;
    private final AttendanceRecordRepository recordRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserPositionRepository userPositionRepository;

    @Bean
    public DelegatingAttendanceProvider attendanceProvider() {
        DelegatingAttendanceProvider provider = new DelegatingAttendanceProvider(checkRepository);

        provider.addProvider(
            AttendanceClockInProvider.builder()
                .publisher(publisher)
                .checkRepository(checkRepository)
                .recordRepository(recordRepository)
                .userClient(userClient)
                .teamUserRepository(teamUserRepository)
                .userPositionRepository(userPositionRepository)
                .build());

        provider.addProvider(
            AttendanceClockOutProvider.builder()
                .publisher(publisher)
                .checkRepository(checkRepository)
                .recordRepository(recordRepository)
                .userClient(userClient)
                .teamUserRepository(teamUserRepository)
                .userPositionRepository(userPositionRepository)
                .build());

        return provider;
    }
}
