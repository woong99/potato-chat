package potatowoong.potatochat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXIST_ID(HttpStatus.BAD_REQUEST, "A0001", "이미 존재하는 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "A0002", "비밀번호가 일치하지 않습니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "A0003", "이미 존재하는 닉네임입니다.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
