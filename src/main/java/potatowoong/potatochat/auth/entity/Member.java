package potatowoong.potatochat.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import potatowoong.potatochat.auth.dto.request.MemberSignUpReqDto;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member implements Persistable<String> {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
    @Comment("사용자 ID")
    private String userId;

    @Column(name = "password", nullable = false, length = 200)
    @Comment("비밀번호")
    private String password;

    @Column(name = "nickname", nullable = false, length = 20)
    @Comment("닉네임")
    private String nickname;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성일")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Comment("수정일")
    private LocalDateTime updatedAt;

    public static Member toEntity(MemberSignUpReqDto dto) {
        return Member.builder()
            .userId(dto.getUserId())
            .password(dto.getPassword())
            .nickname(dto.getNickname())
            .build();
    }

    @Override
    public boolean isNew() {
        return this.createdAt == null;
    }

    @Override
    public String getId() {
        return this.userId;
    }
}
