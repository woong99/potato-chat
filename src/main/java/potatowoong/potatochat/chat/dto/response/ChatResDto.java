package potatowoong.potatochat.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import potatowoong.potatochat.auth.dto.response.MemberResDto;
import potatowoong.potatochat.chat.entity.Chat;

@Getter
@Builder
@ToString
public class ChatResDto {

    private Long chatId;

    private String message;

    private MemberResDto member;

    public static ChatResDto toDto(Chat chat) {
        return ChatResDto.builder()
            .chatId(chat.getChatId())
            .message(chat.getMessage())
            .member(MemberResDto.toDto(chat.getMember()))
            .build();
    }
}
