package services.progressit.warmmeup.rest.dto;

public class SomeResponseDto {
    private String message;

    private String uuid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "SomeResponseDto{" +
                "message='" + message + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
