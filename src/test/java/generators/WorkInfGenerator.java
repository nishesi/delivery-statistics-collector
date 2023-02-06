package generators;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkInfGenerator {
    private static long daysCount = 0;
    private static long allDaysCount = 0;
    static Random random = new Random();
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/StatsCollector",
                "postgres",
                "Qwerty228");
        long[] userIds = insertUsers();
        long[] usersDaysIds = insertUsersDays(userIds);
        insertDaysShifts(usersDaysIds);
        insertDaysExpenses(usersDaysIds);
        System.out.println((double) daysCount / allDaysCount);
    }

    private static long[] insertUsers() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        long[] usersIds = new long[100];
        final String SQL_INSERT_USERS = "insert into users (id, email, username, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USERS)) {
            for (int i = 1; i <= 100; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "user" + i);
                preparedStatement.setString(3, "user" + i);
                preparedStatement.setString(4, passwordEncoder.encode("test"));
                preparedStatement.addBatch();
                usersIds[i - 1] = i;
            }
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usersIds;
    }

    private static long[] insertUsersDays(long[] userIds) {
        String SQL_INSERT_USERS_DAY = "insert into work_dates (user_id, date, delivery_gain, note) VALUES (?, ?, ?, ?)";

        try (PreparedStatement prs = connection.prepareStatement(SQL_INSERT_USERS_DAY)) {
            for (Long userId : userIds) {
                LocalDate localDate = LocalDate.now().minusYears(1);

                for (int i = 0; i < random.nextInt(1000) + 1000; i++) {
                    int probability = random.nextInt(3);
                    allDaysCount++;
                    if (probability == 0 || probability == 1) {
                        daysCount++;
                        prs.setLong(1, userId);
                        prs.setObject(2, localDate.plusDays(i));
                        prs.setInt(3, random.nextInt(2000) + 500);
                        prs.setString(4, random.nextInt(1000) + "`s comment");
                        prs.addBatch();
                    }
                }
            }
            prs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Long> daysIds = new ArrayList<>();
        String SQL_SELECT_ID_OF_DAYS = "SELECT id from work_dates;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ID_OF_DAYS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                daysIds.add(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return daysIds.stream().mapToLong(id -> id).toArray();
    }

    private static void insertDaysShifts(long[] usersDaysIds) {
        String SQL_INSERT_SHIFTS_BY_DAY_ID = "INSERT INTO shifts " +
                "(date_id, start_time, end_time, average_ratio, orders_count, gain, distance)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prs = connection.prepareStatement(SQL_INSERT_SHIFTS_BY_DAY_ID)) {
            for (Long id : usersDaysIds) {
                LocalTime time = LocalTime.of(8, 30);

                for (int i = 0; i < random.nextInt(10); i++) {
                    if (time.getHour() > 18) {
                        break;
                    }
                    prs.setLong(1, id);
                    prs.setObject(2, time);
                    time = time
                            .plusHours(random.nextInt(5))
                            .plusMinutes(random.nextInt(30));
                    prs.setObject(3, time);
                    prs.setFloat(4, random.nextFloat() + 1.0f);
                    prs.setInt(5, random.nextInt(10));
                    prs.setInt(6, random.nextInt(1000));
                    prs.setFloat(7, random.nextFloat() * 10);
                    prs.addBatch();
                }
            }
            prs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertDaysExpenses(long[] daysIds) {
        String SQL_INSERT_EXPENSES_BY_DAY_ID = "INSERT INTO expenses (date_id, name, value) VALUES (?, ?, ?)";
        long expenseId = 1;

        try (PreparedStatement prs = connection.prepareStatement(SQL_INSERT_EXPENSES_BY_DAY_ID)) {
            for (Long id : daysIds) {
                for (int i = 0; i < random.nextInt(10); i++) {
                    prs.setLong(1, id);
                    prs.setString(2, "expense " + expenseId);
                    expenseId++;
                    prs.setInt(3, random.nextInt(150) + 1);
                    prs.addBatch();
                }
            }
            prs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
