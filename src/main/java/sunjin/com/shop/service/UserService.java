package sunjin.com.shop.service;

import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String email, String password, String name) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
