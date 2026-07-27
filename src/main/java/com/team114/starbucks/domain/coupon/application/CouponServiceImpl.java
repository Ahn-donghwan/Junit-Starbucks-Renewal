package com.team114.starbucks.domain.coupon.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team114.starbucks.common.exception.BaseException;
import com.team114.starbucks.common.response.BaseResponseStatus;
import com.team114.starbucks.domain.coupon.dto.in.CreateCouponReqDto;
import com.team114.starbucks.domain.coupon.dto.in.UpdateCouponReqDto;
import com.team114.starbucks.domain.coupon.dto.out.CreateCouponResDto;
import com.team114.starbucks.domain.coupon.dto.out.GetAllCouponsResDto;
import com.team114.starbucks.domain.coupon.dto.out.GetCouponResDto;
import com.team114.starbucks.domain.coupon.dto.out.UpdateCouponResDto;
import com.team114.starbucks.domain.coupon.entity.Coupon;
import com.team114.starbucks.domain.coupon.infrastructure.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public CreateCouponResDto saveCoupon(CreateCouponReqDto createCouponReqDto) {

        try {
            Coupon newCoupon = createCouponReqDto.toEntity(UUID.randomUUID().toString());

            Coupon savedCoupon = couponRepository.save(newCoupon);

            return CreateCouponResDto.from(savedCoupon);

        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_SAVE);
        }
    }

    @Override
    public List<GetAllCouponsResDto> findAllCoupons() {

        return couponRepository.findAll().stream().map(GetAllCouponsResDto::from).toList();

    }

    @Override
    public GetCouponResDto findCouponByUuid(String couponUuid) {

        Coupon coupon = couponRepository.findByCouponUuid(couponUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_FIND));

        return GetCouponResDto.from(coupon);
    }

    @Transactional
    @Override
    public UpdateCouponResDto updateCoupon(String couponUuid, UpdateCouponReqDto updateCouponReqDto) {

        Coupon coupon = couponRepository.findByCouponUuid(couponUuid).orElseThrow(
                () -> new BaseException(BaseResponseStatus.FAILED_TO_FIND)
        );

        Coupon updatedCoupon = Coupon.builder()
                .id(coupon.getId())
                .couponUuid(coupon.getCouponUuid())
                .name(updateCouponReqDto.getCouponName() == null ? coupon.getName() : updateCouponReqDto.getCouponName())
                .description(updateCouponReqDto.getCouponDescription() == null ? coupon.getDescription() : updateCouponReqDto.getCouponDescription())
                .discountType(updateCouponReqDto.getDiscountType() == null ? coupon.getDiscountType() : updateCouponReqDto.getDiscountType())
                .discountValue(updateCouponReqDto.getDiscountValue() == null ? coupon.getDiscountValue() : updateCouponReqDto.getDiscountValue())
                .minOrderPrice(updateCouponReqDto.getMinOrderPrice() == null ? coupon.getMinOrderPrice() : updateCouponReqDto.getMinOrderPrice())
                .maxDiscountPrice(updateCouponReqDto.getMaxDiscountPrice() == null ? coupon.getMaxDiscountPrice() : updateCouponReqDto.getMaxDiscountPrice())
                .validDays(updateCouponReqDto.getValidDays() == null ? coupon.getValidDays() : updateCouponReqDto.getValidDays())
                .build();

        couponRepository.save(updatedCoupon);

        return UpdateCouponResDto.from(updatedCoupon);
    }

    @Transactional
    @Override
    public void deleteCoupon(String couponUuid) {

        couponRepository.deleteByCouponUuid(couponUuid).orElseThrow(
                () -> new BaseException(BaseResponseStatus.FAILED_TO_FIND)
        );
    }
}
