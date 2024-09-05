package ru.fitnes.fitnestreaker.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.entity.enums.Role;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.repository.UserSpecification;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.UserService;

import java.util.List;
/**
 * Сервис для управления пользователями.
 * Этот сервис предоставляет методы для регистрации, обновления, удаления пользователей,
 * а также для поиска и получения информации о пользователях.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получение информации о пользователе по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return информация о пользователе.
     * @throws LocalException если пользователь с указанным идентификатором не найден.
     */
    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.", id)));


        return userMapper.userResponseToDto(user);
    }

    /**
     * Получение информацию о текущем пользователе.
     *
     * @return информация о текущем пользователе.
     */
    @Override
    public UserResponseDto getUserInfo() {
        User user = userRepository.findUserById(securityConfig.getCurrentUser().getId());

        return userMapper.userResponseToDto(user);
    }

    /**
     * Поиск пользователей по любому из указанных полей (email, firstName, lastName).
     *
     * @param email электронная почта пользователя.
     * @param firstName имя пользователя.
     * @param lastName фамилия пользователя.
     * @return список пользователей соответствующих критериям поиска.
     */
    @Override
    public List<UserResponseDto> searchUsersByAnyFields(String email, String firstName, String lastName) {
        Specification<User> spec = Specification.where(UserSpecification.hasFirstName(firstName))
                .or(UserSpecification.hasLastName(lastName))
                .or(UserSpecification.hasEmail(email));

        if (email != null && !email.isEmpty()) {
            spec = spec.and(UserSpecification.hasEmail(email));
        }
        if (firstName != null && !firstName.isEmpty()) {
            spec = spec.and(UserSpecification.hasFirstName(firstName));
        }
        if (lastName != null && !lastName.isEmpty()) {
            spec = spec.and(UserSpecification.hasLastName(lastName));
        }

        List<User> userList = userRepository.findAll(spec);

        return userMapper.userResponseToListDto(userList);
    }

    /**
     * Получение списка всех пользователей.
     *
     * @return список с информацией обо всех пользователях.
     */
    @Override
    public List<UserResponseDto> getAll() {
        List<User> userList = userRepository.findAll();
        return userMapper.userResponseToListDto(userList);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userRequestDto объект для регистрации нового пользователя, содержащий необходимые для регистрации данные.
     * @return информация о зарегистрированном пользователе.
     * @throws MessagingException если произошла ошибка при отправке письма.
     */
    @Override
    public UserResponseDto registerNewUser(UserRequestDto userRequestDto) throws MessagingException {
        if (userRepository.checkEmailExists(userRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email is already register");
        }

        User user = userMapper.userRequestToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);
        mailService.sendRegistrationMail(userRequestDto.getEmail(),
                                         userRequestDto.getFirstName(),
                                         userRequestDto.getLastName());

        return userMapper.userResponseToDto(savedUser);
    }

    /**
     * Изменение роли пользователя.
     *
     * @param id идентификатор пользователя.
     * @param role новая роль пользователя.
     * @return информация о пользователе с обновленной ролью.
     * @throws LocalException если пользователь с указанным идентификатором не найден.
     */
    @Override
    public UserResponseDto changeRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.", id)));

        user.setRole(role);

        User savedUser = userRepository.save(user);

        return userMapper.userResponseToDto(savedUser);
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param userRequestDto объект, содержащий обновленные данные пользователя.
     * @param id идентификатор пользователя.
     * @return  информация об обновленном пользователе.
     * @throws LocalException если пользователь с указанным идентификатором не найден.
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.",id)));

        userMapper.merge(userToUpdate, userMapper.userRequestToEntity(userRequestDto));
        userToUpdate.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        User savedUser = userRepository.save(userToUpdate);

        return userMapper.userResponseToDto(savedUser);
    }

    /**
     * Удаление пользователя по его идентификатору и выход из приложения.
     *
     * @param id идентификатор пользователя.
     * @param session текущая HTTP сессия.
     * @throws LocalException если пользователь с указанным идентификатором не найден.
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public void delete(Long id, HttpSession session) {
        userRepository.deleteById(id);
        logout(session);

    }

    /**
     * Выход из приложения.
     *
     * @param session текущая HTTP сессия.
     */
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

    }
}
