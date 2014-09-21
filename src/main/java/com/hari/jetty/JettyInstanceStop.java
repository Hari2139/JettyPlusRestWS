package com.hari.jetty;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * Description: 
 * File: $ JettyInstanceStop.java $
 * Module:  com.hari
 * Created: Oct 10, 2013
 * @author Hari 
 * @version $Revision:  $
 * Last Changed: $Date: Oct 10, 2013 10:11:52 PM $
 * Last Changed By: $ Hari $
 */
public class JettyInstanceStop {
	private static Logger logger = Log.getLogger(JettyInstanceStop.class);

	public static void main(String[] args) {
		try {
			logger.info("Sending STOP signal to Jetty server ...");
			int stopPort = Integer.valueOf(System.getProperty("STOP_PORT",
					"8079"));
			Socket socket = new Socket(InetAddress.getLocalHost(), stopPort);
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write("\r\n".getBytes());
			outputStream.flush();
			socket.close();
			logger.info("Done");
		}
		catch (Exception ex) {
			logger.warn(ex.getMessage(), ex);
		}
	}
}
/**
 *  Modification History:
 *
 *  $Log:  $
 *
 */
