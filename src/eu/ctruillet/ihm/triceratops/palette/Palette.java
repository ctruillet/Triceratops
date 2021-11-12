package eu.ctruillet.ihm.triceratops.palette;

import eu.ctruillet.ihm.triceratops.command.Command;
import eu.ctruillet.ihm.triceratops.command.CommandMerger;
import eu.ctruillet.ihm.triceratops.ivy.TrIvyceratops;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Palette extends PApplet {
    //Attributs
    public static PApplet processing;
    public static FSM fsm = FSM.INIT;
    public static ArrayList<Command> commands = new ArrayList<>();
    public CommandMerger commandMerger = new CommandMerger();

    protected TrIvyceratops bus = new TrIvyceratops(commandMerger);


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

        commands.add(new Command(Action.CREER, Shape.RECTANGLE, Couleur.ROUGE, new PVector(100, 100)));
        commands.add(new Command(Action.CREER, Shape.TRIANGLE, Couleur.BLEU, new PVector(300, 100)));
        commands.add(new Command(Action.CREER, Shape.CIRCLE, Couleur.VERT, new PVector(250, 150)));
    }

    public void draw(){
        try {
            background(200);
            fill(0);
            text(fsm.name(), 600, 460);

            switch (fsm) {
                case INIT:
                    fsm = FSM.WAIT;
                    break;

                case SUPPRIMER:
                case ANNULER:
                    bus.sendFeedback(new Command(fsm), fsm);
                    fsm = FSM.WAIT;
                    break;
                case MODIFIER:
                case DEPLACER:
                case CREER:
                    bus.sendFeedback(commands.isEmpty() ? null : commands.get(commands.size()-1), fsm);
                    fsm = FSM.WAIT;
                    break;

                case WAIT:
                    //Afficher le feedback de la derniere commande
                    commandMerger.draw();
//                Command.drawFeedBack(commands.isEmpty() ? null : commands.get(commands.size()-1));


                    //new Command(Action.SUPPRIMER,Shape.RECTANGLE).drawFeedback();

                    for (int i = 0; i < commands.size(); i++) {
                        commands.get(i).drawCommand();
                    }

                    break;

                case QUITTER:
                    bus.sendFeedback(new Command(fsm), fsm);
                    Palette.processing.exit();
                    break;

                default:
                    fsm = FSM.INIT;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            Palette.processing.exit();
        }
    }

    public void mousePressed(){
        this.commandMerger.addCommandMouseClick(mouseX, mouseY);


        if(getShapeAt(new PVector(mouseX, mouseY)) != null) {
            System.out.println("Mouse pressed on shape " + getShapeAt(new PVector(mouseX, mouseY)).getName());
        }else{
            println("No shape at : " + mouseX + "; " + mouseY);
        }
    }

    public static void addCommand(Command c){
        System.out.println(c);
        switch (c.getAction())  {
            case CREER:
                commands.add(c);
                fsm = FSM.CREER;
                break;

            case ANNULER:
                for (int i = commands.size()-1; i >= 0 && !commands.isEmpty(); i--) {
                    System.out.println("ANNULER ---> " + commands.get(i));
                    if(commands.get(i).getAction() == Action.CREER){
                        commands.remove(i);
                        break;
                    }else if(commands.get(i).getAction() == Action.DEPLACER){
                        System.out.println("\t ---->" + commands.get(i).commandCreer);
                        commands.get(i).commandCreer.removeCommandDeplacer(commands.get(i));
                        commands.remove(i);
                        break;
                    }
                }

                fsm = FSM.ANNULER;
                break;

            case DEPLACER:
                c.commandCreer.addCommandDeplacer(c);
                commands.add(c);
                fsm = FSM.DEPLACER;
                break;

            case MODIFIER:
                // ToDo

                break;

            case ERREUR:
                break;
            case QUITTER:
                // Ouvrir une page web vers https://www.youtube.com/watch?v=5KBo4dGAF8c
                fsm = FSM.QUITTER;
                break;

            default:
                break;
        }
    }

    public static Shape getShapeAt(PVector localisation) {
//        System.out.println("---> LOCALISATION" + localisation);
        return (getCommandAt(localisation)==null) ? null:getCommandAt(localisation).getShape();
    }

    public static Couleur getCouleurAt(PVector localisation) {
        return (getCommandAt(localisation)==null) ? null:getCommandAt(localisation).getCouleur();
    }

    public static Command getCommandAt(PVector localisation) {
        if(localisation == null) return null;

        for (int i = commands.size()-1; i >= 0; i--) {
            Command c = commands.get(i);
            if(c.getAction() == Action.ANNULER ) continue;
            if (c.isMouseOnShape(localisation.x, localisation.y)) {
                return c;
            }
        }
        return null;
    }
}
