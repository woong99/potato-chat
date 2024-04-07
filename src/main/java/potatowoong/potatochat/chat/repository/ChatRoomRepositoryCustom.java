package potatowoong.potatochat.chat.repository;

import java.util.List;
import potatowoong.potatochat.chat.dto.response.ChatRoomResDto;

public interface ChatRoomRepositoryCustom {

    String findChatRoomIdBySenderIdAndReceiverId(String senderId, String receiverId);

    List<ChatRoomResDto> findChatRoomsWithChatByUserId(String userId);
}
