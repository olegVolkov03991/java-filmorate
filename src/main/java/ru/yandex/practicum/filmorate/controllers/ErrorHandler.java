package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleValidationException(ValidationException e) {
		return Map.of("ERROR", e.getMessage());
	}

	@ExceptionHandler({UserNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleUserFoundEcxeption(RuntimeException e) {
		return Map.of("ERROR", e.getMessage());
	}

	@ExceptionHandler({FilmNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleFilmFoundEcxeption(RuntimeException e) {
		return Map.of("ERROR", e.getMessage());
	}

	@ExceptionHandler({UserAlreadyExistException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> handleUserAlreadyExistsException(RuntimeException e) {
		return Map.of("ERROR", e.getMessage());
	}

	@ExceptionHandler({FileAlreadyExistsException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> handleFilmAlreadyExistsException(RuntimeException e) {
		return Map.of("ERROR", e.getMessage());
	}
}
