package potatowoong.potatochat.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LoginReqDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;
}
