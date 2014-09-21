package com.hari.jetty;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 * Description: 
 * File: $ JettyInstance.java $
 * Module:  com.hari
 * Created: Oct 9, 2013
 * @author Hari 
 * @version $Revision:  $
 * Last Changed: $Date: Oct 9, 2013 9:01:59 PM $
 * Last Changed By: $ Hari $
 */
public class JettyInstance {
	/** The logger. */
	public static Logger logger = Log.getLogger(JettyInstance.class);
	/** The server. */
	private static Server server;
	//Secret key to secure certain REST operations
	/** The Constant SECRET_KEY. */
	public static final String SECRET_KEY = new BigInteger(128,
			new SecureRandom()).toString();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		//Setup Logging
		File logConfig = new File("log4j.properties");
		if (logConfig.exists()) {
			PropertyConfigurator.configureAndWatch(logConfig.getPath());
		}
		server = new Server(8080);
		WebAppContext webAppContext = new WebAppContext();
		URL location = JettyInstance.class.getProtectionDomain()
				.getCodeSource().getLocation();
		logger.info("WAR location: " + location.toString());
		webAppContext.setDescriptor(Resource.newResource(
				JettyInstance.class.getResource("/WEB-INF/web.xml")
						.toExternalForm()).toString());
		webAppContext.setWar(location.toExternalForm());
		webAppContext.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(), new TagLibConfiguration(),
				new PlusConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration(), new WebInfConfiguration(),
				new WebXmlConfiguration(), new MetaInfConfiguration() });
		server.setHandler(webAppContext);
		MonitorThread monitorThread = new MonitorThread();
		monitorThread.start();
		server.start();
		server.join();
	}

	/**
	 * The Class MonitorThread.
	 */
	private static class MonitorThread extends Thread {
		/** The server socket. */
		private ServerSocket serverSocket;

		/**
		 * Instantiates a new monitor thread.
		 */
		public MonitorThread() {
			setDaemon(true);
			setName("Stop monitor");
			try {
				int stopPort = Integer.valueOf(System.getProperty("STOP_PORT",
						"8079"));
				serverSocket = new ServerSocket(stopPort, 1,
						InetAddress.getLocalHost());
			}
			catch (Exception ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			//Open a socket and listen
			logger.info("Running Jetty STOP thread ...");
			try {
				Socket socket = serverSocket.accept();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				reader.readLine();
				logger.info("Stopping embedded Jetty server ...");
				server.stop();
				socket.close();
				serverSocket.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.warn(e.getMessage(), e);
			}
		}
	}
}
