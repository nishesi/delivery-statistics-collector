package ru.itis.nishesi.statscollector.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.nishesi.statscollector.model.dto.work.Day;
import ru.itis.nishesi.statscollector.model.repository.StatisticsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;


    public List<Day> getLastWeekDays(long userId) {
        LocalDate localDate = LocalDate.now();
        return statisticsRepository.getDaysFromTo(userId, localDate.minusDays(7), localDate);
    }

    public List<Day> getCurrentMonthDays(long userId) {
        LocalDate localDate = LocalDate.now();
        return statisticsRepository.getDaysFromTo(userId, localDate.withDayOfMonth(1), localDate);
    }

    public List<Day> getLastMonthDays(long userId) {
        LocalDate localDate = LocalDate.now();
        return statisticsRepository.getDaysFromTo(userId, localDate.minusMonths(1), localDate);
    }
}
