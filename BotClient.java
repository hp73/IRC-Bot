import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.text.*;
import java.nio.charset.StandardCharsets;
import java.math.*;

/*
 * Runs the Bot given a specified server, port, nick, user, and channel.
 * Implements IRC commands such as JOIN, NICK, USER, PRIVMSG, TOPIC, KICK, RESTART, REHASH
 * 
 * @author Harry Pinkerton
 *
 * 
 */
public class BotClient {
  
  private BufferedWriter bwriter;
  private Socket socket;
  private String server   = "card.freenode.net";
  private final int port;
  private String nick = "PythonBot";
  private String user = "PythonBot 0 * :PythonBot";
  private String channel  = "#Cheers";
  private String botOutput  = "I am on a Quest For the Holy Grail";
  private String userInput; 

  /**
     * Initialise a new client. To run the client, call run(). Taken from Binary Client Practical.
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
     * Returns the current time and date 
     * @returns strDate - a string of the current time and date.
     */
  
  public String getTime(){
    Date date = Calendar.getInstance().getTime();  
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
    String strDate = dateFormat.format(date);
    return strDate;
  }
    
  public String getInsult(){
   // create a list of String type 
        String[] strArray = new String[5]; 
        // add 5 element in strArray
        strArray[0] = "You smell"; 
        strArray[1] = "You are ugly"; 
        strArray[2] = "Go away or I will taunt you a second time."; 
        strArray[3] = "I fart in your general direction"; 
        strArray[4] = "Your mother was a hamster, and your father smelt of elderberries"; 
        
        Random r=new Random();
        int randomNumber=r.nextInt(strArray.length);
        
        String strchosen = strArray[randomNumber];
        return strchosen;
  }
  

    
    /**
     * Prints the length of the packet recieved using ??'s and spaces. Taken from Binary CLient Practical.
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
      
      
       // Use the input and output stream directly
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();
      
      
      // Open OutputStreamWriter to write to a channel
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output);
      
      System.out.println("Opened BufferedWriter.");
      BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
     

     //Open InputStreamWriter to take commands from the user
      InputStreamReader inputStreamReader = new InputStreamReader(input);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      
      
      
      // Bot connects to specified channel
      sendMsg(bwriter,"NICK "+nick);
      sendMsg(bwriter,"USER "+ user);
      sendMsg(bwriter,"JOIN "+channel);
      sendMsg(bwriter,"PRIVMSG "+channel+" :"+ botOutput);
      sendMsg(bwriter, "TOPIC " + channel+" :" + "I'm on a Quest");
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
      System.out.println(str1);
 
 
      //List of Commands the Bot can perform while running.
      
      //!time command
      if (str1.contains("PRIVMSG "+ channel+ " :!time")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ "The time is " + getTime());
        bwriter.flush();
        }
      
      //!insult command
      if (str1.contains("PRIVMSG "+ channel+ " :!insult")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ getInsult());
        bwriter.flush();
        }
      
      // !greetings command
      if (str1.contains("PRIVMSG "+ channel+ " :!greetings")) {
        sendMsg(bwriter,"PRIVMSG "+channel+" :"+ "Why hello there");
        bwriter.flush();
        }
        
      // !goaway command
      if (str1.contains("PRIVMSG "+ channel+ " :!goaway")) {
        sendMsg(bwriter,"KICK " + channel + " " + nick + " :Rude but okay" );
        bwriter.flush();
        }
        
      // !rehash command
      if (str1.contains("PRIVMSG "+ channel+ " :!rehash")) {
        sendMsg(bwriter,"REHASH" );
        bwriter.flush();
        }
        
        // !restart command
      if (str1.contains("PRIVMSG "+ channel+ " :!restart")) {
        sendMsg(bwriter,"RESTART" );
        bwriter.flush();
        }

    }
    
  }

  /**
   * Main taken from binary client Practical.
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