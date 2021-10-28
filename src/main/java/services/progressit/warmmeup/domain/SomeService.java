package services.progressit.warmmeup.domain;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SomeService {
    public String getUuid() {
        return UUID.randomUUID().toString();
    }
}
