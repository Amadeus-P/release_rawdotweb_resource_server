package com.main.web.siwa.auth.service;

import com.main.web.siwa.entity.SiwaUserDetails;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.MemberRole;
import com.main.web.siwa.repository.MemberRepository;
import com.main.web.siwa.repository.MemberRoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SiwaUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    public SiwaUserDetailsService(MemberRepository memberRepository,
                                  MemberRoleRepository memberRoleRepository) {
        this.memberRepository = memberRepository;
        this.memberRoleRepository = memberRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username);
        System.out.println("회원정보 " + member);
        if(member==null)
            throw new UsernameNotFoundException("회원을 찾을 수 없습니다.");
        List<MemberRole> roles = memberRoleRepository.findAllByMember_Username(username);
        System.out.println("서비스 회원 프로필 이름" + member.getProfileName());

        // 중복방지 set
        Set<SimpleGrantedAuthority> authorities = roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getRole().getName())
        ).collect(Collectors.toSet());

        // SigninResponseDto에 필요한 데이터 보내기
        return SiwaUserDetails
                .builder()
                .id(member.getId())
                .username(username)
                .password(member.getPassword())
                .profileName(member.getProfileName())
                .profileImage(member.getProfileImage())
                .regDate(member.getRegDate())
                .authorities(authorities)
                .build();
    }
}
