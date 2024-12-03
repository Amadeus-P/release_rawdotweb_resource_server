package com.main.web.siwa.auth.controller;

import com.main.web.siwa.auth.dto.SigninRequestDto;
import com.main.web.siwa.auth.dto.SigninResponseDto;
import com.main.web.siwa.auth.dto.SignupRequestDto;
import com.main.web.siwa.auth.dto.SignupResponseDto;
import com.main.web.siwa.auth.service.SignupService;
import com.main.web.siwa.entity.SiwaUserDetails;
import com.main.web.siwa.utility.JwtUtil;
import com.main.web.siwa.error.UserAlreadyExistsException;
import com.main.web.siwa.admin.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    private final SignupService signupService;

    // authenticationManager AutoWired는 Security Config에서
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            SignupService signupService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.signupService = signupService;
    }
    @PostMapping("signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequestDto requestDto
    ) {
        try {
            SignupResponseDto responseDto = signupService.create(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            // 이미 존재하는 회원일 경우 409 Conflict 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // 기타 에러 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            // 500
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입중 오류가 발생했습니다.");
        }
    }

    @PostMapping("signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 객체에 담기
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // UserDetailsService를 통해서 사용자 정보를 받아서 인증을 처리한 후
            // 인증에 성공하게 되면 SecurityContextHolder에 사용자 인증 정보를 담아 놓게 됨.

            // 인증 성공 시 사용자 정보 가져오기
            SiwaUserDetails userDetails = (SiwaUserDetails) authentication.getPrincipal();
            System.out.println("컨트롤러"+ userDetails.getProfileName());
            
            // 토큰 담기
            String token = jwtUtil.generateToken(userDetails); // 여기에 인증 상태

            System.out.println(userDetails.getProfileName());
            System.out.println(userDetails.getProfileImage());
            return ResponseEntity.ok(SigninResponseDto
                    .builder()
                    .memberId(userDetails.getId())
                    .profileName(userDetails.getProfileName())
                    .profileImage(userDetails.getProfileImage())
                    .token(token) // 여기에 인증 상태 정보를 모두 담아서 전달함
                    .build());

        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }



    // 자난 시간 수업 내용 400 에러 상태
//    @PostMapping("signin")
//    public ResponseEntity<?> signin(@RequestParam("username") String username,
//                                 @RequestParam("password") String password){
//
//        System.out.println(username);
//
//        return ResponseEntity.ok("Signin successful");
//    }
}
