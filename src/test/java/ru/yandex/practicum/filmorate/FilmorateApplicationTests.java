package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;

	@Test
	public void testFindUserById() {

		User userTest = new User();
		userTest.setEmail("test@m.com");
		userTest.setBirthday(LocalDate.of(1998, 10, 10));
		userTest.setLogin("Su");
		userTest.setName("Wu");
		userTest = userStorage.addUser(userTest);
		Optional<User> userOptional = Optional.of(userStorage.getUserById(1));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);

		userTest.setLogin("newlogin");
		userStorage.updateUser(userTest);
		userOptional = Optional.of(userStorage.getUserById(1));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "newlogin")
				);

		userTest = new User();
		userTest.setEmail("test@m.com");
		userTest.setBirthday(LocalDate.of(1998, 10, 10));
		userTest.setLogin("Su");
		userTest.setName("Wu");
		userTest = userStorage.addUser(userTest);

		userStorage.addFriend(userTest.getId(), 1L);
		userOptional = Optional.of(userStorage.getUserById(userTest.getId()));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user.getFriendsIds().size()).isEqualTo(1)
				);

		userOptional = Optional.of(userStorage.getUserById(1L));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user.getFriendsIds().size()).isEqualTo(0)
				);

		userStorage.removeFriend(2L, 1L);
		userOptional = Optional.of(userStorage.getUserById(2L));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user.getFriendsIds().size()).isEqualTo(0));



	}
}
