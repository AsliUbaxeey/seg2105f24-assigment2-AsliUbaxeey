package client.ui;

import client.backend.ChatClient;
import client.common.ChatIF;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginId,String host, int port) 
  {
    try 
    {
      client= new ChatClient(loginId,host, port,this);
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) {
	String loginId;
    String host = "localhost";
    int port = DEFAULT_PORT;
    
    if (args.length < 1) {
        System.out.println("ERROR: No login ID specified.. Connection failed.");
        System.exit(1);
    }
    loginId = args[0];
    
 // assume 2nd thing given is host
    if (args.length >= 2) {
      host = args[1];
    }
   
    // assume 3rd thing given is port
    if (args.length >= 3) {
    	
      try {
        port = Integer.parseInt(args[2]);
         } 
      catch (NumberFormatException e) { // catch if not number given
        System.out.println("Invalid. Using default port "+ DEFAULT_PORT);
        port = DEFAULT_PORT;
      }
    }

    ClientConsole chat = new ClientConsole(loginId,host, port);
    chat.accept();
  }
}
//End of ConsoleChat class
