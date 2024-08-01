package ru.fitnes.fitnestreaker.baseresponse;

import lombok.Builder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.exception.LocalException;

@Service
@Builder
public class BaseResponseService {
    public <T> ResponseWrapper<T> wrapSuccessResponse(T body) {
        return ResponseWrapper
                .<T>builder()
                .success(true)
                .body(body)
                .build();
    }
    public ResponseWrapper<?> wrapErrorResponse(LocalException localException) {
        ErrorDto error = ErrorDto.builder()
                .code(localException.getType().name())
                .title(localException.getType().getTitle())
                .text(localException.getType().getText())
                .build();
        return ResponseWrapper
                .builder()
                .success(false)
                .error(error)
                .build();
    }
}