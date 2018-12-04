package GameAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;

@CacheEvict
@SpringBootApplication()
public class PokerYnovGameApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokerYnovGameApiApplication.class, args);
    }
}
