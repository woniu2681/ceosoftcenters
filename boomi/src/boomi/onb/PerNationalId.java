package boomi.onb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PerNationalId {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    Document doc = builder.build("src/boomi/onb/OnboardingDataExchange1.XML");
    Element root = doc.getRootElement();
    String sitelCountry = root.getChildText("SitelCountry");

    if (sitelCountry != null && !sitelCountry.isEmpty()) {
      String country = null;
      String cardType = null;
      String nationalId = null;
      switch (sitelCountry) {
      case "Belgium":
        country = "BEL";
        cardType = "EESocialIdNumber";
        nationalId = root.getChildText("GlobalNIDNumBelg1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }

        country = "BEL";
        cardType = "NationalRegisterNumber";
        nationalId = root.getChildText("GlobalNIDNumBelg");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        break;
      case "Brazil":
        country = "BRA";
        cardType = "PIS";
        nationalId = root.getChildText("GlobalNIDNumBraz3");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }

        country = "BRA";
        cardType = "CPF";
        nationalId = root.getChildText("GlobalNIDNumBraz2");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }

        country = "BRA";
        cardType = "RG";
        nationalId = root.getChildText("GlobalNIDNumBraz1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }

        country = "BRA";
        cardType = "CTPS";
        nationalId = root.getChildText("GlobalNIDNumBraz");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        break;
      case "India":
        country = "IND";
        cardType = "UAN";
        nationalId = root.getChildText("GlobalNIDNumIND1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        
        country = "IND";
        cardType = "PAN";
        nationalId = root.getChildText("GlobalNIDNumIND");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        break;
      case "Mexico":
        country = "MEX";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNIDNumMex1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        
        country = "MEX";
        cardType = "RFC13";
        nationalId = root.getChildText("GlobalNIDNumMex");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }

        country = "MEX";
        cardType = "PR";
        nationalId = root.getChildText("GlobalNIDMex");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        break;
      case "Morocco":
        country = "MAR";
        cardType = "INC1";
        nationalId = root.getChildText("GlobalNIDMorc2");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        
        country = "MAR";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNIDMorc1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        
        country = "MAR";
        cardType = "INC";
        nationalId = root.getChildText("GlobalNIDMorc");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        break;
      case "Spain":
        country = "ESP";
        cardType = "NIF";
        nationalId = root.getChildText("GlobalNIDNumSpai2");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        
        country = "ESP";
        cardType = "NIE";
        nationalId = root.getChildText("GlobalNIDNumSpai1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        
        country = "ESP";
        cardType = "DNI";
        nationalId = root.getChildText("GlobalNIDNumSpai");
        if (nationalId != null && !nationalId.isEmpty()) {
          Element perNationalId = new Element("SFOData.PerNationalId");
          perNationalId.addContent(new Element("cardType").setText(cardType));
          perNationalId.addContent(new Element("country").setText(country));
          perNationalId.addContent(new Element("isPrimary").setText("false"));
          perNationalId.addContent(new Element("nationalId").setText(nationalId));
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(perNationalId));
        }
        break;
      default:
        break;
      }

      country = null;
      cardType = null;
      nationalId = null;
      switch (sitelCountry) {
      case "Bulgaria":
        country = "BGR";
        cardType = "PIN";
        nationalId = root.getChildText("GlobalNIDNumBulg");
        break;
      case "Canada":
        country = "CAN";
        cardType = "sin";
        nationalId = root.getChildText("GlobalNIDNumCan");
        break;
      case "Colombia":
        country = "COL";
        cardType = "CitizenshipCard";
        nationalId = root.getChildText("GlobalNIDNumColm");
        break;
      case "Denmark":
        country = "DNK";
        cardType = "CPR";
        nationalId = root.getChildText("GlobalNIDNumDen");
        break;
      case "France":
        country = "FRA";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNIDNumFranc");
        break;
      case "Germany":
        country = "DEU";
        cardType = "essn";
        nationalId = root.getChildText("GlobalNIDNumGermny");
        break;
      case "Ireland":
        country = "IRL";
        cardType = "PPSN";
        nationalId = root.getChildText("GlobalNIDNumIRL");
        break;
      case "Italy":
        country = "ITA";
        cardType = "FC";
        nationalId = root.getChildText("GlobalNIDNumIta");
        break;
      case "New Zealand":
        country = "NZL";
        cardType = "NHI";
        nationalId = root.getChildText("GlobalNIDNZ");
        break;
      case "Netherlands":
        country = "NLD";
        cardType = "BSN";
        nationalId = root.getChildText("GlobalNIDNether");
        break;
      case "Nicaragua":
        country = "NIC";
        cardType = "CitizenshipCard";
        nationalId = root.getChildText("GlobalNIDNicha");
        break;
      case "Norway":
        country = "NOR";
        cardType = "BN";
        nationalId = root.getChildText("GlobalNIDNo");
        break;
      case "Philippines":
        country = "PHL";
        cardType = "SSS";
        nationalId = root.getChildText("GlobalNIDPH");
        break;
      case "Panama":
        country = "PAN";
        cardType = "CDC";
        nationalId = root.getChildText("GlobalNIDPana");
        break;
      case "Poland":
        country = "POL";
        cardType = "PESEL";
        nationalId = root.getChildText("GlobalNIDPol");
        break;
      case "Portugal":
        country = "PRT";
        cardType = "SSN";
        nationalId = root.getChildText("GlobalNIDPortug");
        break;
      case "Serbia":
        country = "SRB";
        cardType = "JMBG ID";
        nationalId = root.getChildText("GlobalNIDSerb");
        break;
      case "United Kingdom":
        country = "GBR";
        cardType = "NINO";
        nationalId = root.getChildText("GlobalNIDNumUK");
        break;
      case "United States":
        country = "USA";
        cardType = "ssn";
        nationalId = root.getChildText("SSN");
        break;
      default:
        break;
      }

      if (nationalId != null && !nationalId.isEmpty()) {
        Element perNationalId = new Element("SFOData.PerNationalId");
        perNationalId.addContent(new Element("cardType").setText(cardType));
        perNationalId.addContent(new Element("country").setText(country));
        perNationalId.addContent(new Element("isPrimary").setText("false"));
        perNationalId.addContent(new Element("nationalId").setText(nationalId));
        InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(perNationalId).getBytes("UTF-8"));
        System.out.println(xmlOutputter.outputString(perNationalId));
      }
    }
  }
}
