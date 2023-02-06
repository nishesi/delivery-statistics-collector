package ru.itis.nishesi.statscollector.model.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itis.nishesi.statscollector.model.dto.work.Day;
import ru.itis.nishesi.statscollector.model.dto.work.Expense;
import ru.itis.nishesi.statscollector.model.dto.work.GroupedDay;
import ru.itis.nishesi.statscollector.model.dto.work.Shift;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsRepository {
    static RowMapper<GroupedDay> GROUPED_DAY_MAPPER = (rs, rowNum) ->
            GroupedDay.builder()
                    .id(rs.getLong("id"))
                    .date(LocalDate.parse(rs.getString("date")))
                    .deliveryGain(rs.getFloat("delivery_gain"))
                    .gain(rs.getFloat("gain"))
                    .expenses(rs.getFloat("expenses"))
                    .ordersCount(rs.getShort("orders_count"))
                    .averageRatio(rs.getFloat("avg_ratio"))
                    .distance(rs.getFloat("distance"))
                    .note(rs.getString("note"))
                    .build();
    static String SQL_SELECT_DAYS_FROM_TO =
            "SELECT * FROM work_dates " +
                    "WHERE user_id = :userId and :startDate <= date " +
                    "and date < :endDate;";

    static String SQL_SELECT_GROUPED_DAYS_FROM_TO = "SELECT * FROM group_by_days_table(:userId, :startDate, :endDate);";
    static RowMapper<Day> DAY_MAPPER = (ResultSet rs, int rowNum) ->
            Day.builder()
                    .id(rs.getLong("id"))
                    .date(LocalDate.parse(rs.getString("date")))
                    .note(rs.getString("comment"))
                    .build();
    static String SQL_SELECT_SHIFTS_OF_DAY_BY_ID = "SELECT * FROM shifts WHERE date_id = :dateId;";
    static RowMapper<Shift> SHIFT_MAPPER = (rs, rowNum) -> Shift.builder()
            .id(rs.getLong("id"))
            .gain(rs.getInt("gain"))
            .averageRatio(rs.getFloat("average_ratio"))
            .startTime(LocalTime.parse(rs.getString("start_time")))
            .endTime(LocalTime.parse(rs.getString("end_time")))
            .distance(rs.getFloat("distance"))
            .ordersCount(rs.getShort("orders_count"))
            .build();
    static String SQL_SELECT_EXPENSES_OF_DAY_BY_ID = "SELECT * FROM expenses WHERE date_id = :dateId;";
    static RowMapper<Expense> EXPENSE_MAPPER = (rs, rowNum) ->
            Expense.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .value(rs.getInt("value"))
                    .build();
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<Day> getDaysFromTo(long userId, LocalDate startDate, LocalDate endDate) {

        List<Day> days = jdbcTemplate.query(
                SQL_SELECT_DAYS_FROM_TO,
                Map.of(
                        "userId", userId,
                        "startDate", startDate,
                        "endDate", endDate),
                DAY_MAPPER);

        for (Day day : days) {
            day.setShifts(
                    jdbcTemplate.query(
                            SQL_SELECT_SHIFTS_OF_DAY_BY_ID,
                            Map.of("dateId", day.getId()),
                            SHIFT_MAPPER)
            );
            day.setExpenses(
                    jdbcTemplate.query(
                            SQL_SELECT_EXPENSES_OF_DAY_BY_ID,
                            Map.of("dateId", day.getId()),
                            EXPENSE_MAPPER)
            );
        }
        return days;
    }

    public List<GroupedDay> getGroupedDaysFromTo(long userId, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                SQL_SELECT_GROUPED_DAYS_FROM_TO,
                Map.of(
                        "userId", userId,
                        "startDate", startDate,
                        "endDate", endDate
                ),
                GROUPED_DAY_MAPPER);
    }
}
