package eu.ctruillet.ihm.triceratops.palette;

public enum Shape {
    RECTANGLE,
    CIRCLE,
    TRIANGLE;

    public static Shape getShape(String shape) {
        switch (shape) {
            case "RECTANGLE":
                return RECTANGLE;
            case "TRIANGLE":
                return TRIANGLE;
            case "CIRCLE":
                return CIRCLE;
            default:
                return null;
        }
    }

    public String getName() {
        switch (this) {
            case RECTANGLE:
                return "Rectangle";
            case TRIANGLE:
                return "Triangle";
            case CIRCLE:
                return "Circle";
            default:
                return null;
        }
    }
}
