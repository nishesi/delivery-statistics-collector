package ru.itis.nishesi.statscollector.model.dto.work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupedDay {
    private Long id;
    private LocalDate date;
    private float deliveryGain;
    private float gain;
    private float expenses;
    private short ordersCount;
    private float averageRatio;
    private float distance;
    private String note;
}
