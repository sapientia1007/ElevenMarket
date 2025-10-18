package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.UsersService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.users.req.DeactivateRequestDto;
import com.wid.elevenmarket.presentation.dto.users.req.EditUserRequestDto;
import com.wid.elevenmarket.presentation.dto.users.req.JoinUserRequestDto;
import com.wid.elevenmarket.presentation.dto.users.resp.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/join")
    public CommonResponseEntity<UserResponseDto> joinUser(@RequestBody JoinUserRequestDto joinUserRequestDto) {
        return success(usersService.joinUser(joinUserRequestDto));
    }

    @PatchMapping("/edit/{userId}")
    public CommonResponseEntity<UserResponseDto> editPasswordByUser(@PathVariable Long userId, @RequestBody EditUserRequestDto editUserRequestDto) {
        return success(usersService.editUser(userId, editUserRequestDto));
    }

    @GetMapping("/withdraw")
    public CommonResponseEntity<UserResponseDto> withdrawUser(@RequestBody DeactivateRequestDto deactivateRequestDto) {
        return success(usersService.DeactivateUser(deactivateRequestDto));
    }
}
