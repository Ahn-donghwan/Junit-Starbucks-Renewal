package com.team114.starbucks.domain.option.dto.out;

import com.team114.starbucks.domain.option.entity.Option;
import com.team114.starbucks.domain.option.vo.out.GetAllOptionResVo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OptionResponseDto {

    private final Long optionId;
    private final String productUuid;
    private final Long colorId;
    private final Long sizeId;
    private final Integer stock;
    private final Long optionPrice;
    private final Integer discountRate;

    @Builder
    public OptionResponseDto(
            Long optionId,
            String productUuid,
            Long colorId,
            Long sizeId,
            Integer stock,
            Long optionPrice,
            Integer discountRate
    ) {
        this.optionId = optionId;
        this.productUuid = productUuid;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.stock = stock;
        this.optionPrice = optionPrice;
        this.discountRate = discountRate;
    }

    public GetAllOptionResVo toVo() {
        return GetAllOptionResVo.builder()
                .optionId(optionId)
                .productUuid(productUuid)
                .colorId(colorId)
                .sizeId(sizeId)
                .stock(stock)
                .optionPrice(optionPrice)
                .discountRate(discountRate)
                .build();
    }

    public static OptionResponseDto from(Option option) {
        return OptionResponseDto.builder()
                .optionId(option.getOptionId())
                .productUuid(option.getProductUuid())
                .colorId(option.getColor().getColorId())
                .sizeId(option.getSize().getSizeId())
                .stock(option.getStock())
                .optionPrice(option.getOptionPrice())
                .discountRate(option.getDiscountRate())
                .build();

    }
}