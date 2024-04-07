package potatowoong.potatochat.chat.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import potatowoong.potatochat.chat.entity.ChatRoom;
import potatowoong.potatochat.chat.enums.ChatRoomType;

@Getter
@Builder
public class ChatRoomResDto {

    private String chatRoomId;

    @Setter
    private String name;

    private ChatRoomType chatRoomType;

    @Setter
    private String receiverId;

    @Setter
    private List<ChatRoomMemberResDto> chatRoomMembers;

    private List<ChatResDto> chats;

    public static ChatRoomResDto toDto(ChatRoom chatRoom) {
        return ChatRoomResDto.builder()
            .chatRoomId(chatRoom.getChatRoomId())
            .name(chatRoom.getName())
            .chatRoomType(chatRoom.getChatRoomType())
            .chatRoomMembers(chatRoom.getChatRoomMembers().stream()
                .map(ChatRoomMemberResDto::toDto)
                .toList())
            .chats(chatRoom.getChats().stream()
                .map(ChatResDto::toDto)
                .toList())
            .build();
    }
}
