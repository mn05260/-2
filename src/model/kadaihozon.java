 package model;
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
       //ブラウザから送られたURLとメソッドの処理を切り分ける
       //GETでデータを取得（一覧表示）、POSTでデータを追加
        if (path.equals("/add") && method.equals("POST")) {
            String body;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"))) {
                //追加する課題の内容を受け取る
                //reader.lines()でデータを一行ずつ集めて、最後に一つにまとめる
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
   //処理する直前にファイルを読み込んで、処理が終わった瞬間にファイルに保存する
        List<String> tasks = TaskFIleManager.loadTasks();

    // 「締め切り」という文字列の場所を慶安して日付順に並び変える
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