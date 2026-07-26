package com.team114.starbucks.domain.auth.dto.in;

import com.team114.starbucks.domain.member.entity.Member;
import com.team114.starbucks.domain.member.enums.Gender;
import com.team114.starbucks.domain.member.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CreateSignUpReqDtoTest {

    @Test
    @DisplayName("회원가입 DTO를 엔티티로 변환하면 회원 정보가 설정된다.")
    void toEntitySuccess() {

        /*

            검증할 내용 :
            - DTO 의 이메일과 이름이 엔티티에 전달된다.
            - toEntity() 에 전달한 암호화 비밀번호가 저장된다.
            - 사용자 역할이 ROLE_USER 로 설정된다.
            - 내부에서 생성한 UUID 가 null 도 아니고 빈 문자열도 아니다.

        * */

        // given
        Date birthday = Date.from(Instant.parse(
                "2000-01-01T00:00:00Z")
        );

        CreateSignUpReqDto reqDto = CreateSignUpReqDto.builder()
                .email("testEmail")
                .name("testName")
                .nickname("testNickname")
                .password("testPassword")
                .birthday(birthday)
                .phoneNumber("testPhoneNumber")
                .gender(Gender.GENDER_MALE)
                .build();

        // when
        Member member = reqDto.toEntity("test-password");

        // then
        assertNotNull(member.getMemberUuid());
        assertFalse(member.getMemberUuid().isBlank());

        assertEquals("testEmail", member.getEmail());
        assertEquals("testName", member.getName());
        assertEquals("testNickname", member.getNickname());
        assertEquals("test-password", member.getPassword());
        assertEquals(birthday, member.getBirthday());
        assertEquals("testPhoneNumber", member.getPhoneNumber());
        assertEquals(Gender.GENDER_MALE, member.getGender());
        assertEquals(UserRole.ROLE_USER, member.getUserRole());
    }

}