package lib.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that deals with parsing input via an InputStream to the entire
 * list of json nodes. This is for use within a test harness
 */
public class InputParser {

    private static final String regex = "\\]\\s*\\[";
    private static final String replacement = "],[";

    public static List<JsonNode> parse(InputStream inputStream) throws IOException {
        List<JsonNode> results = new ArrayList<>();
        Scanner sc = new Scanner(inputStream);
        Pattern p = Pattern.compile(regex);

        String line = "";
        while (sc.hasNextLine()) {
            // Could have 1 or multiple commands
            line += sc.nextLine();

            if (isBalanced(line)) {
                Matcher m = p.matcher(line);

                String result = m.replaceAll(replacement);
                String commandArray = "[" + result + "]";

                List<JsonNode> commands = getJson(commandArray);
                results.addAll(commands);
                line = "";
            }
        }

        return results;
    }

    private static boolean isBalanced(String input) {
        int openBraces = StringUtils.countMatches(input, "[");
        int closedBraces = StringUtils.countMatches(input, "]");
        return openBraces == closedBraces;
    }

    private static List<JsonNode> getJson(String maybeJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode commandList = objectMapper.readTree(maybeJson);

        List<JsonNode> results = new ArrayList<>();
        for (int i = 0; i < commandList.size(); i++) {
            results.add(commandList.get(i));
        }

        return  results;
    }
}
