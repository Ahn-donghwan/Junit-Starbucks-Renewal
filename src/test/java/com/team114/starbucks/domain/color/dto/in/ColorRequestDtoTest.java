package com.team114.starbucks.domain.color.dto.in;

import com.team114.starbucks.domain.color.entity.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ColorRequestDtoTest {

    @Test
    @DisplayName("DTO를 엔티티로 변환하면 색상명이 유지된다.")
    void toEntitySuccess() {

        // given
        ColorRequestDto dto = ColorRequestDto.builder()
                .colorName("Green")
                .build();

        // when
        Color color = dto.toEntity();

        // then
        assertThat(color.getColorName()).isEqualTo("Green");
    }
}