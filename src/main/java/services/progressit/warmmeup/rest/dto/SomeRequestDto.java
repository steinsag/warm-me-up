package services.progressit.warmmeup.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class SomeRequestDto {
    @NotNull
    private String inputMessage;

    @Min(100)
    @Max(200)
    private BigDecimal someNumber;

    @Pattern(regexp = "this is a fixed string")
    @NotBlank
    private String patternString;

    @Valid
    @NotNull
    private SomeOptionsDto selectOne;

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    public BigDecimal getSomeNumber() {
        return someNumber;
    }

    public void setSomeNumber(BigDecimal someNumber) {
        this.someNumber = someNumber;
    }

    public String getPatternString() {
        return patternString;
    }

    public void setPatternString(String patternString) {
        this.patternString = patternString;
    }

    public SomeOptionsDto getSelectOne() {
        return selectOne;
    }

    public void setSelectOne(SomeOptionsDto selectOne) {
        this.selectOne = selectOne;
    }

    @Override
    public String toString() {
        return "SomeRequestDto{" +
                "inputMessage='" + inputMessage + '\'' +
                ", someNumber=" + someNumber +
                ", patternString='" + patternString + '\'' +
                ", selectOne=" + selectOne +
                '}';
    }
}
