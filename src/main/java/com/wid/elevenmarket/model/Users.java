package com.wid.elevenmarket.model;

import com.wid.elevenmarket.global.entity.BaseTimeEntity;
import com.wid.elevenmarket.presentation.dto.users.req.EditUserRequestDto;
import com.wid.elevenmarket.presentation.dto.users.req.JoinUserRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private Integer validation;

    public static Users joinUser(JoinUserRequestDto joinUserRequestDto) {
        Users newUser = new Users(null, joinUserRequestDto.getUserName(), joinUserRequestDto.getUserEmail(),
                joinUserRequestDto.getUserPassword(), joinUserRequestDto.getUserPhone(), 1);
        return newUser;
    }

    public void editPassword(EditUserRequestDto editUserRequestDto) {
        this.password = editUserRequestDto.getUserNewPassword();
    }

    public void dropOut() {
        this.validation = 0;
    }
}
