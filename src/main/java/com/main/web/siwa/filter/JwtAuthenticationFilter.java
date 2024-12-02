package com.main.web.siwa.filter;


import com.main.web.siwa.auth.service.SiwaUserDetailsService;
import com.main.web.siwa.entity.SiwaUserDetails;
import com.main.web.siwa.utility.JwtUtil;
import com.main.web.siwa.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 인증 검사 필터
// 인증은 여러 번 검사 할 필요없이 요청 당(각 thread 마다) 한 번만: OncePerRequestFilter
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    // 내가 구현해도 됨
    private MemberRepository memberRepository;
    private SiwaUserDetailsService siwaUserDetailsService;

    // JWT는 인코딩되어 있어서 디코딩 과정이 필요한데 그 기능을 구현할 클래스
    private JwtUtil jwtUtil;


//    public JwtAuthenticationFilter(JwtUtil jwtUtil){
//         this.jwtUtil = jwtUtil;
//    }
    public JwtAuthenticationFilter(
            MemberRepository memberRepository,
            SiwaUserDetailsService siwaUserDetailsService,
            JwtUtil jwtUtil
    ) {
        this.memberRepository = memberRepository;
        this.siwaUserDetailsService = siwaUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        //https://jwt.io/ JWT가 가지는 구조(예시)
        // JWT의 Header의 Authorization가 있는지
        String authHeader = request.getHeader("Authorization");

        String requestURL = request.getRequestURI();
        if(requestURL.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }


        if(authHeader != null && authHeader.startsWith("Bearer ")) {

            // Header의 Authorization: "Bearer " 이후 문자열을 잘라서 JsonWebToken 얻기
            String token = authHeader.substring(7);

            // 이제 얻은 토큰이 유효한지 검사하는 작업(내가 구현할 예정)
            if(jwtUtil.validateToken(token)) {
                
                // 사용자 정보 꺼내기
                String username = jwtUtil.extractUsername(token);
                List<String> roles = jwtUtil.extractRoles(token);

                if(username != null && !username.isEmpty()) {

                    SiwaUserDetails userDetails =
                            (SiwaUserDetails) siwaUserDetailsService.loadUserByUsername(username);

                    if(userDetails != null){

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 이 필터는 세션에 담아주는 대신 (스레드가 실행 중일 때=)요청이 들어오면 SCH에 토큰을 저장하고 꺼내 씀(stateless)
                        // 서버에 메모리를 사용하는 세션의 단점, 서버 간 공유를 분리하기 위한 분산(마이크로) 시스템에 적합함
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }//
            }
        }
        filterChain.doFilter(request, response);
    } // doFilterInternal
}
