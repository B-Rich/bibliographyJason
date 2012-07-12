package scraping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Webkit {
	private static final String webkit = "/home/simon/projects/webkit-server/build/webkit_server --ignore-ssl-errors --port ";
	private static final String default_port = "50000";
	private BufferedReader in;
	private PrintStream out;
	private Socket socket;

	public Webkit(String port) {
		in = null;
		out = null;
		socket = null;
		
		try
		{
			String command = webkit;
			
			if(port == null || port.trim().equals("")) {
				port = default_port;
			}
			
			command += port;
			
			System.out.println("command: "+command);
			
			Runtime.getRuntime().exec(command);
			
			Thread.sleep(2000);
			
			socket = new Socket("localhost", Integer.valueOf(port).intValue());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream(), true);
			System.out.println("initialized");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Webkit() {
		this(null);
	}
	
	
	public String sendCommand(String cmd, String... args) throws Exception {
		System.out.println("sendCommand: "+cmd);
		System.out.println("args: "+args.length);
		out.print(cmd+"\n");
		out.print(String.valueOf(args.length)+"\n");
		for(String arg : args) {
			System.out.println("arg: "+arg);
			out.print(arg.length()+"\n");
			out.print(arg);
		}
		return receiveMessage();
	}
	
	public String receiveMessage() throws Exception {
		System.out.println("receiveMessage");
		String result = "";
		String message = in.readLine();
		
		System.out.println("msg: "+message);
		
		if(!message.equals("ok"))
			throw new Exception("Invalid message");
		
		message = in.readLine();
		System.out.println("message: "+message);
		
		int length = Integer.valueOf(message);
		System.out.println("length: "+length);
		
		/*int length = Integer.valueOf(message.substring(0, 1));
		System.out.println("length: "+length);
		
		if(length > 0) {
			result += message.substring(1);
			length -= message.length() - 1;
			System.out.println("length: "+length);
			System.out.println("result: "+result);
		}*/
		
		char[] buffer = new char[length];
		int charread = 0;
		
		while(length > 0) {
			System.out.println("read");
			charread = in.read(buffer, charread, length);
			length -= charread;
			result += String.valueOf(buffer).trim();
			System.out.println("length: "+length);
		}
		
		System.out.println("return: "+result);
				
		return result;
	}
}