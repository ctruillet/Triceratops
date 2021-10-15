package eu.ctruillet.ihm.triceratops.palette;

import eu.ctruillet.ihm.triceratops.ivy.IvyListener;
import processing.core.PApplet;

import java.util.ArrayList;

public class Palette extends PApplet {
    //Attributs
    public static PApplet processing;
    protected IvyListener bus = new IvyListener();
    protected ArrayList<Command> commands = new ArrayList<>();
    protected FSM fsm = FSM.INIT;

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
        surface.setTitle("Triceratops - L'application qui galope !");

    }

    public void draw(){
        switch (this.fsm){
            case INIT:
                fsm = FSM.WAIT;
                break;

            case WAIT:
                //System.out.println(this.bus.getNextCommand());
                Command.drawFeedBack(this.bus.getNextCommand());

                //new Command(Action.SUPPRIMER,Shape.RECTANGLE).drawFeedback();

                //Afficher le feedback de la derniere commande

                for (Command command : commands) {
                    //command.drawCommand();
                }

                break;

            default:
                fsm = FSM.INIT;
                break;
        }
    }
}
