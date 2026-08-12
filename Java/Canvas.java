public class Canvas {
    private final int width;
    private final int height;
    private final char[][] pixels;

    public Canvas(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Canvas width and height must be positive integers.");
        }
        this.width = width;
        this.height = height;
        this.pixels = new char[height][width];
        clear();
    }

    private void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = ' ';
            }
        }
    }

    private void validatePoint(int x, int y) {
        if (x < 1 || x > width || y < 1 || y > height) {
            throw new IllegalArgumentException(String.format("Point (%d,%d) is outside the canvas.", x, y));
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        validatePoint(x1, y1);
        validatePoint(x2, y2);
        if (x1 != x2 && y1 != y2) {
            throw new IllegalArgumentException("Only horizontal or vertical lines are supported.");
        }
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                pixels[y - 1][x - 1] = 'x';
            }
        }
    }

    public void drawRectangle(int x1, int y1, int x2, int y2) {
        validatePoint(x1, y1);
        validatePoint(x2, y2);
        int left = Math.min(x1, x2);
        int right = Math.max(x1, x2);
        int top = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);
        if (left == right || top == bottom) {
            throw new IllegalArgumentException("Rectangle must have non-zero width and height.");
        }

        drawLine(left, top, right, top);
        drawLine(left, bottom, right, bottom);
        drawLine(left, top, left, bottom);
        drawLine(right, top, right, bottom);
    }

    public void bucketFill(int x, int y, char color) {
        validatePoint(x, y);
        char target = pixels[y - 1][x - 1];
        if (target == color) {
            return;
        }

        fill(x - 1, y - 1, target, color);
    }

    private void fill(int x, int y, char target, char color) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        if (pixels[y][x] != target) {
            return;
        }
        pixels[y][x] = color;
        fill(x - 1, y, target, color);
        fill(x + 1, y, target, color);
        fill(x, y - 1, target, color);
        fill(x, y + 1, target, color);
    }

    public String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("-").append("-".repeat(width)).append("-").append(System.lineSeparator());
        for (int y = 0; y < height; y++) {
            builder.append('|');
            for (int x = 0; x < width; x++) {
                builder.append(pixels[y][x]);
            }
            builder.append('|').append(System.lineSeparator());
        }
        builder.append("-").append("-".repeat(width)).append("-");
        return builder.toString();
    }
}
