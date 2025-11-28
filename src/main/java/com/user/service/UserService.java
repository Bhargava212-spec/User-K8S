package com.user.service;

import com.user.dto.UserDto;

public interface UserService {

    String createUser(UserDto userDto);

    String updateUser(UserDto userDto);

    UserDto getUserDetails(String userName);

    String deleteUser(String userName);
}
