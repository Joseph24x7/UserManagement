package com.user.mgmt.IT;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.exception.BadRequestException;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.repository.UserInfoRepository;
import com.user.mgmt.request.LoginRequest;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.request.VerifyEmailRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerIT extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private static final String TEST_EMAIL = "abc@gmail.com";
    private static final String TEST_ROLE = "seller";

    private static UserEntity userEntity;
    private static HttpHeaders httpHeaders;
    private static ResponseEntity<String> verifyResponse;

    @BeforeAll
    public static void setupClass() {
        httpHeaders = new HttpHeaders();
    }

    @Test
    @Order(0)
    public void testLoginWithAccessCode() {
        LoginRequest emailRequest = new LoginRequest();
        emailRequest.setEmail(TEST_EMAIL);
        emailRequest.setRole(TEST_ROLE);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/login-with-access-code", emailRequest, String.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    }

    @Test
    @Order(1)
    public void testVerifyAccessCode() {
        userEntity = userInfoRepository.findByEmail(TEST_EMAIL).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));

        VerifyEmailRequest verifyRequest = new VerifyEmailRequest();
        verifyRequest.setEmail(TEST_EMAIL);
        verifyRequest.setAccessCode(String.valueOf(userEntity.getOtp()));

        verifyResponse = restTemplate.postForEntity("/verify-access-code", verifyRequest, String.class);
        assertEquals(HttpStatus.OK, verifyResponse.getStatusCode());
        assertNotNull(verifyResponse.getBody());
    }

    @Test
    @Order(2)
    public void testUserInfo() {
        httpHeaders.add(HttpHeaders.AUTHORIZATION, verifyResponse.getBody());
        httpHeaders.add("X-BookMyGift-Role", TEST_ROLE);

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

        userEntity = userInfoRepository.findByEmail(TEST_EMAIL).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));
        assertEquals(HttpStatus.OK, updateEntityResponse.getStatusCode());
        assertEquals(userEntity, updateEntityResponse.getBody());
    }
}
