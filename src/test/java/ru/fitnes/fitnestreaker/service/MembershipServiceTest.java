package ru.fitnes.fitnestreaker.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.CustomUserDetails;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.impl.MembershipServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipMapper membershipMapper;

    @Mock
    private SecurityConfig securityConfig;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MembershipServiceImpl membershipService;

    @Test
    void testGetMembershipById() {

        Long membershipId = 1L;

        Membership membership = new Membership();
        membership.setId(membershipId);

        MembershipResponseDto membershipResponseDto = MembershipResponseDto.builder()
                .id(membership.getId())
                .build();


        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(membershipMapper.membershipResponseToDto(membership)).thenReturn(membershipResponseDto);


        MembershipResponseDto result = membershipService.getMembershipById(membershipId);


        assertNotNull(result);
        assertEquals(membershipResponseDto, result);
        verify(membershipRepository).findById(membershipId);
        verify(membershipMapper).membershipResponseToDto(membership);

    }

    @Test
    void testNotFoundMembershipById() {

        Long membershipId = 1L;

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () -> membershipService
                .getMembershipById(membershipId));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("Membership with id: 1 not found.", exception.getMessage());
        verify(membershipRepository).findById(membershipId);
    }

    @Test
    void testFindMembershipsByYourId() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Membership membership1 = new Membership();
        membership1.setId(1L);
        Membership membership2 = new Membership();
        membership2.setId(2L);

        MembershipResponseDto membershipResponseDto1 = MembershipResponseDto.builder()
                .id(1L)
                .build();
        MembershipResponseDto membershipResponseDto2 = MembershipResponseDto.builder()
                .id(2L)
                .build();

        List<Membership> membershipList = List.of(membership1,membership2);
        List<MembershipResponseDto> membershipResponseList = List.of(membershipResponseDto1,membershipResponseDto2);

        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));
        when(membershipRepository.findMembershipsByUserId(userId)).thenReturn(membershipList);
        when(membershipMapper.membershipResponseToListDto(membershipList)).thenReturn(membershipResponseList);

        List<MembershipResponseDto> result = membershipService.findYourMemberships();

        assertNotNull(result);
        assertEquals(membershipResponseList, result);
        verify(securityConfig).getCurrentUser();
        verify(membershipRepository).findMembershipsByUserId(userId);
        verify(membershipMapper).membershipResponseToListDto(membershipList);
    }

    @Test
    void testGetAllMembership() {

        Membership membership1 = new Membership();
        membership1.setId(1L);
        Membership membership2 = new Membership();
        membership2.setId(2L);

        MembershipResponseDto membershipResponseDto1 = MembershipResponseDto.builder()
                .id(1L)
                .build();
        MembershipResponseDto membershipResponseDto2 = MembershipResponseDto.builder()
                .id(2L)
                .build();

        List<Membership> membershipList = List.of(membership1,membership2);
        List<MembershipResponseDto> membershipResponseList = List.of(membershipResponseDto1,membershipResponseDto2);

        when(membershipRepository.findAll()).thenReturn(membershipList);
        when(membershipMapper.membershipResponseToListDto(membershipList)).thenReturn(membershipResponseList);

        List<MembershipResponseDto> result = membershipService.getAllMembership();

        assertNotNull(result);
        assertEquals(membershipResponseList, result);
        verify(membershipRepository).findAll();
        verify(membershipMapper).membershipResponseToListDto(membershipList);
    }

    @Test
    void testCreateMembershipWithExistingSubscription() {

        MembershipRequestDto membershipRequestDto = new MembershipRequestDto();
        MembershipType membershipType = MembershipType.SMALL;

        User user = new User();
        user.setId(1L);

        Membership existingMembership = new Membership();
        Membership membership = new Membership();

        MembershipResponseDto membershipResponseDto = new MembershipResponseDto();

        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(membershipMapper.membershipRequestToEntity(membershipRequestDto)).thenReturn(membership);
        when(membershipRepository.findMembershipsByUserId(user.getId())).thenReturn(List.of(existingMembership));
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.membershipResponseToDto(membership)).thenReturn(membershipResponseDto);

        MembershipResponseDto result = membershipService.createMembership(membershipRequestDto, membershipType);

        assertNotNull(result);
        assertEquals(membershipResponseDto, result);

        assertNull(membership.getStartDate());
        assertNull(membership.getEndDate());

        verify(securityConfig, times(2)).getCurrentUser();
        verify(userRepository).getReferenceById(user.getId());
        verify(membershipRepository).findMembershipsByUserId(user.getId());
        verify(membershipRepository).save(membership);
        verify(membershipMapper).membershipResponseToDto(membership);
    }

    @Test
    void testCrateMembershipWhenUserDontHaveMembership() {

        MembershipRequestDto membershipRequestDto = MembershipRequestDto.builder()
                .startDate(LocalDate.now())
                .build();
        MembershipType membershipType = MembershipType.SMALL;

        User user = new User();
        user.setId(1L);

        Membership membership = new Membership();

        MembershipResponseDto membershipResponseDto = new MembershipResponseDto();

        List<Membership> membershipList = new ArrayList<>();

        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(membershipMapper.membershipRequestToEntity(membershipRequestDto)).thenReturn(membership);
        when(membershipRepository.findMembershipsByUserId(user.getId())).thenReturn(membershipList);
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.membershipResponseToDto(membership)).thenReturn(membershipResponseDto);


        MembershipResponseDto result = membershipService.createMembership(membershipRequestDto, membershipType);

        assertNotNull(result);
        assertEquals(membershipResponseDto, result);
        assertNotNull(membership.getStartDate());
        assertNotNull(membership.getEndDate());

        verify(securityConfig, times(2)).getCurrentUser();
        verify(userRepository).getReferenceById(user.getId());
        verify(membershipRepository).findMembershipsByUserId(user.getId());
        verify(membershipRepository).save(membership);
        verify(membershipMapper).membershipResponseToDto(membership);
    }

    @Test
    void testCreateMembershipWhenStartDateNull() {

        MembershipRequestDto membershipRequestDto = new MembershipRequestDto();
        MembershipType membershipType = MembershipType.SMALL;

        User user = new User();
        user.setId(1L);

        Membership membership = new Membership();

        List<Membership> membershipList = new ArrayList<>();

        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(membershipMapper.membershipRequestToEntity(membershipRequestDto)).thenReturn(membership);
        when(membershipRepository.findMembershipsByUserId(user.getId())).thenReturn(membershipList);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> membershipService
                .createMembership(membershipRequestDto,membershipType));

        assertEquals("Start date must not be null", exception.getMessage());
        verify(membershipRepository, never()).save(membership);
    }

    @Test
    void testActiveMembership() {

        Long membershipId = 1L;

        MembershipRequestDto membershipRequestDto = MembershipRequestDto.builder()
                .startDate(LocalDate.now())
                .build();

        Membership nonActiveMembership = new Membership();
        nonActiveMembership.setId(membershipId);
        nonActiveMembership.setMembershipType(MembershipType.ANNUAL);
        nonActiveMembership.setFreezingDays(MembershipType.ANNUAL.getFreezeDays());

        MembershipResponseDto membershipResponseDto = new MembershipResponseDto();

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(nonActiveMembership));
        when(membershipRepository.save(nonActiveMembership)).thenReturn(nonActiveMembership);
        when(membershipMapper.membershipResponseToDto(nonActiveMembership)).thenReturn(membershipResponseDto);

        MembershipResponseDto result = membershipService.activeMembership(membershipId, membershipRequestDto);

        assertNotNull(result);
        assertEquals(membershipResponseDto, result);
        assertNotNull(nonActiveMembership.getStartDate());
        assertNotNull(nonActiveMembership.getEndDate());

        verify(membershipRepository).findById(membershipId);
        verify(membershipRepository).save(nonActiveMembership);
        verify(membershipMapper).membershipResponseToDto(nonActiveMembership);
    }

    @Test
    void testActiveMembershipWhenMembershipAlreadyActive() {

        Long membershipId = 1L;

        MembershipRequestDto membershipRequestDto = MembershipRequestDto.builder()
                .startDate(LocalDate.now())
                .build();

        Membership nonActiveMembership = new Membership();
        nonActiveMembership.setId(membershipId);
        nonActiveMembership.setMembershipType(MembershipType.ANNUAL);
        nonActiveMembership.setFreezingDays(MembershipType.ANNUAL.getFreezeDays());
        nonActiveMembership.setStartDate(LocalDate.of(2024,10,1));
        nonActiveMembership.setEndDate(nonActiveMembership.getStartDate().plusDays(360));

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(nonActiveMembership));


        LocalException exception = assertThrows(LocalException.class, () ->
                membershipService.activeMembership(membershipId,membershipRequestDto));


        assertNotNull(nonActiveMembership.getStartDate());
        assertNotNull(nonActiveMembership.getEndDate());

        assertEquals(ErrorType.CLIENT_ERROR, exception.getType());
        assertEquals("Your membership is already active", exception.getMessage());
        verify(membershipRepository, never()).save(nonActiveMembership);

    }

    @Test
    void testActiveMembershipWhenStartDateNull() {

        Long membershipId = 1L;

        MembershipRequestDto membershipRequestDto = new MembershipRequestDto();

        Membership nonActiveMembership = new Membership();
        nonActiveMembership.setId(membershipId);
        nonActiveMembership.setMembershipType(MembershipType.ANNUAL);
        nonActiveMembership.setFreezingDays(MembershipType.ANNUAL.getFreezeDays());

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(nonActiveMembership));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                membershipService.activeMembership(membershipId,membershipRequestDto));

        assertEquals("Start date must not be null", exception.getMessage());
        verify(membershipRepository, never()).save(nonActiveMembership);
    }

    @Test
    void testActiveMembershipWhenMembershipNotFound() {
        Long membershipId = 1L;

        MembershipRequestDto membershipRequestDto = new MembershipRequestDto();

        Membership nonActiveMembership = new Membership();
        nonActiveMembership.setId(membershipId);
        nonActiveMembership.setMembershipType(MembershipType.ANNUAL);
        nonActiveMembership.setFreezingDays(MembershipType.ANNUAL.getFreezeDays());

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () ->
                membershipService.activeMembership(membershipId,membershipRequestDto));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("Membership with id: 1 not found.", exception.getMessage());
        verify(membershipRepository, never()).save(nonActiveMembership);
    }

    @Test
    void testFreezeMembership() {

        Long membershipId = 1L;

        Long days = 15L;

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setFreezingDays(30L);
        membership.setEndDate(LocalDate.now().plusDays(15));

        MembershipResponseDto membershipResponseDto =  new MembershipResponseDto();
        membershipResponseDto.setId(membershipId);
        membershipResponseDto.setFreezingDays(15L);
        membershipResponseDto.setEndDate(LocalDate.now().plusDays(30));


        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.membershipResponseToDto(membership)).thenReturn(membershipResponseDto);

        MembershipResponseDto result = membershipService.freezeMembership(membershipId, days);

        assertNotNull(result);
        assertEquals(membershipResponseDto, result);
        assertEquals(membership.getEndDate(), LocalDate.now().plusDays(30));
        assertEquals(15L,membership.getFreezingDays());


        verify(membershipRepository).findById(membershipId);
        verify(membershipRepository).save(membership);
        verify(membershipMapper).membershipResponseToDto(membership);
    }

    @Test
    void testFreezeMembershipWhenCountDaysNegative() {

        Long membershipId = 1L;

        Long days = -5L;

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setFreezingDays(30L);
        membership.setEndDate(LocalDate.now().plusDays(15));

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));

        LocalException exception = assertThrows(LocalException.class, () -> membershipService
                .freezeMembership(membershipId,days));

        assertEquals(ErrorType.CLIENT_ERROR, exception.getType());
        assertEquals("The number of freeze days cannot be negative", exception.getMessage());
        verify(membershipRepository, never()).save(membership);

    }

    @Test
    void testFreezeMembershipWhenMembershipNotFound() {

        Long membershipId = 1L;

        Long days = 15L;

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () -> membershipService
                .freezeMembership(membershipId,days));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("Membership with id: 1 not found.", exception.getMessage());
        verify(membershipRepository).findById(membershipId);
    }

    @Test
    void testFreezeMembershipWhenUserDontHaveEnoughFreezingDaysAvailable() {


        Long membershipId = 1L;

        Long days = 15L;

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setFreezingDays(11L);
        membership.setEndDate(LocalDate.now());

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));

        LocalException exception = assertThrows(LocalException.class, () -> membershipService
                .freezeMembership(membershipId,days));

        assertEquals(ErrorType.CLIENT_ERROR, exception.getType());
        assertEquals("You don't have enough freeze days available", exception.getMessage());
        verify(membershipRepository).findById(membershipId);
        verify(membershipRepository, never()).save(membership);
    }

    @Test
    void testCheckMembershipStatusWhenMembershipActive() {

        Long membershipId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setEndDate(LocalDate.of(2024,11,11));
        membership.setUser(user);

        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));


        MembershipStatus result = membershipService.checkMembershipStatus(membershipId);

        assertNotNull(result);
        assertEquals(membershipStatus, result);

        verify(membershipRepository).findById(membershipId);
        verify(securityConfig).getCurrentUser();
    }

    @Test
    void testCheckMembershipStatusWhenMembershipInactive() {

        Long membershipId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setEndDate(LocalDate.of(2024,8,11));
        membership.setUser(user);

        MembershipStatus membershipStatus = MembershipStatus.INACTIVE;

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));


        MembershipStatus result = membershipService.checkMembershipStatus(membershipId);

        assertNotNull(result);
        assertEquals(membershipStatus, result);

        verify(membershipRepository).findById(membershipId);
        verify(securityConfig).getCurrentUser();
    }

    @Test
    void testCheckMembershipStatusWhenMembershipNotFound() {

        Long membershipId = 1L;

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () -> membershipService
                .checkMembershipStatus(membershipId));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("Membership with id: 1 not found.", exception.getMessage());
        verify(membershipRepository).findById(membershipId);
    }

    @Test
    void testCheckMembershipStatusAnotherUser() {

        Long membershipId = 1L;
        Long userId1 = 1L;
        Long userId2 = 2L;

        User user1 = new User();
        user1.setId(userId1);

        User user2 = new User();
        user2.setId(userId2);

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setUser(user2);

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user1));


        LocalException exception = assertThrows(LocalException.class, () -> membershipService
                .checkMembershipStatus(membershipId));

        assertEquals(ErrorType.CLIENT_ERROR, exception.getType());
        assertEquals("You do not have access to check the status of this membership.", exception.getMessage());
        verify(membershipRepository).findById(membershipId);
        verify(securityConfig).getCurrentUser();
    }

    @Test
    void testCheckMembershipStatusWhenMembershipInactiveAndEndDateNull() {

        Long membershipId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Membership membership = new Membership();
        membership.setId(membershipId);
        membership.setUser(user);

        MembershipStatus membershipStatus = MembershipStatus.INACTIVE;

        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));
        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));


        MembershipStatus result = membershipService.checkMembershipStatus(membershipId);

        assertNotNull(result);
        assertEquals(membershipStatus, result);

        verify(membershipRepository).findById(membershipId);
        verify(securityConfig).getCurrentUser();
    }

    @Test
    void testDeleteMembershipById() {

        Long membershipId = 1L;

        doNothing().when(membershipRepository).deleteById(membershipId);

        membershipService.deleteMembership(membershipId);

        verify(membershipRepository).deleteById(membershipId);

    }

    @Test
    void testCalculateEndDateForSmallMembership() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.SMALL);
        membership.setStartDate(LocalDate.of(2024, 1, 1));

        LocalDate expectedEndDate = LocalDate.of(2024, 1, 1)
                .plusDays(MembershipType.SMALL.getDuration());
        LocalDate actualEndDate = membershipService.calculateEndDate(membership);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    void testCalculateEndDateForBasicMembership() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.BASIC);
        membership.setStartDate(LocalDate.of(2024, 2, 2));

        LocalDate expectedEndDate = LocalDate.of(2024, 2, 2)
                .plusDays(MembershipType.BASIC.getDuration());
        LocalDate actualEndDate = membershipService.calculateEndDate(membership);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    void testCalculateEndDateForMediumMembership() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.MEDIUM );
        membership.setStartDate(LocalDate.of(2024, 3, 3));

        LocalDate expectedEndDate = LocalDate.of(2024, 3, 3)
                .plusDays(MembershipType.MEDIUM .getDuration());
        LocalDate actualEndDate = membershipService.calculateEndDate(membership);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    void testCalculateEndDateForLargeMembership() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.LARGE );
        membership.setStartDate(LocalDate.of(2024, 4, 4));

        LocalDate expectedEndDate = LocalDate.of(2024, 4, 4)
                .plusDays(MembershipType.LARGE .getDuration());
        LocalDate actualEndDate = membershipService.calculateEndDate(membership);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    void testCalculateEndDateForQuarterlyMembership() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.QUARTERLY );
        membership.setStartDate(LocalDate.of(2024, 5, 5));

        LocalDate expectedEndDate = LocalDate.of(2024, 5, 5)
                .plusDays(MembershipType.QUARTERLY .getDuration());
        LocalDate actualEndDate = membershipService.calculateEndDate(membership);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    void testCalculateEndDateForAnnualMembership() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.ANNUAL);
        membership.setStartDate(LocalDate.of(2024, 6, 6));

        LocalDate expectedEndDate = LocalDate.of(2024, 6, 6)
                .plusDays(MembershipType.ANNUAL.getDuration());
        LocalDate actualEndDate = membershipService.calculateEndDate(membership);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    void testCalculateEndDateWhenMembershipTypeNull() {
        Membership membership = new Membership();
        membership.setMembershipType(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            membershipService.calculateEndDate(membership);
        });

        assertEquals("Membership type cannot be null", exception.getMessage());
    }

    @Test
    void testCalculateEndDateWhenMembershipTypeUnsupported() {
        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.UNKNOWN);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            membershipService.calculateEndDate(membership);
        });

        assertEquals("Unsupported membership type", exception.getMessage());
    }
}
