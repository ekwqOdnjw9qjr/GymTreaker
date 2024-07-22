package ru.fitnes.fitnestreaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.MembershipDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.Exception;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipMapper membershipMapper;
    private final MembershipRepository membershipRepository;


    public MembershipDto getById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(()-> new Exception(ErrorType.NOT_FOUND,"Membership with id: " + id + " not found"));
        return membershipMapper.toDto(membership);
    }

    public List<MembershipDto> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.toListDto(membershipList);
    }

    public MembershipDto create(MembershipDto membershipDto) {
        Membership membership = membershipMapper.toEntity(membershipDto);
        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.toDto(savedMembership);
    }

    public MembershipDto update(MembershipDto membershipDto, Long id) {
        Membership oldMembership = membershipRepository.findById(id)
                .orElseThrow(()-> new Exception(ErrorType.NOT_FOUND,"Membership with id: " + id + " not found"));
        Membership newMembership = membershipMapper.toEntity(membershipDto);
        membershipMapper.merge(oldMembership,newMembership);
        Membership savedMembership = membershipRepository.save(oldMembership);
        return membershipMapper.toDto(savedMembership);
    }

    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }
}
