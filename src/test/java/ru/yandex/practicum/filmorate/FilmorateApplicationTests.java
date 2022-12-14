package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	public void testUserStorage() {

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

		assertThrows(StorageException.class,
				()->{userStorage.removeFriend(2L, 200L);});

		User finalUserTest = userTest;
		assertThrows(StorageException.class,
				()->{userStorage.addUser(finalUserTest);});

		finalUserTest.setId(1000L);
		assertThrows(StorageException.class,
				()->{userStorage.addUser(finalUserTest);});

		assertThat(userStorage.getAllFriends(1).size()).isEqualTo(0);

		userTest = new User();
		userTest.setEmail("test@m.com");
		userTest.setBirthday(LocalDate.of(1998, 10, 10));
		userTest.setLogin("Suqqq");
		userTest.setName("Wop");

		userStorage.addUser(userTest);
		userStorage.addFriend(1, 2);
		assertThat(userStorage.getAllFriends(1).size()).isEqualTo(1);
		assertThat(userStorage.getAllFriends(2).size()).isEqualTo(0);
	}

	@Test
	public void testFilmStorage() {
		Film filmTest = new Film();
		filmTest.setName("Test name");
		filmTest.setDuration(100);
		filmTest.setReleaseDate(LocalDate.of(1990, 9, 10));
		filmTest.setDescription("desc");
		Mpa mpa = new Mpa();
		mpa.setId(1);
		filmTest.setMpa(mpa);
		filmTest = filmStorage.addFilm(filmTest);
		Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(1L));
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film.getId()).isEqualTo(1L)
				);
		filmTest = filmStorage.likeFilm(filmTest.getId(), 1);
		filmOptional = Optional.of(filmStorage.getFilmById(filmTest.getId()));
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film.getLikes().size()).isEqualTo(1)
				);
		Film finalFilmTest = filmTest;
		assertThrows(StorageException.class,
				()->{filmStorage.likeFilm(finalFilmTest.getId(),-9);});

		filmTest.setId(90L);
		Film finalFilmTest1 = filmTest;
		assertThrows(StorageException.class,
				()->{filmStorage.updateFilm(finalFilmTest1);});
	}
}
