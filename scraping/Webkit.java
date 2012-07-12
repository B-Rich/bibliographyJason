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
	private Process process;

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
			
			process = Runtime.getRuntime().exec(command);
			
			Thread.sleep(2000);
			
			socket = new Socket("localhost", Integer.valueOf(default_port).intValue());
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
		char[] buffer = new char[8192];
		int characters_read;
		String message = in.readLine();
		
		System.out.println("msg: "+(message.equals("")));
		
		if(!message.equals("ok"))
			throw new Exception("Invalid message");
		
		characters_read = in.read(buffer, message.length(), 1);
		
		String value = String.valueOf(buffer).trim();
		System.out.println("value: <<"+value+">>");
		
		int length = Integer.valueOf(value);
		
		System.out.println("length: "+length);
		
		message = "";
		
		if(length==0) {
			message = in.readLine();
			System.out.println("msg2: "+(message.equals("")));
		}
		
		while(length > 0) {
			System.out.println("leggo");
			characters_read = in.read(buffer);
			System.out.println("characters_read: "+characters_read);
			message += String.valueOf(buffer).trim();
			length -= characters_read;
		}
		
		System.out.println("return: "+message);
				
		return message;
	}
	
	public static void main(String args[]) throws Exception {
		Webkit c = new Webkit();
		
		//Set ErrorTolerant
		c.sendCommand("SetErrorTolerance", "true");
		
		// Visit Command
		// String result = c.sendCommand("Visit", "http://www.google.it");
		String result = c.sendCommand("Visit", "http://localhost/scholar.htm");
		
		System.out.println("result: "+result);
	}
}