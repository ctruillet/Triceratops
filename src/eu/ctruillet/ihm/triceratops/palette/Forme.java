package eu.ctruillet.ihm.triceratops.palette;

import processing.core.PVector;

public class Forme {
    //Attributs
    protected Couleur color;
    protected Shape shape;
    protected PVector position;

    //Constructeur


    //Méthodes
    public Color getColor() {
        return color;
    }

    public void setColor(Couleur color) {
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }
}
