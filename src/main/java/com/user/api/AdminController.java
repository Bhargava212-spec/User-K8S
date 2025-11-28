package com.user.api;

import com.user.service.AdminService;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/admin/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;


    @PostMapping("/disable-user/{userName}")
    public String disableUser(@PathVariable("userName") String userName) {
        return adminService.disableUser(userName);
    }

    @PostMapping("/delete-user/{userName}")
    public String deleteUser(@PathVariable("userName") String userName) {
        return userService.deleteUser(userName);
    }
}
