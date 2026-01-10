package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.presentation.dto.users.req.DeactivateRequestDto;
import com.wid.elevenmarket.presentation.dto.users.req.EditUserRequestDto;
import com.wid.elevenmarket.presentation.dto.users.req.JoinUserRequestDto;
import com.wid.elevenmarket.presentation.dto.users.resp.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    // 회원 가입
    @Transactional
    public UserResponseDto joinUser(JoinUserRequestDto joinUserRequestDto){
        if (!joinUserRequestDto.getUserPassword().equals(joinUserRequestDto.getUserConfirmPassword())) {
            throw new CustomException("비밀번호 확인이 일치하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        else {
            Users toSaveUser = Users.joinUser(joinUserRequestDto);
            usersRepository.save(toSaveUser);
            return new UserResponseDto(toSaveUser);
        }
    }

    // 비밀번호 수정(회원 정보 수정)
    @Transactional
    public UserResponseDto editUser(Long userId, EditUserRequestDto editUserRequestDto){
        Users savedUser = validateAccount(userId, editUserRequestDto.getUserCurrentPassword());
        if (!editUserRequestDto.getUserNewPassword().equals(editUserRequestDto.getConfirmUserPassword())) {
            throw new CustomException("새로운 비밀번호가 일치하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        else {
            savedUser.editPassword(editUserRequestDto);
        }
        return new UserResponseDto(savedUser);
    }

    // 회원 탈퇴
    @Transactional
    public UserResponseDto DeactivateUser(DeactivateRequestDto deactivateRequestDto){
        Users savedUser = validateAccount(deactivateRequestDto.getUserId(), deactivateRequestDto.getUserPassword());
        if (!savedUser.getEmail().equals(deactivateRequestDto.getUserEmail())) throw new CustomException("이메일을 다시 확인해주세요", HttpStatus.BAD_REQUEST);
        if(deactivateRequestDto.getUserPassword().equals(savedUser.getPassword())) {
            savedUser.dropOut();
            usersRepository.save(savedUser);
        }

        return new UserResponseDto(savedUser);
    }

    private Users validateAccount(Long userId, String currentPassword) {
        Users savedUser = usersRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다", HttpStatus.NOT_FOUND));
        if (!currentPassword.equals(savedUser.getPassword())) {throw  new CustomException("비밀번호가 일치하지 않습니다",  HttpStatus.BAD_REQUEST);}
        else return savedUser;
    }
}
