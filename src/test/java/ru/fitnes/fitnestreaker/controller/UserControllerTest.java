package ru.fitnes.fitnestreaker.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.Role;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUserRegister() throws Exception {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .password("zxc")
                .build();


        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        given(userService.registerNewUser(userRequestDto)).willReturn(userResponseDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(1L))
                .andExpect(jsonPath("$.body.email").value(userResponseDto.getEmail()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUser() throws Exception {

        UserResponseDto userResponseDto1 = UserResponseDto.builder()
                .id(1L)
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        UserResponseDto userResponseDto2 = UserResponseDto.builder()
                .id(2L)
                .email("zxcdao@gmail.com")
                .firstName("Joe")
                .lastName("Freak")
                .build();

        List<UserResponseDto> userResponseDtoList = List.of(userResponseDto1,userResponseDto2);

        given(userService.getAll()).willReturn(userResponseDtoList);

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(userResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(userResponseDto1.getId()))
                .andExpect(jsonPath("$.body[0].email").value(userResponseDto1.getEmail()))
                .andExpect(jsonPath("$.body[0].firstName").value(userResponseDto1.getFirstName()))
                .andExpect(jsonPath("$.body[1].id").value(userResponseDto2.getId()))
                .andExpect(jsonPath("$.body[1].email").value(userResponseDto2.getEmail()))
                .andExpect(jsonPath("$.body[1].firstName").value(userResponseDto2.getFirstName()))
                .andDo(print());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUserById() throws Exception {
        Long userId = 1L;
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        given(userService.getById(userId)).willReturn(userResponseDto);
        mockMvc.perform(get("/api/v1/users/{id}",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.body.email").value(userResponseDto.getEmail()))
                .andExpect(jsonPath("$.body.firstName").value(userResponseDto.getFirstName()))
                .andDo(print());
    }

    @Test
    public void testGetUserInfo() throws Exception {
        Long userId = 1L;
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        given(userService.getUserInfo()).willReturn(userResponseDto);
        mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.body.email").value(userResponseDto.getEmail()))
                .andExpect(jsonPath("$.body.firstName").value(userResponseDto.getFirstName()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsersByAnyAvailableParameter() throws Exception {
        String lastName = "Freak";


        UserResponseDto userResponseDto1 = UserResponseDto.builder()
                .id(1L)
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName(lastName)
                .build();

        UserResponseDto userResponseDto2 = UserResponseDto.builder()
                .id(2L)
                .email("zxcdao@gmail.com")
                .firstName("Joe")
                .lastName(lastName)
                .build();



        List<UserResponseDto> userResponseDtoList = List.of(userResponseDto1,userResponseDto2);

        given(userService.searchUsersByAnyFields(null, null, lastName)).willReturn(userResponseDtoList);

        mockMvc.perform(get("/api/v1/users/parameter")
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(userResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(userResponseDto1.getId()))
                .andExpect(jsonPath("$.body[0].email").value(userResponseDto1.getEmail()))
                .andExpect(jsonPath("$.body[0].firstName").value(userResponseDto1.getFirstName()))
                .andExpect(jsonPath("$.body[1].id").value(userResponseDto2.getId()))
                .andExpect(jsonPath("$.body[1].email").value(userResponseDto2.getEmail()))
                .andExpect(jsonPath("$.body[1].firstName").value(userResponseDto2.getFirstName()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testChangeUserRole() throws Exception {

        Long userId = 1L;

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .email("zxcqwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        given(userService.changeRole(userId, Role.ROLE_TRAINER)).willReturn(userResponseDto);

        mockMvc.perform(patch("/api/v1/users/user/{id}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("role", Role.ROLE_TRAINER.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.body.email").value(userResponseDto.getEmail()))
                .andExpect(jsonPath("$.body.firstName").value(userResponseDto.getFirstName()))
                .andDo(print());
    }

    @Test
    public void testUpdateUser() throws Exception {

        Long userId = 1L;

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("qweasd@gmail.com")
                .firstName("Alexander")
                .lastName("Freak")
                .password("zxc")
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .email("qweasd@gmail.com")
                .firstName("Alexander")
                .lastName("Freak")
                .build();

        given(userService.update(userRequestDto,userId)).willReturn(userResponseDto);

        mockMvc.perform(put("/api/v1/users/{id}",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.body.email").value(userResponseDto.getEmail()))
                .andExpect(jsonPath("$.body.firstName").value(userResponseDto.getFirstName()))
                .andDo(print());
    }

    @Test
    public void testDeleteUserById() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(userService, times(1)).delete(eq(userId), any(HttpSession.class));
    }

}
