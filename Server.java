import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        List<String> authorizedIps = Arrays.asList("192.168.29.168", "0.0.0.0", "127.0.0.1");
        String authorizedToken = "Hi, I am under the water";
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                try {
                    socket = serverSocket.accept();
                    String clientIp = socket.getInetAddress().getHostAddress();
                    if(!authorizedIps.contains(clientIp)) {
                        System.out.println("Unauthorized IP: " + clientIp);
                        socket.close();
                        continue;
                    }
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                    bufferedReader = new BufferedReader(inputStreamReader);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String token = bufferedReader.readLine();
                    if(!token.equals(authorizedToken)) {
                        System.out.println("Unauthorized Token: " + token);
                        socket.close();
                        continue;
                    }

                    System.out.println("Connected!!");
                    while (true) {
                        String messageFromClient = bufferedReader.readLine();
                        System.out.println("Client: " + messageFromClient);

                        bufferedWriter.write("Message Recived");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        if (messageFromClient.equalsIgnoreCase("BYE")) {
                            break;
                        }
                    }
                    socket.close();
                    inputStreamReader.close();
                    outputStreamWriter.close();
                    bufferedReader.close();
                    bufferedWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
