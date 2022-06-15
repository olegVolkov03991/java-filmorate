package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdGenerator {
	private int id;
	@Autowired
	public int generator(){
		return ++id;
	}
}
