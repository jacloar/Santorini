package lib.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static List<JsonNode> getJsonArray(List<String> strings) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<JsonNode> parsedNodes = new ArrayList<JsonNode>();
        String possibleJson = "";
        for (String s : strings) {
            try {
                String toParse = possibleJson + s;
                JsonNode node = objectMapper.readTree(toParse);
                parsedNodes.add(node);
                possibleJson = "";
            } catch (IOException e) {
                possibleJson += s;
            }
        }

        return parsedNodes;
    }
}
