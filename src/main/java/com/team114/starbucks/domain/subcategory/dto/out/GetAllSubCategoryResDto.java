package com.team114.starbucks.domain.subcategory.dto.out;

import com.team114.starbucks.domain.subcategory.entity.SubCategory;
import com.team114.starbucks.domain.subcategory.vo.out.GetAllSubCategoryResVo;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GetAllSubCategoryResDto {

    private String mainCategoryUuid;
    private String subCategoryUuid;
    private String subCategoryName;

    @Builder
    public GetAllSubCategoryResDto(String mainCategoryUuid, String subCategoryUuid, String subCategoryName) {
        this.mainCategoryUuid = mainCategoryUuid;
        this.subCategoryUuid = subCategoryUuid;
        this.subCategoryName = subCategoryName;
    }

    public static GetAllSubCategoryResDto from(SubCategory subCategory) {
        return GetAllSubCategoryResDto.builder()
                .mainCategoryUuid(subCategory.getMainCategoryUuid())
                .subCategoryUuid(subCategory.getSubCategoryUuid())
                .subCategoryName(subCategory.getSubCategoryName())
                .build();

    }

    public GetAllSubCategoryResVo toVo() {
        return GetAllSubCategoryResVo.builder()
                .mainCategoryUuid(mainCategoryUuid)
                .subCategoryUuid(subCategoryUuid)
                .subCategoryName(subCategoryName)
                .build();
    }
}
