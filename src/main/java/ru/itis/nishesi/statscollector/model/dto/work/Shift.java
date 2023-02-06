package ru.itis.nishesi.statscollector.model.dto.work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shift {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private float averageRatio;
    private short ordersCount;
    private int gain;
    private float distance;
}
