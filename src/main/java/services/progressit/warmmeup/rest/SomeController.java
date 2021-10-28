package services.progressit.warmmeup.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.progressit.warmmeup.domain.SomeService;
import services.progressit.warmmeup.rest.dto.SomeRequestDto;
import services.progressit.warmmeup.rest.dto.SomeResponseDto;

import javax.validation.Valid;

@RestController("/")
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class SomeController {

    @Autowired
    private SomeService someService;

    @PostMapping
    public ResponseEntity<SomeResponseDto> post(@RequestBody @Valid SomeRequestDto someRequestDto) {
        final SomeResponseDto responseDto = new SomeResponseDto();
        responseDto.setMessage(someRequestDto.getInputMessage());
        responseDto.setUuid(someService.getUuid());

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("warmup")
    public ResponseEntity<SomeResponseDto> warmUp(@RequestBody @Valid SomeRequestDto someRequestDto) {
        final SomeResponseDto responseDto = new SomeResponseDto();
        responseDto.setUuid("some fixed UUID");
        responseDto.setMessage("some fixed message");

        return ResponseEntity.ok(responseDto);
    }
}
