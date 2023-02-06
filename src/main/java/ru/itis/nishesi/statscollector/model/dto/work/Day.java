package ru.itis.nishesi.statscollector.model.dto.work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Day {
    private Long id;
    private LocalDate date;
    private float deliveryGain;
    private List<Shift> shifts;
    private List<Expense> expenses;
    private String note;
}
