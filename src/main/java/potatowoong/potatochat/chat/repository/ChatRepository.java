package potatowoong.potatochat.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import potatowoong.potatochat.chat.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Chat> findByChatRoomIdOrderByCreatedAtDesc(String chatRoomId);
}
