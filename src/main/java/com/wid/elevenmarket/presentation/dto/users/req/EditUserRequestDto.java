package com.wid.elevenmarket.presentation.dto.users.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditUserRequestDto {
    private String userCurrentPassword;
    private String userNewPassword;
    private String confirmUserPassword;
}
