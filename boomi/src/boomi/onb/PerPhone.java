package boomi.onb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PerPhone {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    Document doc = builder.build("src/boomi/onb/OnboardingDataExchange1.XML");
    Element root = doc.getRootElement();
    String homePhoneNumber = root.getChildText("EveningPhoneNum");
    String workPhoneNumber = root.getChildText("DaytimePhoneNum");

    if (homePhoneNumber != null && !homePhoneNumber.isEmpty()) {
      Element perPhone = new Element("SFOData.PerPhone");
      perPhone.addContent(new Element("phoneType").setText("8179"));
      perPhone.addContent(new Element("areaCode").setText(root.getChildText("EveningPhoneAC")));
      perPhone.addContent(new Element("countryCode").setText(root.getChildText("EveningPhoneCountryCode")));
      perPhone.addContent(new Element("isPrimary").setText("false"));
      perPhone.addContent(new Element("phoneNumber").setText(root.getChildText("EveningPhoneNum")));
      InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perPhone).getBytes("UTF-8"));
      System.out.println(xmlOutputter.outputString(perPhone));
    }

    if (workPhoneNumber != null && !workPhoneNumber.isEmpty()) {
      Element perPhone = new Element("SFOData.PerPhone");
      perPhone.addContent(new Element("phoneType").setText("8180"));
      perPhone.addContent(new Element("areaCode").setText(root.getChildText("DaytimePhoneAC")));
      perPhone.addContent(new Element("countryCode").setText(root.getChildText("DaytimePhoneCountryCode")));
      perPhone.addContent(new Element("isPrimary").setText("true"));
      perPhone.addContent(new Element("phoneNumber").setText(root.getChildText("DaytimePhoneNum")));
      InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perPhone).getBytes("UTF-8"));
      System.out.println(xmlOutputter.outputString(perPhone));
    }
  }
}
