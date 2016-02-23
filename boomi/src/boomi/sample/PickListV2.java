package boomi.sample;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PickListV2 {
  public static void main(String[] args) throws Exception {
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    SAXBuilder builder = new SAXBuilder();
    JDOMXPath labelXPath = new JDOMXPath("./*[contains(name(),'label_')]");

    InputStream is = new FileInputStream("src/boomi/sample/PickListV2.xml");
    Document doc = builder.build(is);
    Element pickListV2 = doc.getRootElement();

    List<Element> pickListValueV2List = pickListV2.getChildren("values");
    for (Element pickListValueV2 : pickListValueV2List) {
      String pickListV2_id = pickListValueV2.getChildText("PickListV2_id");
      String externalCode = pickListValueV2.getChildText("externalCode");

      Element newPickListValueV2 = new Element("PickListValueV2");
      newPickListValueV2.addContent(new Element("PickListV2_id").setText(pickListV2_id));
      newPickListValueV2.addContent(new Element("externalCode").setText(externalCode));

      List<Element> labelList = labelXPath.selectNodes(pickListValueV2);
      for (Element label : labelList) {
        newPickListValueV2.addContent((Element) label.clone());
      }

      InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(newPickListValueV2).getBytes("UTF-8"));
      System.out.println(xmlOutputter.outputString(newPickListValueV2));
    }
  }
}