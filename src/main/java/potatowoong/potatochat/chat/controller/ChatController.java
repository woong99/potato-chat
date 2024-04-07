package potatowoong.potatochat.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import potatowoong.potatochat.chat.dto.response.ChatResDto;
import potatowoong.potatochat.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     * 채팅방의 채팅 목록 조회
     */
    @GetMapping("/list/{chatRoomId}")
    public ResponseEntity<List<ChatResDto>> getChatList(@PathVariable("chatRoomId") String chatRoomId) {
        return ResponseEntity.ok(chatService.getChatList(chatRoomId));
    }
}
