package ctif.md.aimicroservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${llama.base-url}")
    private String llamaBaseUrl;
    @Bean
    public WebClient webClient() {
        return WebClient.create(llamaBaseUrl);
    }
}
