import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // 1. 担当者(Handler)を1つ作る（保存と読み込みを両方やるやつ）
        kadaihozon myHandler = new kadaihozon();

        // 2. ページを表示する設定
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] response = Files.readAllBytes(Paths.get("haruyasumikadai/Todoirasuto.html"));
                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        });

        // 3. 機能を登録する（URLと担当者を紐付ける）
        server.createContext("/add", myHandler);  // 課題の追加用
        server.createContext("/list", myHandler); // 課題の読み込み用（これが必要！）

        // 4. すべての準備が整ってからサーバーを起動
        System.out.println("サーバーが起動したよ！ http://localhost:8080 にアクセスしてね");
        server.start();
    }
}