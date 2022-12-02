package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;

import static org.mockito.Mockito.when;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	UserController userController;
	FilmController filmController;

	@BeforeEach
	void setUserController() {
		userController = new UserController(new UserService(new InMemoryUserStorage()));
		filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
	}

	@Test
	void testUserService() {
		UserService userService = new UserService(new InMemoryUserStorage());
		User a = new User();
		a.setName("Jo");
		a.setLogin("JoLOG");
		a.setEmail("gi@gmail.com");
		a.setBirthday(LocalDate.of(1990, 11, 10));
		userService.addUser(a);
		Assertions.assertEquals(1, userService.getAllUsers().size());
		a.setId(2L);
		userService.addUser(a);
		Assertions.assertEquals(2, userService.getAllUsers().size());


		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

	}

	@Test
	void testUserAdd() {
		ResponseEntity<User> responseEntity = userController.addUser(new User());
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);

		User user = new User();
		user.setName("Jo");
		user.setLogin("JoLOG");
		user.setEmail("gi@gmail.com");
		user.setBirthday(LocalDate.of(1990, 11, 10));
		responseEntity = userController.addUser(user);
		assertThat(responseEntity.getBody().getName()).isEqualTo(user.getName());
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		ResponseEntity<Collection<User>> allUsers = userController.getAllUsers();
		assertThat(allUsers.getBody().size()).isEqualTo(1);

		user = new User();
		user.setName("Fu");
		user.setLogin("Lo");
		user.setEmail("bi@gmail.com");
		user.setBirthday(LocalDate.of(1990, 11, 10));
		responseEntity = userController.addUser(user);
		assertThat(responseEntity.getBody().getName()).isEqualTo(user.getName());
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		allUsers = userController.getAllUsers();
		assertThat(allUsers.getBody().size()).isEqualTo(2);
		responseEntity = userController.addFriend(1, 2);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		ResponseEntity<Collection<User>> allFriends = userController.getAllFriends(1);
		assertThat(allFriends.getBody().size()).isEqualTo(1);
	}

	@Test
	void testFilmAdd() {
		Film film = new Film();
		film.setName("FI");
		film.setDescription("text super");
		film.setReleaseDate(LocalDate.of(2000, 11, 10));
		film.setDuration(63);
		ResponseEntity<Film> responseEntity =  filmController.addFilm(film);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().getName()).isEqualTo(film.getName());
		ResponseEntity<Collection<Film>> collectionResponseEntity = filmController.getPopularCounted(9);
		assertThat(collectionResponseEntity.getBody().size()).isEqualTo(1);

		film = new Film();
		film.setName("SI");
		film.setDescription("NNN");
		film.setReleaseDate(LocalDate.of(2013, 10, 10));
		film.setDuration(69);
		responseEntity =  filmController.addFilm(film);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().getName()).isEqualTo(film.getName());
		collectionResponseEntity = filmController.getPopularCounted(9);
		assertThat(collectionResponseEntity.getBody().size()).isEqualTo(2);

		responseEntity = filmController.likeFilm(1, 13);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		collectionResponseEntity = filmController.getPopularCounted(9);
		assertThat(((Film)(collectionResponseEntity.getBody().toArray()[0])).getName()).isEqualTo("FI");
		assertThat(((Film)(collectionResponseEntity.getBody().toArray()[1])).getName()).isEqualTo("SI");


		responseEntity = filmController.likeFilm(1, -9);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
