package sunjin.com.shop.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.repository.UserRepository;

import java.security.Key;
import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String jwtSecret;
    private static final long JWT_EXPIRATION_MS = 86_400_000; // 24시간 (토큰 유효 기간)

    /**
     * UserService 생성자
     * @param userRepository 사용자 리포지토리
     * @param passwordEncoder 비밀번호 암호화 객체
     * @param jwtSecret JWT 서명에 사용할 비밀 키
     */
    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String jwtSecret
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
    }

    /**
     * 새로운 사용자를 생성한다.
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @param name 사용자 이름
     * @return 생성된 사용자 객체
     * @throws IllegalArgumentException 이메일이 이미 존재할 경우
     */
    public User createUser(String email, String password, String name) {
        checkIfEmailExists(email);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);

        return userRepository.save(user);
    }

    /**
     * 이메일로 사용자를 조회한다.
     * @param email 사용자 이메일
     * @return 사용자 객체, 없으면 null
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 사용자 이메일로 JWT 토큰을 생성한다.
     * @param email 사용자 이메일
     * @return 생성된 JWT 토큰
     */
    public String generateJwtToken(String email) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    private void checkIfEmailExists(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + email);
        }
    }
}