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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerIT extends IntegrationTestBase {

    private static String verifyResponse;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private static final String TEST_EMAIL = "abc@gmail.com";
    private static final String TEST_ROLE = "SELLER";

    private static UserEntity userEntity;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public static void setupClass() {
    }

    @Test
    @Order(0)
    public void testLoginWithAccessCode() {
        LoginRequest emailRequest = new LoginRequest();
        emailRequest.setEmail(TEST_EMAIL);
        emailRequest.setRole(TEST_ROLE);

        webTestClient.post()
                .uri("/login-with-access-code")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(emailRequest)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    @Order(1)
    public void testVerifyAccessCode() {
        userEntity = userInfoRepository.findByEmail(TEST_EMAIL).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));

        VerifyEmailRequest verifyRequest = new VerifyEmailRequest();
        verifyRequest.setEmail(TEST_EMAIL);
        verifyRequest.setAccessCode(String.valueOf(userEntity.getOtp()));

        verifyResponse = webTestClient.post()
                .uri("/verify-access-code")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(verifyRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(Assertions::assertNotNull)
                .returnResult().getResponseBody();

    }

    @Test
    @Order(2)
    public void testUserInfo() {

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/user-info")
                        .queryParam("role", TEST_ROLE)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, verifyResponse)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    @Order(3)
    public void testUpdateUserInfo() {
        MyProfileRequest myProfileRequest = new MyProfileRequest();
        myProfileRequest.setEmail(userEntity.getEmail());
        myProfileRequest.setCity("Chennai");
        myProfileRequest.setUsername("Joseph123");

        webTestClient.put()
                .uri("/update-user-info")
                .header(HttpHeaders.AUTHORIZATION, verifyResponse)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(myProfileRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserEntity.class)
                .value(updatedUserEntity -> {
                    userEntity = userInfoRepository.findByEmail(TEST_EMAIL).orElseThrow(() -> new BadRequestException(ErrorEnums.USER_NOT_FOUND));
                    assertEquals(userEntity, updatedUserEntity);
                });

    }
}
