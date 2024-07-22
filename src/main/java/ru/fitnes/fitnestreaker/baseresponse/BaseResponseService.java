package ru.fitnes.fitnestreaker.baseresponse;

import lombok.Builder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.exception.Exception;

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
    public ResponseWrapper<?> wrapErrorResponse(Exception exception) {
        ErrorDto error = ErrorDto.builder()
                .code(exception.getType().name())
                .title(exception.getType().getTitle())
                .text(exception.getType().getText())
                .build();
        return ResponseWrapper
                .builder()
                .success(false)
                .error(error)
                .build();
    }
}