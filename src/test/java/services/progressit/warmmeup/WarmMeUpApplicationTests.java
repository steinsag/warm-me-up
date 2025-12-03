package services.progressit.warmmeup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import services.progressit.warmmeup.initialization.PreloadComponent;

@SpringBootTest
@MockitoBean(types = PreloadComponent.class)
class WarmMeUpApplicationTests {

    @Test
    void contextLoads() {
    }
}
