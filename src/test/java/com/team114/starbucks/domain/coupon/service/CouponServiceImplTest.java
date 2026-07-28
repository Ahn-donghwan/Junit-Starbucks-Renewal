package com.team114.starbucks.domain.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team114.starbucks.common.exception.BaseException;
import com.team114.starbucks.common.response.BaseResponseStatus;
import com.team114.starbucks.domain.coupon.application.CouponServiceImpl;
import com.team114.starbucks.domain.coupon.dto.in.CreateCouponReqDto;
import com.team114.starbucks.domain.coupon.dto.in.UpdateCouponReqDto;
import com.team114.starbucks.domain.coupon.dto.out.CreateCouponResDto;
import com.team114.starbucks.domain.coupon.dto.out.GetAllCouponsResDto;
import com.team114.starbucks.domain.coupon.dto.out.GetCouponResDto;
import com.team114.starbucks.domain.coupon.dto.out.UpdateCouponResDto;
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
	
	@Test
	@DisplayName("쿠폰 저장에 실패한다.")
	void saveCouponFail() {
		
		// given
		CreateCouponReqDto dto = createCouponReqDto();
		
		when(couponRepository.save(any(Coupon.class))).thenThrow(new RuntimeException());
		
		// when
		// then
		BaseException exception = assertThrows(
				BaseException.class, () -> couponService.saveCoupon(dto));
		assertEquals(BaseResponseStatus.FAILED_TO_SAVE, exception.getStatus());
		
		verify(couponRepository).save(any(Coupon.class));
	}
	
	@Test
	@DisplayName("쿠폰 전체 조회에 성공한다.")
	void findAllCouponsSuccess() {
		
		// given
		Coupon coupon1 = Coupon.builder()
				.id(1L)
				.couponUuid("coupon-uuid-1")
				.name("coupon-name-1")
				.description("coupon-description-1")
				.discountType(DiscountType.DISCOUNT_TYPE_PRICE)
				.discountValue(2000)
				.minOrderPrice(10000)
				.maxDiscountPrice(2000)
				.validDays(30L)
				.build();
		
		Coupon coupon2 = Coupon.builder()
				.id(2L)
				.couponUuid("coupon-uuid-2")
				.name("coupon-name-2")
				.description("coupon-description-2")
				.discountType(DiscountType.DISCOUNT_TYPE_PERCENT)
				.discountValue(10)
				.minOrderPrice(10000)
				.maxDiscountPrice(2000)
				.validDays(30L)
				.build();
		
		List<Coupon> coupons = List.of(coupon1, coupon2);
		
		when(couponRepository.findAll()).thenReturn(coupons);
		
		// when
		List<GetAllCouponsResDto> result = couponService.findAllCoupons();
		
		// then
		assertEquals(2, result.size());
		
		assertEquals(coupon1.getCouponUuid(), result.get(0).getCouponUuid());
		assertEquals(coupon2.getCouponUuid(), result.get(1).getCouponUuid());
		
		assertEquals(coupon1.getName(), result.get(0).getName());
		assertEquals(coupon2.getName(), result.get(1).getName());
		
		verify(couponRepository).findAll();
		
	}
	
	@Test
	@DisplayName("등록된 쿠폰이 없으면 빈 목록을 반환한다.")
	void findAllCouponsEmpty() {
		
		// given
		when(couponRepository.findAll()).thenReturn(Collections.emptyList());
		
		// when
		List<GetAllCouponsResDto> result = couponService.findAllCoupons();
		
		// then
		assertTrue(result.isEmpty());
		
		verify(couponRepository).findAll();
		
	}
	
	@Test
	@DisplayName("쿠폰 수정에 성공한다.")
	void updateCouponSuccess() {
		
		// given : 기존 쿠폰 준비, 수정 요청 DTO 준비, 
		// 		   findByCouponUuid() 가 기존 쿠폰 반환
		String couponUuid = "coupon-uuid";
		
		Coupon coupon = Coupon.builder()
				.id(1L)
				.couponUuid(couponUuid)
				.name("기존 쿠폰")
				.discountType(DiscountType.DISCOUNT_TYPE_PRICE)
				.discountValue(1000)
				.minOrderPrice(10000)
				.maxDiscountPrice(1000)
				.validDays(10L)
				.build();
		
		UpdateCouponReqDto dto = UpdateCouponReqDto.builder()
				.couponName("수정된 쿠폰")
				.couponDescription("수정된 설명")
				.discountType(DiscountType.DISCOUNT_TYPE_PERCENT)
				.discountValue(20)
				.minOrderPrice(20000)
				.maxDiscountPrice(2000)
				.validDays(20L)
				.build();
		
		when(couponRepository.findByCouponUuid(couponUuid)).thenReturn(Optional.of(coupon));
		
		// when : updateCoupon() 호출
		UpdateCouponResDto result = couponService.updateCoupon(couponUuid, dto);
		
		// then : 반환 DTO 가 수정된 값을 가지고 있는지 확인
		// 		  findByCouponUuid() 호출 확인, save() 호출 확인
		ArgumentCaptor<Coupon> couponCaptor = 
				ArgumentCaptor.forClass(Coupon.class);
		
		verify(couponRepository).findByCouponUuid(couponUuid);
		verify(couponRepository).save(couponCaptor.capture());
		
		Coupon savedCoupon = couponCaptor.getValue();
		
		// [1] Repository 에 저장된 객체 검증
		assertEquals(coupon.getId(), savedCoupon.getId());
		assertEquals(couponUuid, savedCoupon.getCouponUuid());
		
		assertEquals(dto.getCouponName(), savedCoupon.getName());
		assertEquals(dto.getCouponDescription(), savedCoupon.getDescription());
		assertEquals(dto.getDiscountType(), savedCoupon.getDiscountType());
		assertEquals(dto.getDiscountValue(), savedCoupon.getDiscountValue());
		assertEquals(dto.getMinOrderPrice(), savedCoupon.getMinOrderPrice());
		assertEquals(dto.getMaxDiscountPrice(), savedCoupon.getMaxDiscountPrice());
		assertEquals(dto.getValidDays(), savedCoupon.getValidDays());
		
		// [2] 반환 DTO 도 검증
		assertEquals(savedCoupon.getName(), result.getCouponName());
		assertEquals(savedCoupon.getDescription(), result.getCouponDescription());
		assertEquals(savedCoupon.getDiscountType(), result.getDiscountType());
		assertEquals(savedCoupon.getDiscountValue(), result.getDiscountValue());
		assertEquals(savedCoupon.getMinOrderPrice(), result.getMinOrderPrice());
		assertEquals(savedCoupon.getMaxDiscountPrice(), result.getMaxDiscountPrice());
		assertEquals(savedCoupon.getValidDays(), result.getValidDays());
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
