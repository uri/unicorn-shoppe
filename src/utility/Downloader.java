package utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Downloader {

	private static final String PROG_NAME = "run.jar";
	private static final String RUN_CMD = "java -jar ";
	private static final String RUN_PARAMS = "1200";
	private static final String REPO_URL = "http://stores.googlecode.com/files/run.jar";

	public static void main(String[] args) throws Exception {

		// download();
		// runProgram();
//		SaveLoad.createXML();
	}

	/**
	 * Downloads a file
	 * 
	 * @throws Exception
	 */
	public static void download() throws Exception {

		URL url = new URL(REPO_URL);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream output = new FileOutputStream(
				System.getProperty("user.home") + "\\Desktop\\" + PROG_NAME);
		output.getChannel().transferFrom(rbc, 0, 1 << 24);
	}

	/**
	 * Runs a program using java command
	 * 
	 * @param s
	 *            the name of the program.
	 * @throws IOException
	 *             Just Lazy
	 */
	public static void runProgram() throws IOException {
		Runtime.getRuntime().exec(
				RUN_CMD + System.getProperty("user.home") + "\\Desktop\\"
						+ PROG_NAME + " " + RUN_PARAMS);
	}

	
}
