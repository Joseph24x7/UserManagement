package com.user.mgmt.IT;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.exception.BadRequestException;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.repository.UserInfoRepository;
import com.user.mgmt.request.LoginWithEmailRequest;
import com.user.mgmt.request.MyProfileRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerIT extends IntegrationTestBase {

    private static final LoginWithEmailRequest emailRequest = new LoginWithEmailRequest();
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private static ResponseEntity<String> verifyResponse;
    private static UserEntity userEntity;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    @Order(0)
    public void testLoginWithAccessCode() {
        emailRequest.setEmail("abc@gmail.com");
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/login-with-access-code", emailRequest, String.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    }

    @Test
    @Order(1)
    public void testVerifyAccessCode() {
        userEntity = userInfoRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));

        emailRequest.setAccessCode(userEntity.getOtp());
        verifyResponse = restTemplate.postForEntity("/verify-access-code", emailRequest, String.class);
        assertEquals(HttpStatus.OK, verifyResponse.getStatusCode());
        assertNotNull(verifyResponse.getBody());
    }

    @Test
    @Order(2)
    public void testUserInfo() {

        httpHeaders.add(HttpHeaders.AUTHORIZATION, verifyResponse.getBody());
        HttpEntity<?> userInfoEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange("/user-info", HttpMethod.GET, userInfoEntity, String.class);
        assertEquals(HttpStatus.OK, userInfoResponse.getStatusCode());
    }

    @Test
    @Order(3)
    public void testUpdateUserInfo() {
        MyProfileRequest myProfileRequest = new MyProfileRequest();
        myProfileRequest.setEmail(userEntity.getEmail());
        myProfileRequest.setCity("Chennai");
        myProfileRequest.setUsername("Joseph123");
        HttpEntity<?> updateInfoEntity = new HttpEntity<>(myProfileRequest, httpHeaders);
        ResponseEntity<UserEntity> updateEntityResponse = restTemplate.exchange("/update-user-info", HttpMethod.PUT, updateInfoEntity, UserEntity.class);

        userEntity = userInfoRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));
        assertEquals(HttpStatus.OK, updateEntityResponse.getStatusCode());
        assertEquals(userEntity, updateEntityResponse.getBody());
    }

}
