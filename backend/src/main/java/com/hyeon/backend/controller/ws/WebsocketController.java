package com.hyeon.backend.controller.ws;

import com.hyeon.backend.utils.Formatter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class WebsocketController extends TextWebSocketHandler {

  private static List<WebSocketSession> list = new ArrayList<>();

  @Override
  protected void handleTextMessage(
    WebSocketSession session,
    TextMessage message
  ) throws Exception {
    String payload = message.getPayload();
    log.info("payload : " + payload);

    for (WebSocketSession sess : list) {
      if (!sess.getId().equals(session.getId())) {
        sess.sendMessage(message);
      }
    }
  }

  /* Client가 접속 시 호출되는 메서드 */
  @Override
  public void afterConnectionEstablished(WebSocketSession session)
    throws Exception {
    list.add(session);
    log.info(session + " 클라이언트 접속");
  }

  /* Client가 접속 해제 시 호출되는 메서드 */
  @Override
  public void afterConnectionClosed(
    WebSocketSession session,
    CloseStatus status
  ) throws Exception {
    list.remove(session);
  }

  @Scheduled(fixedRate = 1000)
  public void sendMetricsToClients() {
    try {
      String diskUsage = "0";
      String memoryUsage = "0";
      String cpuUsage = "0";

      for (WebSocketSession sess : list) {
        sess.sendMessage(
          new TextMessage(Formatter.formatJsonWithKey("diskUsage", diskUsage))
        );
        sess.sendMessage(
          new TextMessage(
            Formatter.formatJsonWithKey("memoryUsage", memoryUsage)
          )
        );
        sess.sendMessage(
          new TextMessage(Formatter.formatJsonWithKey("cpuUsage", cpuUsage))
        );
      }
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
      // TODO: 웹소켓 서버 자체에서 에러가 발생하면 어떻게 할 것인지?
      // TODO: Retry?

    }
  }
  // private static final String FAKE_QUERY = "INVALID QUERY"; // test
  // private static final String PROMETHEUS_DISK_QUERY =
  //   "100 - (sum by(mountpoint) (system_filesystem_usage_bytes{instance=\"otel-collector-1:9464\",state=\"free\",mode=\"rw\"} * 100) / sum by(mountpoint) (system_filesystem_usage_bytes{instance=\"otel-collector-1:9464\"}))";
  // private static final String PROMETHEUS_MEMORY_QUERY =
  //   "( system_memory_usage_bytes{instance=\"otel-collector-1:9464\",state=\"used\"} / on() sum(system_memory_usage_bytes{instance=\"otel-collector-1:9464\", state!=\"slab_reclaimable\", state!=\"slab_unreclaimable\"}) ) * 100";
  // private static final String PROMETHEUS_CPU_QUERY =
  //   "(1.0 - (sum(rate(system_cpu_time_seconds_total{instance=\"otel-collector-1:9464\",state=\"idle\"}[1m])) / on() sum(rate(system_cpu_time_seconds_total{instance=\"otel-collector-1:9464\",state!=\"interrupt\",state!=\"softirq\",state!=\"steal\",state!=\"wait\"}[1m])))) * 100";
}
