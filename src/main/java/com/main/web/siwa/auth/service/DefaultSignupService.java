package com.main.web.siwa.auth.service;

import com.main.web.siwa.auth.dto.SignupRequestDto;
import com.main.web.siwa.auth.dto.SignupResponseDto;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.MemberRole;
import com.main.web.siwa.entity.Role;
import com.main.web.siwa.repository.MemberRepository;
import com.main.web.siwa.repository.MemberRoleRepository;
import com.main.web.siwa.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultSignupService implements SignupService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberRoleRepository memberRoleRepository;

    public DefaultSignupService(MemberRepository memberRepository,
                                RoleRepository roleRepository,
                                ModelMapper modelMapper,
                                PasswordEncoder passwordEncoder, MemberRoleRepository memberRoleRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.memberRoleRepository = memberRoleRepository;
    }

    @Override
    public SignupResponseDto create(SignupRequestDto requestDto) {
        // 중복 검사
        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + requestDto.getUsername());
        }
        // 비번 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(encodedPassword);

        Member createdMember = memberRepository.save(modelMapper.map(requestDto, Member.class));

        // 회원, 관리자
        Role role = roleRepository.findByName("ROLE_MEMBER");
        if (role == null) {
            throw new IllegalArgumentException("기본 역할을 찾을 수 없습니다.");
        }
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(createdMember);
        memberRole.setRole(role);

        System.out.println("memberRole.getMember()" + memberRole.getMember());
        System.out.println("memberRole.getRole()" + memberRole.getRole());

        memberRoleRepository.save(memberRole);

        SignupResponseDto responseDto = SignupResponseDto.builder()
                .id(createdMember.getId())
                .username(createdMember.getUsername())
                .profileName(createdMember.getProfileName())
                .profileImage(createdMember.getProfileImage())
                .build();
        return responseDto;
    }
}
