package potatowoong.potatochat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ErrorResponseEntity {

    private final int status;

    private final String name;

    private final String code;

    private final String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e) {
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(new ErrorResponseEntity(e.getHttpStatus().value(), e.name(), e.getCode(), e.getMessage()));
    }
}
