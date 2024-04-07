package potatowoong.potatochat.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLRestriction;
import potatowoong.potatochat.auth.entity.Member;
import potatowoong.potatochat.config.jpa.BaseEntity;
import potatowoong.potatochat.config.jpa.UseFlag;

@Entity
@Table(name = "chat_room_member")
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Comment("채팅방-사용자 연결 테이블")
@SQLRestriction("use_flag = 'Y'")
public class ChatRoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_member_id")
    @Comment("채팅방-사용자 연결 정보 ID")
    private Long chatRoomMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @Comment("사용자 ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id", nullable = false)
    @Comment("채팅방 정보 ID")
    private ChatRoom chatRoom;

    @Column(name = "exit_flag", nullable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'Y'")
    @Comment("퇴장여부")
    private UseFlag exitFlag;

    public void changeChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
