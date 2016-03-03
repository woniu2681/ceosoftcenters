package boomi.onb;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PaymentInformationDetailV3 {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    TreeMap<String, List<String>> map = new TreeMap<String, List<String>>();
    String USA = "United States";
    String CAN = "Canada";

    for (int i = 0; i < 2; i++) {
      if (i == 0) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/boomi/onb/DirectDepositList.csv")));
        String line = null;
        while ((line = reader.readLine()) != null) {
          String[] strArr = line.split(",");
          if (strArr != null && strArr.length > 0) {
            List<String> strList = Arrays.asList(strArr);
            if (strList.get(0) != null) {
              map.put(strList.get(0), strList);
            }
          }
        }
      } else if (i == 1) {
        Document doc = builder.build("src/boomi/onb/OnboardingDataExchange1.xml");
        Element root = doc.getRootElement();
        String sitelCountry = root.getChildText("SitelCountry");

        if (map != null && !map.isEmpty()) {
          List<String> sourceList = map.get("Source");
          List<String> targetList = map.get("Target");
          List<String> directDepositList = map.get(sitelCountry);
          if (directDepositList != null && !directDepositList.isEmpty()) {
            Element account1 = new Element("SFOData.PaymentInformationDetailV3");
            Element account2 = new Element("SFOData.PaymentInformationDetailV3");
            Element account3 = new Element("SFOData.PaymentInformationDetailV3");

            for (int j = 1; j < directDepositList.size(); j++) {
              if (directDepositList.get(j).equals("1")) {
                String source = sourceList.get(j);
                String target = targetList.get(j);
                if (target != null && target.equals("cust_depositType")) {
                  String deposit1 = root.getChildText("GlobalDeposit_1");
                  if (sitelCountry != null && sitelCountry.equals(USA)) {
                    deposit1 = root.getChildText("USDDDeposit");
                  } else if (sitelCountry != null && sitelCountry.equals(CAN)) {
                    deposit1 = root.getChildText("CADDDeposit");
                  }
                  switch (deposit1) {
                  case "Partial Amount":
                    account1.addContent(new Element("cust_depositType").setText("percentage"));
                    account1.addContent(new Element("payType").setText("PAYROLL"));
                    break;
                  case "Full Amount":
                    account1.addContent(new Element("cust_depositType").setText("falt"));
                    account1.addContent(new Element("payType").setText("MAIN"));
                    break;
                  case "Balance A":
                    account1.addContent(new Element("cust_depositType").setText("balance"));
                    account1.addContent(new Element("payType").setText("MAIN"));
                    break;
                  default:
                    break;
                  }
                  String deposit2 = root.getChildText("GlobalDeposit_2");
                  if (sitelCountry != null && sitelCountry.equals(USA)) {
                    deposit2 = root.getChildText("USDDDeposit1");
                  } else if (sitelCountry != null && sitelCountry.equals(CAN)) {
                    deposit2 = root.getChildText("CADDDeposit1");
                  }
                  switch (deposit2) {
                  case "Partial":
                    account2.addContent(new Element("cust_depositType").setText("percentage"));
                    account2.addContent(new Element("payType").setText("PAYROLL"));
                    break;
                  case "Full":
                    account2.addContent(new Element("cust_depositType").setText("falt"));
                    account2.addContent(new Element("payType").setText("MAIN"));
                    break;
                  case "Balance":
                    account2.addContent(new Element("cust_depositType").setText("balance"));
                    account2.addContent(new Element("payType").setText("MAIN"));
                    break;
                  default:
                    break;
                  }
                  String deposit3 = root.getChildText("GlobalDeposit_3");
                  switch (deposit3) {
                  case "Partial":
                    account3.addContent(new Element("cust_depositType").setText("percentage"));
                    account3.addContent(new Element("payType").setText("PAYROLL"));
                    break;
                  case "Full":
                    account3.addContent(new Element("cust_depositType").setText("falt"));
                    account3.addContent(new Element("payType").setText("MAIN"));
                    break;
                  case "Balance":
                    account3.addContent(new Element("cust_depositType").setText("balance"));
                    account3.addContent(new Element("payType").setText("MAIN"));
                    break;
                  default:
                    break;
                  }
                } else {
                  if (sitelCountry != null && sitelCountry.equals(USA)) {
                    switch (target) {
                    case "routingNumber":
                      source = "DDBank1RoutNum";
                      break;
                    case "cust_bankName":
                      source = "DDAccount1TypeText";
                      break;
                    case "accountType":
                      source = "DDAccount1Type";
                      break;
                    case "amount":
                      source = "Sitel_DDAmount";
                      break;
                    case "accountNumber":
                      source = "Sitel_DDAcctNum";
                      break;
                    default:
                      break;
                    }
                  } else if (sitelCountry != null && sitelCountry.equals(CAN)) {
                    switch (target) {
                    case "cust_transitNumer":
                      source = "CADDTransit";
                      break;
                    case "cust_bankInstitutionCAN":
                      source = "CADDInstitution";
                      break;
                    case "cust_bankName":
                      source = "CADDBankName";
                      break;
                    case "accountNumber":
                      source = "CADDAccount";
                      break;
                    case "bankCountry":
                      source = "BankCountry";
                      break;
                    case "amount":
                      source = "CANAmount";
                      break;
                    default:
                      break;
                    }
                    account1.addContent(new Element(target).setText(root.getChildText(source)));
                    account2.addContent(new Element(target).setText(root.getChildText(source + "_1")));
                    account3.addContent(new Element(target).setText(root.getChildText(source + "_2")));
                  } else {
                    account1.addContent(new Element(target).setText(root.getChildText(source + "_1")));
                    account2.addContent(new Element(target).setText(root.getChildText(source + "_2")));
                    account3.addContent(new Element(target).setText(root.getChildText(source + "_3")));
                  }
                }
              }
            }

            if (account1 != null && account1.getContentSize() > 0) {
              account1.addContent(new Element("paymentMethod").setText("05"));
              InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(account1).getBytes("UTF-8"));
              System.out.println(xmlOutputter.outputString(account1));
            }

            if (account2 != null && account2.getContentSize() > 0) {
              account2.addContent(new Element("paymentMethod").setText("05"));
              InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(account2).getBytes("UTF-8"));
              System.out.println(xmlOutputter.outputString(account2));
            }

            if (account3 != null && account3.getContentSize() > 0) {
              account3.addContent(new Element("paymentMethod").setText("05"));
              InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(account3).getBytes("UTF-8"));
              System.out.println(xmlOutputter.outputString(account3));
            }
          }
        }
      }
    }
  }
}
