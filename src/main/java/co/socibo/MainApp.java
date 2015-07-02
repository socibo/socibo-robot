package co.socibo;

import co.socibo.bot.Bot;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    static final Bot bot = new Bot();
    public static void main(String... args) throws Exception {

	final AtomicBoolean shutdownCalled = new AtomicBoolean(false);

	final ScriptEngineManager manager = new ScriptEngineManager();
	final ScriptEngine engine = manager.getEngineByName("JavaScript");

	engine.put("bot", bot);

	Runtime.getRuntime().addShutdownHook(new Thread() {
		public void run() { shutdownCalled.set(true); }
	    });
	
	ServerSocket serverSock = new ServerSocket();
	InetAddress addr = InetAddress.getByName("0.0.0.0");
	serverSock.bind(new InetSocketAddress(addr, 6789));

	while(!shutdownCalled.get()){
	
	    final Socket connectionSocket = serverSock.accept();
	    while(!shutdownCalled.get()){
		String line;
	    
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		line = inFromClient.readLine();
		if(line == null)
		    break; // end session

	    
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
}

