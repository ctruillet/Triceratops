package eu.ctruillet.ihm.triceratops.command;

import eu.ctruillet.ihm.triceratops.palette.Action;
import eu.ctruillet.ihm.triceratops.palette.Couleur;
import eu.ctruillet.ihm.triceratops.palette.Palette;
import eu.ctruillet.ihm.triceratops.palette.Shape;
import processing.core.PVector;

public class CommandMerger {
    //Constantes
    private static float MOUSE_CLICKED_DELAY = 5000.f;
    private static float SRA_DELAY = 1500.f;
    private static float ONEDOLLAR_DELAY = 5000.f;

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
        this.timeSRA = Palette.processing.millis();

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

        switch (action) {
            case CREER:
                if (shape != null){
                    this.shape = shape;
                }else{
                    this.shape = Palette.getShapeAt(this.localisation1);
                    isLocation2Needed = true;
                }
                System.out.println("shape : " + this.shape);

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

        this.isMouseClickUsed = true;
        this.timeMouseClicked = Palette.processing.millis();

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
                return (this.localisation1 != null && this.shape != null && (!isLocation2Needed || this.localisation2 != null));
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
        Command command = new Command(this.action, this.shape, this.color, this.localisation2==null?this.localisation1:this.localisation2, this.confidenceOneDollar, this.confidenceSra5);
        System.out.println(command);
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
        Palette.processing.text("MouseClick (" + (Palette.processing.millis() - timeMouseClicked)/1000. + "s)", 10, 340);
    }

    private void checkDelayTimeMouseClick(){
        if(Palette.processing.millis() - this.timeMouseClicked > MOUSE_CLICKED_DELAY){
            this.localisation1 = null;
            this.localisation2 = null;
            this.isMouseClickUsed = false;
        }
    }

    private void drawSRA() {
        Palette.processing.text("SRA (" + (Palette.processing.millis() - timeSRA)/1000. + "s)", 10, 360);
    }

    private void checkDelayTimeSRA(){
        if(Palette.processing.millis() - this.timeSRA > SRA_DELAY){
            this.shape = null;
            this.color = null;
            this.confidenceSra5 = 0.f;
            this.isSRAUsed = false;
        }
    }

    private void drawOneDollar() {
        Palette.processing.text("OneDollar (" + (Palette.processing.millis() - timeOneDollar)/1000. + "s)", 10, 380);
    }

    private void checkDelayTimeOneDollar(){
        if(Palette.processing.millis() - this.timeOneDollar > ONEDOLLAR_DELAY){
            this.shape = null;
            this.confidenceOneDollar = 0.f;
            this.isOneDollarUsed = false;
        }
    }
}
