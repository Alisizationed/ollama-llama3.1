//package ctif.md.aimicroservice.config;
//
//import ctif.md.aimicroservice.handlers.MyWebSocketHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.HandlerMapping;
//import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.server.WebSocketService;
//import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//class WebConfig {
//
//  @Bean
//  public HandlerMapping handlerMapping() {
//    Map<String, WebSocketHandler> map = new HashMap<>();
//    map.put("/path", new MyWebSocketHandler());
//    int order = -1;
//
//    return new SimpleUrlHandlerMapping(map, order);
//  }
//
//    @Bean
//    public WebSocketHandlerAdapter handlerAdapter() {
//        return new WebSocketHandlerAdapter(webSocketService());
//    }
//
//    @Bean
//    public WebSocketService webSocketService() {
//        ReactorNettyRequestUpgradeStrategy strategy = new ReactorNettyRequestUpgradeStrategy();
////        strategy.setMaxSessionIdleTimeout(0L);
//        return new HandshakeWebSocketService(strategy);
//    }
//}
