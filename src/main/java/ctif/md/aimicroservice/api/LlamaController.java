package ctif.md.aimicroservice.api;

import ctif.md.aimicroservice.service.LlamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/llama")
public class LlamaController {
    private final LlamaService llamaService;
    
    @PostMapping(value="/", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> promptLlama(
            @RequestBody Mono<String> input
    ) {
        return llamaService.promptLlama(input)
                .log();
    }
}
