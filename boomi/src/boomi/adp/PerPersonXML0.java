package boomi.adp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.TreeMap;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PerPersonXML0 {
  public static void main(String[] args) throws Exception {

    TreeMap<String, Element> perPersonTreeMap = new TreeMap<String, Element>();
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    JDOMXPath personIdExternalXPath = new JDOMXPath("//personIdExternal");
    JDOMXPath perPersonRelationshipXPath = new JDOMXPath("//PerPersonRelationship");

    for (int i = 0; i <= 2; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/adp/PerPerson" + i + ".xml"));
      Element personIdExternalElement = (Element) personIdExternalXPath.selectSingleNode(doc);
      Element perPersonRelationshipElement = (Element) perPersonRelationshipXPath.selectSingleNode(doc);

      String personIdExternal = personIdExternalElement.getText();

      if (personIdExternal != null && !personIdExternal.isEmpty()) {
        if (perPersonTreeMap.containsKey(personIdExternal)) {
          Element perPersonElement = perPersonTreeMap.get(personIdExternal);
          perPersonElement.addContent((Element) perPersonRelationshipElement.clone());
        } else {
          perPersonTreeMap.put(personIdExternal, doc.getRootElement());
        }
      }

      if (i == 2) {
        Element perPersonListElement = new Element("PerPersonList");
        perPersonListElement.setAttribute("isCurrent", "true");

        for (Element perPersonElement : perPersonTreeMap.values()) {
          perPersonListElement.addContent((Element)perPersonElement.clone());
        }

        InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perPersonListElement).getBytes("UTF-8"));
        System.out.println(xmlOutputter.outputString(perPersonListElement));
      }
    }
  }
}
