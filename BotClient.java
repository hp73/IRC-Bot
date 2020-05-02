import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;

/*
 * Runs the Bot given a specified server, port, nick, and channel
 * 
 * @author Harry Pinkerton
 * 
 */

public class BotClient {
  
  private BotCommands BotCmd;
  private BufferedWriter bwriter;
  private Socket socket;
  private String server   = "beckett.freenode.net";
  private final int port;
  private String nick = "SadBoi";
  private String user = "SadBot 0 * :SadBot";
  private String channel  = "#mIRCSetUp";
  private String msgOut  = "[insert reply here]";
  private String msgIn; 
  
  
  /**
     * Initialise a new client. To run the client, call run().
     * @param port the port number to connect to
     */
  
    public BotClient(int port) {
        this.port = port;
    }
  
  
  void printHexLine(byte packet[]) {
	// Implement this!
    for (int i = 0; i < packet.length; ++i) {
	    System.out.print("??");
	    System.out.print(" ");
	    
      if (i % 8 == 7) {
		System.out.print(" ");
	    }
    }
	System.out.println("");	
  }
  
  public void run() throws IOException, InterruptedException{
      
      System.out.println("BinaryClient connecting to " + server + ": " + port);
      Socket socket = new Socket(server,port);
      BotCommands BotCmd = new BotCommands();
      
       // Use the input and output stream directly
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();
      
      
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
      System.out.println("*** Opened OutputStreamWriter.");
      BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
      System.out.println("*** Opened BufferedWriter.");
     
     
      InputStreamReader inputStreamReader = new InputStreamReader(input);
      System.out.println("*** Opened InPutStreamReader");
      
      BufferedReader breader = new BufferedReader(inputStreamReader);
      System.out.println("*** Opened BufferedReader.");
      
      msgIn = breader.readLine();
  
      BotCmd.sendMsg(bwriter,"NICK "+nick);
      BotCmd.sendMsg(bwriter,"USER "+ user);
      BotCmd.sendMsg(bwriter,"JOIN "+channel);
     
     
      BotCmd.sendMsg(bwriter,"PRIVMSG "+channel+" :"+ msgOut);
    
    
    
      
      
      while (true) {  // The client loops until interrupted

	    // If there is not a line's worth of data...
	    while (input.available() < 16) {
        Thread.sleep(1000);  // wait 1 second
	    }

	    // An array of byte to hold the packet
	    byte packet[] = new byte[16];

	    // Read it in
	    input.read(packet, 0, packet.length);

	    // Print it out in hex
	    printHexLine(packet);
    }


  }

  /**
     * @param args the command line arguments
     */
  public static void main(String args[]) throws IOException, InterruptedException {
    
    String usage = "Usage: java BotClient [<port-number>] ";
        if (args.length > 1) {
            throw new Error(usage);
        }
        
        int port = 0x1A0B;
        
        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            throw new Error(usage + "\n" + "<port-number> must be an integer");
        }
        
    BotClient client = new BotClient(port);
    client.run();
    
    
    
  }
}