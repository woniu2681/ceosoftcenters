package boomi.onb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PerAddressDEFLT {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    Document doc = builder.build("src/boomi/onb/OnboardingDataExchange1.XML");
    Element root = doc.getRootElement();
    String homeCountry = root.getChildText("Country");
    String mailCountry = root.getChildText("MailCountry");

    if (homeCountry != null && !homeCountry.isEmpty()) {
      Element perAddressDEFLT = new Element("SFOData.PerAddressDEFLT");
      perAddressDEFLT.addContent(new Element("addressTye").setText("home"));
      perAddressDEFLT.addContent(new Element("address1").setText(root.getChildText("Address1")));
      perAddressDEFLT.addContent(new Element("address2").setText(root.getChildText("Address2")));
      perAddressDEFLT.addContent(new Element("address3").setText(root.getChildText("Address3")));
      perAddressDEFLT.addContent(new Element("address4").setText(root.getChildText("Address4")));
      perAddressDEFLT.addContent(new Element("address5").setText(root.getChildText("Apartment")));
      perAddressDEFLT.addContent(new Element("city").setText(root.getChildText("City")));
      perAddressDEFLT.addContent(new Element("country").setText(root.getChildText("Country")));
      perAddressDEFLT.addContent(new Element("state").setText(root.getChildText("State")));
      perAddressDEFLT.addContent(new Element("zipCode").setText(root.getChildText("Zip")));
      InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perAddressDEFLT).getBytes("UTF-8"));
      System.out.println(xmlOutputter.outputString(perAddressDEFLT));
    }

    if (mailCountry != null && !mailCountry.isEmpty()) {
      Element perAddressDEFLT = new Element("SFOData.PerAddressDEFLT");
      perAddressDEFLT.addContent(new Element("addressTye").setText("mailing"));
      perAddressDEFLT.addContent(new Element("address2").setText(root.getChildText("MailAddress2")));
      perAddressDEFLT.addContent(new Element("address3").setText(root.getChildText("MailApartment")));
      perAddressDEFLT.addContent(new Element("city").setText(root.getChildText("MailCity")));
      perAddressDEFLT.addContent(new Element("country").setText(root.getChildText("MailCountry")));
      perAddressDEFLT.addContent(new Element("state").setText(root.getChildText("MailAddressState")));
      perAddressDEFLT.addContent(new Element("zipCode").setText(root.getChildText("MailZip")));
      InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perAddressDEFLT).getBytes("UTF-8"));
      System.out.println(xmlOutputter.outputString(perAddressDEFLT));
    }
  }
}
