package sunjin.com.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.config.JwtUtil;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "유저 등록", description = "새로운 유저를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "유저 등록 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (ex: 이메일 중복)")
    public ResponseEntity<User> registerUser(
            @Parameter(description = "이메일") @RequestParam String email,
            @Parameter(description = "비밀번호") @RequestParam String password,
            @Parameter(description = "이름") @RequestParam String name) {
        User user = userService.createUser(email, password, name);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저가 로그인하고 JWT 토큰을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    public ResponseEntity<String> login(
            @Parameter(description = "이메일") @RequestParam String email,
            @Parameter(description = "비밀번호") @RequestParam String password) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // 인증 성공 시 JWT 토큰 생성
        String jwtToken = jwtUtil.generateToken(email);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/me")
    @Operation(summary = "현재 유저 정보 조회", description = "현재 로그인한 유저 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저 정보 반환")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{email}")
    @Operation(summary = "유저 조회", description = "이메일로 유저를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저 정보 반환")
    @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    public ResponseEntity<User> getUser(
            @Parameter(description = "이메일") @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    @Operation(summary = "유저 로그아웃", description = "유저가 로그아웃합니다. 클라이언트에서 토큰을 삭제하세요.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 성공");
    }
}