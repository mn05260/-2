 import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.List;
public class kadaihozon implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (path.equals("/add") && method.equals("POST")) {
            String body;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"))) {
                body = reader.lines().collect(Collectors.joining());
            }

            List<String> tasks = TaskFIleManager.loadTasks();
            tasks.add(body);
            TaskFIleManager.saveTasks(tasks);
            sendResponse(exchange, "OK");
        }
        else if (path.equals("/delete") && method.equals("POST")) {
            String body;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"))) {
                body = reader.lines().collect(Collectors.joining());
            }
            System.out.println("削除リクエスト受信: [" + body + "]");
            List<String> tasks = TaskFIleManager.loadTasks();
            // 完全一致じゃなくその文字を含む行を削除する
            boolean removed = tasks.removeIf(task -> task.contains(body));
            System.out.println("★削除成功したか？: " + removed);
            TaskFIleManager.saveTasks(tasks); // 書き直して保存
            sendResponse(exchange, "DELETED");
        }
       else if (path.equals("/list") && method.equals("GET")) {
    List<String> tasks = TaskFIleManager.loadTasks();

    // 締切で並び替え
    tasks.sort((a, b) -> {
        try {
            String dateA = a.substring(a.indexOf("締切: ") + 4, a.length() - 1);
            String dateB = b.substring(b.indexOf("締切: ") + 4, b.length() - 1);
            return dateA.compareTo(dateB);
        } catch (Exception e) {
            return 0;
        }
    });

    String response = String.join("\n", tasks);
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