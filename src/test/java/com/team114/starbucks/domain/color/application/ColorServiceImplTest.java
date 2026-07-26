package com.team114.starbucks.domain.color.application;

import com.team114.starbucks.domain.color.dto.out.ColorResponseDto;
import com.team114.starbucks.domain.color.entity.Color;
import com.team114.starbucks.domain.color.infrastructure.ColorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColorServiceImplTest {

    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private ColorServiceImpl colorService;

    @Test
    @DisplayName("색상 ID로 조회하면 색상 정보를 반환한다.")
    void findByColorId() {

        // given : mock 응답 설정
        Long colorId = 1L;

        Color color = Color.builder()
                .colorName("Green")
                .colorId(colorId)
                .build();

        // [1] Repository 가 호출되면 어떤 값을 돌려줄지 설정
        when(colorRepository.findByColorId(colorId)).thenReturn(Optional.of(color));

        // when : 서비스 실행
        // colorService.findByColorId(colorId) 실행
        ColorResponseDto result = colorService.findByColorId(colorId);

        // then : 반환 결과 검증, Repository 호출 검증
        // 반환된 DTO 의 색상 이름 검증
        assertEquals("Green", result.getColorName());

        // Repository 가 해당 ID 로 한 번 호출됐는지 검증
        verify(colorRepository, times(1)).findByColorId(colorId);
    }
}