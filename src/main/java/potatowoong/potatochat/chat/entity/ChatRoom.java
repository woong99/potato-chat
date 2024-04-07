package potatowoong.potatochat.chat.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.domain.Persistable;
import potatowoong.potatochat.chat.enums.ChatRoomType;
import potatowoong.potatochat.config.jpa.BaseEntity;

@Entity
@Table(name = "chat_room")
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Comment("채팅방 정보")
@SQLRestriction("use_flag = 'Y'")
public class ChatRoom extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "chat_room_id", length = 36)
    @Comment("채팅방 정보 ID")
    private String chatRoomId;

    @Column(name = "name", length = 200)
    @Comment("채팅방 이름")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "chat_room_type", nullable = false)
    @Comment("채팅방 유형")
    private ChatRoomType chatRoomType;

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Chat> chats = new ArrayList<>();

    public void addChatRoomMember(List<ChatRoomMember> chatRoomMembers) {
        if (this.chatRoomMembers == null) {
            this.chatRoomMembers = new HashSet<>();
        }
        this.chatRoomMembers.addAll(chatRoomMembers);
        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            chatRoomMember.changeChatRoom(this);
        }
    }

    public void addChat(Chat chat) {
        if (this.chats == null) {
            this.chats = new ArrayList<>();
        }
        this.chats.add(chat);
        chat.changeChatRoom(this);
    }

    @Override
    public String getId() {
        return this.chatRoomId;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
