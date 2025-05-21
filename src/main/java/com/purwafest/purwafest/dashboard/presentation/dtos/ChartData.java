package com.purwafest.purwafest.dashboard.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartData {
    private BigInteger finalAmount;
    private Instant paymentDate;
}
