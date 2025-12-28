package exe3KnockKnock;
import java.net.*;
import java.io.*;

public class KnockKnockServer 
{
    public static void main(String[] args) throws IOException
    {

        ServerSocket serverSocket = null;
        try 
        {
            serverSocket = new ServerSocket(4444);
        } 
        catch (IOException e) 
        {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        while(true)
	    {
	        Socket clientSocket = null;
	        try 
	        {
	            clientSocket = serverSocket.accept();
	            System.out.println("New client connected: " + clientSocket);
	        } 
	        catch (IOException e) {
	            System.err.println("Accept failed: " + e.getMessage());
	            continue; 
	        }
	        new Thread(new ClientHandler(clientSocket)).start();
	    }
    }
}
