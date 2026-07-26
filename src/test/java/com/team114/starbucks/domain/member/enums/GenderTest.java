package com.team114.starbucks.domain.member.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenderTest {

    @Test
    @DisplayName("남성을 입력하면 GENDER_MALE을 반환한다.")
    void fromStringMaleSuccess() {

        // given
        String value = "남성";

        // when
        Gender gender = Gender.fromString(value);

        // then
        assertEquals(Gender.GENDER_MALE, gender);

    }

    @Test
    @DisplayName("존재하지 않는 성별을 입력하면 예외가 발생한다.")
    void fromStringFail() {

        // given
        String value = "알 수 없음";

        // when
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> Gender.fromString(value)
        );

        // then
        assertEquals(
                "Unknown value: " + value,
                exception.getMessage()
        );
    }
}