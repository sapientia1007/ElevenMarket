package com.wid.elevenmarket.presentation.dto.users.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeactivateRequestDto {
    private Long userId;
    private String userEmail;
    private String userPassword;
}
