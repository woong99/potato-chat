package potatowoong.potatochat.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import potatowoong.potatochat.auth.dto.request.MemberSignUpReqDto;
import potatowoong.potatochat.auth.repository.MemberRepository;
import potatowoong.potatochat.exception.CustomException;
import potatowoong.potatochat.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("회원가입")
    class 회원가입 {

        @Test
        @DisplayName("회원가입 성공")
        void 회원가입_성공() {
            // given
            MemberSignUpReqDto dto = MemberSignUpReqDto.builder()
                .userId("userId")
                .password("password")
                .passwordCheck("password")
                .nickname("nickname")
                .build();

            when(memberRepository.existsById(anyString())).thenReturn(false);
            when(memberRepository.existsByNickname(anyString())).thenReturn(false);

            // when & then
            assertThatCode(() -> {
                memberService.signUp(dto);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("회원가입 실패 - 아이디 중복")
        void 회원가입_실패_아이디_중복() {
            // given
            MemberSignUpReqDto dto = MemberSignUpReqDto.builder()
                .userId("userId")
                .password("password")
                .passwordCheck("password")
                .nickname("nickname")
                .build();

            when(memberRepository.existsById(anyString())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> {
                memberService.signUp(dto);
            }).isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.EXIST_ID);
        }

        @Test
        @DisplayName("회원가입 실패 - 닉네임 중복")
        void 회원가입_실패_닉네임_중복() {
            // given
            MemberSignUpReqDto dto = MemberSignUpReqDto.builder()
                .userId("userId")
                .password("password")
                .passwordCheck("password")
                .nickname("nickname")
                .build();

            when(memberRepository.existsById(anyString())).thenReturn(false);
            when(memberRepository.existsByNickname(anyString())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> {
                memberService.signUp(dto);
            }).isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.EXIST_NICKNAME);
        }

        @Test
        @DisplayName("회원가입 실패 - 비밀번호 불일치")
        void 회원가입_실패_비밀번호_불일치() {
            // given
            MemberSignUpReqDto dto = MemberSignUpReqDto.builder()
                .userId("userId")
                .password("password")
                .passwordCheck("passwordCheck")
                .nickname("nickname")
                .build();

            when(memberRepository.existsById(anyString())).thenReturn(false);
            when(memberRepository.existsByNickname(anyString())).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> {
                memberService.signUp(dto);
            }).isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}