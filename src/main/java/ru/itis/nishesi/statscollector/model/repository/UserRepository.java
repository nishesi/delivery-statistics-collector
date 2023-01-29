package ru.itis.nishesi.statscollector.model.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itis.nishesi.statscollector.model.dto.User;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> User.builder()
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .username(rs.getString("username"))
            .build();
    private static final String SQL_SELECT_USER = "SELECT email, password, username FROM users WHERE email = :email;";
    private static final String SQL_SELECT_USER_ROLES = "SELECT authority FROM authorities WHERE email = :email;";
    private static final String SQL_INSERT_USER = "INSERT INTO users (email, password, username) VALUES (:email, :password, :username);";
    private static final String SQL_INSERT_AUTHORITY = "INSERT INTO authorities (email, authority) VALUES (:email, :authority);";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<User> findByEmail(String email) {
        try {
            Optional<User> userOptional = Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            SQL_SELECT_USER,
                            Collections.singletonMap("email", email),
                            USER_ROW_MAPPER)
            );
            userOptional.ifPresent(user -> user.setRoles(
                            jdbcTemplate.query(
                                    SQL_SELECT_USER_ROLES,
                                    Collections.singletonMap("email", email),
                                    (rs, rowNum) -> rs.getString("authority"))
                    )
            );
            return userOptional;
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public User save(User user) {
        Map<String, String> userParameters = new HashMap<>();
        userParameters.put("email", user.getEmail());
        userParameters.put("password", user.getPassword());
        userParameters.put("username", user.getUsername());
        jdbcTemplate.update(SQL_INSERT_USER, userParameters);
        for (String authority : user.getRoles()) {
            jdbcTemplate.update(
                    SQL_INSERT_AUTHORITY,
                    Map.of("email", user.getEmail(), "authority", authority));
        }
        return user;
    }

}
