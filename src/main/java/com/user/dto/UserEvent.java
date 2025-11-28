package com.user.dto;

import com.user.core.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserEvent {

        private User user;
        private EventType eventType;
        private Date date;
}
