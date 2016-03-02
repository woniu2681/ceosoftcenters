package boomi.onb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class GenerateNationalIdFilter {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    Document doc = builder.build("src/boomi/onb/OnboardingDataExchange1.XML");
    Element root = doc.getRootElement();
    String sitelCountry = root.getChildText("SitelCountry");

    if (sitelCountry != null && !sitelCountry.isEmpty()) {
      List<Map<String, String>> list = new ArrayList<Map<String, String>>();
      String country = null;
      String cardType = null;
      String nationalId = null;
      switch (sitelCountry) {
      case "Belgium":
        country = "BEL";
        cardType = "EESocialIdNumber";
        nationalId = root.getChildText("GlobalNIDNumBelg1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "BEL";
        cardType = "NationalRegisterNumber";
        nationalId = root.getChildText("GlobalNIDNumBelg");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Brazil":
        country = "BRA";
        cardType = "PIS";
        nationalId = root.getChildText("GlobalNIDNumBraz3");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "BRA";
        cardType = "CPF";
        nationalId = root.getChildText("GlobalNIDNumBraz2");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "BRA";
        cardType = "RG";
        nationalId = root.getChildText("GlobalNIDNumBraz1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "BRA";
        cardType = "CTPS";
        nationalId = root.getChildText("GlobalNIDNumBraz");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "India":
        country = "IND";
        cardType = "UAN";
        nationalId = root.getChildText("GlobalNIDNumIND1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "IND";
        cardType = "PAN";
        nationalId = root.getChildText("GlobalNIDNumIND");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Mexico":
        country = "MEX";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNIDNumMex1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "MEX";
        cardType = "RFC13";
        nationalId = root.getChildText("GlobalNIDNumMex");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "MEX";
        cardType = "PR";
        nationalId = root.getChildText("GlobalNIDMex");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Morocco":
        country = "MAR";
        cardType = "INC1";
        nationalId = root.getChildText("GlobalNIDMorc2");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "MAR";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNIDMorc1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "MAR";
        cardType = "INC";
        nationalId = root.getChildText("GlobalNIDMorc");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Spain":
        country = "ESP";
        cardType = "NIF";
        nationalId = root.getChildText("GlobalNIDNumSpai2");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "ESP";
        cardType = "NIE";
        nationalId = root.getChildText("GlobalNIDNumSpai1");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }

        country = "ESP";
        cardType = "DNI";
        nationalId = root.getChildText("GlobalNIDNumSpai");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Bulgaria":
        country = "BGR";
        cardType = "PIN";
        nationalId = root.getChildText("GlobalNIDNumBulg");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Canada":
        country = "CAN";
        cardType = "sin";
        nationalId = root.getChildText("GlobalNIDNumCan");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Colombia":
        country = "COL";
        cardType = "CitizenshipCard";
        nationalId = root.getChildText("GlobalNIDNumColm");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Denmark":
        country = "DNK";
        cardType = "CPR";
        nationalId = root.getChildText("GlobalNIDNumDen");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "France":
        country = "FRA";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNIDNumFranc");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Germany":
        country = "DEU";
        cardType = "essn";
        nationalId = root.getChildText("GlobalNIDNumGermny");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Ireland":
        country = "IRL";
        cardType = "PPSN";
        nationalId = root.getChildText("GlobalNIDNumIRL");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Italy":
        country = "ITA";
        cardType = "FC";
        nationalId = root.getChildText("GlobalNIDNumIta");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "New Zealand":
        country = "NZL";
        cardType = "NHI";
        nationalId = root.getChildText("GlobalNIDNZ");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Netherlands":
        country = "NLD";
        cardType = "BSN";
        nationalId = root.getChildText("GlobalNIDNether");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Nicaragua":
        country = "NIC";
        cardType = "CitizenshipCard";
        nationalId = root.getChildText("GlobalNIDNicha");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Norway":
        country = "NOR";
        cardType = "BN";
        nationalId = root.getChildText("GlobalNIDNo");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Philippines":
        country = "PHL";
        cardType = "SSS";
        nationalId = root.getChildText("GlobalNIDPH");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Panama":
        country = "PAN";
        cardType = "CDC";
        nationalId = root.getChildText("GlobalNIDPana");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Poland":
        country = "POL";
        cardType = "PESEL";
        nationalId = root.getChildText("GlobalNIDPol");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Portugal":
        country = "PRT";
        cardType = "SSN";
        nationalId = root.getChildText("GlobalNIDPortug");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "Serbia":
        country = "SRB";
        cardType = "JMBG ID";
        nationalId = root.getChildText("GlobalNIDSerb");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "United Kingdom":
        country = "GBR";
        cardType = "NINO";
        nationalId = root.getChildText("GlobalNIDNumUK");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      case "United States":
        country = "USA";
        cardType = "ssn";
        nationalId = root.getChildText("GlobalNID");
        if (nationalId != null && !nationalId.isEmpty()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("country", country);
          map.put("cardType", cardType);
          map.put("nationalId", nationalId);
          list.add(map);
        }
        break;
      default:
        break;
      }

      String filter = "personIdExternal eq null";
      if (list != null && !list.isEmpty()) {
        filter = "nationalIdNav/country eq '" + country + "' and (";
        for (int j = 0; j < list.size(); j++) {
          Map<String, String> m = list.get(j);
          if (j == 0) {
            filter = filter + "(nationalIdNav/cardType eq '" + m.get("cardType") + "' and nationalIdNav/nationalId eq '" + m.get("nationalId") + "')";
          } else {
            filter = filter + "or (nationalIdNav/cardType eq '" + m.get("cardType") + "' and nationalIdNav/nationalId eq '" + m.get("nationalId") + "')";
          }
        }
        filter = filter + ")";
      }
      System.out.println(filter);
    }
    InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(doc).getBytes("UTF-8"));
    System.out.println(xmlOutputter.outputString(doc));
  }
}
