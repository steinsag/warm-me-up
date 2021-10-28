package services.progressit.warmmeup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import services.progressit.warmmeup.initialization.PreloadComponent;

@SpringBootTest
@MockBean(PreloadComponent.class)
class WarmMeUpApplicationTests {

    @Test
    void contextLoads() {
    }
}
