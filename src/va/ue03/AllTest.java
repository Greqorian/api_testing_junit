package va.ue03;

import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllTest {
	
	Ue03DirectoryClient uedc;
	
	@BeforeEach
	void setUp() throws Exception 
	{
		uedc = new Ue03DirectoryClient();
	}

	@AfterEach
	void tearDown() throws Exception 
	{
		
	}

	/**
	 * Test der Query-Funktion auf mitgeschickte SID
	 */
	@Test
	void testQuery() {
		
		// In der speteren Implementation werden wir bei der Query-Anfrage alle registrierten 
		// Teilnehmer erwarten. 
		// expect array of users to exists
		
		String echoAnswer 	= uedc.query("1");
		JSONObject jo 		= (JSONObject)JSONValue.parse(echoAnswer);
		String echoSID 	= (String)jo.get("SID");
		
		
		assertEquals("1", echoSID);			// Ergebnis-Vergleich f�r den TEST
	}
	
	/**
	 * Test empty SID number
	 */
	@Test
	void testQueryEmptySID()
	{
		String echoAnswer = uedc.query(null); 
		JSONObject jo = (JSONObject)JSONValue.parse(echoAnswer);
		String echoSID = (String)jo.get("SID");
		
		assertEquals(null, echoSID);			// Ergebnis-Vergleich f�r den TEST
	}

	/**
	 * Test returned number
	 */
	@Test
	void testReturnedNumber()
	{
		int returnedNumber = uedc.returnNumber(123);
		assertEquals(123, returnedNumber);
			
	}

	@Test
	void testQueryUsers() {
		
		// expect array of users to exists
		
		String echoAnswer 	= uedc.query("1");
		JSONObject jo 		= (JSONObject)JSONValue.parse(echoAnswer);
		String echoUSers 	= (String)jo.get("users");
		
		assertTrue(echoUSers != null);
		
	}

	@Test
	void testRegistration() {
		
		// expect UID and name exist 
		// the values of UID and name are broken in EchoServer
		
		String echoAnswer 	= uedc.register("UID1234","Gregor");
		JSONObject jo 		= (JSONObject)JSONValue.parse(echoAnswer);
		String echoUID	= (String)jo.get("UID");
		String echoName	= (String)jo.get("name");
		
		assertEquals("UID1234 Gregor", echoUID + " " + echoName );
		
	}

	@Test
	void testReset() {
		
		// expect SID not to exist
		
		String echoAnswer 	= uedc.reset("1");
		JSONObject jo 		= (JSONObject)JSONValue.parse(echoAnswer);
		String echoSID	= (String)jo.get("SID");
		
		
		assertTrue( echoSID == null );
		
	}

	@Test
	void testRegistrationEmptyUID() {
		
		// expect error message 
		
		String echoAnswer 	= uedc.register(null,null);
		JSONObject jo 		= (JSONObject)JSONValue.parse(echoAnswer);
		String echoError	= (String)jo.get("error");
		
		assertEquals("cannot find UID or name", echoError );
		
	}

	@Test
	void testUnregister() {
		
		// expect test to be failed / expect  UID and name changed by Server
		
		String echoAnswer 	= uedc.unregister("UID1234","Gregor");
		JSONObject jo 		= (JSONObject)JSONValue.parse(echoAnswer);
		String echoUID	= (String)jo.get("UID");
		String echoname	= (String)jo.get("name");
		
		assertEquals("UID1234 Gregor", echoUID + echoname );
		
	}

}
