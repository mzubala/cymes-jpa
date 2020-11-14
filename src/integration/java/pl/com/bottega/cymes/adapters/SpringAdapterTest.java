package pl.com.bottega.cymes.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.cymes.client.CymesClient;
import pl.com.bottega.cymes.client.DbClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class SpringAdapterTest {

    @Autowired
    private DbClient dbClient;

    @Autowired
    private WebTestClient webTestClient;

    protected CymesClient cymesClient;

    @BeforeEach
    public void setup() {
        dbClient.clean();
        cymesClient = new CymesClient(webTestClient);
    }
}
