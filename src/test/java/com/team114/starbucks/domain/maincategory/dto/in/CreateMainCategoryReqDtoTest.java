package com.team114.starbucks.domain.maincategory.dto.in;

import com.team114.starbucks.domain.maincategory.entity.MainCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateMainCategoryReqDtoTest {

    @Test
    @DisplayName("DTO를 엔티티로 변환하면 카테고리 정보가 유지된다.")
    void toEntitySuccess() {

        // given : 빌더로 DTO 를 생성
        CreateMainCategoryReqDto dto = CreateMainCategoryReqDto.builder()
                .mainCategoryName("testName")
                .mainCategoryImage("testImage")
                .build();

        // when : dto.toEntity("category-uuid") 를 실행
        MainCategory mainCategory = dto.toEntity("testUuid");

        // then : 엔티티의 UUID, 이름, 이미지를 assertThat() 으로 각각 검증
        assertThat(mainCategory.getMainCategoryName()).isEqualTo("testName");
        assertThat(mainCategory.getMainCategoryImage()).isEqualTo("testImage");
        assertThat(mainCategory.getMainCategoryUuid()).isEqualTo("testUuid");
    }
}