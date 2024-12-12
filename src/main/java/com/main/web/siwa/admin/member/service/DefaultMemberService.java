package com.main.web.siwa.admin.member.service;

import com.main.web.siwa.admin.member.dto.MemberUpdateDto;
import com.main.web.siwa.auth.dto.SignupRequestDto;
import com.main.web.siwa.auth.dto.SignupResponseDto;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.MemberRole;
import com.main.web.siwa.entity.Role;
import com.main.web.siwa.admin.member.dto.MemberListDto;
import com.main.web.siwa.admin.member.dto.MemberResponseDto;
import com.main.web.siwa.admin.member.dto.MemberSearchDto;
import com.main.web.siwa.repository.MemberRepository;
import com.main.web.siwa.repository.MemberRoleRepository;
import com.main.web.siwa.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("AdminMemberService")
public class DefaultMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public DefaultMemberService(MemberRepository memberRepository,
                                RoleRepository roleRepository,
                                ModelMapper modelMapper,
                                PasswordEncoder passwordEncoder, MemberRoleRepository memberRoleRepository) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public MemberResponseDto getList(MemberSearchDto memberSearchDto) {
        return null;
    }

    @Override
    public MemberListDto getById(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId));
        return modelMapper.map(member, MemberListDto.class);
    }

    @Override
    public MemberListDto update(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository
                .findById(memberUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + memberUpdateDto.getId()));

        // 일괄 업데이트가 아닌 부분 업데이트 로직을 구현함
        if(memberUpdateDto.getUsername() != null)
            member.setUsername(memberUpdateDto.getUsername());
        if(memberUpdateDto.getProfileImage() != null)
            member.setProfileImage(memberUpdateDto.getProfileImage());
        if(memberUpdateDto.getProfileName() != null)
            member.setProfileName(memberUpdateDto.getProfileName());
        if(memberUpdateDto.getPassword() != null)
            member.setPassword(memberUpdateDto.getPassword());

        memberRepository.save(member);

        Member updateMember = memberRepository
                .findById(memberUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + memberUpdateDto.getId()));

        return modelMapper.map(updateMember, MemberListDto.class);
    }

    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }




}
