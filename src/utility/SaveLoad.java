package utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Item;
import model.ItemFactory;
import model.Store;
import model.World;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SaveLoad {
	
	public final static String USR_DESKTOP = System.getProperty("user.home") + "\\Desktop\\";
	public static final String FILE_NAME = "stores.xml";

//	public static void main(String[] args) {
//		try {
//			SaveLoad.readXML();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Creates the XML file for the game that should be used to keep track of
	 * everything.
	 * 
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void createXML(World world) throws ParserConfigurationException, TransformerException, SAXException, IOException {

		Document xml = getDocument(null);
		Element money, gameTime, game;
		Element store, quantity, type, item;

		game = xml.createElement("Game");
		xml.appendChild(game);

		// Money
		money = xml.createElement("money");
		money.setTextContent(String.valueOf(world.getMoney()));
		game.appendChild(money);

		gameTime = xml.createElement("gametime");
		gameTime.setTextContent(String.valueOf(System.currentTimeMillis()));
		game.appendChild(gameTime);

		
		// You'll need to loop through this eventually
		store = xml.createElement("store");
		game.appendChild(store);
		Attr attr = xml.createAttribute("id");
		attr.setValue("1");
		store.setAttributeNode(attr);
		
		// Loop through the items
		for (Item i : world.getStore().getItems()) {
			item = xml.createElement("item");
			store.appendChild(item);
			attr = xml.createAttribute("id");
			attr.setValue("" + i.getType());
			item.setAttributeNode(attr);
			
			quantity = xml.createElement("quantity");
			quantity.setTextContent("" + i.getAmount());
			item.appendChild(quantity);
	
		}
		// Write the content into xml file
		writeXML(xml, new File(FILE_NAME));

	}

	/**
	 * Modifies the XML file
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void modifyXML() throws ParserConfigurationException, SAXException, IOException {
		Document doc = getDocument(new File(USR_DESKTOP + FILE_NAME));
		// ...
		// ...
		// ...
	}
	
	public static World load() {
	    try {
            return readXML();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}

	/**
	 * Should return an array of store objects
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static World readXML() throws ParserConfigurationException, SAXException, IOException {
		File xml = new File(FILE_NAME);
		Document doc = getDocument(xml);
		doc.normalize();
		
		// Get the gameTime and previous money
		long gameTime = Long.parseLong(readSingleElement(doc, "gametime"));
		float money = Float.parseFloat(readSingleElement(doc, "money"));
		
		Item[] itemsList = new Item[ItemFactory.ITEM_COUNT];
		
		// Loop through the stores (there's only 1 so far)
		NodeList storeList = doc.getElementsByTagName("store");
		for (int i = 0; i < storeList.getLength(); i++) {
			Element outterE = (Element)storeList.item(i);
			
			// Loop through the items in the stores
			NodeList items = outterE.getElementsByTagName("item");
			for (int j = 0; j < items.getLength(); j++) {
				Element e = (Element)items.item(j);
				
				// Create and add the items to the store
				int itemID = Integer.parseInt(e.getAttribute("id"));
				
				
				int quantity = Integer.parseInt(readSingleElement(e, "quantity"));
				itemsList[itemID] = (ItemFactory.createItem(itemID, quantity));
			}
		}
		
		System.out.println("SaveLoad:: Returning saved world.");
		
		// Create the store
		Store store = new Store(money, gameTime, itemsList);
		// Return the user's saved game
		return new World(gameTime, money, store);
	}
/******************************************************************************************
 * 			HELPERS
 ******************************************************************************************/
	/**
	 * Helper method used to reduce redundancy
	 * 
	 * @param filepath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document getDocument(File filepath) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		if (filepath != null) {
			return docBuilder.parse(filepath);
		} else {
			return docBuilder.newDocument();

		}

	}

	/**
	 * Actually outputs the xml file
	 * @throws TransformerException
	 * 
	 */
	private static void writeXML(Document doc, File filepath) throws TransformerException {
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result;
		if (filepath != null)
			result = new StreamResult(filepath);
		else
			result = new StreamResult(System.out);

		transformer.transform(source, result);

	}
	
	/**
	 * Returns a single element specified.
	 * @param doc
	 * @param eleName
	 * @return
	 */
	private static String readSingleElement(Document doc, String eleName) {
		NodeList ele;
		ele = doc.getElementsByTagName(eleName);
		return ((Element)ele.item(0)).getTextContent();
	}
	
	   private static String readSingleElement(Element e, String eleName) {
	        NodeList ele;
	        ele = e.getElementsByTagName(eleName);
	        return ((Element)ele.item(0)).getTextContent();
	    }

	public static void save(World world) throws ParserConfigurationException, TransformerException, SAXException, IOException {
		System.out.println("Saving world...");
		File savePath = new File(".\\" + FILE_NAME);
		
		if (savePath != null) {
			createXML(world);
			System.out.println("Game saved successfully.");
		}
	}
}
