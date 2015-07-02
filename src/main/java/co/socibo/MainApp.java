package co.socibo;

import co.socibo.bot.Bot;
import java.util.List;
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
import javax.script.ScriptEngineFactory;
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

	//List<ScriptEngineFactory> factories = manager.getEngineFactories();

	// for (ScriptEngineFactory factory : factories) {

	//     System.out.println("ScriptEngineFactory Info");

	//     String engName = factory.getEngineName();
	//     String engVersion = factory.getEngineVersion();
	//     String langName = factory.getLanguageName();
	//     String langVersion = factory.getLanguageVersion();

	//     System.out.printf("\tScript Engine: %s (%s)%n", engName, engVersion);

	//     List<String> engNames = factory.getNames();
	//     for(String name : engNames) {
	// 	System.out.printf("\tEngine Alias: %s%n", name);
	//     }

	//     System.out.printf("\tLanguage: %s (%s)%n", langName, langVersion);
	// }
	
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

		StringBuffer sb = new StringBuffer(4096);// FIXME: see how large scripts are basically


		boolean lastLineWasEmpty = false;
		while(!shutdownCalled.get()){
		    line = inFromClient.readLine();
		    if(line.isEmpty()){
			if(lastLineWasEmpty){
			    break; // end transfer
			}
			
			lastLineWasEmpty = true;
		    } else {
			lastLineWasEmpty = false;
			sb.append(line);
		    }
		}

		
		if(sb.equals("]]^")){
		    connectionSocket.close();
		    break; // end session		    
		}
		try {
		    Object result = engine.eval(sb.substring(0));
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

