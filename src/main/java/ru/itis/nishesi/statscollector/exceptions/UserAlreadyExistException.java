package ru.itis.nishesi.statscollector.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class UserAlreadyExistException extends RuntimeException {
    private final Map<String, String> problems;
}
