package eu.ctruillet.ihm.triceratops.palette;

import eu.ctruillet.ihm.triceratops.command.Command;
import eu.ctruillet.ihm.triceratops.command.CommandMerger;
import eu.ctruillet.ihm.triceratops.ivy.IvyListener;
import processing.core.PApplet;

import java.util.ArrayList;

public class Palette extends PApplet {
    //Attributs
    public static PApplet processing;
    public static FSM fsm = FSM.INIT;
    public static ArrayList<Command> commands = new ArrayList<>();
    public CommandMerger commandMerger = new CommandMerger();

    protected IvyListener bus = new IvyListener(commandMerger);


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
        background(200);
        fill(0);
        text(fsm.name(), 600, 460);

        switch (fsm){
            case INIT:
                fsm = FSM.WAIT;
                break;

            case WAIT:
                //Afficher le feedback de la derniere commande
                Command.drawFeedBack(commands.isEmpty() ? null : commands.get(commands.size()-1));


                //new Command(Action.SUPPRIMER,Shape.RECTANGLE).drawFeedback();



                for (Command command : commands) {
                    command.drawCommand();
                    // ToDo Quid de cancel une action MODIFIER ?
                }

                break;

            case CREER:
                fsm = FSM.WAIT;
                break;

            case SUPPRIMER:
                fsm = FSM.WAIT;
                break;

            default:
                fsm = FSM.INIT;
                break;
        }
    }

    public void mousePressed(){
        this.commandMerger.addCommandMouseClick(mouseX, mouseY);
        //System.out.println("Mouse pressed : " + mouseX + "; " + mouseY);
    }

    public static void addCommand(Command c){
        switch (c.getAction())  {
            case CREER:
                commands.add(c);
                fsm = FSM.CREER;
                break;

            case CANCEL:
                for (int i = commands.size()-1; (i >= 0 && !commands.isEmpty()); i--) {
                    if(commands.get(i).getAction() == Action.CREER){
                        commands.remove(i);
                        break;
                    }
                }

                commands.add(c);
                fsm = FSM.SUPPRIMER;
                break;

            default:
                break;
        }
    }
}
