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
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.session.CoachingTimeResponse;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.TrainerResponse;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionServiceImpl sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetSessionById() throws Exception {

        Long sessionId = 1L;

        CoachingTimeResponse coachingTimeResponse = CoachingTimeResponse.builder()
                .startOfTraining(LocalTime.of(8,10))
                .endOfTraining(LocalTime.of(10,10))
                .build();

        SessionResponseDto sessionResponseDto = SessionResponseDto.builder()
                .id(sessionId)
                .dateOfTraining(LocalDate.now())
                .coachingTime(coachingTimeResponse)
                .userComment("zxc")
                .build();

        given(sessionService.getById(sessionId)).willReturn(sessionResponseDto);

        mockMvc.perform(get("/api/v1/sessions/{id}",sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(sessionResponseDto.getId()))
                .andExpect(jsonPath("$.body.dateOfTraining").value(sessionResponseDto
                        .getDateOfTraining().toString()))
                .andDo(print());
    }

    @Test
    public void testGetYourSessions() throws Exception {

        CoachingTimeResponse coachingTimeResponse1 = CoachingTimeResponse.builder()
                .startOfTraining(LocalTime.of(8,10))
                .endOfTraining(LocalTime.of(10,10))
                .build();

        CoachingTimeResponse coachingTimeResponse2 = CoachingTimeResponse.builder()
                .startOfTraining(LocalTime.of(7,10))
                .endOfTraining(LocalTime.of(9,10))
                .build();

        SessionResponseDto sessionResponseDto1 = SessionResponseDto.builder()
                .id(1L)
                .dateOfTraining(LocalDate.now())
                .coachingTime(coachingTimeResponse1)
                .userComment("zxc")
                .build();

        SessionResponseDto sessionResponseDto2 = SessionResponseDto.builder()
                .id(1L)
                .dateOfTraining(LocalDate.now())
                .coachingTime(coachingTimeResponse2)
                .userComment("qwe")
                .build();

        List<SessionResponseDto> sessionResponseDtoList = List.of(sessionResponseDto1,sessionResponseDto2);

        given(sessionService.getYourSessions()).willReturn(sessionResponseDtoList);

        mockMvc.perform(get("/api/v1/sessions/my-sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(sessionResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(sessionResponseDto1.getId()))
                .andExpect(jsonPath("$.body[1].id").value(sessionResponseDto2.getId()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllSession() throws Exception {
        CoachingTimeResponse coachingTimeResponse1 = CoachingTimeResponse.builder()
                .startOfTraining(LocalTime.of(8,10))
                .endOfTraining(LocalTime.of(10,10))
                .build();

        CoachingTimeResponse coachingTimeResponse2 = CoachingTimeResponse.builder()
                .startOfTraining(LocalTime.of(7,10))
                .endOfTraining(LocalTime.of(9,10))
                .build();

        SessionResponseDto sessionResponseDto1 = SessionResponseDto.builder()
                .id(1L)
                .dateOfTraining(LocalDate.now())
                .coachingTime(coachingTimeResponse1)
                .userComment("zxc")
                .build();

        SessionResponseDto sessionResponseDto2 = SessionResponseDto.builder()
                .id(1L)
                .dateOfTraining(LocalDate.now())
                .coachingTime(coachingTimeResponse2)
                .userComment("qwe")
                .build();

        List<SessionResponseDto> sessionResponseDtoList = List.of(sessionResponseDto1,sessionResponseDto2);

        given(sessionService.getAll()).willReturn(sessionResponseDtoList);

        mockMvc.perform(get("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(sessionResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(sessionResponseDto1.getId()))
                .andExpect(jsonPath("$.body[1].id").value(sessionResponseDto2.getId()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testAddTrainerCommentForSessions() throws Exception {

        Long sessionId = 1L;

        SessionCommentRequest sessionCommentRequest = SessionCommentRequest.builder()
                .trainerComment("zxc")
                .build();

        given(sessionService.addTrainerCommentForSessions(sessionId,sessionCommentRequest))
                .willReturn(sessionCommentRequest);

        mockMvc.perform(patch("/api/v1/sessions/trainer-comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    public void testCreateSession() throws Exception {

        SessionRequestDto sessionRequestDto = SessionRequestDto.builder()
                .coachingTimeId(1L)
                .dateOfTraining(LocalDate.now())
                .userComment("zxc")
                .build();

        TrainerResponse trainerResponse = TrainerResponse.builder()
                .firstName("Zxcqwe")
                .lastName("Qwezxc")
                .build();

        CoachingTimeResponse coachingTimeResponse = CoachingTimeResponse.builder()
                .startOfTraining(LocalTime.of(7,10))
                .endOfTraining(LocalTime.of(9,10))
                .build();

        SessionResponseDto sessionResponseDto = SessionResponseDto.builder()
                .id(1L)
                .dateOfTraining(LocalDate.now())
                .trainer(trainerResponse)
                .coachingTime(coachingTimeResponse)
                .userComment("zxc")
                .build();

        given(sessionService.create(sessionRequestDto)).willReturn(sessionResponseDto);

        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(sessionResponseDto.getId()))
                .andExpect(jsonPath("$.body.dateOfTraining").value(sessionResponseDto
                        .getDateOfTraining().toString()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testChangeSessionStatus() throws Exception {

        Long sessionId = 1L;
        SessionStatus newStatus = SessionStatus.COMPLETED;


        mockMvc.perform(patch("/api/v1/session/{id}/status", sessionId)
                        .param("status", newStatus.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testDeleteSessionById() throws Exception {
        Long sessionId = 1L;

        mockMvc.perform(delete("/api/v1/sessions/{id}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(sessionService, times(1)).delete(eq(sessionId));
    }
}
