package va.ue03;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class EchoServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024]; //Buffergr��e von 256 kb
    
    /**
     * Konstruktor, bindet den Echo-Server an Port 11111
     * 
     * @throws SocketException
     */
    public EchoServer() throws SocketException 
    {
    	socket = new DatagramSocket(11111);
    }
    
    
    /**
     * Main mit Instanziierung des Echo-Servers und starten des Threads
     * 
     * @param args
     */
    public static void main(String[] args) {
    	try {
    		// Echo-Server l�uft in einem eigenen Thread
			EchoServer es = new EchoServer();
			es.run();
		} catch (SocketException e) 
    	{
			e.printStackTrace();
		}
	}
    
    /**
     *  Thread-Run
     *  
     *  Der Echo-Server l�uft in einer Endlosschleife, bis die Ubertragende Nachricht "end" enthalt.
     */
    public void run() 
    {
    	System.out.println("Start Server at 127.0.0.1");
        running = true;
        while (running) {
        	buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try 
            {
				socket.receive(packet);
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
			}
            
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            
            packet = new DatagramPacket(buf, buf.length, address, port);
            
            // Empfangene Nachricht besteht aus der Data-Load vom empfangenen Packet
            String received = new String(packet.getData(), 0, packet.getLength()); 
            
            // Begrenze den empfangenen String auf die letzte geschweifte Klammer (Buffer ist l�nger als emfange Nachricht)
            received = received.substring(0,received.lastIndexOf("}")+1 );
            
            // Wenn "end" empfangen wird dann beende die Schleife und damit den Thread
            if (received.equals("end")) 
            {
            	running = false;
            	continue;
            } 
            else
            {
            	// Empfangener String wird �ber die breakMsg-Funktion manipuliert und zur�ckgeschickt
            	String returnMsg  = breakMsg(received);
            	packet.setData(returnMsg.getBytes());
            	try 
            	{
            		socket.send(packet); // Zur�cksenden vom empfangenen Paket
            	} 
            	catch (IOException e) 
            	{
            		e.printStackTrace();
            	}
            }
        }
        socket.close();
    }
    
    /**
     * Versndere das empfangene Paket 
     * @param msg - empfangene Nachricht als String
     * @return - veranderter JSON-String
     * 
     * Die empfangene Nachricht wird als JSON geparsed und in ein JSONObject gecasted.
     * 
     */
    private String breakMsg(String msg)
    {
    	//JSOn-String in ein JSON-Object parsen und casten
    	System.out.println("REQUEST JSON-String to Break: " + msg);
    	
    	JSONObject jo = (JSONObject)JSONValue.parse(msg);
    	
    	String command = (String)jo.getOrDefault("Command", null);
    	if(command != null)
    	{
    		switch (command) 
    		{
			case "query": // if query command, send changed SID
				String SID = (String)jo.getOrDefault("SID", null);
                String[] users = {"UID1","UID2","UID3"};

				if(SID != null)
				{
					jo.remove("SID");
					jo.put("SID", "returned all users for sid: "+SID);
                    jo.put("users", Arrays.toString(users));
				}else{  jo.put("error", "cannot find SID"); }
				
				break;

            case "reset": // if reset command, removes SID
				String SIDres = (String)jo.getOrDefault("SID", null);
				if(SIDres != null)
				{
					jo.remove("SID");
					
				}else{  jo.put("error", "cannot find UID"); }
				
				break;


            case "register": // if register user command, send back changed uid and name
				String UID = (String)jo.getOrDefault("UID", null);
                String name = (String)jo.getOrDefault("name", null);

				if(UID != null)
				{
					jo.remove("UID");
					jo.put("UID", UID+" registered");
                    jo.remove("name");
					jo.put("name", name+" registered");
				}else{
                    jo.put("error", "cannot find UID");
                 }

				break;

            case "unregister": // if unregister command, send back removed uid and name
				String uidDel = (String)jo.getOrDefault("UID", null);
                String nameDel = (String)jo.getOrDefault("name", null);
				if(uidDel != null && nameDel != null)
				{
					jo.remove("UID");
					jo.put("UID", uidDel+" removed");
                    jo.remove("name");
					jo.put("name", nameDel+" removed");
				}else{
                    jo.put("error", "cannot find UID or name");
                 }
				break;

			default:
				break;
			}
    	}
    	
    	String changedJSONstring = jo.toJSONString();
    	System.out.println("RESPONSE Broken JSON-String : " + changedJSONstring);
    	
    	return changedJSONstring;	// JSONObject in String umwandeln.
    }
}