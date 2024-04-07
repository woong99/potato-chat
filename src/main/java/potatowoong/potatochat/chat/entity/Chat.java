package potatowoong.potatochat.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLRestriction;
import potatowoong.potatochat.auth.entity.Member;
import potatowoong.potatochat.config.jpa.BaseEntity;

@Entity
@Table(name = "chat")
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Comment("채팅 내역")
@SQLRestriction("use_flag = 'Y'")
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("채팅 내역 ID")
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id", nullable = false)
    @Comment("채팅방 정보 ID")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @Comment("사용자 ID")
    private Member member;

    @Column(name = "message", length = 1000, nullable = false)
    @Comment("채팅 내용")
    private String message;

    public void changeChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
