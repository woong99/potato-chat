package potatowoong.potatochat.chat.interceptor;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import potatowoong.potatochat.jwt.component.JwtTokenProvider;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider tokenProvider;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (accessor.getCommand()) {
            case CONNECT, SEND:
                final String jwt = resolveToken(accessor);
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    accessor.setUser(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                break;
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }

    private String resolveToken(StompHeaderAccessor accessor) {
        final String bearerToken = Objects.requireNonNull(accessor.getNativeHeader("Authorization")).get(0);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
