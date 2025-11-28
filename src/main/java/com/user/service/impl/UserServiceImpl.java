package com.user.service.impl;

import com.user.common.KafkaProducerService;
import com.user.common.exceptions.IncompleteDataException;
import com.user.core.User;
import com.user.core.repository.UserRepository;
import com.user.dto.EventType;
import com.user.dto.UserDto;
import com.user.dto.UserEvent;
import com.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public String createUser(UserDto userDto) {

        if (Objects.nonNull(userDto)) {

            if (Objects.isNull(userDto.getUserName()) || userDto.getUserName().isEmpty()) {
                throw new IncompleteDataException("User name cannot be empty or null");
            }

            if (Objects.isNull(userDto.getPassword()) || userDto.getPassword().isEmpty()) {
                throw new IncompleteDataException("password cannot be empty or null");
            }

            if (Objects.isNull(userDto.getEmail()) || userDto.getEmail().isEmpty()) {
                throw new IncompleteDataException("email cannot be empty or null");
            }

            User user = userRepository.findByEmail(userDto.getEmail());

            if (Objects.nonNull(user)) {
                throw new RuntimeException("user already exists with Email " + userDto.getEmail());
            }

            user = userRepository.findByUsername(userDto.getUserName());
            if (Objects.nonNull(user)) {
                throw new RuntimeException("user already exists with userName " + userDto.getEmail() + "choose different userName");
            }
            user = new User();
            user.setUsername(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRoles("USER");
            user.setUpdatedBy("USER");
            user.setCreatedDate(new Date());
            user.setUpdatedDate(new Date());
            userRepository.save(user);
            kafkaProducerService.publishEvent("user-events", generatePayLoad(user, EventType.CREATE));
            logger.info("User created successfully with userid : {}", userDto.getUserName());
            return "User created successfully with userid " + userDto.getUserName();
        }
        throw new IncompleteDataException("User details are null");
    }

    private UserEvent generatePayLoad(User user, EventType eventType) {
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEventType(eventType);
        userEvent.setDate(new Date());
        return userEvent;
    }

    @Override
    public String updateUser(UserDto userDto) {
        if (Objects.nonNull(userDto)) {

            User user = userRepository.findByUsernameAndEnabled(userDto.getUserName(), true);

            if (Objects.isNull(user)) {
                throw new RuntimeException("user not found " + userDto.getUserName());
            }
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setUpdatedDate(new Date());
            user.setUpdatedBy("USER");
            userRepository.save(user);
            kafkaProducerService.publishEvent("user-events", generatePayLoad(user, EventType.UPDATE));
            logger.info("User updated successfully with userid : {}", userDto.getUserName());
            return "User details has been updated successfully with userid " + userDto.getUserName();

        }
        throw new IncompleteDataException("User details are null");
    }

    @Override
    public UserDto getUserDetails(String userName) {
        logger.info("retrieving user details for the userName : {}", userName);
        if (Objects.nonNull(userName) && !userName.isEmpty()) {
            User user = userRepository.findByUsername(userName);

            if (Objects.isNull(user)) {
                throw new EntityNotFoundException("user does not exist");
            }

            UserDto userDto = new UserDto();
            userDto.setEmail(user.getEmail());
            userDto.setUserName(user.getUsername());
            userDto.setCreatedDate(user.getCreatedDate());
            userDto.setLastUpdatedDate(user.getUpdatedDate());
            kafkaProducerService.publishEvent("user-events", generatePayLoad(user, EventType.READ));
            return userDto;

        }
        throw new IncompleteDataException("Username cannot be empty or null");
    }

    @Override
    public String deleteUser(String userName) {
        logger.info("deleting user details for the userName : {}", userName);
        if (Objects.nonNull(userName) && !userName.isEmpty()) {
            User user = userRepository.findByUsername(userName);

            if (Objects.isNull(user)) {
                throw new EntityNotFoundException("user does not exist");
            }
            userRepository.delete(user);
            kafkaProducerService.publishEvent("user-events", generatePayLoad(user, EventType.DELETE));
            return "User successfully deleted for the userName " + userName;

        }
        throw new IncompleteDataException("Username cannot be empty or null");
    }
}
