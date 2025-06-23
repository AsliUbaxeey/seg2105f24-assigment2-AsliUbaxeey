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
      System.out.println("Received message: " + message);// debugging cus i dont think ChatClient is calling this method
      
      if (message.startsWith("#login")) {
          if (client.getInfo("loginId") != null) {
              try {
                  client.sendToClient("ERROR - Already logged in. Connection will close.");
                  client.close(); // close connection
              } catch (IOException e) {
                  System.out.println("Error closing client: " + e);
              }
          } else {
              String loginId = message.substring(7).trim();
              client.setInfo("loginId", loginId);
              System.out.println("Logged in as: " + loginId);
          }
          return;
      }

      // For all other messages
      String loginId = (String) client.getInfo("loginId");

      if (loginId == null) {
          try {
              client.sendToClient("ERROR - Must log in first. Connection will close.");
              client.close();
          } catch (IOException e) {
              System.out.println("Error closing client: " + e);
          }
          return;
      }

      // Send normal messages prefixed with loginId
      System.out.println(loginId + "> " + message);
      sendToAllClients(loginId + "> " + message);
  }




    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
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
	        System.out.println("Client connected: " + client);
    }

  @Override
    protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected: " + client);
    }

  @Override
    protected void clientException(ConnectionToClient client, Throwable exception) {
        System.out.println("Client connection closed unexpectedly: " + client);
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
