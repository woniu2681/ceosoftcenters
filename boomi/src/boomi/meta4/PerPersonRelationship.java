package boomi.meta4;

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

public class PerPersonRelationship {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    JDOMXPath dateOfBirthXPath = new JDOMXPath("//dateOfBirth");

    TreeMap<String, Element> relationShipTreeMap = new TreeMap<String, Element>();

    for (int i = 0; i <= 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/meta4/PerPersonRelationship" + i + ".xml"));
      Element root = doc.getRootElement();
      String personIdExternal = root.getChildText("personIdExternal");

      String firstName = root.getChildText("firstName");
      String lastName = root.getChildText("lastName");
      String fullName = "";
      if (firstName != null && !firstName.isEmpty()) {
        fullName = firstName;
      }
      if (lastName != null && !lastName.isEmpty()) {
        fullName = fullName + " " + lastName;
      }

      Element dateOfBirthElement = (Element) dateOfBirthXPath.selectSingleNode(doc);
      String dateOfBirth = dateOfBirthElement != null ? dateOfBirthElement.getText() : null;
      dateOfBirth = dateOfBirth != null ? dateOfBirth.replace("-", "/") : null;

      if (relationShipTreeMap.containsKey(personIdExternal)) {
        Element relationShip = relationShipTreeMap.get(personIdExternal);

        String childrenNumberStr = relationShip.getChildText("childrenNumber");
        int childrenNumber = Integer.parseInt(childrenNumberStr) + 1;
        relationShip.addContent(new Element("childrenNumber").setText(Integer.toString(childrenNumber)));

        Element childElement = new Element("Child_" + childrenNumber);
        childElement.addContent(new Element("Child_Name_1").setText(fullName));
        childElement.addContent(new Element("Child_Birth_Date_1").setText(dateOfBirth));

        relationShip.addContent(childElement);

        relationShipTreeMap.put(personIdExternal, relationShip);
      } else {
        Element relationShip = new Element("PerPersonRelationship");
        relationShip.addContent(new Element("personIdExternal").setText(personIdExternal));
        relationShip.addContent(new Element("childrenNumber").setText("1"));

        Element childElement = new Element("Child_1");
        childElement.addContent(new Element("Child_Name_1").setText(fullName));
        childElement.addContent(new Element("Child_Birth_Date_1").setText(dateOfBirth));

        relationShip.addContent(childElement);

        relationShipTreeMap.put(personIdExternal, relationShip);
      }

      if (i == 1) {
        for (Element relationShip : relationShipTreeMap.values()) {
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(relationShip).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(relationShip));
        }
      }
    }
  }
}
