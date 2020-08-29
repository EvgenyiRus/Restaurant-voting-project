package restaurant.votingsystem.util.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ErrorInfo {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private final String url;
    private final ErrorType type;
    private final String typeMessage;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... details) {
        this.timestamp=LocalDateTime.now();
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public String[] getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}