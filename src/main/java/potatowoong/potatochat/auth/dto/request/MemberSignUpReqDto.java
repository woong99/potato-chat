package potatowoong.potatochat.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MemberSignUpReqDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
    private final String userId;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    @Size(max = 20, message = "비밀번호 확인은 20자 이하로 입력해주세요.")
    private final String passwordCheck;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    private final String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 20, message = "비밀번호는 20자 이하로 입력해주세요.")
    @Setter
    private String password;
}
