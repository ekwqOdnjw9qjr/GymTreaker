package ru.fitnes.fitnestreaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.service.impl.MembershipServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MembershipServiceImpl membershipServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private Membership membership;

    @BeforeEach
    public void init() {
        membership = new Membership();
                membership.setId(1L);
                membership.setStartDate(LocalDate.now());
                membership.setEndDate(LocalDate.now().plusDays(30));
                membership.setMembershipType(MembershipType.SMALL);
                membership.setFreezingDays(10L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getMembershipByIdTest() throws Exception {

        Long membershipId = 1L;

        MembershipResponseDto membershipResponse = MembershipResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        given(membershipServiceImpl.getById(membershipId)).willReturn(membershipResponse);

        mockMvc.perform(get("/memberships/{id}",membershipId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(1L))
                .andExpect(jsonPath("$.body.freezingDays").value(10L))
                .andExpect(jsonPath("$.body.membershipType").value(MembershipType.SMALL.name()));
    }


    @Test
    public void testCheckMembershipStatus() throws Exception {
        Long membershipId = membership.getId();

        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;


        given(membershipServiceImpl.checkStatus(membershipId)).willReturn(membershipStatus);


        mockMvc.perform(get("/memberships/membership/{id}/status", membershipId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").value(membershipStatus.name()));
    }

    @Test
    public void testFreezeMembership() throws Exception {
        Long membershipId = 1L;
        Long freezeDays = 5L;

        MembershipResponseDto membershipResponse = MembershipResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        given(membershipServiceImpl.freezeMembership(membershipId,freezeDays)).willReturn(membershipResponse);

        mockMvc.perform(patch("/memberships/membership/{id}/frost",membershipId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    public void testCreateMembership() throws Exception {


        MembershipRequestDto membershipRequest = MembershipRequestDto.builder()
                .startDate(LocalDate.now())
                .build();


        MembershipResponseDto membershipResponse = MembershipResponseDto.builder()
                .id(1L)
                .startDate(membershipRequest.getStartDate())
                .endDate(membershipRequest.getStartDate().plusDays(30))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        given(membershipServiceImpl.create(membershipRequest, MembershipType.SMALL)).willReturn(membershipResponse);

        mockMvc.perform(post("/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membershipRequest))
                        .param("membershipType", MembershipType.SMALL.name()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body.id").value(1L))
                .andExpect(jsonPath("$.body.startDate").value(membershipRequest.getStartDate().toString()))
                .andExpect(jsonPath("$.body.membershipType").value(MembershipType.SMALL.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllMembership() throws Exception {

        MembershipResponseDto membership1 = MembershipResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2024, 8, 30))
                .endDate(LocalDate.of(2024, 10, 29))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        MembershipResponseDto membership2 = MembershipResponseDto.builder()
                .id(2L)
                .startDate(LocalDate.of(2024, 8, 30))
                .endDate(LocalDate.of(2024, 10, 29))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        List<MembershipResponseDto> membershipResponseDtoList = List.of(membership1, membership2);

        given(membershipServiceImpl.getAll()).willReturn(membershipResponseDtoList);

        mockMvc.perform(get("/memberships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(membershipResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(membership1.getId()))
                .andExpect(jsonPath("$.body[0].startDate").value(membership1.getStartDate().toString()))
                .andExpect(jsonPath("$.body[0].endDate").value(membership1.getEndDate().toString()))
                .andExpect(jsonPath("$.body[0].membershipType").value(membership1.getMembershipType().toString()))
                .andExpect(jsonPath("$.body[0].freezingDays").value(membership1.getFreezingDays()))
                .andExpect(jsonPath("$.body[1].id").value(membership2.getId()))
                .andExpect(jsonPath("$.body[1].startDate").value(membership2.getStartDate().toString()))
                .andExpect(jsonPath("$.body[1].endDate").value(membership2.getEndDate().toString()))
                .andExpect(jsonPath("$.body[1].membershipType").value(membership2.getMembershipType().toString()))
                .andExpect(jsonPath("$.body[1].freezingDays").value(membership2.getFreezingDays()));
    }

    @Test
    public void testGetAllYourMembership() throws Exception {

        MembershipResponseDto membership1 = MembershipResponseDto.builder()
                .id(1L)
                .startDate(LocalDate.of(2024, 8, 30))
                .endDate(LocalDate.of(2024, 10, 29))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        MembershipResponseDto membership2 = MembershipResponseDto.builder()
                .id(2L)
                .startDate(LocalDate.of(2024, 8, 30))
                .endDate(LocalDate.of(2024, 10, 29))
                .membershipType(MembershipType.SMALL)
                .freezingDays(10L)
                .build();

        List<MembershipResponseDto> membershipResponseDtoList = List.of(membership1, membership2);

        given(membershipServiceImpl.findYourMemberships()).willReturn(membershipResponseDtoList);

        mockMvc.perform(get("/memberships/my-memberships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.body.length()").value(membershipResponseDtoList.size()))
                .andExpect(jsonPath("$.body[0].id").value(membership1.getId()))
                .andExpect(jsonPath("$.body[0].startDate").value(membership1.getStartDate().toString()))
                .andExpect(jsonPath("$.body[0].endDate").value(membership1.getEndDate().toString()))
                .andExpect(jsonPath("$.body[0].membershipType").value(membership1.getMembershipType().toString()))
                .andExpect(jsonPath("$.body[0].freezingDays").value(membership1.getFreezingDays()))
                .andExpect(jsonPath("$.body[1].id").value(membership2.getId()))
                .andExpect(jsonPath("$.body[1].startDate").value(membership2.getStartDate().toString()))
                .andExpect(jsonPath("$.body[1].endDate").value(membership2.getEndDate().toString()))
                .andExpect(jsonPath("$.body[1].membershipType").value(membership2.getMembershipType().toString()))
                .andExpect(jsonPath("$.body[1].freezingDays").value(membership2.getFreezingDays()));

    }

    @Test
    public void testDeleteMembershipById() throws Exception {

        Long membershipId = 1L;

        mockMvc.perform(delete("/memberships/{id}",membershipId))
                .andExpect(status().isNoContent());

        verify(membershipServiceImpl, times(1)).delete(1L);
    }


}
