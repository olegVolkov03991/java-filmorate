package ru.yandex.practicum.filmorate.service.idGenerator;

import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {
	private long id = 0;

	public long generator() {
		++id;
		return id;
	}
}