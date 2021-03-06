package ru.lesson.springBootProject.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {
    private boolean success;
//    @JsonAlias("challenge_ts")
//    private LocalDateTime challengeTs;
    //привязываем следующее поле к ключам Json, определённым в CaptchaRest
    @JsonAlias("error-codes")
    private Set<String> errorCodes;

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
