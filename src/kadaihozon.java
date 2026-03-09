import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class kadaihozon implements HttpHandler {
    @Override
public void handle(HttpExchange exchange) throws IOException {
    // 1. URLをチェックする
    String path = exchange.getRequestURI().getPath();
    String method = exchange.getRequestMethod();

    // 2. もし「追加」のリクエストなら（今までの処理）
    if (path.equals("/add") && method.equals("POST")) {
        String body;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"))) {
            body = reader.lines().collect(Collectors.joining());
        }
        java.util.List<String> tasks = TaskFIleManager.loadTasks();
        tasks.add(body);
        TaskFIleManager.saveTasks(tasks);
        
        sendResponse(exchange, "OK");
    }
    else if (path.equals("/list") && method.equals("GET")) {
        java.util.List<String> tasks = TaskFIleManager.loadTasks();
        String response = String.join("\n", tasks); // リストを改行区切りの文字列にする
        sendResponse(exchange, response);
    }
}
private void sendResponse(HttpExchange exchange, String response) throws IOException {
    byte[] responseBytes = response.getBytes("utf-8");
    exchange.sendResponseHeaders(200, responseBytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
    }
}
    }

    


