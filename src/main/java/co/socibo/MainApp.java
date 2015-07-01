package co.socibo;

import co.socibo.bot.Bot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Character;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
/**
 * A Camel Application
 */
public class MainApp {

    private static final Pattern parsePattern = Pattern.compile("(.*)\\((.*)\\)");
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    static final Bot bot = new Bot();
    public static void main(String... args) throws Exception {

	final ScriptEngineManager manager = new ScriptEngineManager();
	final ScriptEngine engine = manager.getEngineByName("JavaScript");

	engine.put("bot", bot);
	
	ServerSocket welcomeSocket = new ServerSocket(6789);
	final Socket connectionSocket = welcomeSocket.accept();
	while(true){
	    String line;
	    
	    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	    line = inFromClient.readLine();

	    
	    try {
		Object result = engine.eval(line);
		if(result != null){
		    outToClient.writeChars(result.toString() + "\n");		    
		} else {
		    outToClient.writeChars("OK\n");
		};


	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} 
    }
}
