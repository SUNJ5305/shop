package sunjin.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.dto.LoginRequest;
import sunjin.com.shop.dto.RegisterRequest;
import sunjin.com.shop.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * UserController 생성자
     * @param userService 사용자 서비스
     * @param authenticationManager 인증 매니저
     */
    @Autowired
    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 사용자 등록 API
     * @param request 등록 요청 데이터
     * @return 등록된 사용자 객체
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@ModelAttribute RegisterRequest request) {
        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
        return ResponseEntity.ok(user);
    }

    /**
     * 로그인 API
     * @param request 로그인 요청 데이터
     * @return 생성된 JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@ModelAttribute LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = userService.generateJwtToken(request.getEmail());
        return ResponseEntity.ok(jwtToken);
    }

    /**
     * 현재 사용자 정보 조회 API
     * @param userDetails 현재 인증된 사용자 정보
     * @return 현재 사용자 객체
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    /**
     * 이메일로 사용자 조회 API
     * @param email 조회할 사용자 이메일
     * @return 사용자 객체
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 로그아웃 API
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 성공");
    }
}