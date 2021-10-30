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
}
