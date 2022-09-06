package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@Slf4j
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    @Nullable
    private String name;
    private LocalDate birthday;
}
