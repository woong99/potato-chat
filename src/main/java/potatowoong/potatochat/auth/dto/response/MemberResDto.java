package potatowoong.potatochat.auth.dto.response;

import lombok.Builder;
import potatowoong.potatochat.auth.entity.Member;

@Builder
public record MemberResDto(String userId, String nickname) {

    public static MemberResDto toDto(Member member) {
        return MemberResDto.builder()
            .userId(member.getUserId())
            .nickname(member.getNickname())
            .build();
    }
}
