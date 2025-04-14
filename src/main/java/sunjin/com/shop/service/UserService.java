package sunjin.com.shop.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.dto.RegisterRequest;
import sunjin.com.shop.repository.UserRepository;

import java.security.Key;
import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String jwtSecret;
    private static final long JWT_EXPIRATION_MS = 86_400_000; // 24시간 (토큰 유효 기간)

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String jwtSecret
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
    }

    public User createUser(RegisterRequest request) {
        checkIfEmailExists(request.getEmail());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getPassword());

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

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