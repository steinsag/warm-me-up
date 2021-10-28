package services.progressit.warmmeup.rest.dto;

import javax.validation.constraints.NotNull;

public class SomeRequestDto {
    @NotNull
    private String inputMessage;

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    @Override
    public String toString() {
        return "SomeRequestDto{" +
                "inputMessage='" + inputMessage + '\'' +
                '}';
    }
}
