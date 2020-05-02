import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;

/*
 * Contains all Bot commands for the IRC bot.
 * 
 * @author Harry Pinkerton
 *
 * IRC protocol used: Nick, user, join, private message, 
 */

public class BotCommands {
  
  public void setBotName(String server, String channel, String nick){
    this.
    return;
  }
  
  
  public void sendMsg(BufferedWriter bw, String str) {
    try {
      bw.write(str + "\r\n");
      bw.flush();
    }
    catch (Exception e) {
      System.out.println("Exception: "+e);
    }
  }
  
  public void recieveMSG(BufferedReader br){
    
  }
  
  public void connect(String server, String channel, String nick){
    
    return;
  }
 
  public void GetText(){
    return;
  }
  
  public void GetTime(){
    Date date=java.util.Calendar.getInstance().getTime();
    System.out.println(date);
  }
}