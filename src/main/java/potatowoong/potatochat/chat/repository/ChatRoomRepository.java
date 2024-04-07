package potatowoong.potatochat.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import potatowoong.potatochat.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>, ChatRoomRepositoryCustom {

}
