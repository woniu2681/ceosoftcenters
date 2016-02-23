package boomi.gdw;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class EventReason {
  public static void main(String[] args) throws Exception {

    TreeMap<String, String> previousTreeMap = new TreeMap<String, String>();
    String PIPE = "|";
    int KEY_INDEX = 0;

    for (int i = 0; i < 2; i++) {
      InputStream is = new FileInputStream("src/boomi/gdw/EventReason" + i + ".txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      String line = null;
      while ((line = reader.readLine()) != null) {
        String[] strArr = line.split("\\" + PIPE, -1);
        if (strArr != null && strArr.length > KEY_INDEX + 1) {
          String key = strArr[KEY_INDEX];

          if (i == 0) {
            previousTreeMap.put(key, line);
          } else if (i == 1) {
            String previousStr = previousTreeMap.get(key);
            String currentStr = line;

            if (previousStr != null && !previousStr.isEmpty()) {
              if (!currentStr.equals(previousStr)) {
                InputStream os = new ByteArrayInputStream(currentStr.getBytes("UTF-8"));
                System.out.println(currentStr);
              }
            } else {
              InputStream os = new ByteArrayInputStream(currentStr.getBytes("UTF-8"));
              System.out.println(currentStr);
            }
          }
        }
      }
    }
  }
}
