package ru.itis.nishesi.statscollector.model.dto.converters;

import ru.itis.nishesi.statscollector.model.dto.User;
import ru.itis.nishesi.statscollector.model.dto.UserDto;

public class Converters {
    public static User from(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .username(userDto.getUsername())
                .build();
    }
}
