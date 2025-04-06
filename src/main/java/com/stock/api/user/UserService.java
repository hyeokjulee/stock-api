package com.stock.api.user;

import com.stock.api.user.UserRepository;
import com.stock.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void registerUserIfNotExists(String email, String name) {

        if (!userRepository.existsByEmail(email)) {
            userRepository.save(new User(email, name));
        }
    }
}
