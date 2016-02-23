package boomi.gdw;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PositionListXML_Full {
	public static void main(String[] args) throws Exception {
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

		for (int i = 0; i < 1; i++) {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new FileInputStream("src/boomi/gdw/PositionList" + i + ".xml"));

			Element root = doc.getRootElement();
			List<Element> elements = root.getChildren("Position");

			if (elements != null && !elements.isEmpty()) {
				for (Element element : elements) {
					element.addContent(new Element("action").setText("F - FirstRun"));
					InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(element).getBytes("UTF-8"));
					System.out.println(xmlOutputter.outputString(element));
				}
			}
		}
	}
}
