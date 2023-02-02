package ru.itis.nishesi.statscollector.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.nishesi.statscollector.model.dto.User;
import ru.itis.nishesi.statscollector.model.dto.work.Day;
import ru.itis.nishesi.statscollector.model.service.StatisticsService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/current/month")
    @ResponseBody
    public List<Day> getStats(@SessionAttribute User user) {
        return statisticsService.getCurrentMonthDays(user.getId());
    }

    @GetMapping("/last/week")
    @ResponseBody
    public List<Day> getLastWeekDays(@SessionAttribute User user) {
        return statisticsService.getLastWeekDays(user.getId());
    }

    @GetMapping("/last/month")
    @ResponseBody
    public List<Day> getLastMonthDays(@SessionAttribute User user) {
        return statisticsService.getLastMonthDays(user.getId());
    }

    @PostMapping("/add")
    public Day addDay(@RequestBody Day day) {
        return day;
    }
}
