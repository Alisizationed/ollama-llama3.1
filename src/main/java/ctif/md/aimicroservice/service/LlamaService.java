package ctif.md.aimicroservice.service;

import ctif.md.aimicroservice.repository.LlamaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class LlamaService {
    private LlamaRepository llamaRepository;

    public Flux<String> promptLlama(Mono<String> input) {
        return llamaRepository.streamPrompt(input);
    }
}
