package com.dexwin.notesapp;

import org.springframework.boot.SpringApplication;

public class TestNotesappApplication {

	public static void main(String[] args) {
		SpringApplication.from(NotesappApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
