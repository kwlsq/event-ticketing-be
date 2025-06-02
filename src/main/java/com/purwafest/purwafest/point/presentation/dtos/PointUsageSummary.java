package com.purwafest.purwafest.point.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointUsageSummary {
    private BigInteger totalUsedPoint;
    private int rowUsed;
}
