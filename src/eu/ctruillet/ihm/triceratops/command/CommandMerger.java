package eu.ctruillet.ihm.triceratops.command;

import eu.ctruillet.ihm.triceratops.palette.Action;
import eu.ctruillet.ihm.triceratops.palette.Couleur;
import eu.ctruillet.ihm.triceratops.palette.Palette;
import eu.ctruillet.ihm.triceratops.palette.Shape;
import processing.core.PVector;

public class CommandMerger {
    //Attributs
    private boolean isOneDollarUsed = false;
    private boolean isSRAUsed = false;
    private boolean isMouseClickUsed = false;
    protected float confidenceOneDollar = 0.f;
    protected float confidenceSra5 = 0.f;

    private Shape shape = null;
    private Couleur color = null;
    private PVector localisation1 = null;
    private PVector localisation2 = null;
    private Action action = null;



    //Constructeur


    //Méthodes
    public void addCommandOneDollar(Shape shape, float confidence){
        if(confidence < 0.80){
            return;
        }
        this.shape = shape;
        this.confidenceOneDollar = confidence;
        this.isOneDollarUsed = true;

        if(this.action == null)
            this.action = Action.CREER;

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandOneDollar(Action action, float confidence){
        if(confidence < 0.80){
            return;
        }
        this.action = action;
        this.confidenceOneDollar = confidence;
        this.isOneDollarUsed = true;

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandSRA(Action action, float confidence){
        if(confidence < 0.80){
            return;
        }

        this.action = action;
        this.isSRAUsed = true;

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandSRA(Action action, String what, Shape shape, Couleur color, String where, float confidence){
        if(confidence < 0.80){
            return;
        }
        this.confidenceSra5 = confidence;
        this.action = action;

        switch (action){
            case CREER:
                if(shape != null)
                    this.shape = shape;
                this.color = color;


                break;

            case SUPPRIMER:
                // ToDo
                break;

            case DEPLACER:
                // ToDo
                break;

            default:
                break;
        }

        this.isSRAUsed = true;
        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public void addCommandMouseClick(float x, float y){
        if(this.localisation1 == null){
            this.localisation1 = new PVector(x,y);
        }else if(this.localisation2 == null){
            this.localisation2 = new PVector(x,y);
        }else{
            this.localisation1 = this.localisation2;
            this.localisation2 = new PVector(x,y);
        }

        this.isMouseClickUsed = true;

        if(this.isCommandValid()){
            this.addCommand();
        }
    }

    public boolean isCommandValid(){
        // Check si la commande est multimodale
        if(!((this.isOneDollarUsed || this.isSRAUsed) &&
                (this.isOneDollarUsed || this.isMouseClickUsed) &&
                (this.isSRAUsed || this.isMouseClickUsed))){
            return this.action != null && this.action == Action.CANCEL;
        }

        if(this.action == null){
            return false;
        }

        switch (this.action){
            case CREER:
                return (this.localisation1 != null && this.shape != null);
            case DEPLACER:
                break;
            case MODIFIER:
                break;
            case ERREUR:
                break;
            case CANCEL:
                return true;
            default:
                return false;
        }
        return false;
    }

    private void addCommand(){
        Command command = new Command(this.action, this.shape, this.color, this.localisation1, this.confidenceOneDollar, this.confidenceSra5);
        System.out.println(command);
        Palette.addCommand(command);

        this.clean();
    }

    private void clean(){
        this.isOneDollarUsed = false;
        this.isSRAUsed = false;
        this.isMouseClickUsed = false;

        this.shape = null;
        this.color = null;
        this.localisation1 = null;
        this.localisation2 = null;
        this.action = null;
        this.confidenceOneDollar = 0.f;
        this.confidenceSra5 = 0.f;
    }
}
