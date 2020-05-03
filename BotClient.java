import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.nio.charset.StandardCharsets;

/*
 * Runs the Bot given a specified server, port, nick, user, and channel. Implements IRC commands such as JOIN, NICK, USER, PRIVMSG, 
 * 
 * @author Harry Pinkerton
 *
 * 
 */
public class BotClient {
  
  private BotCommands BotCmd;
  private BufferedWriter bwriter;
  private Socket socket;
  private String server   = "selsey.nsqdc.city.ac.uk";
  private final int port;
  private String nick = "RudeBot";
  private String user = "RudeBot 0 * :RudeBot";
  private String channel  = "#Cheers";
  private String botOutput  = "I am now connected";
  private String userInput; 

  /**
     * Initialise a new client. To run the client, call run().
     * @param port the port number to connect to
     */
  
    public BotClient(int port) {
        this.port = port;
    }
  
    
    /**
     * Used to send messages across a channel from the bot to users.
     * @param bw - BufferedWriter that 
     * @param str - message to be sent ac
     */
    
    public void sendMsg(BufferedWriter bw, String str) {
    try {
      bw.write(str + "\r\n");
      bw.flush();
    }
    catch (Exception e) {
      System.out.println("Exception: "+e);
    }
  }
  
    /**
     * Reads user input from the IRC Server
     * @param br  - Buffered Reader to read the lines from the inputstream.
     */
  
  public void recieveMsg(BufferedReader br){
    try {
      userInput = br.readLine();
      System.out.println(userInput);
    }
    catch (Exception e) {
      System.out.println("Exception: "+e);
    }
  }
  
  
  /**
     * Returns the current time
     * @returns 
     */
  
  public void getTime(){
    Date date=java.util.Calendar.getInstance().getTime();
    System.out.println(date);
  }
    
    
    /**
     * Prints the length of the packet recieved using ??'s and spaces.
     * @param packet[] - array in which the byte stream is stored
     */
    
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
      
      System.out.println("BotClient connecting to " + server + ": " + port);
      
      // Initialize socket and Botcommand objects
      Socket socket = new Socket(server,port);
      
      getTime();
      
      
       // Use the input and output stream directly
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();
      
      
      // Open OutputStreamWriter to write to a channel
      System.out.println("Opened OutPutStreamWriter.");
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
      
      System.out.println("Opened BufferedWriter.");
      BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
     

     //Open InputStreamWriter to take commands from the user
      System.out.println("Opened InPutStreamReader");
      InputStreamReader inputStreamReader = new InputStreamReader(input);
      
      System.out.println("Opened BufferedReader.");
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      
      
      
      // Bot connects to specified channel
      sendMsg(bwriter,"NICK "+nick);
      sendMsg(bwriter,"USER "+ user);
      sendMsg(bwriter,"JOIN "+channel);
      sendMsg(bwriter,"PRIVMSG "+channel+" :"+ botOutput);
      sendMsg(bwriter, "TOPIC " + channel+" :" + "I run this town now");
      bwriter.flush();
     
       
      while (true) {  // The client loops until interrupted

	    // If there is not a line's worth of data...
	    while (input.available() < 16) {
        Thread.sleep(1000);  // wait 1 second
	    }

	    // An array of byte to hold the packet
	    byte packet[] = new byte[512];

	    // Read it in
	    input.read(packet, 0, packet.length);

	    // Print it out in hex
	    //printHexLine(packet);
      
      String str1 = new String(packet);
 
 
 
      //List of Commands the Bot can perform while running. 
      if (str1.contains("!tellthetime")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ "The time is ___");
        bwriter.flush();
        }
      
      if (str1.contains("!insultme")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ "You're ugly");
        bwriter.flush();
        }
      
      if (str1.contains("!help")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ "I would but I am pretty busy right now");
        bwriter.flush();
        }
        
      if (str1.contains("!commands")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ "I would but I am pretty busy right now");
        bwriter.flush();
        }
      
      if (str1.contains("!quit")) {
        sendMsg(bwriter, "QUIT : I never wanna see you again");
        bwriter.flush();
        }
      
      if (str1.contains("PING")) {
        sendMsg(bwriter, "QUIT : I never wanna see you again");
        bwriter.flush();
        }  
     
  
      
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