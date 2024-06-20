package i.need.it.IneedIt.utils;

import java.util.List;
import java.util.Map;

public class MapUtil {


    public static <T> List<T> findValueByCaseInsensitiveKey(Map<String, List<T>> map, String searchKey) {
        if (searchKey == null) return null;  // Handle null case if necessary

        // Iterate over the key set of the map
        for (String key : map.keySet()) {
            if (key.equalsIgnoreCase(searchKey)) {
                return map.get(key);  // Return the value if a match is found
            }
        }
        return null;  // Return null if no match is found
    }
}
