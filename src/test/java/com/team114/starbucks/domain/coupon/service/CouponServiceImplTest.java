package com.team114.starbucks.domain.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team114.starbucks.domain.coupon.application.CouponServiceImpl;
import com.team114.starbucks.domain.coupon.dto.out.GetCouponResDto;
import com.team114.starbucks.domain.coupon.entity.Coupon;
import com.team114.starbucks.domain.coupon.enums.DiscountType;
import com.team114.starbucks.domain.coupon.infrastructure.CouponRepository;

@ExtendWith(MockitoExtension.class)
public class CouponServiceImplTest {

	@Mock
	private CouponRepository couponRepository;
	
	@InjectMocks
	private CouponServiceImpl couponService;
	
	@Test
	@DisplayName("쿠폰 UUID로 조회하면 쿠폰 정보를 반환한다.")
	void findCouponByUuidSuccess() {
		
		// given	
		Coupon coupon = createCoupon();
		String couponUuid = coupon.getCouponUuid();
		
		when(couponRepository.findByCouponUuid(couponUuid)).thenReturn(Optional.of(coupon));
		
		// when
		GetCouponResDto result = couponService.findCouponByUuid(couponUuid);
		
		// then
		assertEquals(coupon.getCouponUuid(), result.getCouponUuid());
		assertEquals(coupon.getName(), result.getCouponName());
		assertEquals(coupon.getDescription(), result.getCouponDescription());
		assertEquals(coupon.getDiscountType(), result.getDiscountType());
		assertEquals(coupon.getDiscountValue(), result.getDiscountValue());
		assertEquals(coupon.getMinOrderPrice(), result.getMinOrderPrice());
		assertEquals(coupon.getMaxDiscountPrice(), result.getMaxDiscountPrice());
		assertEquals(coupon.getValidDays(), result.getValidDays());
		
		verify(couponRepository, times(1)).findByCouponUuid(couponUuid);
		
	}

	private Coupon createCoupon() {
		String couponUuid = "coupon-uuid";
		return Coupon.builder()
				.id(1L)
				.couponUuid(couponUuid)
				.name("coupon-name")
				.description("coupon-description")
				.discountType(DiscountType.DISCOUNT_TYPE_PERCENT)
				.discountValue(50)
				.minOrderPrice(12000)
				.maxDiscountPrice(2000)
				.validDays(30L)
				.build();
	}
	
}
