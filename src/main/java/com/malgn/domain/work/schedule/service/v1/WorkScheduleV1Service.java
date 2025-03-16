package com.malgn.domain.work.schedule.service.v1;

import static com.malgn.domain.work.schedule.model.v1.WorkScheduleV1Response.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.domain.work.schedule.entity.WorkSchedule;
import com.malgn.domain.work.schedule.model.CreateWorkScheduleRequest;
import com.malgn.domain.work.schedule.model.WorkScheduleResponse;
import com.malgn.domain.work.schedule.model.v1.CreateWorkScheduleV1Request;
import com.malgn.domain.work.schedule.repository.WorkScheduleRepository;
import com.malgn.domain.work.schedule.service.WorkScheduleService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WorkScheduleV1Service implements WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;

    @Transactional
    @Override
    public WorkScheduleResponse createSchedule(CreateWorkScheduleRequest createRequest) {

        CreateWorkScheduleV1Request createV1Request = (CreateWorkScheduleV1Request)createRequest;

        WorkSchedule createdSchedule =
            WorkSchedule.builder()
                .contents(createV1Request.contents())
                .description(createV1Request.description())
                .startDate(createV1Request.startDate())
                .endDate(createV1Request.endDate())
                .isRepeated(createV1Request.isRepeated())
                .isClosed(createV1Request.isClosed())
                .build();

        createdSchedule = workScheduleRepository.save(createdSchedule);

        log.debug("created work schedule: {}", createdSchedule);

        return from(createdSchedule);
    }
}
