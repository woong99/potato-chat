package potatowoong.potatochat.jwt.dto;

import java.util.Date;
import lombok.Builder;

@Builder
public record AccessTokenDto(
    String accessToken,
    
    Date expireDate
) {

}
