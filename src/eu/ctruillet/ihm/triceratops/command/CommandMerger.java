package eu.ctruillet.ihm.triceratops.command;

import eu.ctruillet.ihm.triceratops.palette.Action;
import eu.ctruillet.ihm.triceratops.palette.Couleur;
import eu.ctruillet.ihm.triceratops.palette.Palette;
import eu.ctruillet.ihm.triceratops.palette.Shape;
import processing.core.PConstants;
import processing.core.PVector;

import javax.swing.text.PlainDocument;
import java.awt.*;

import static processing.core.PConstants.CENTER;

public class CommandMerger {
    //Constantes
    private static final float MOUSE_CLICKED_DELAY = 5000.f;
    private static final float SRA_DELAY = 1500.f;
    private static final float ONEDOLLAR_DELAY = 5000.f;
    public static final double SeuilSRA5 = 0.70;

    //Attributs
    private boolean isOneDollarUsed = false;
    private boolean isSRAUsed = false;
    private boolean isMouseClickUsed = false;

    private boolean isLocation2Needed = false;

    private float timeMouseClicked = 0.f;
    private float timeSRA = 0.f;
    private float timeOneDollar = 0.f;


    protected float confidenceOneDollar = 0.f;
    protected float confidenceSra5 = 0.f;

    private Shape shape = null;
    private Couleur color = null;
    private PVector localisation1 = null;
    private PVector localisation2 = null;
    private Action action = null;
    private Command commandcreer = null;



    //Constructeur


    //Méthodes
    public void addCommandOneDollar(Shape shape, float confidence){
        if(confidence < SeuilSRA5){
            return;
        }
        this.shape = shape;
        this.confidenceOneDollar = confidence;
        this.isOneDollarUsed = true;
        this.timeOneDollar = Palette.processing.millis();

        if(this.action == null)
            this.action = Action.CREER;

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandOneDollar(Action action, float confidence){
        if(confidence < SeuilSRA5){
            return;
        }
        this.action = action;
        this.confidenceOneDollar = confidence;
        this.isOneDollarUsed = true;
        this.timeOneDollar = Palette.processing.millis();

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandSRA(Action action, float confidence){
        if(confidence < SeuilSRA5){
            return;
        }

        this.action = action;
        this.isSRAUsed = true;
        this.timeSRA = Palette.processing.millis();

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandSRA(Action action, String what, Shape shape, Couleur color, String where, float confidence){
        if(confidence < SeuilSRA5){
            return;
        }
        this.confidenceSra5 = confidence;
        this.action = action;

        switch (action) {
            case CREER:
                if (shape != null){
                    this.shape = shape;
                }else{
                    this.shape = Palette.getShapeAt(this.localisation1);
                    this.isLocation2Needed = true;
                }
                System.out.println("shape : " + this.shape);

                if(color == null){
                    this.color = Palette.getCouleurAt(this.localisation1);
                }else{
                    this.color = color;
                    this.isLocation2Needed = true;
                }
                System.out.println("color : " + this.color);

                break;

            case MODIFIER:
                if(color == null){
                    this.color = Palette.getCouleurAt(this.localisation2);
                }else{
                    this.color = color;
                }
                if (commandcreer == null){
                    this.commandcreer = Palette.getCommandAt(this.localisation1);
                }
                System.out.println("color : " + this.color);

                break;

            case SUPPRIMER:
                if (shape != null){
                    this.shape = shape;
                }else{
                    this.shape = Palette.getShapeAt(this.localisation1);
                    this.isLocation2Needed = true;
                }
                System.out.println("shape : " + this.shape);

                break;

            case DEPLACER:
                // Deplace ça là                    -> 2 locations FAIT
                // Deplace le rectangle rouge ici   -> 1 location  /ToDo
                // Deplace le rectangle ici         -> 1 location  //ToDo
                // Deplace ce rectangle ici         -> 2 locations //ToDo
                if (commandcreer == null){
                    this.commandcreer = Palette.getCommandAt(this.localisation1);
                    this.isLocation2Needed = true;
                }
                break;

            case ANNULER:
                break;

            case QUITTER:
                break;

            default:
                break;
        }

        this.isSRAUsed = true;
        this.timeSRA = Palette.processing.millis();

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandMouseClick(float x, float y){
        if(this.localisation1 == null){
            this.localisation1 = new PVector(x,y);
            if(isSRAUsed && this.shape == null){
                this.shape = Palette.getShapeAt(this.localisation1);
                isLocation2Needed = true;
            }
        }else if(this.localisation2 == null){
            this.localisation2 = new PVector(x,y);
        }else{
            this.localisation1 = this.localisation2;
            this.localisation2 = new PVector(x,y);
        }

        if(this.action == Action.MODIFIER && this.commandcreer == null){
            this.commandcreer = Palette.getCommandAt(new PVector(x,y));
        }

        this.isMouseClickUsed = true;
        this.timeMouseClicked = Palette.processing.millis();

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public boolean isCommandValid(){
        if(this.action == Action.QUITTER){
            return true;
        }
        // Check si la commande est multimodale
        if(!((this.isOneDollarUsed || this.isSRAUsed) &&
                (this.isOneDollarUsed || this.isMouseClickUsed) &&
                (this.isSRAUsed || this.isMouseClickUsed))){
            //return this.action != null && this.action == Action.ANNULER;
            return false;
        }

        if(this.action == null){
            return false;
        }

        System.out.println(this);

        switch (this.action){
            case CREER:
                return (this.localisation1 != null && this.shape != null && (!isLocation2Needed || this.localisation2 != null));
            case DEPLACER:
                return (this.localisation1 != null && this.commandcreer != null && (!isLocation2Needed || this.localisation2 != null));
            case MODIFIER:
                return (this.localisation1 != null && this.commandcreer != null && this.color != null && this.color != this.commandcreer.color);
            case ERREUR:
                //Todo
                break;
            case SUPPRIMER:
                return (this.localisation1 != null);
            case ANNULER:
                return true;
            default:
                return false;
        }
        return false;
    }

    private void addCommand(){
        Command command = new Command(this.action, this.shape, this.color, this.localisation2==null?this.localisation1:this.localisation2, this.confidenceOneDollar, this.confidenceSra5);
        command.setCommandCreer(this.commandcreer);
        Palette.addCommand(command);

        this.clean();
    }

    private void clean(){
        this.isOneDollarUsed = false;
        this.isSRAUsed = false;
        this.isMouseClickUsed = false;

        this.isLocation2Needed = false;

        this.shape = null;
        this.color = null;
        this.localisation1 = null;
        this.localisation2 = null;
        this.action = null;
        this.commandcreer = null;
        this.confidenceOneDollar = 0.f;
        this.confidenceSra5 = 0.f;
    }

    public void draw(){
        Palette.processing.fill(0);
        Palette.processing.textSize(20);
        Palette.processing.textAlign(Palette.LEFT);

        if(this.isOneDollarUsed){
            this.checkDelayTimeOneDollar();
            this.drawOneDollar();
        }

        if(this.isSRAUsed){
            this.checkDelayTimeSRA();
            this.drawSRA();
        }

        if(this.isMouseClickUsed){
            this.checkDelayTimeMouseClick();
            this.drawMouseClick();
        }
    }

    private void drawMouseClick() {
        int alpha = this.getAlpha(timeMouseClicked, MOUSE_CLICKED_DELAY);
        if(alpha<= 0){
            return;
        }
        Palette.processing.noStroke();
        Palette.processing.fill(new Color(147, 36, 36,  alpha).getRGB());

        Palette.processing.rect(0.0f, 0.0f, (Palette.processing.width/3.0f), 30.0f);
        Palette.processing.fill(new Color(0, 0, 0,  alpha).getRGB());

        Palette.processing.textAlign(CENTER, CENTER);
        Palette.processing.textSize(18);
        Palette.processing.text("MouseClick", Palette.processing.width/6.0f, 9.0f);
        Palette.processing.textSize(12);
        Palette.processing.text("" + (Palette.processing.millis() - timeMouseClicked)/1000. + "s", Palette.processing.width/6.0f, 22);
    }

    private void checkDelayTimeMouseClick(){
        if(Palette.processing.millis() - this.timeMouseClicked >= MOUSE_CLICKED_DELAY){
            this.localisation1 = null;
            this.localisation2 = null;
            this.isMouseClickUsed = false;
        }
    }

    private void drawSRA() {
        int alpha = this.getAlpha(timeSRA, SRA_DELAY);
        if(alpha<= 0){
            return;
        }
        Palette.processing.noStroke();
        Palette.processing.fill(new Color(82, 175, 82, alpha).getRGB());

        Palette.processing.rect((Palette.processing.width/3.0f), 0.0f, (Palette.processing.width/3.0f), 30.0f);
        Palette.processing.fill(new Color(0, 0, 0,  alpha).getRGB());

        Palette.processing.textAlign(CENTER, CENTER);

        Palette.processing.textSize(18);
        Palette.processing.text("SRA", Palette.processing.width/2.0f, 10);
        Palette.processing.textSize(12);
        Palette.processing.text("" + (Palette.processing.millis() - timeSRA)/1000. + "s", Palette.processing.width/2.0f, 25);
    }

    private void checkDelayTimeSRA(){
        if(Palette.processing.millis() - this.timeSRA > SRA_DELAY){
            this.shape = null;
            this.color = null;
            this.commandcreer = null;
            this.confidenceSra5 = 0.f;
            this.isSRAUsed = false;
        }
    }

    private void drawOneDollar() {
        int alpha = this.getAlpha(timeOneDollar, ONEDOLLAR_DELAY);
        if(alpha<= 0){
            return;
        }
        Palette.processing.noStroke();
        Palette.processing.fill(new Color(59, 79, 166, alpha).getRGB());

        Palette.processing.rect((2*Palette.processing.width/3.0f), 0.0f, (Palette.processing.width/3.0f), 30.0f);
        Palette.processing.fill(new Color(0, 0, 0,  alpha).getRGB());

        Palette.processing.textAlign(CENTER, CENTER);
        Palette.processing.textSize(18);
        Palette.processing.text("OneDollar", 5*Palette.processing.width/6.0f, 9.0f);
        Palette.processing.textSize(12);
        Palette.processing.text("" + (Palette.processing.millis() - timeOneDollar)/1000. + "s", 5*Palette.processing.width/6.0f, 22);

    }

    private void checkDelayTimeOneDollar(){
        if(Palette.processing.millis() - this.timeOneDollar > ONEDOLLAR_DELAY){
            this.shape = null;
            this.confidenceOneDollar = 0.f;
            this.isOneDollarUsed = false;
        }
    }

    private int getAlpha(float time, float delay){
        if((Palette.processing.millis()-time) <= delay - 1000)
            return 255;
        if((Palette.processing.millis()-time) > delay)
            return 0;

//        System.out.println("alpha : " + Math.max((int) (255.f - 255.f * ((Palette.processing.millis() - time) - (delay - 1000)) / 1000.f), 0));
        return Math.max((int) (255.f - 255.f * ((Palette.processing.millis() - time) - (delay - 1000)) / 1000.f), 0);
    }

    @Override
    public String toString() {
        return "CommandMerger{" +
                "confidenceOneDollar=" + confidenceOneDollar +
                ", confidenceSra5=" + confidenceSra5 +
                ", shape=" + shape +
                ", color=" + color +
                ", localisation1=" + localisation1 +
                ", localisation2=" + localisation2 +
                ", action=" + action +
                ", commandcreer=" + commandcreer +
                '}';
    }
}
