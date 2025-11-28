package com.user.api;

import com.user.dto.UserDto;
import com.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
@Tag(name = "User Operations", description = "Operations related to users")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    @Operation(summary = "create user", description = "Retrieve a user by their unique ID.")
    public String createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("/update-user")
    public String updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @GetMapping("/get-user-details/{userName}")
    @Operation(
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public UserDto getUserDetails(@PathVariable("userName") String userName) {
        return userService.getUserDetails(userName);
    }

    @DeleteMapping("/delete-user/{userName}")
    public String deleteUser(@PathVariable("userName") String userName) {
        return userService.deleteUser(userName);
    }
}
