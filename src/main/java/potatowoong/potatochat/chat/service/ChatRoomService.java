package potatowoong.potatochat.chat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import potatowoong.potatochat.auth.dto.response.MemberResDto;
import potatowoong.potatochat.chat.dto.response.ChatRoomMemberResDto;
import potatowoong.potatochat.chat.dto.response.ChatRoomResDto;
import potatowoong.potatochat.chat.entity.ChatRoomMember;
import potatowoong.potatochat.chat.enums.ChatRoomType;
import potatowoong.potatochat.chat.repository.ChatRoomMemberRepository;
import potatowoong.potatochat.chat.repository.ChatRoomRepository;
import potatowoong.potatochat.exception.CustomException;
import potatowoong.potatochat.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional(readOnly = true)
    public String getChatRoomBySenderIdAndReceiverId(String senderId, String receiverId) {
        if (senderId.equals(receiverId)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        final String savedChatRoomId = chatRoomRepository.findChatRoomIdBySenderIdAndReceiverId(senderId, receiverId);
        if (!StringUtils.hasText(savedChatRoomId)) {
            return UUID.randomUUID().toString();
        }
        return savedChatRoomId;
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResDto> getChatRoomList(final String userId) {
        List<ChatRoomResDto> chatRoomResDtos = chatRoomRepository.findChatRoomsWithChatByUserId(userId);

        List<String> chatRoomIds = chatRoomResDtos.stream()
            .map(ChatRoomResDto::getChatRoomId)
            .toList();

        // chatRoomId를 기준으로 chatRoomMember 조회
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByChatRoomIdIn(chatRoomIds);
        Map<String, List<ChatRoomMember>> chatRoomMemberMap = new HashMap<>();
        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            final String chatRoomId = chatRoomMember.getChatRoom().getChatRoomId();
            chatRoomMemberMap.computeIfAbsent(chatRoomId, k -> new ArrayList<>()).add(chatRoomMember);
        }

        for (ChatRoomResDto chatRoomResDto : chatRoomResDtos) {
            // chatRoomId를 기준으로 chatRoomMember 등록
            chatRoomResDto.setChatRoomMembers(chatRoomMemberMap.get(chatRoomResDto.getChatRoomId()).stream()
                .map(ChatRoomMemberResDto::toDto)
                .toList());

            // 1대1 채팅방인 경우 상대방의 닉네임을 채팅방 이름으로 설정, 상대방의 userId를 receiverId로 설정
            final ChatRoomType chatRoomType = chatRoomResDto.getChatRoomType();
            if (chatRoomType == ChatRoomType.PERSONAL) {
                final MemberResDto memberResDto = chatRoomResDto.getChatRoomMembers().stream()
                    .filter(d -> !d.getMember()
                        .getUserId()
                        .equals(userId))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST))
                    .getMember();
                chatRoomResDto.setName(memberResDto.getNickname());
                chatRoomResDto.setReceiverId(memberResDto.getUserId());
            }
        }

        return chatRoomResDtos;
    }
}
