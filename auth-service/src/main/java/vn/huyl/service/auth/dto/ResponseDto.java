package vn.huyl.service.auth.dto;

import lombok.Data;
import vn.huyl.service.auth.util.DateTimeUtil;

import java.time.LocalDateTime;

@Data
public class ResponseDto {

    private String timestamp;

    private int status;

    private String message;

    private Object result;

    public ResponseDto(int status, String message, Object result) {
        this.setStatus(status);
        this.setMessage(message);
        this.setResult(result);
        this.setTimestamp(DateTimeUtil.formatDateTime(LocalDateTime.now()));
    }

    public ResponseDto() {
        this.setTimestamp(DateTimeUtil.formatDateTime(LocalDateTime.now()));
    }
}
