package potatowoong.potatochat.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import potatowoong.potatochat.auth.dto.response.MemberResDto;
import potatowoong.potatochat.chat.entity.ChatRoomMember;
import potatowoong.potatochat.config.jpa.UseFlag;

@Getter
@Builder
public class ChatRoomMemberResDto {

    private Long chatRoomMemberId;

    private MemberResDto member;

    private UseFlag exitFlag;

    public static ChatRoomMemberResDto toDto(ChatRoomMember chatRoomMember) {
        return ChatRoomMemberResDto.builder()
            .chatRoomMemberId(chatRoomMember.getChatRoomMemberId())
            .member(MemberResDto.toDto(chatRoomMember.getMember()))
            .exitFlag(chatRoomMember.getExitFlag())
            .build();
    }
}
