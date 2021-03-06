package eu.ctruillet.ihm.triceratops.command;

/*
* ACTION
*   Créer - JAUNE
*   Déplacer - BLEU
*   Erreur - ROUGE
* SHAPE - Bloc ??? avec la forme à l'interieur en Noir (affichage de la couleur selectionnée dans un bloc à part)
*   Rectangle -
*   Circle -
*   Triangle -
* LOCALISATION
*
* COULEUR
*   Rouge
*   Vert
*   Bleu
 */

import eu.ctruillet.ihm.triceratops.palette.*;
import processing.core.PVector;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public class Command {
    //Attributs
    protected Action action;
    protected Shape shape;
    protected Forme forme;
    protected Couleur color;
    protected PVector localisation;
    protected float confidenceOneDollar;
    protected float confidenceSra5;

    public Command commandCreer;
    //ToDo annuler la suppression
    protected ArrayList<Command> commandDeplacer = new ArrayList<>();
    protected ArrayList<Command> commandModifier = new ArrayList<>();
    // Quand on va pour dessiner la commande (CREER)
    // ON check cette ArrayList
    //Si elle est vide - > comme d'hab
    //sinon, on prend la commande Deplacer à la fin de l'arrayList
    //  -> on récupere la position associée
    //      -> on dessine la commande Creer à cette position
    // A chaque qu'on réçoit une commande deplacer -> on lm'ajouter à cette arraylist


    //Constructeur
    public Command(){
        this(null, null, null, new PVector((float) (Math.random() * 640), (float) (Math.random() * 480)), 0.f, 0.f);
    }

    public Command(Action action, Shape shape) {
        this(action,shape,null, null, 0.f, 0.f);
    }

    public Command(FSM fsm) {
        this(null, null,null, null, 0.f, 0.f);
        switch (fsm){
            case ERREUR:
                this.action = Action.ERREUR;
                break;
            case ANNULER:
                this.action = Action.ANNULER;
                break;
            case SUPPRIMER:
                this.action = Action.SUPPRIMER;
                break;
            case QUITTER:
                this.action = Action.QUITTER;
                break;
            default:
                break;
        }
    }

    public Command(Action action, Shape shape, float confidenceShape) {
        this(action,shape,null, null, confidenceShape, 0.f);
    }

    public Command(Action action, Shape shape, Couleur color, PVector localisation) {
        this(action, shape, color, localisation, 0.f, 0.f);
    }

    public Command(Action action, Shape shape, Couleur color, float confidenceShape, float confidenceColor) {
        this(action,shape,color, null, confidenceShape, confidenceColor);
    }

    public Command(Action action, Shape shape, PVector localisation, float confidenceShape) {
        this(action, shape, null, localisation, confidenceShape, 0.f);
    }

    public Command(Action action, Shape shape, Couleur color, PVector localisation, float confidenceShape, float confidenceColor) {
        this.action = action;
        this.shape = shape;
        this.color = getColor(color);
        this.localisation = localisation;
        this.confidenceOneDollar = confidenceShape;
        this.confidenceSra5 = confidenceColor;

        this.creerForme(this.color, this.shape, this.localisation);
    }

    //Méthodes
    //Couleur par défaut
    private Couleur getColor(Couleur c){
        if(c != null)
            return c;

        Couleur color;
        switch ((int)(Math.random() * 3)){
            case 0:
                color = Couleur.ROUGE;
                break;
            case 1:
                color = Couleur.VERT;
                break;
            case 2:
                color = Couleur.BLEU;
                break;
            default:
                color = Couleur.ROUGE;
                break;
        }
        return color;
    }

    public void addCommandDeplacer(Command c){
        if(c.getLocalisation() != null)
            if(this.action != Action.CREER){
                this.commandCreer.addCommandDeplacer(c);
            }else{
                commandDeplacer.add(c);
            }

    }

    public void removeCommandDeplacer(Command c){
        for (int i = 0; i < commandDeplacer.size(); i++) {
            if(commandDeplacer.get(i).equals(c)){
                commandDeplacer.remove(i);
                return;
            }
        }
    }

    public void addCommandModifier(Command c){
        if(c.getLocalisation() != null)
            if(this.action != Action.CREER){
                this.commandCreer.addCommandModifier(c);
            }else{
                commandModifier.add(c);
            }

    }

    public void removeCommandModifier(Command c){
        for (int i = 0; i < commandModifier.size(); i++) {
            if(commandModifier.get(i).equals(c)){
                commandModifier.remove(i);
                return ;
            }
        }
    }

    public PVector getLocalisation(){
        return localisation;
    }

    public void drawCommand(){

        if(this.action != Action.CREER)
            return;

        float x = localisation.x;
        float y = localisation.y;
        Couleur color = this.color;

        if(this.commandDeplacer != null && !this.commandDeplacer.isEmpty()){
            x = this.commandDeplacer.get(this.commandDeplacer.size() - 1).localisation.x;
            y = this.commandDeplacer.get(this.commandDeplacer.size() - 1).localisation.y;
        }

        if(this.commandModifier != null && !this.commandModifier.isEmpty()){
            color = this.commandModifier.get(this.commandModifier.size() - 1).color;
        }


        switch (this.shape){
            case RECTANGLE:
                Palette.processing.rectMode(CENTER);
                Palette.processing.fill(Couleur.getColor(color).getRGB());
                Palette.processing.rect(x, y, 100,60);
                Palette.processing.rectMode(CORNER);
                break;
            case TRIANGLE:
                Palette.processing.fill(Couleur.getColor(color).getRGB());
                Palette.processing.triangle(x - 50, y + 30 , x + 50, y + 30 , x , y - 50 );
                break;
            case CIRCLE:
                Palette.processing.fill(Couleur.getColor(color).getRGB());
                Palette.processing.circle(x, y, 100);
                break;
            default:
                break;
        }

    }

    public void creerForme(Couleur color, Shape shape, PVector position) {
        this.forme = new Forme(color, shape, position);
        //forme.drawForme();
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setColor(Couleur color) {
        this.color = color;
    }

    public void setLocalisation(PVector localisation) {
        this.localisation = localisation;
    }

    public void setConfidenceOneDollar(float confidenceOneDollar) {
        this.confidenceOneDollar = confidenceOneDollar;
    }

    public void setConfidenceSra5(float confidenceSra5) {
        this.confidenceSra5 = confidenceSra5;
    }


    @Override
    public String toString() {
        return "Command{" +
                "action=" + action +
                ", shape=" + shape +
                ", color=" + color +
                ", localisation=" + localisation +
                ", confidenceOneDollar=" + confidenceOneDollar +
                ", confidenceSra5=" + confidenceSra5 +
                '}';
    }

    public Shape getShape() {
        return this.shape;
    }

    public Couleur getCouleur() {
        return this.color;
    }

    public boolean isMouseOnShape(float mouseX, float mouseY) {
        if(this.shape == null) return false;

        boolean isOnShape = false;

        float x = localisation.x;
        float y = localisation.y;


        if(this.commandDeplacer != null && !this.commandDeplacer.isEmpty()){
            x = this.commandDeplacer.get(this.commandDeplacer.size() - 1).localisation.x;
            y = this.commandDeplacer.get(this.commandDeplacer.size() - 1).localisation.y;
        }

        if(this.commandCreer != null)
            this.shape = commandCreer.getShape();

        switch (this.getShape()) {
            case TRIANGLE:
                PVector AB = new PVector( (int) (x + 50 - (x - 50)), 0);
                PVector AC = new PVector( (int) (x - (x - 50)),(int) ((y - 50) - (y + 30)));
                PVector AM = new PVector( (int) (mouseX - (x - 50)),(int) (mouseY - (y + 30)));

                PVector BA = new PVector( (int) ((x - 50) - (x + 50)), 0);
                PVector BC = new PVector( (int) (x - (x + 50)),(int) ((y - 50) - (y + 30)));
                PVector BM = new PVector( (int) (mouseX - (x + 50)),(int) (mouseY - (y + 30)));

                PVector CA = new PVector( (int) ((x - 50) - x), (int) ((y + 30) - (y - 50)));
                PVector CB = new PVector( (int) ((x + 50) - x), (int) ((y + 30) - (y - 50)));
                PVector CM = new PVector( (int) (mouseX - x), (int) (mouseY - (y - 50)));

                if ( ((AB.cross(AM)).dot(AM.cross(AC)) >=0) && ((BA.cross(BM)).dot(BM.cross(BC)) >=0) && ((CA.cross(CM)).dot(CM.cross(CB)) >=0) ) {
                    isOnShape = true;
                }
                break;
            case RECTANGLE:
                // RectMode CENTER
                if (mouseX > x - 50 && mouseX < x + 50 && mouseY > y - 30 && mouseY < y + 30) {
                    isOnShape = true;
                }
                break;
            case CIRCLE:
                // Radius = 50
                if (sqrt(pow(mouseX - x, 2) + pow(mouseY - y, 2)) < 50) {
                    isOnShape = true;
                }
                break;
            default:
                break;
        }

        return isOnShape;
    }

    public int hashCode(){
        return this.action.hashCode() + this.shape.hashCode() + this.color.hashCode() + this.localisation.hashCode();
    }

    public boolean equals(Object o){
        if(o == null)
            return false;
        if(o instanceof Command){
            Command c = (Command) o;
            return c == this;
        }
        return false;
    }

    public void setCommandCreer(Command c) {
        if(c == null) return;
        if(c.getAction() != Action.CREER){
            this.commandCreer = c.getCommandCreer();
        }else{
            this.commandCreer = c;
        }
    }

    private Command getCommandCreer() {
        if(this.commandCreer.getAction() != Action.CREER){
            return this.commandCreer.getCommandCreer();
        }else{
            return this.commandCreer;
        }
    }

    public String getFeedBack() {
        String msg = "";

        switch (this.action) {
            case CREER:
                msg = "Creation d'un " + this.shape.getName() + " de couleur " + this.color.getName();
                break;
            case ANNULER:
                msg = "Annulation";
                break;
            case DEPLACER:
                msg = "Daiplacement du " + this.commandCreer.getShape().getName() + " de couleur " + this.commandCreer.getCouleur().getName();
                break;
            case MODIFIER:
                msg = "Changement de couleur du " + this.commandCreer.getShape().getName() + " " + this.commandCreer.getCouleur().getName() + " en " + this.color.getName();
                break;
            case QUITTER:
                msg = "Au revoir";
                break;
            case SUPPRIMER:
                if(this.commandCreer == null) break;
                msg = "Suppression du " + this.commandCreer.getShape().getName() + " de couleur " + this.commandCreer.getCouleur().getName();
                break;
            default:
                msg = "Ola qui voila ? Une belle erreur";
                break;
        }
        return msg;
    }
}
