package com.myownbook.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;


    private ErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.errors = builder.errors; // 새로 추가된 errors도 넣어주자!
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class Builder {
        private String code;
        private String message;
        private List<ValidationError> errors; // 빌더에도 errors 필드 추가!

        // 빌더 메소드들: 죄다 자기 자신(this) 리턴해서 체이닝 가능하게!
        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errors(List<ValidationError> errors) { // errors 설정하는 빌더 메소드 추가!
            this.errors = errors;
            return this;
        }

        // 최종적으로 ErrorResponse 객체 반환하는 메소드
        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }

    public static class ValidationError {
        //@Valid로 에러가 들어왔을때
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public static ValidationError of(final FieldError fieldError) {
            return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
