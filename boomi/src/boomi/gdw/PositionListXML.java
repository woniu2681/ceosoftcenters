package boomi.gdw;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PositionListXML {
	public static void main(String[] args) throws Exception {
		TreeMap<String, Element> previousTreeMap = new TreeMap<String, Element>();
		TreeMap<String, Element> currentTreeMap = new TreeMap<String, Element>();
		TreeMap<String, Element> outputTreeMap = new TreeMap<String, Element>();
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

		for (int i = 0; i < 2; i++) {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new FileInputStream("src/boomi/gdw/PositionList" + i + ".xml"));

			Element root = doc.getRootElement();
			List<Element> elements = root.getChildren("Position");

			if (elements != null && !elements.isEmpty()) {
				for (Element element : elements) {
					String code = element.getChildText("code");
					String date = element.getChildText("effectiveStartDate");
					String key = code + "|" + date;
					Element ele = (Element) element.clone();
					if (i == 0) {
						previousTreeMap.put(key, ele);
					} else if (i == 1) {
						currentTreeMap.put(key, ele);
					}
				}
			}
			
			if (i == 1) {
				for (String key : previousTreeMap.keySet()) {
					if (!currentTreeMap.containsKey(key)) {
						Element previousElement = previousTreeMap.get(key);
						previousElement.addContent(new Element("action").setText("D - Delete"));
						outputTreeMap.put(key, previousElement);
					}
				}

				for (String key : currentTreeMap.keySet()) {
					Element currentElement = currentTreeMap.get(key);
					if (previousTreeMap.containsKey(key)) {
						Element previousElement = previousTreeMap.get(key);
						if (!xmlOutputter.outputString(currentElement).equals(xmlOutputter.outputString(previousElement))) {
							currentElement.addContent(new Element("action").setText("U - Update"));
							outputTreeMap.put(key, currentElement);
						}
					} else {
						currentElement.addContent(new Element("action").setText("I - Insert"));
						outputTreeMap.put(key, currentElement);
					}
				}

				for (Element element : outputTreeMap.values()) {
					InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(element).getBytes("UTF-8"));
					System.out.println(xmlOutputter.outputString(element));
				}
			}
		}
	}
}
