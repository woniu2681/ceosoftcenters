package boomi.adp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EmployeeXML {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    Element employeeList = new Element("EmployeeList");

    for (int i = 0; i <= 2; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/adp/Employee" + i + ".xml"));
      Element root = doc.getRootElement();
      employeeList.addContent((Element)root.clone());

      if (i == 2) {
        InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(employeeList).getBytes("UTF-8"));
        System.out.println(xmlOutputter.outputString(employeeList));
      }
    }
  }
}
