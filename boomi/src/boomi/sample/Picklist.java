package boomi.sample;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Picklist {

  public static void main(String[] args) throws Exception {
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    SAXBuilder builder = new SAXBuilder();

    InputStream is = new FileInputStream("src/boomi/sample/Picklist.xml");
    Document doc = builder.build(is);
    Element picklist = doc.getRootElement();
    String picklistId = picklist.getChildText("picklistId");

    List<Element> pickListOptionList = picklist.getChildren("picklistOptions");
    for (Element pickListOption : pickListOptionList) {
      String id = pickListOption.getChildText("id");
      String externalCode = pickListOption.getChildText("externalCode");

      Element newPickListOption = new Element("PicklistOption");
      newPickListOption.addContent(new Element("id").setText(id));
      newPickListOption.addContent(new Element("externalCode").setText(externalCode));

      List<Element> picklistLabelList = pickListOption.getChildren("picklistLabels");
      for (Element picklistLabel : picklistLabelList) {
        String locale = picklistLabel.getChildText("locale");
        String label = picklistLabel.getChildText("label");
        if (locale != null && !locale.isEmpty()) {
          newPickListOption.addContent(new Element(locale).setText(label));
        }
      }
      
      newPickListOption.addContent(new Element("picklistId").setText(picklistId));
      InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(newPickListOption).getBytes("UTF-8"));
      System.out.println(xmlOutputter.outputString(newPickListOption));
    }
  }
}