package potatowoong.potatochat.jwt.component;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import potatowoong.potatochat.auth.entity.Member;
import potatowoong.potatochat.auth.entity.MemberAdapter;
import potatowoong.potatochat.auth.repository.MemberRepository;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new MemberAdapter(member);
    }
}
