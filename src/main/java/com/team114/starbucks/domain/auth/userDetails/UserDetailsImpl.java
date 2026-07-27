package com.team114.starbucks.domain.auth.userDetails;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.team114.starbucks.domain.member.entity.Member;
import com.team114.starbucks.domain.member.enums.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserDetailsImpl implements UserDetails {

    private final String memberUuid;
    private final UserRole userRole;
    private final String password;

    @Builder
    public UserDetailsImpl(Member member) {
        this.memberUuid = member.getMemberUuid();
        this.userRole = member.getUserRole();
        this.password = member.getPassword();
    }

    // 사용자가 가진 권한을 List 로 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.getUserRole()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // 멤버 식별자 (uuid) 반환
    @Override
    public String getUsername() {
        return this.memberUuid;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있는지 여부
    // 예 : 로그인 실패 5회 시 계정 잠김 등의 로직 제어
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명 (비밀번호)의 만료 여부 판단
    // 비밀번호 주기적 변경 정책이 있을 때 사용
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 상태 여부 판단
    // 비활성화된 계정, 탈퇴당한 회원 등의 판단 및 처리 제어
    @Override
    public boolean isEnabled() {
        return true;
    }
}