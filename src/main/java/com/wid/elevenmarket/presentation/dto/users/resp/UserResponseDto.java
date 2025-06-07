package com.wid.elevenmarket.presentation.dto.users.resp;

import com.wid.elevenmarket.model.Users;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String userName;

    public UserResponseDto(Users user) {
        this.id = user.getId();
        this.userName = user.getUserName();
    }
}
