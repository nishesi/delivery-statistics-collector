package ru.itis.nishesi.statscollector.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.nishesi.statscollector.exceptions.UserAlreadyExistException;
import ru.itis.nishesi.statscollector.exceptions.UserNotFoundException;
import ru.itis.nishesi.statscollector.model.dto.User;
import ru.itis.nishesi.statscollector.model.dto.UserDto;
import ru.itis.nishesi.statscollector.model.dto.converters.Converters;
import ru.itis.nishesi.statscollector.model.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) throws UserAlreadyExistException {

        userRepository.findByEmail(userDto.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistException(Map.of("error", "User already exists"));
        });

        User user = Converters.from(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ROLE_USER"));

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}
