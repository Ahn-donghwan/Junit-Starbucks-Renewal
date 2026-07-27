package com.team114.starbucks.domain.cart.dto.in;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUuidReqDto {

    private String memberUuid;
    private String productUuid;

    @Builder
    public ProductUuidReqDto(String memberUuid, String productUuid) {
        this.memberUuid = memberUuid;
        this.productUuid = productUuid;
    }

    public static ProductUuidReqDto of(String memberUuid, String productUuid) {
        return ProductUuidReqDto.builder()
                .memberUuid(memberUuid)
                .productUuid(productUuid)
                .build();
    }

}