package com.main.web.siwa.member.profile.service;

import com.main.web.siwa.entity.Member;
import com.main.web.siwa.member.profile.dto.ProfileListDto;
import com.main.web.siwa.member.profile.dto.ProfileResponseDto;
import com.main.web.siwa.member.profile.dto.ProfileSearchDto;
import com.main.web.siwa.repository.MemberRepository;
import com.main.web.siwa.repository.MemberRoleRepository;
import com.main.web.siwa.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultProfileService implements ProfileService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public DefaultProfileService(MemberRepository memberRepository,
                                 RoleRepository roleRepository,
                                 ModelMapper modelMapper,
                                 PasswordEncoder passwordEncoder, MemberRoleRepository memberRoleRepository) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ProfileResponseDto getList(ProfileSearchDto profileSearchDto) {
        return null;
    }

    @Override
    public ProfileListDto getById(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId));
        return modelMapper.map(member, ProfileListDto.class);
    }

    @Override
    public ProfileListDto update(ProfileListDto profileListDto) {
        Member member = memberRepository
                .findById(profileListDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + profileListDto.getId()));

        // 일괄 업데이트가 아닌 부분 업데이트 로직을 구현함
        if(profileListDto.getUsername() != null)
            member.setUsername(profileListDto.getUsername());
        if(profileListDto.getProfileImage() != null)
            member.setProfileImage(profileListDto.getProfileImage());
        if(profileListDto.getProfileName() != null)
            member.setProfileName(profileListDto.getProfileName());
        if(profileListDto.getPassword() != null)
            member.setPassword(profileListDto.getPassword());

        memberRepository.save(member);

        // 업데이트 된 웹사이트 가져오기
        Member updateMember = memberRepository
                .findById(profileListDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + profileListDto.getId()));

        return modelMapper.map(updateMember, ProfileListDto.class);
    }

    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }




}
