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
        kadaihozon myHandler = new kadaihozon();
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
        server.createContext("/add", myHandler);  
        server.createContext("/list", myHandler);
server.createContext("/delete", myHandler);
        System.out.println("サーバーが起動完了！ http://localhost:8080 ");
        server.start();
    }
}