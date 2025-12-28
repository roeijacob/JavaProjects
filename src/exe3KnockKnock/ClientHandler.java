package exe3KnockKnock;
import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable
{
	private Socket socket;
	
	public ClientHandler(Socket socket)
	{
		this.socket = socket;
	}
	public void run()
	{
		System.out.println("Handling client in thread: " + Thread.currentThread().getName());

		try
		{
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        String inputLine, outputLine;
        KnockKnockProtocol kkp = new KnockKnockProtocol();

        outputLine = kkp.processInput(null);
        out.println(outputLine);

        while ((inputLine = in.readLine()) != null) 
        {
		if (inputLine.equals("q"))  break;
             outputLine = kkp.processInput(inputLine);
             out.println(outputLine);
             
        }
		
        out.close();
        in.close();
        this.socket.close();
		}
		catch(IOException e)
		{
			System.err.println("Client error: " + e.getMessage());
		}
	}
}
