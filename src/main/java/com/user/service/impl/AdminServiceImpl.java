package com.user.service.impl;

import com.user.common.exceptions.IncompleteDataException;
import com.user.core.User;
import com.user.core.repository.UserRepository;
import com.user.dto.UserDto;
import com.user.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private UserRepository userRepository;

    @Override
    public String disableUser(String userName) {

        if (Objects.isNull(userName) || userName.isEmpty()) {
            throw new IncompleteDataException("User name cannot be empty or null");
        }

        User user = userRepository.findByUsername(userName);

        if (Objects.isNull(user)) {
            throw new RuntimeException("user does not exist with userName " + userName);
        }
        user.setEnabled(false);
        user.setUpdatedBy("ADMIN");
        userRepository.save(user);
        return "User has been successfully disabled by Admin with userName" + userName;
    }
}
