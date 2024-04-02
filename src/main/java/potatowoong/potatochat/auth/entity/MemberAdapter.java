package potatowoong.potatochat.auth.entity;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberAdapter extends User {

    private final Member member;

    public MemberAdapter(Member member) {
        super(member.getUserId(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole().name())));

        this.member = member;
    }
}
