package potatowoong.potatochat.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import potatowoong.potatochat.auth.dto.request.LoginReqDto;
import potatowoong.potatochat.auth.dto.request.MemberSignUpReqDto;
import potatowoong.potatochat.auth.service.MemberService;
import potatowoong.potatochat.jwt.component.JwtTokenProvider;
import potatowoong.potatochat.jwt.dto.AccessTokenDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    private final JwtTokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 사용자 회원가입 API
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody MemberSignUpReqDto dto) {
        memberService.signUp(dto);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@Valid @RequestBody LoginReqDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final AccessTokenDto accessTokenDto = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(accessTokenDto);
    }
}
