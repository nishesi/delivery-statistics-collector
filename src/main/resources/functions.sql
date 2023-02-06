DROP FUNCTION IF EXISTS group_by_days_table(BIGINT, DATE, DATE);

CREATE OR REPLACE FUNCTION group_by_days_table(BIGINT, DATE, DATE)
    RETURNS TABLE
            (
                id            BIGINT,
                date          DATE,
                delivery_gain NUMERIC(7, 2),
                gain          NUMERIC(7, 2),
                expenses      NUMERIC(7, 2),
                orders_count  SMALLINT,
                avg_ratio     NUMERIC(3, 2),
                distance      NUMERIC(5, 2),
                note          VARCHAR(255)
            )
AS
$$
WITH user_work_days AS (SELECT *
                        FROM work_dates
                        WHERE user_id = $1
                          AND date >= $2
                          AND date < $3)
SELECT user_work_days.id,
       user_work_days.date,
       user_work_days.delivery_gain,
       sum(shifts.gain)          AS gain,
       sum(expenses.value)       AS expenses,
       sum(shifts.orders_count)  AS orders_ocunt,
       avg(shifts.average_ratio) AS avg_ratio,
       sum(shifts.distance)      AS distance,
       user_work_days.note
FROM user_work_days
         INNER JOIN shifts ON user_work_days.id = shifts.date_id
         INNER JOIN expenses ON user_work_days.id = expenses.date_id
GROUP BY user_work_days.id, date, delivery_gain, note
$$ language sql;