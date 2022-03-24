package va.ue03;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author Gregor Pawlak
 * @StudenId s0563317
 * 
 * @TODO
 */
public class Ue03DirectoryClient 
{
	private static JSONObject jo;
	private EchoClient el;

	public Ue03DirectoryClient() 
	{
		try {
			el = new EchoClient();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		Ue03DirectoryClient uedc = new Ue03DirectoryClient();
		uedc.query("exampleSID");
		uedc.query(null);
		uedc.register("UID12345","Gregor");
		uedc.unregister("UID12345","Gregor");
		uedc.reset("SIDexemple");
	}	

	public String query(String SID)
	{
		jo = new JSONObject();
		jo.put("Command", "query");
		if(SID != null)
		{
			jo.put("SID", SID);
		}
		return sendMsg(jo.toJSONString());
	}

	public String reset(String SID)
	{
		jo = new JSONObject();
		jo.put("Command", "reset");
		if(SID != null)
		{
			jo.put("SID", SID);
		}
		return sendMsg(jo.toJSONString());
	}

	public String register(String UID, String name)
	{
		jo = new JSONObject();
		jo.put("Command", "register");
		if(UID != null)
		{
			jo.put("UID", UID);
		}
		if(name != null)
		{
			jo.put("name", name);
		}
		return sendMsg(jo.toJSONString());
	}

	public String unregister(String UID, String name)
	{
		jo = new JSONObject();
		jo.put("Command", "unregister");
		if(UID != null)
		{
			jo.put("UID", UID);
		}
		if(name != null)
		{
			jo.put("name", name);
		}
		return sendMsg(jo.toJSONString());
	}
	
	public int returnNumber(int number)
	{
		return number;
	}

	private String sendMsg(String msg)
	{
		try {
			return el.sendEcho(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
