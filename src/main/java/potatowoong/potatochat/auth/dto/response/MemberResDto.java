package potatowoong.potatochat.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import potatowoong.potatochat.auth.entity.Member;

@Getter
@Builder
public class MemberResDto {

    private String userId;

    private String nickname;
    
    public static MemberResDto toDto(Member member) {
        return MemberResDto.builder()
            .userId(member.getUserId())
            .nickname(member.getNickname())
            .build();
    }
}
