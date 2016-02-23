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

public class EmployeeListXML {
  public static void main(String[] args) throws Exception {
    TreeMap<String, Element> previousTreeMap = new TreeMap<String, Element>();
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    JDOMXPath employeeIdXPath = new JDOMXPath("//EMPLOYEE_ID");

    for (int i = 0; i < 2; i++) {
      InputStream is = new FileInputStream("src/boomi/adp/EmployeeList" + i + ".xml");
      Document doc = builder.build(is);
      Element root = doc.getRootElement();
      List<Element> employeeList = root.getChildren("Employee");

      if (employeeList != null && !employeeList.isEmpty()) {
        for (Element employeeElement : employeeList) {
          Element newEmployee = (Element) employeeElement.clone();
          Document newDocument = new Document(newEmployee);
          Element personIdExternalElement = (Element) employeeIdXPath.selectSingleNode(newDocument);
          String personIdExternal = personIdExternalElement.getText();

          if (i == 0) {
            previousTreeMap.put(personIdExternal, newEmployee);
          } else if (i == 1) {
            Element previousElement = previousTreeMap.get(personIdExternal);
            Element currentElement = newEmployee;
            Element outputElement = (Element) newEmployee.clone();
            if (previousElement != null) {
              List<Element> currentChildren = currentElement.getChildren();
              if (currentChildren != null && !currentChildren.isEmpty()) {
                boolean isChanged = false;
                for (Element currentChild : currentChildren) {
                  String currentChildName = currentChild.getName();
                  Element previousChild = previousElement.getChild(currentChildName);

                  String previousChildXML = previousChild != null ? xmlOutputter.outputString(previousChild) : null;
                  String currentChildXML = xmlOutputter.outputString(currentChild);

                  if (previousChildXML != null && !previousChildXML.isEmpty()) {
                    if (!currentChildXML.equals(previousChildXML)) {
                      isChanged = true;
                    } else {
                      outputElement.removeChild(currentChildName);
                    }
                  } else {
                    isChanged = true;
                  }
                }

                if (isChanged) {
                  if (outputElement.getChild("PIV_KY_REG") == null) {
                    Element employeeIdElement = new Element("EMPLOYEE_ID");
                    employeeIdElement.setText(personIdExternal);
                    Element keyElement = new Element("PIV_KY_REG");
                    keyElement.setContent(employeeIdElement);
                    outputElement.addContent(keyElement);
                  }
                  InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(outputElement).getBytes("UTF-8"));
                  System.out.println(xmlOutputter.outputString(outputElement));
                }
              }
            } else {
              InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(outputElement).getBytes("UTF-8"));
              System.out.println(xmlOutputter.outputString(outputElement));
            }
          }
        }
      }
    }
  }
}
