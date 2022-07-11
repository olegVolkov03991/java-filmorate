package ru.yandex.practicum.filmorate.service.idGenerator;

import org.springframework.stereotype.Component;

@Component
public class IdGeneratorFilm {
	private long id = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long generator() {
		setId(id + 1);
		return id;
	}
}
