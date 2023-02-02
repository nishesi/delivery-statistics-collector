package ru.itis.nishesi.statscollector.model.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itis.nishesi.statscollector.model.dto.work.Day;
import ru.itis.nishesi.statscollector.model.dto.work.Expense;
import ru.itis.nishesi.statscollector.model.dto.work.Shift;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private static final String SQL_SELECT_DAYS_FROM_TO =
            "SELECT * FROM work_dates " +
                    "WHERE user_id = :userId and :startDate <= date " +
                    "and date < :endDate;";
    private static final RowMapper<Day> DAY_MAPPER = (ResultSet rs, int rowNum) ->
            Day.builder()
                    .id(rs.getLong("id"))
                    .date(LocalDate.parse(rs.getString("date")))
                    .message(rs.getString("comment"))
                    .build();
    private static final String SQL_SELECT_SHIFTS_OF_DAY_BY_ID = "SELECT * FROM shifts WHERE date_id = :dateId;";
    private static final RowMapper<Shift> SHIFT_MAPPER = (rs, rowNum) -> Shift.builder()
            .id(rs.getLong("id"))
            .gain(rs.getInt("gain"))
            .averageRatio(rs.getFloat("average_ratio"))
            .startTime(LocalTime.parse(rs.getString("start_time")))
            .endTime(LocalTime.parse(rs.getString("end_time")))
            .distance(rs.getFloat("distance"))
            .ordersCount(rs.getInt("orders_count"))
            .build();
    private static final String SQL_SELECT_EXPENSES_OF_DAY_BY_ID = "SELECT * FROM expenses WHERE date_id = :dateId;";
    private static final RowMapper<Expense> EXPENSE_MAPPER = (rs, rowNum) ->
            Expense.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .value(rs.getInt("value"))
                    .build();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Day> getDaysFromTo(long userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> arguments = Map.of(
                "userId", userId,
                "startDate", startDate,
                "endDate", endDate);
        List<Day> days = jdbcTemplate.query(SQL_SELECT_DAYS_FROM_TO, arguments, DAY_MAPPER);
        for (Day day : days) {
            day.setShifts(
                    jdbcTemplate.query(SQL_SELECT_SHIFTS_OF_DAY_BY_ID, Map.of("dateId", day.getId()), SHIFT_MAPPER)
            );
            day.setExpenses(
                    jdbcTemplate.query(SQL_SELECT_EXPENSES_OF_DAY_BY_ID, Map.of("dateId", day.getId()), EXPENSE_MAPPER)
            );
        }
        return days;
    }
}
