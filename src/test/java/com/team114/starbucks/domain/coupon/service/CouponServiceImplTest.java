package com.team114.starbucks.domain.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team114.starbucks.common.exception.BaseException;
import com.team114.starbucks.domain.coupon.application.CouponServiceImpl;
import com.team114.starbucks.domain.coupon.dto.in.CreateCouponReqDto;
import com.team114.starbucks.domain.coupon.dto.out.CreateCouponResDto;
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
	
	@Test
	@DisplayName("유효하지 않은 쿠폰 UUID로 조회하면 예외가 발생한다.")
	void findCouponByUuidFail() {
		
		// given
		String unvalidUuid = "unvalid-uuid";
		when(couponRepository.findByCouponUuid(unvalidUuid)).thenReturn(Optional.empty());
		
		// when
		// then
		assertThrows(BaseException.class, () -> couponService.findCouponByUuid(unvalidUuid));
		verify(couponRepository).findByCouponUuid(unvalidUuid);
		
	}
	
	@Test
	@DisplayName("쿠폰 저장에 성공한다.")
	void saveCouponSuccess() {
		
		// given
		CreateCouponReqDto dto = createCouponReqDto();
		Coupon coupon = createCoupon(dto);
		
		when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);
		
		// when
		CreateCouponResDto result = couponService.saveCoupon(dto);
		
		// then
		assertNotNull(result.getCouponUuid());
		assertFalse(result.getCouponUuid().isBlank());
		
		assertEquals(coupon.getName(), result.getCouponName());
		assertEquals(coupon.getDescription(), result.getCouponDescription());
		assertEquals(coupon.getDiscountType(), result.getDiscountType());
		assertEquals(coupon.getDiscountValue(), result.getDiscountValue());
		assertEquals(coupon.getMinOrderPrice(), result.getMinOrderPrice());
		assertEquals(coupon.getValidDays(), result.getValidDays());
		
		verify(couponRepository).save(any(Coupon.class));
		
	}

	private CreateCouponReqDto createCouponReqDto() {
		return CreateCouponReqDto.builder()
				.couponName("coupon-name")
				.couponDescription("coupon-description")
				.discountType(DiscountType.DISCOUNT_TYPE_PERCENT)
				.discountValue(50)
				.minOrderPrice(12000)
				.maxDiscountPrice(2000)
				.validDays(30L)
				.build();
	}
	
	private Coupon createCoupon(CreateCouponReqDto dto) {
		String couponUuid = "coupon-uuid";
		return Coupon.builder()
				.id(1L)
				.couponUuid(couponUuid)
				.name(dto.getCouponName())
				.description(dto.getCouponDescription())
				.discountType(dto.getDiscountType())
				.discountValue(dto.getDiscountValue())
				.minOrderPrice(dto.getMinOrderPrice())
				.maxDiscountPrice(dto.getMaxDiscountPrice())
				.validDays(dto.getValidDays())
				.build();
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
