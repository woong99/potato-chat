package potatowoong.potatochat.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import potatowoong.potatochat.auth.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByNickname(String nickname);
}
