package potatowoong.potatochat.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import potatowoong.potatochat.chat.entity.ChatRoomMember;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<ChatRoomMember> findByChatRoomIdIn(List<String> chatRoomIds);
}
