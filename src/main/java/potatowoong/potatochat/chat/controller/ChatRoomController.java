package potatowoong.potatochat.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import potatowoong.potatochat.chat.dto.response.ChatRoomResDto;
import potatowoong.potatochat.chat.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 ID 조회
     */
    @GetMapping("/personal/chat-room-id/{user-id}")
    public ResponseEntity<String> getPersonalChatRoomId(@PathVariable("user-id") String userId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatRoomService.getChatRoomBySenderIdAndReceiverId(user.getUsername(), userId));
    }

    /**
     * 채팅방 목록 + 최근 채팅 내용 조회
     */
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResDto>> getChatRoomList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatRoomService.getChatRoomList(user.getUsername()));
    }
}
