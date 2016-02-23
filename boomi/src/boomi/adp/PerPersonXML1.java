package boomi.adp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.TreeMap;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PerPersonXML1 {
  public static void main(String[] args) throws Exception {

    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    TreeMap<String, Element> previousTreeMap = new TreeMap<String, Element>();

    for (int i = 0; i < 2; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/adp/PerPersonList" + i + ".xml"));
      Element root = doc.getRootElement();
      String isCurrent = root.getAttributeValue("isCurrent");
      List<Element> perPersonList = root.getChildren();

      if (perPersonList != null && !perPersonList.isEmpty()) {
        for (Element perPerson : perPersonList) {
          String personIdExternal = perPerson.getChildText("personIdExternal");
          Element currentPerPerson = (Element) perPerson.clone();
          List<Element> currentPerPersonRelationshipList = currentPerPerson.getChildren("PerPersonRelationship");

          if (1 == 1) {
            if (isCurrent != null && isCurrent.equals("true")) {
              if (currentPerPersonRelationshipList != null && !currentPerPersonRelationshipList.isEmpty()) {
                int identificationMax = -1;
                for (Element currentPerPersonRelationship : currentPerPersonRelationshipList) {
                  identificationMax++;
                  currentPerPersonRelationship.addContent(new Element("identification").setText(Integer.toString(identificationMax)));
                }
                currentPerPerson.addContent(new Element("identificationMax").setText(Integer.toString(identificationMax)));
                InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(currentPerPerson).getBytes("UTF-8"));
                System.out.println(xmlOutputter.outputString(currentPerPerson));
              }
            }
          } else {
            if (i == 0) {
              previousTreeMap.put(personIdExternal, (Element) perPerson.clone());
            } else if (i == 1) {
              Element previousPerPerson = previousTreeMap.get(personIdExternal);
              Document previousDoc = new Document(previousPerPerson);

              if (previousPerPerson != null) {
                if (currentPerPersonRelationshipList != null && !currentPerPersonRelationshipList.isEmpty()) {
                  int identificationMax = -1;
                  for (Element currentPerPersonRelationship : currentPerPersonRelationshipList) {
                    String relatedPersonIdExternal = currentPerPersonRelationship.getChildText("relatedPersonIdExternal");
                    JDOMXPath relatedPersonIdExternalXPath = new JDOMXPath("//PerPersonRelationship[relatedPersonIdExternal='" + relatedPersonIdExternal + "']");
                    Element previousPerPersonRelationship = (Element) relatedPersonIdExternalXPath.selectSingleNode(previousPerPerson);
                    if (previousPerPersonRelationship != null) {
                      String identification = previousPerPersonRelationship.getChildText("identification");
                      currentPerPersonRelationship.addContent(new Element("identification").setText(identification));
                    } else {
                      String identificationMaxStr = previousPerPerson.getChildText("identificationMax");
                      if (identificationMaxStr != null) {
                        identificationMax = Integer.parseInt(identificationMaxStr);
                      }
                      identificationMax++;
                      currentPerPersonRelationship.addContent(new Element("identification").setText(Integer.toString(identificationMax)));
                    }
                  }
                  currentPerPerson.addContent(new Element("identificationMax").setText(Integer.toString(identificationMax)));
                  InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(currentPerPerson).getBytes("UTF-8"));
                  System.out.println(xmlOutputter.outputString(currentPerPerson));
                }
              } else {
                if (currentPerPersonRelationshipList != null && !currentPerPersonRelationshipList.isEmpty()) {
                  int identificationMax = -1;
                  for (Element currentPerPersonRelationship : currentPerPersonRelationshipList) {
                    identificationMax++;
                    currentPerPersonRelationship.addContent(new Element("identification").setText(Integer.toString(identificationMax)));
                  }
                  currentPerPerson.addContent(new Element("identificationMax").setText(Integer.toString(identificationMax)));
                  InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(currentPerPerson).getBytes("UTF-8"));
                  System.out.println(xmlOutputter.outputString(currentPerPerson));
                }
              }
            }
          }
        }
      }
    }
  }
}
