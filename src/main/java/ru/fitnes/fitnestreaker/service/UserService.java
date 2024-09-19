package ru.fitnes.fitnestreaker.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.Role;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.util.List;
/**
 * Сервис для управления пользователями.
 * Этот сервис предоставляет методы для регистрации, обновления, удаления пользователей,
 * а также для поиска и получения информации о пользователях.
 */
public interface UserService {

     /**
      * Получение информации о пользователе по его идентификатору.
      *
      * @param id идентификатор пользователя.
      * @return информация о пользователе.
      * @throws LocalException если пользователь с указанным идентификатором не найден.
      */
     UserResponseDto getById(Long id);

     /**
      * Получение информацию о текущем пользователе.
      *
      * @return информация о текущем пользователе.
      */
     UserResponseDto getUserInfo();

     /**
      * Поиск пользователей по любому из указанных полей (email, firstName, lastName).
      *
      * @param email электронная почта пользователя.
      * @param firstName имя пользователя.
      * @param lastName фамилия пользователя.
      * @return список пользователей соответствующих критериям поиска.
      */
     List<UserResponseDto> searchUsersByAnyFields(String email, String firstName, String lastName);

     /**
      * Получение списка всех пользователей.
      *
      * @return список с информацией обо всех пользователях.
      */
     List<UserResponseDto> getAll();

     /**
      * Регистрация нового пользователя.
      *
      * @param userRequestDto объект для регистрации нового пользователя, содержащий необходимые для регистрации данные.
      * @return информация о зарегистрированном пользователе.
      * @throws MessagingException если произошла ошибка при отправке письма.
      */
     UserResponseDto registerNewUser(UserRequestDto userRequestDto) throws MessagingException;

     /**
      * Обновление информации о пользователе.
      *
      * @param userRequestDto объект, содержащий обновленные данные пользователя.
      * @param id идентификатор пользователя.
      * @return  информация об обновленном пользователе.
      * @throws LocalException если пользователь с указанным идентификатором не найден.
      */
     UserResponseDto update(UserRequestDto userRequestDto, Long id);

     /**
      * Изменение роли пользователя.
      *
      * @param id идентификатор пользователя.
      * @param role новая роль пользователя.
      * @return информация о пользователе с обновленной ролью.
      * @throws LocalException если пользователь с указанным идентификатором не найден.
      */
     UserResponseDto changeRole(Long id, Role role);

     /**
      * Удаление пользователя по его идентификатору и выход из приложения.
      *
      * @param id идентификатор пользователя.
      * @param session текущая HTTP сессия.
      * @throws LocalException если пользователь с указанным идентификатором не найден.
      */
     void delete(Long id, HttpSession session);

     /**
      * Выход из приложения.
      *
      * @param session текущая HTTP сессия.
      */
     void logout(HttpSession session);
}
