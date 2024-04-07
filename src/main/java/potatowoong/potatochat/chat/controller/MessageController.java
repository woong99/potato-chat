package potatowoong.potatochat.chat.controller;


import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import potatowoong.potatochat.chat.dto.common.MessageDto;
import potatowoong.potatochat.chat.dto.response.ChatResDto;
import potatowoong.potatochat.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;

    /**
     * 1:1 채팅 메시지 전송
     */
    @MessageMapping("/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, MessageDto messageDto, SimpMessageHeaderAccessor accessor) {
        ChatResDto chatResDto = chatService.handleMessage(chatRoomId, messageDto, Objects.requireNonNull(accessor.getUser()).getName());

        messagingTemplate.convertAndSend("/topic/" + chatRoomId, chatResDto);
    }
}
