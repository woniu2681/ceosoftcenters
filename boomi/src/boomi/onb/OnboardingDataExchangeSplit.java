package boomi.onb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OnboardingDataExchangeSplit {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    Document doc = builder.build("src/boomi/onb/OnboardingDataExchange.XML");
    Element root = doc.getRootElement();
    List<Element> list = root.getChildren();

    if (list != null && !list.isEmpty()) {
      for (Element element : list) {
        Element onboardingDataExchange = new Element("OnboardingDataExchange");
        List<Element> fields = element.getChildren("Field");
        if (fields != null && !fields.isEmpty()) {
          for (Element field : fields) {
            String id = field.getChildText("ID");
            String value = field.getChildText("Value");
            if (id != null && !id.isEmpty()) {
              id = id.replaceAll("\\W", "_");
              onboardingDataExchange.addContent(new Element(id).setText(value));
            }
          }
        }
        if (onboardingDataExchange != null && onboardingDataExchange.getContentSize() > 0) {
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(onboardingDataExchange).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(onboardingDataExchange));
        }
      }
    }
  }
}
