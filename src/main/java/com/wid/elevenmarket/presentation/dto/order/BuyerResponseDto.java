package com.wid.elevenmarket.presentation.dto.order;

import com.wid.elevenmarket.model.Users;
import lombok.Getter;

public class BuyerResponseDto {    @Getter
    private Long id;
    private String userName;
    private String email;

    public BuyerResponseDto(Users buyer) {
        this.id = buyer.getId();
        this.userName = buyer.getUserName();
        this.email = buyer.getEmail();
    }
}
