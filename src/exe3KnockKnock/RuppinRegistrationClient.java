package exe3KnockKnock;
import java.io.*;
import java.net.Socket;

public class RuppinRegistrationClient {
    public static void main(String[] args) throws IOException {

        try (
                Socket rrSocket = new Socket("127.0.0.1", 4445); 
                PrintWriter out = new PrintWriter(rrSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(rrSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String fromServer;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equalsIgnoreCase("Bye.") 
                        || fromServer.toLowerCase().contains("goodbye")
                        || fromServer.toLowerCase().contains("registration complete")) {
                    break;
                }
                String fromUser = stdIn.readLine();
                if (fromUser == null)
                	break;

                System.out.println("Client: " + fromUser);
                out.println(fromUser);
            }
        }
    }
}
