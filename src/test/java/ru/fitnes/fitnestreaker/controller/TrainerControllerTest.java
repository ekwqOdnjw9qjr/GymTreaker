package ru.fitnes.fitnestreaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.TrainerResponse;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.service.impl.TrainerServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerServiceImpl trainerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTrainerById() throws Exception {

        Long trainerId = 1L;

        TrainerResponseDto trainerResponseDto = TrainerResponseDto.builder()
                .id(trainerId)
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .specialty("Squats")
                .description("qwertyuio24pasd4534fghfdgjkl")
                .build();

        given(trainerService.getById(trainerId)).willReturn(trainerResponseDto);

        mockMvc.perform(get("/api/v1/trainers/{id}",trainerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(trainerResponseDto.getId()))
                .andExpect(jsonPath("$.body.firstName").value(trainerResponseDto.getFirstName()))
                .andExpect(jsonPath("$.body.lastName").value(trainerResponseDto.getLastName()))
                .andDo(print());
    }

    @Test
    void testGetScheduleTrainerByTrainerId() throws Exception {
        Long trainerId = 1L;

        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        trainer.setFirstName("Zxcvbn");
        trainer.setLastName("Qwerty");
        trainer.setSpecialty("Squats");
        trainer.setDescription("sdafsdfgsdrfweterg4t34gs");

        TrainerResponse trainerResponse = TrainerResponse.builder()
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .build();

        CoachingTimeResponseDto coachingTimeResponseDto1 = CoachingTimeResponseDto.builder()
                .id(1L)
                .trainer(trainerResponse)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startOfTraining(LocalTime.of(8,10))
                .endOfTraining(LocalTime.of(10,10))
                .build();

        CoachingTimeResponseDto coachingTimeResponseDto2 = CoachingTimeResponseDto.builder()
                .id(2L)
                .trainer(trainerResponse)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startOfTraining(LocalTime.of(8,10))
                .endOfTraining(LocalTime.of(10,10))
                .build();

        List<CoachingTimeResponseDto> coachingTimeResponseDtoList = List
                .of(coachingTimeResponseDto1,coachingTimeResponseDto2);

        given(trainerService.findCoachingTimeByTrainerId(trainerId)).willReturn(coachingTimeResponseDtoList);

        mockMvc.perform(get("/api/v1/trainers/trainer/{id}/schedule",trainerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(coachingTimeResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(coachingTimeResponseDto1.getId()))
                .andExpect(jsonPath("$.body[1].id").value(coachingTimeResponseDto2.getId()))
                .andDo(print());
    }

    @Test
    void testGetAllTrainer() throws Exception {

        TrainerResponseDto trainerResponseDto1 = TrainerResponseDto.builder()
                .id(1L)
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .specialty("Squats")
                .description("qwertyuio24pasd4534fghfdgjkl")
                .build();

        TrainerResponseDto trainerResponseDto2 = TrainerResponseDto.builder()
                .id(2L)
                .firstName("Assaa")
                .lastName("Freak")
                .specialty("Bench press")
                .description("qghfdghrwthgfvbszcrstaew")
                .build();

        List<TrainerResponseDto> trainerResponseDtoList = List.of(trainerResponseDto1,trainerResponseDto2);

        given(trainerService.getAll()).willReturn(trainerResponseDtoList);

        mockMvc.perform(get("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(trainerResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(trainerResponseDto1.getId()))
                .andExpect(jsonPath("$.body[0].firstName").value(trainerResponseDto1.getFirstName()))
                .andExpect(jsonPath("$.body[0].lastName").value(trainerResponseDto1.getLastName()))
                .andExpect(jsonPath("$.body[1].id").value(trainerResponseDto2.getId()))
                .andExpect(jsonPath("$.body[1].firstName").value(trainerResponseDto2.getFirstName()))
                .andExpect(jsonPath("$.body[1].lastName").value(trainerResponseDto2.getLastName()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testCreateTrainer() throws Exception {

        TrainerRequestDto trainerRequestDto = TrainerRequestDto.builder()
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .specialty("Squats")
                .description("qwertyuio24pasd4534fghfdgjkl")
                .build();

        TrainerResponseDto trainerResponseDto = TrainerResponseDto.builder()
                .id(1L)
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .specialty("Squats")
                .description("qwertyuio24pasd4534fghfdgjkl")
                .build();

        given(trainerService.create(trainerRequestDto)).willReturn(trainerResponseDto);

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(trainerResponseDto.getId()))
                .andExpect(jsonPath("$.body.firstName").value(trainerResponseDto.getFirstName()))
                .andExpect(jsonPath("$.body.lastName").value(trainerResponseDto.getLastName()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testUpdateTrainer() throws Exception {

        Long trainerId = 1L;

        TrainerRequestDto trainerRequestDto = TrainerRequestDto.builder()
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .specialty("Squats")
                .description("qwertyuio24pasd4534fghfdgjkl")
                .build();

        TrainerResponseDto trainerResponseDto = TrainerResponseDto.builder()
                .id(trainerId)
                .firstName("Zxcvbn")
                .lastName("Qwerty")
                .specialty("Squats")
                .description("qwertyuio24pasd4534fghfdgjkl")
                .build();

        given(trainerService.update(trainerId,trainerRequestDto)).willReturn(trainerResponseDto);

        mockMvc.perform(put("/api/v1/trainers/{id}",trainerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(trainerResponseDto.getId()))
                .andExpect(jsonPath("$.body.firstName").value(trainerResponseDto.getFirstName()))
                .andExpect(jsonPath("$.body.lastName").value(trainerResponseDto.getLastName()))
                .andDo(print());
    }

    @Test
    void testSetMainTrainer() throws Exception {
        Long trainerId = 1L;



        mockMvc.perform(patch("/api/v1/trainers/main/{id}", trainerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(trainerService, times(1)).choosingTheMainTrainer(trainerId);
    }

    @Test
    void testUntieTheTrainer() throws Exception {

        mockMvc.perform(patch("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(trainerService, times(1)).deleteTheMainTrainer();
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testFindAllUserByTrainer() throws Exception {
        Long trainerId = 1L;

        UserResponseDto userResponseDto1 = UserResponseDto.builder()
                .id(1L)
                .email("zxc@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        UserResponseDto userResponseDto2 = UserResponseDto.builder()
                .id(2L)
                .email("qwe@gmail.com")
                .firstName("Joe")
                .lastName("Freak")
                .build();

        List<UserResponseDto> userResponseDtoList = List.of(userResponseDto1,userResponseDto2);

        given(trainerService.getUsersByTrainerId(trainerId)).willReturn(userResponseDtoList);

        mockMvc.perform(get("/api/v1/trainers/trainer/{id}/users", trainerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(userResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(userResponseDto1.getId()))
                .andExpect(jsonPath("$.body[1].id").value(userResponseDto2.getId()))
                .andDo(print());

        verify(trainerService, times(1)).getUsersByTrainerId(trainerId);
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testDeleteTrainerById() throws Exception {
        Long trainerId = 1L;

        mockMvc.perform(delete("/api/v1/trainers/{id}", trainerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(trainerService, times(1)).delete((trainerId));
    }
}
