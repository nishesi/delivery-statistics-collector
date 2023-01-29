package ru.itis.nishesi.statscollector.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.itis.nishesi.statscollector.exceptions.UserAlreadyExistException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserAlreadyExistException.class)
    String handleUserAlreadyExistException(UserAlreadyExistException ex, Model model) {
        model.addAttribute("problems", ex.getProblems());
        return "registration-page";
    }
}
