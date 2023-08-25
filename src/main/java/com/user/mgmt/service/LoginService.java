package com.user.mgmt.service;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.exception.BadRequestException;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.mapper.MyProfileMapper;
import com.user.mgmt.repository.UserInfoRepository;
import com.user.mgmt.request.LoginWithEmailRequest;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.util.CommonUtil;
import com.user.mgmt.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserInfoRepository userInfoRepository;
    private final EmailService emailService;
    private final TokenUtil tokenUtil;
    private final MyProfileMapper myProfileMapper = MyProfileMapper.INSTANCE;

    public UserEntity saveUserInfo(String email, String actionType) {

        Optional<UserEntity> optionalGoogleUserEntity = userInfoRepository.findByEmail(email);

        UserEntity userEntity;
        if (optionalGoogleUserEntity.isPresent()) {
            userEntity = optionalGoogleUserEntity.get();
        } else {
            userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setRegisteredOn(LocalDateTime.now());
        }

        if (CommonUtil.LOGIN_WITH_ACCESS_CODE.equalsIgnoreCase(actionType)) {
            userEntity.setOtp(generateTwoFaCode());
            emailService.sendOtpEmail(email, userEntity.getOtp());
        }

        userEntity.setRecentLogin(LocalDateTime.now());
        return userInfoRepository.save(userEntity);

    }

    public UserEntity updateUserInfo(MyProfileRequest myProfileRequest, String email) {

        Optional<UserEntity> optionalGoogleUserEntity = userInfoRepository.findByEmail(email);

        if (optionalGoogleUserEntity.isPresent()) {
            UserEntity userEntity = optionalGoogleUserEntity.get();
            myProfileMapper.updateUserEntity(myProfileRequest, userEntity);
            return userInfoRepository.save(userEntity);
        } else {
            throw new BadRequestException(ErrorEnums.USER_NOT_FOUND);
        }
    }

    public String verifyAccessCode(LoginWithEmailRequest emailRequest) {
        UserEntity userEntity = userInfoRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));
        if (userEntity.getOtp().equals(emailRequest.getAccessCode())) {
            return tokenUtil.generateToken(userEntity);
        } else {
            throw new BadRequestException(ErrorEnums.INVALID_ACCESS_CODE);
        }
    }

    private Integer generateTwoFaCode() {
        return new Random().nextInt(900000) + 100000;
    }


}