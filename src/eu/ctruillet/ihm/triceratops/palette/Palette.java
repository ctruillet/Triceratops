package eu.ctruillet.ihm.triceratops.palette;

import processing.core.PApplet;

public class Palette  extends PApplet {
    //Attributs
    public static PApplet processing;

    //Méthodes
    public static void main(String[] args) {
        PApplet.main("eu.ctruillet.ihm.triceratops.palette.Palette",args);
    }

    public void settings(){
        size(640, 480);
    }

    public void setup(){
        processing = this;
        surface.setIcon(loadImage("data/logo.png"));
        surface.setTitle("Une petite Creme (Patrick) Bruel");
    }

    public void draw(){
        new Command(Action.CREER,Shape.RECTANGLE).draw();
    }
}
