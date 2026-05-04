// MessageParser.java

// handles message encoding and decoded between threads
// keeping this centralised makes it easier to tweak long-term

/**
 * message format:
 * INIT		id|x|y|dx|dy|radius|r|g|b
 * TICK		iteration
 * STATE	id|x|y|dx|dy
 * TRANSFER	id|x|y|dx|dy|radius|r|g|b|targetRegion
 * BOUNDARY	id|x|y|dx|dy|radius
 * UPDATE	id|x|y|dx|dy
 * ASSIGN	id|x|y|dx|dy|radius|r|g|b
 * DONE		workerIndex
 * SHUTDOWN	
 */
public class MessageParser {

    // ── encoding
    public static String encodeInit(int id, double x, double y, double dx, double dy,
                                    int radius, int r, int g, int b) {

        return "INIT|" + id + "|" + x + "|" + y + "|" + dx + "|" + dy + "|" + radius
             + "|" + r + "|" + g + "|" + b;
    }

    public static String encodeTick(int iteration) {
        return "TICK|" + iteration;
    }

    public static String encodeState(int id, double x, double y, double dx, double dy) {
        return "STATE|" + id + "|" + x + "|" + y + "|" + dx + "|" + dy;
    }

    public static String encodeTransfer(int id, double x, double y, double dx, double dy,
                                        int radius, int r, int g, int b, int targetRegion) {

        return "TRANSFER|" + id + "|" + x + "|" + y + "|" + dx + "|" + dy + "|" + radius
             + "|" + r + "|" + g + "|" + b + "|" + targetRegion;
    }

    public static String encodeBoundary(int id, double x, double y,
                                        double dx, double dy, int radius) {

        return "BOUNDARY|" + id + "|" + x + "|" + y + "|" + dx + "|" + dy + "|" + radius;
    }

    public static String encodeUpdate(int id, double x, double y, double dx, double dy) {
        return "UPDATE|" + id + "|" + x + "|" + y + "|" + dx + "|" + dy;
    }

    public static String encodeAssign(int id, double x, double y, double dx, double dy,
                                      int radius, int r, int g, int b) {

        return "ASSIGN|" + id + "|" + x + "|" + y + "|" + dx + "|" + dy + "|" + radius
             + "|" + r + "|" + g + "|" + b;
    }

    public static String encodeDone(int workerIndex) {
        return "DONE|" + workerIndex;
    }

    public static String encodeShutdown() {
        return "SHUTDOWN";
    }

    // ── decoding 
    // quick check for message type (everything before first '|') 
    public static String getType(String message) {
        int split = message.indexOf('|');
        return (split == -1) ? message : message.substring(0, split);
    }

    // parses messages with structure: id, x, y, dx, dy
    // used by STATE / UPDATE
    public static double[] parseIdXYDxDy(String message) {
        String[] parts = message.split("\\|");

        return new double[] {
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2]),
            Double.parseDouble(parts[3]),
            Double.parseDouble(parts[4]),
            Double.parseDouble(parts[5])
        };
    }

     // parses INIT / ASSIGN
     // [id, x, y, dx, dy, radius, r, g, b]
    public static double[] parseInit(String message) {
        String[] parts = message.split("\\|");

        return new double[] {
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2]),
            Double.parseDouble(parts[3]),
            Double.parseDouble(parts[4]),
            Double.parseDouble(parts[5]),
            Double.parseDouble(parts[6]),
            Double.parseDouble(parts[7]),
            Double.parseDouble(parts[8]),
            Double.parseDouble(parts[9])
        };
    }

    // parses TRANSFER
    // includes target region at the end
    public static double[] parseTransfer(String message) {
        String[] parts = message.split("\\|");

        return new double[] {
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2]),
            Double.parseDouble(parts[3]),
            Double.parseDouble(parts[4]),
            Double.parseDouble(parts[5]),
            Double.parseDouble(parts[6]),
            Double.parseDouble(parts[7]),
            Double.parseDouble(parts[8]),
            Double.parseDouble(parts[9]),
            Double.parseDouble(parts[10])
        };
    }
    
    // parses BOUNDARY messages
    // [id, x, y, dx, dy, radius]
    public static double[] parseBoundary(String message) {
        String[] parts = message.split("\\|");

        return new double[] {
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2]),
            Double.parseDouble(parts[3]),
            Double.parseDouble(parts[4]),
            Double.parseDouble(parts[5]),
            Double.parseDouble(parts[6])
        };
    }

    public static int parseDone(String message) {
        return Integer.parseInt(message.split("\\|")[1]);
    }

    public static int parseTick(String message) {
        return Integer.parseInt(message.split("\\|")[1]);
    }
}
