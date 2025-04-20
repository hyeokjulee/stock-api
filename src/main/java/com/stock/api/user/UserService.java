package com.stock.api.user;

import com.stock.api.user.UserRepository;
import com.stock.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User registerUserIfNotExists(String email, String name) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = userRepository.save(new User(email, name));
        }

        return user;
    }
}
