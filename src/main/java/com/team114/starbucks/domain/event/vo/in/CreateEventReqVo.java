package com.team114.starbucks.domain.event.vo.in;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class CreateEventReqVo {

    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;

}
