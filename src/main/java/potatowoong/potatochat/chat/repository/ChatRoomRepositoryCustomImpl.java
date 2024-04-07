package potatowoong.potatochat.chat.repository;

import static potatowoong.potatochat.auth.entity.QMember.member;
import static potatowoong.potatochat.chat.entity.QChat.chat;
import static potatowoong.potatochat.chat.entity.QChatRoom.chatRoom;
import static potatowoong.potatochat.chat.entity.QChatRoomMember.chatRoomMember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import potatowoong.potatochat.chat.dto.response.ChatRoomResDto;
import potatowoong.potatochat.chat.entity.ChatRoom;
import potatowoong.potatochat.chat.entity.QChatRoomMember;

@RequiredArgsConstructor
@Slf4j
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public String findChatRoomIdBySenderIdAndReceiverId(final String senderId, final String receiverId) {
        QChatRoomMember c1 = new QChatRoomMember("c1");
        QChatRoomMember c2 = new QChatRoomMember("c2");

        return jpaQueryFactory.select(chatRoom.chatRoomId)
            .from(chatRoom)
            .innerJoin(chatRoom.chatRoomMembers, c1)
            .innerJoin(chatRoom.chatRoomMembers, c2)
            .where((c1.member.userId.eq(senderId).and(c2.member.userId.eq(receiverId)))
                .or(c1.member.userId.eq(receiverId).and(c2.member.userId.eq(senderId))))
            .fetchOne();
    }

    @Override
    public List<ChatRoomResDto> findChatRoomsWithChatByUserId(final String userId) {
        List<ChatRoom> chatRooms = jpaQueryFactory.selectFrom(chatRoom)
            .innerJoin(chatRoom.chatRoomMembers, chatRoomMember).fetchJoin()
            .leftJoin(chatRoom.chats, chat).fetchJoin()
            .innerJoin(chat.member, member).fetchJoin()
            .where(chatRoomMember.member.userId.eq(userId))
            .fetch();

        return chatRooms.stream()
            .map(ChatRoomResDto::toDto)
            .toList();
    }
}
