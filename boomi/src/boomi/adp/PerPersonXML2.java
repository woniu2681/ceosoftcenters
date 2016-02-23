package boomi.adp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PerPersonXML2 {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    Element perPersonList = new Element("PerPersonList");

    for (int i = 0; i <= 2; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/adp/PerPerson" + i + ".xml"));
      Element perPerson = doc.getRootElement();
      perPersonList.addContent((Element) perPerson.clone());

      if (i == 2) {
        InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perPersonList).getBytes("UTF-8"));
        System.out.println(xmlOutputter.outputString(perPersonList));
      }
    }
  }
}
