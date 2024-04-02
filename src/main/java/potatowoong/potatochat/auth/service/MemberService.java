package potatowoong.potatochat.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import potatowoong.potatochat.auth.dto.request.MemberSignUpReqDto;
import potatowoong.potatochat.auth.entity.Member;
import potatowoong.potatochat.auth.repository.MemberRepository;
import potatowoong.potatochat.exception.CustomException;
import potatowoong.potatochat.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void signUp(MemberSignUpReqDto dto) {
        final String password = dto.getPassword();
        final String passwordCheck = dto.getPasswordCheck();

        // ID 중복 체크
        if (memberRepository.existsById(dto.getUserId())) {
            throw new CustomException(ErrorCode.EXIST_ID);
        }

        // 닉네임 중복 체크
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(ErrorCode.EXIST_NICKNAME);
        }

        // 비밀번호 일치 여부 체크
        if (!password.equals(passwordCheck)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        memberRepository.save(Member.toEntity(dto));
    }
}
