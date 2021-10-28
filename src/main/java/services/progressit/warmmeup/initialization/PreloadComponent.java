package services.progressit.warmmeup.initialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import services.progressit.warmmeup.rest.dto.SomeRequestDto;
import services.progressit.warmmeup.rest.dto.SomeResponseDto;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class PreloadComponent implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = LoggerFactory.getLogger(PreloadComponent.class);

    @Autowired
    private Environment environment;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // uncomment following line to directly send a REST request on app start-up
//        sendRestRequest();
    }

    private void sendRestRequest() {
        final String serverPort = environment.getProperty("local.server.port");
        final String baseUrl = "http://localhost:" + serverPort;
        final String warmUpEndpoint = baseUrl + "/warmup";

        logger.info("Sending REST request to force initialization of Jackson...");

        final SomeResponseDto response = webClientBuilder.build().post()
                .uri(warmUpEndpoint)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(createSampleMessage()), SomeRequestDto.class)
                .retrieve()
                .bodyToMono(SomeResponseDto.class)
                .timeout(Duration.ofSeconds(5))
                .block();

        logger.info("...done, response received: " + response.toString());
    }

    private SomeRequestDto createSampleMessage() {
        final SomeRequestDto someRequestDto = new SomeRequestDto();
        someRequestDto.setInputMessage("our input message");

        return someRequestDto;
    }
}
