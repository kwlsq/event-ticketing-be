package com.purwafest.purwafest.point.application.impl;

import com.purwafest.purwafest.point.application.PointService;
import com.purwafest.purwafest.point.infrastructure.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;

    public PointServiceImpl(PointRepository pointRepository){
        this.pointRepository = pointRepository;
    }

    @Override
    public Long getTotalPoints(Integer userId){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        Instant startOfDayInstant = startOfDay.toInstant(ZoneOffset.UTC);

        return this.pointRepository.getTotalPointsByUserIdNotExpired(userId, startOfDayInstant);
    }
}
