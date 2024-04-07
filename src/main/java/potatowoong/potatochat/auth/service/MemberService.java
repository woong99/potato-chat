package potatowoong.potatochat.auth.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import potatowoong.potatochat.auth.dto.request.MemberSignUpReqDto;
import potatowoong.potatochat.auth.dto.response.MemberResDto;
import potatowoong.potatochat.auth.entity.Member;
import potatowoong.potatochat.auth.enums.Role;
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

        memberRepository.save(Member.toEntity(dto, Role.ROLE_USER));
    }

    public List<MemberResDto> getMembers(final String userId) {
        return memberRepository.findAll().stream()
            .filter(d -> StringUtils.hasText(d.getId()) && !d.getId().equals(userId))
            .map(MemberResDto::toDto)
            .toList();
    }

    public MemberResDto getMember(final String userId) {
        return MemberResDto.toDto(memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST)));
    }
}
