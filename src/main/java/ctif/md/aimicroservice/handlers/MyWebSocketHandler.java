//package ctif.md.aimicroservice.handlers;
//
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketMessage;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//public class MyWebSocketHandler implements WebSocketHandler {
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//
//        Flux<WebSocketMessage> output = session.receive()
//                .doOnNext(message -> {
//                    // ...
//                })
//                .map(value ->
//                        session.textMessage("Echo " + value));
//
//        return session.send(output);
//    }
//}