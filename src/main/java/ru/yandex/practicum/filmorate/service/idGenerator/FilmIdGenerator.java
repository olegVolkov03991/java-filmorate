package ru.yandex.practicum.filmorate.service.idGenerator;

import org.springframework.stereotype.Component;

@Component
public class FilmIdGenerator {
	private long id = 0;

	public long generator() {
		++id;
		return id;
	}
}
