package scraping;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientBackup {

  public static void main(String args[]) {
    BufferedReader in = null;
    PrintStream out = null;
    Socket socket = null;
    String message;

    try
    {
      socket = new Socket("localhost", 4000);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintStream(socket.getOutputStream(), true);

      message = in.readLine();

      out.close();
      in.close();
    } catch (Exception e) {
		// TODO: handle exception
	}
  }
}
