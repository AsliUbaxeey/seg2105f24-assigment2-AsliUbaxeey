package edu.server.backend;
import java.io.IOException;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  @Override
  protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
      String message = msg.toString();
     
      if (message.startsWith("#login")) {
          if (client.getInfo("loginId") != null) {
              try {
                  client.sendToClient("ERROR :Already logged in... Connection will close.");
                  client.close(); 
              } 
              catch (IOException e) {
                  System.out.println("Error closing client: " + e);
              }
          } 
          else {
              String loginId = message.substring(7).trim();
              client.setInfo("loginId", loginId);
              System.out.println("Logged in as: " + loginId);
              sendToAllClients(loginId + " has logged on.");

          }
          return;
      }
      
      
      // For all other messages
      String loginId = (String) client.getInfo("loginId");

      if (loginId == null) {
          try {
              client.sendToClient("ERROR: Must log in first. Connection will close.");
              client.close();
          } 
          catch (IOException e) {
              System.out.println("Error closing client: " + e);
          }
          return;
      }
      
      System.out.println("Received Message: " + message + " From "+ loginId);
      System.out.println(loginId + "> " + message);
      sendToAllClients(loginId + "> " + message);
  }




    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  @Override
  protected void serverStarted()
  {
    System.out.println("Server listening for clients on port: "+getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  @Override
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
	// part c overriding abstract server methods
  
  @Override
	protected void clientConnected(ConnectionToClient client) {
	  String loginId = (String) client.getInfo("loginId");
	  System.out.println("A new client has connected to the server");
	    if (loginId != null) {
	        System.out.println(loginId + " has connected.");
	        sendToAllClients(loginId + " has connected.");
	    }
    }

  @Override
    protected void clientDisconnected(ConnectionToClient client) {
	  String loginId = (String) client.getInfo("loginId");
	    if (loginId != null) {
	        System.out.println(loginId + " has disconnected.");
	        sendToAllClients(loginId + " has disconnected.");
	    } 
	    else {
	        System.out.println("A client has disconnected before logging in.");
	    }
    }

  @Override
    protected void clientException(ConnectionToClient client, Throwable exception) {
	  String loginId = (String) client.getInfo("loginId");
	    if (loginId != null) {
	        System.out.println("Connection closed with " + loginId );
	    } 
	    else {
	        System.out.println("Connection error with unknown client: " + exception.getMessage());
	    }
    }



  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
