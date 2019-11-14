public class HTMLParser {
 
    public HTMLParser() {}

    public String parse(String string) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        String[] tokens = string.split("\n");
        for (String token : tokens) {
            builder.append(token + "<br>");
        }
        builder.append("</html>");
        return builder + "";
    }
}
