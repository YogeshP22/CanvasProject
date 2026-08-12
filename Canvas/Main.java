import java.util.Scanner;

public class Main {
    private static Canvas canvas = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printIntro();
        printAvailableCommands();

        while (true) {
            System.out.print("enter command: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equalsIgnoreCase("Q")) {
                break;
            }
            try {
                handleCommand(line);
                printAvailableCommands();
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye.");
        scanner.close();
    }

    private static void printIntro() {
        System.out.println("Canvas drawing program. Enter commands or Q to quit.");
    }

    private static void printAvailableCommands() {
        System.out.println("Available commands:");
        System.out.println("  C w h            - Create canvas of width w and height h");
        System.out.println("  L x1 y1 x2 y2    - Draw horizontal or vertical line");
        System.out.println("  R x1 y1 x2 y2    - Draw rectangle");
        System.out.println("  B x y c          - Bucket fill with color c");
        System.out.println("  Q                - Quit");
    }

    private static void handleCommand(String line) {
        String[] parts = line.split("\\s+");
        String command = parts[0].toUpperCase();

        switch (command) {
            case "C":
                requireLength(parts, 3, "Usage: C width height");
                createCanvas(parts);
                break;
            case "L":
                requireCanvas();
                requireLength(parts, 5, "Usage: L x1 y1 x2 y2");
                drawLine(parts);
                break;
            case "R":
                requireCanvas();
                requireLength(parts, 5, "Usage: R x1 y1 x2 y2");
                drawRectangle(parts);
                break;
            case "B":
                requireCanvas();
                requireLength(parts, 4, "Usage: B x y c");
                bucketFill(parts);
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private static void requireCanvas() {
        if (canvas == null) {
            throw new IllegalArgumentException("Canvas not created. Use C width height first.");
        }
    }

    private static void requireLength(String[] parts, int expected, String usage) {
        if (parts.length != expected) {
            throw new IllegalArgumentException(usage);
        }
    }

    private static void createCanvas(String[] parts) {
        int width = parseInt(parts[1], "width");
        int height = parseInt(parts[2], "height");
        canvas = new Canvas(width, height);
        System.out.println(canvas.render());
    }

    private static void drawLine(String[] parts) {
        int x1 = parseInt(parts[1], "x1");
        int y1 = parseInt(parts[2], "y1");
        int x2 = parseInt(parts[3], "x2");
        int y2 = parseInt(parts[4], "y2");
        canvas.drawLine(x1, y1, x2, y2);
        System.out.println(canvas.render());
    }

    private static void drawRectangle(String[] parts) {
        int x1 = parseInt(parts[1], "x1");
        int y1 = parseInt(parts[2], "y1");
        int x2 = parseInt(parts[3], "x2");
        int y2 = parseInt(parts[4], "y2");
        canvas.drawRectangle(x1, y1, x2, y2);
        System.out.println(canvas.render());
    }

    private static void bucketFill(String[] parts) {
        int x = parseInt(parts[1], "x");
        int y = parseInt(parts[2], "y");
        String color = parts[3];
        if (color.length() != 1) {
            throw new IllegalArgumentException("Fill color must be a single character.");
        }
        canvas.bucketFill(x, y, color.charAt(0));
        System.out.println(canvas.render());
    }

    private static int parseInt(String value, String name) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be an integer.");
        }
    }
}
