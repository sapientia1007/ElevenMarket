package com.wid.elevenmarket.presentation.dto.users.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinUserRequestDto {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userConfirmPassword;
    private String userPhone;
}
