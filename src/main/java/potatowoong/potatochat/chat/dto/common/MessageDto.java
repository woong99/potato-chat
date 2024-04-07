package potatowoong.potatochat.chat.dto.common;

import lombok.Builder;
import potatowoong.potatochat.chat.enums.ChatCommand;

@Builder
public record MessageDto(
    String message,
    ChatCommand chatCommand,
    String receiverId,
    String nickname) {

}
