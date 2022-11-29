package services.progressit.warmmeup.initialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class PreloadComponent {

    private final Logger logger = LoggerFactory.getLogger(PreloadComponent.class);

    @Autowired
    private Environment environment;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // doing manual object validation is not sufficient for warm-up
//        manualValidation();

        // also mapping a JSON-string to a DTO doesn't load all classes
//        mapJson();

        // instead, a full request including validation is required
//        sendWarmUpRestRequest();
    }

    private void sendWarmUpRestRequest() {
        final String serverPort = environment.getProperty("local.server.port");
        final String baseUrl = "http://localhost:" + serverPort;
        final String warmUpEndpoint = baseUrl + "/warmup";

        logger.info("Sending REST request to force initialization of Jackson...");

        final String response = webClientBuilder.build().post()
                .uri(warmUpEndpoint)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(createSampleMessage()), WarmUpRequestDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .block();

        logger.info("...done, response received: " + response);
    }

    private void mapJson() {
        final String json = "{\n" +
                "    \"inputMessage\": \"abc\",\n" +
                "    \"someNumber\": 123.4,\n" +
                "    \"patternString\": \"this is a fixed string\",\n" +
                "    \"selectOne\": \"TWO\"\n" +
                "}";

        try {
            final WarmUpRequestDto warmUpRequestDto = objectMapper.readValue(json, WarmUpRequestDto.class);
            logger.info(warmUpRequestDto.toString());
        } catch (JsonProcessingException e) {
            logger.error("problem while parsing warm up JSON");
        }
    }

    private void manualValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<WarmUpRequestDto>> violations = validator.validate(createSampleMessage());
        logger.info("number of violations found: " + violations.size());
    }

    private WarmUpRequestDto createSampleMessage() {
        final WarmUpRequestDto warmUpRequestDto = new WarmUpRequestDto();
        warmUpRequestDto.setWarmUpString("warm me up");
        warmUpRequestDto.setWarmUpNumber(15);
        warmUpRequestDto.setWarmUpBigDecimal(BigDecimal.TEN);
        warmUpRequestDto.setWarmUpEnumDto(WarmUpEnumDto.WARM);

        return warmUpRequestDto;
    }
}
