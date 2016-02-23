package boomi.hewitt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RemoveEMC {
  public static void main(String[] args) throws Exception {
    List<String> list = new ArrayList<String>();
    String[] previousArr = null;
    int categoryIndex = 0;
    int idIndex = 2;

    for (int i = 1; i <= 11; i++) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/boomi/hewitt/Employee" + i)));
      String line = null;
      while ((line = reader.readLine()) != null) {
        String[] currentArr = line.split("[|]", -1);
        if (currentArr != null && currentArr.length > 0) {
          if (previousArr != null && previousArr[idIndex].equals(currentArr[idIndex]) && previousArr[categoryIndex].equals("EMC") && currentArr[categoryIndex].equals("EMC")) {
            boolean isSameFieldChange = true;
            for (int j = 0; j < currentArr.length; j++) {
              boolean isCurrentEmpty = !currentArr[j].isEmpty();
              boolean isPreviousEmpty = !previousArr[j].isEmpty();
              if ((isCurrentEmpty && !isPreviousEmpty) || (!isCurrentEmpty && isPreviousEmpty)) {
                isSameFieldChange = false;
              }
            }

            if (isSameFieldChange) {
              list.set(list.size() - 1, line);
            } else {
              list.add(line);
            }
          } else {
            list.add(line);
          }
          previousArr = currentArr;
        }
      }

      if (i == 11 && list != null && !list.isEmpty()) {
        for (String str : list) {
          InputStream os = new ByteArrayInputStream(str.getBytes("UTF-8"));
          System.out.println(str);
        }
      }
    }
  }
}
