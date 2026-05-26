package com.lanceraro.api.controller;

import com.lanceraro.api.model.User;
import com.lanceraro.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public User login(@RequestBody User loginData) throws Exception {
        User user = userRepository.findByEmail(loginData.getEmail());
        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            return user;
        }
        throw new Exception("Credenciais da guilda inválidas!");
    }

    @PostMapping("/register")
    public User register(@RequestBody User newUser) throws Exception {
        if (userRepository.findByEmail(newUser.getEmail()) != null) {
            throw new Exception("Este e-mail já pertence a outro aventureiro!");
        }
        newUser.setRole("user");
        return userRepository.save(newUser);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updateData) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            if (updateData.getNickname() != null && !updateData.getNickname().isEmpty()) existingUser.setNickname(updateData.getNickname());
            if (updateData.getPassword() != null && !updateData.getPassword().isEmpty()) existingUser.setPassword(updateData.getPassword());
            if (updateData.getAvatarUrl() != null && !updateData.getAvatarUrl().isEmpty()) existingUser.setAvatarUrl(updateData.getAvatarUrl());
            return userRepository.save(existingUser);
        }
        return null;
    }
}
