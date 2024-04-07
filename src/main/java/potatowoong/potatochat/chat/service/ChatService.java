package potatowoong.potatochat.chat.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import potatowoong.potatochat.auth.dto.response.MemberResDto;
import potatowoong.potatochat.auth.entity.Member;
import potatowoong.potatochat.auth.repository.MemberRepository;
import potatowoong.potatochat.chat.dto.common.MessageDto;
import potatowoong.potatochat.chat.dto.response.ChatResDto;
import potatowoong.potatochat.chat.entity.Chat;
import potatowoong.potatochat.chat.entity.ChatRoom;
import potatowoong.potatochat.chat.entity.ChatRoomMember;
import potatowoong.potatochat.chat.enums.ChatCommand;
import potatowoong.potatochat.chat.enums.ChatRoomType;
import potatowoong.potatochat.chat.repository.ChatRepository;
import potatowoong.potatochat.chat.repository.ChatRoomRepository;
import potatowoong.potatochat.config.jpa.UseFlag;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public ChatResDto handleMessage(final String chatRoomId, MessageDto messageDto, final String senderId) {
        ChatCommand chatCommand = messageDto.chatCommand();
        Member sender = memberRepository.getReferenceById(senderId);
        Chat chat = null;

        switch (chatCommand) {
            case FIRST_CHAT -> chat = handleMessageWhenFirstChat(chatRoomId, messageDto, sender);
            case SEND_MESSAGE -> chat = handleMessageWhenSendMessage(chatRoomId, messageDto, sender);
        }
        return ChatResDto.builder()
            .chatId(chat.getChatId())
            .message(messageDto.message())
            .member(MemberResDto.builder()
                .userId(senderId)
                .nickname(messageDto.nickname())
                .build())
            .build();
    }

    /**
     * FIRST_CHAT - 메시지 처리
     */
    private Chat handleMessageWhenFirstChat(final String chatRoomId, MessageDto messageDto, final Member sender) {
        Optional<ChatRoom> savedChatRoom = chatRoomRepository.findById(chatRoomId);
        Chat chat;

        if (savedChatRoom.isEmpty()) {
            Member receiver = memberRepository.getReferenceById(messageDto.receiverId());

            ChatRoomMember senderChatRoomMember = ChatRoomMember.builder()
                .member(sender)
                .exitFlag(UseFlag.Y)
                .build();

            ChatRoomMember receiverChatRoomMember = ChatRoomMember.builder()
                .member(receiver)
                .exitFlag(UseFlag.Y)
                .build();

            chat = Chat.builder()
                .message(messageDto.message())
                .member(sender)
                .build();

            ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .chatRoomType(ChatRoomType.PERSONAL)
                .build();

            chatRoom.addChatRoomMember(List.of(senderChatRoomMember, receiverChatRoomMember));
            chatRoom.addChat(chat);
            chatRoomRepository.save(chatRoom);
        } else {
            chat = Chat.builder()
                .message(messageDto.message())
                .member(sender)
                .chatRoom(savedChatRoom.get())
                .build();
            chat = chatRepository.save(chat);
        }
        return chat;
    }

    /**
     * SEND_MESSAGE - 메시지 처리
     */
    private Chat handleMessageWhenSendMessage(final String chatRoomId, MessageDto messageDto, final Member sender) {
        // TODO : 예외 처리
        ChatRoom savedChatRoom = chatRoomRepository.getReferenceById(chatRoomId);
        Chat chat = Chat.builder()
            .message(messageDto.message())
            .member(sender)
            .chatRoom(savedChatRoom)
            .build();
        return chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public List<ChatResDto> getChatList(final String chatRoomId) {
        return chatRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId).stream()
            .map(ChatResDto::toDto)
            .toList();
    }
}
