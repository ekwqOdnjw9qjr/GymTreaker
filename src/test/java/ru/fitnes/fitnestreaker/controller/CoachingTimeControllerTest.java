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
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
public class CoachingTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoachingTimeServiceImpl coachingTimeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testCreateCoachingTime() throws Exception {

        CoachingTimeRequestDto coachingTimeRequestDto = CoachingTimeRequestDto.builder()
                .startOfTraining(LocalTime.of(7,10))
                .endOfTraining(LocalTime.of(9,10))
                .build();

        DayOfWeek dayOfWeek = DayOfWeek.FRIDAY;

        CoachingTimeResponseDto coachingTimeResponseDto = CoachingTimeResponseDto.builder()
                .id(1L)
                .startOfTraining(LocalTime.of(7,10))
                .endOfTraining(LocalTime.of(9,10))
                .dayOfWeek(DayOfWeek.FRIDAY)
                .build();

        given(coachingTimeService.create(coachingTimeRequestDto,dayOfWeek)).willReturn(coachingTimeResponseDto);

        mockMvc.perform(post("/api/v1/coaching-times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coachingTimeRequestDto))
                .param("dayOfWeek", DayOfWeek.FRIDAY.name()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(coachingTimeResponseDto.getId()))
                .andExpect(jsonPath("$.body.startOfTraining").value("07:10:00"))
                .andExpect(jsonPath("$.body.endOfTraining").value("09:10:00"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testGetCoachingTimesById() throws Exception {

        Long coachingTimeId = 1L;

        CoachingTimeResponseDto coachingTimeResponseDto = CoachingTimeResponseDto.builder()
                .id(coachingTimeId)
                .startOfTraining(LocalTime.of(7,10))
                .endOfTraining(LocalTime.of(9,10))
                .dayOfWeek(DayOfWeek.FRIDAY)
                .build();

        given(coachingTimeService.findById(coachingTimeId)).willReturn(coachingTimeResponseDto);

        mockMvc.perform(get("/api/v1/coaching-times/{id}", coachingTimeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(coachingTimeResponseDto.getId()))
                .andExpect(jsonPath("$.body.startOfTraining").value("07:10:00"))
                .andExpect(jsonPath("$.body.endOfTraining").value("09:10:00"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    public void testDeleteCoachingTimeById() throws Exception {

        Long coachingTimeId = 1L;

        mockMvc.perform(delete("/api/v1/coaching-times/{id}", coachingTimeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(coachingTimeService, times(1)).delete(eq(coachingTimeId));
    }


}
