package client.backend;

import client.common.ChatIF;
import ocsf.client.*;
import java.io.*;


/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String loginId;


  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
    sendToServer("#login " + loginId);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(String message) {
	    if (message.startsWith("#")) {
	        String[] parts = message.split(" ");
	        String command = parts[0];

	        switch (command) {
	        
	            case "#quit":
	                quit();
	                break;

	            case "#logoff":
	                if (isConnected()) {
	                    try {
	                        closeConnection();
	                        clientUI.display("Logged off from the server");
	                     } 
	                    catch (IOException e) {
	                        clientUI.display("Error while logging off");
	                    }
	                } 
	                else {
	                    clientUI.display("You are not connected");
	                }
	                break;
	                

	            case "#sethost":
	                if (!isConnected()) {
	                    if (parts.length >= 2) {
	                        setHost(parts[1]);
	                        clientUI.display("Host set to: " + getHost());
	                      } 
	          
	                   else {
	                    clientUI.display("Cannot set host while connected");
	                   }
	                   break;
	                }
	            case "#setport":
	                if (!isConnected()) {
	                    if (parts.length >= 2) {
	                        try {
	                            setPort(Integer.parseInt(parts[1]));
	                            clientUI.display("Port set to: " + getPort());
	                        } 
	                        catch (NumberFormatException e) {
	                            clientUI.display("Invalid port number");
	                        }
	                    } 
	                    else {
	                    clientUI.display("Cannot set port while connected");
	                    }
	                    break;
	                }
	            case "#gethost":
	                clientUI.display("Current host: " + getHost());
	                break;

	            case "#getport":
	                clientUI.display("Current port: " + getPort());
	                break;

	                }
  			  }    
	        
	          try {
	            sendToServer(message);
	        } catch (IOException e) {
	            clientUI.display("Could not send message to server... Terminating client");
	            quit();
	        }
	      }
     
  

 
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
	// connection to the server is closed
	
	@Override
	public void connectionClosed() {
	  clientUI.display("Server has closed the connection");
	  System.exit(0);
	}
	
	//exception occurs in the connection
	 
	@Override
	public void connectionException(Exception exception) {
	  clientUI.display("The server has shut down unexpectedly");
	  System.exit(0);
	}
	
}
	//End of ChatClient class
