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
    // Quand on va pour dessiner la commande (CREER)
    // ON check cette ArrayList
    //Si elle est vide - > comme d'hab
    //sinon, on prend la commande Deplacer à la fin de l'arrayList
    //  -> on récupere la position associée
    //      -> on dessine la commande Creer à cette position
    // A chaque qu'on réçoit une commande deplacer -> on lm'ajouter à cette arraylist

    // ToDo Action ChangerCouleur (même chose que pour deplacer)


    //Constructeur
    public Command(){
        //ToDo peut être pas une bonne idée de mettre ici les params par défaut -> fout en l'air le check si la commande est valide
        this(null, null, null, new PVector((float) (Math.random() * 640), (float) (Math.random() * 480)), 0.f, 0.f);
    }

    public Command(Action action, Shape shape) {
        // ToDo Gestion de la couleur, la localisation et confidence par defaut
        this(action,shape,null, null, 0.f, 0.f);
    }

    public Command(FSM fsm) {
        this(null, null,null, null, 0.f, 0.f);
        switch (fsm){
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
        // ToDo Gestion de la couleur et de la localisation par defaut
        this(action,shape,null, null, confidenceShape, 0.f);
    }

    public Command(Action action, Shape shape, Couleur color, PVector localisation) {
        // ToDo Gestion de la couleur et de la localisation par defaut
        this(action, shape, color, localisation, 0.f, 0.f);
    }

    public Command(Action action, Shape shape, Couleur color, float confidenceShape, float confidenceColor) {
        // ToDo Gestion de la localisation par defaut
        this(action,shape,color, null, confidenceShape, confidenceColor);
    }

    public Command(Action action, Shape shape, PVector localisation, float confidenceShape) {
        this(action, shape, null, localisation, confidenceShape, 0.f);
    }

    public Command(Action action, Shape shape, Couleur color, PVector localisation, float confidenceShape, float confidenceColor) {
        this.action = action;
        this.shape = shape;
        this.color = color;
        this.localisation = localisation;
        this.confidenceOneDollar = confidenceShape;
        this.confidenceSra5 = confidenceColor;

        this.creerForme(this.color, this.shape, this.localisation);
    }

    //Méthodes

    public void drawCommand(){
        //ToDo Afficher le résultat de la commande sur la palette

        if(this.action != Action.CREER)
            return;

        float x = localisation.x;
        float y = localisation.y;

        if(this.commandDeplacer != null && !this.commandDeplacer.isEmpty()){
            x = this.commandDeplacer.get(this.commandDeplacer.size() - 1).localisation.x;
            y = this.commandDeplacer.get(this.commandDeplacer.size() - 1).localisation.y;
        }


        switch (this.shape){
            case RECTANGLE:
                Palette.processing.rectMode(CENTER);
                Palette.processing.fill(Couleur.getColor(this.color).getRGB());
                Palette.processing.rect(x, y, 100,60);
                Palette.processing.rectMode(CORNER);
                break;
            case TRIANGLE:
                Palette.processing.fill(Couleur.getColor(this.color).getRGB());
                Palette.processing.triangle(x - 50, y + 30 , x + 50, y + 30 , x , y - 50 );
                break;
            case CIRCLE:
                Palette.processing.fill(Couleur.getColor(this.color).getRGB());
                Palette.processing.circle(x, y, 100);
                break;
            default:
                break;
        }

    }

    public void drawFeedback(){
//        Palette.processing.noStroke();
//        switch (action){
//            case CREER:
//                Palette.processing.fill(new java.awt.Color(255, 234, 0).getRGB(),255);
//                break;
//            case DEPLACER:
//                Palette.processing.fill(new java.awt.Color(62, 133, 159).getRGB(),255);
//                break;
//            case ANNULER:
//            case SUPPRIMER:
//                Palette.processing.fill(new java.awt.Color(255, 119, 70).getRGB(),255);
//                break;
//            case MODIFIER:
//                Palette.processing.fill(new java.awt.Color(15, 136, 15).getRGB(),255);
//                break;
//            case ERREUR:
//                Palette.processing.fill(new java.awt.Color(199, 45, 45).getRGB(),255);
//                break;
//            default:
//                break;
//        }
//        Palette.processing.textSize(20);
//
//        int largeurRect = (int)Palette.processing.textWidth(action.name()) + 30 ;
//        Palette.processing.rect(0,0,largeurRect,30);
//        Palette.processing.triangle(largeurRect,0,largeurRect,30,largeurRect+15,15);
//        Palette.processing.textAlign(CENTER, CENTER);
//        Palette.processing.fill(new java.awt.Color(0, 0,0).getRGB(),255);
//        Palette.processing.text(action.name(), (float) (largeurRect/2.0),15);
//
//        // ToDo Afficher Forme
//        if(shape != null) {
//            switch (shape) {
//                case CIRCLE:
//                    break;
//                case TRIANGLE:
//                    break;
//                case RECTANGLE:
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        // ToDo Afficher Localisation
//        // ToDo Afficher Couleur

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

    public static void drawFeedBack(Command c){
        if (c == null)
                return;
        c.drawFeedback();
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
            //return this.action.equals(c.action) && this.shape.equals(c.shape) && this.color.equals(c.color) && this.localisation.equals(c.localisation);
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
        String msg;

        switch (this.action) {
            case CREER:
                msg = "Creation d'un " + this.shape.getName() + " de couleur " + this.color.getName();
                break;
            case ANNULER:
                msg = "Annulation";
                break;
            case DEPLACER:
                msg = "Déplacement du " + this.commandCreer.getShape() .getName() + " de couleur " + this.commandCreer.getCouleur().getName();
                break;
            case MODIFIER:
                msg = "Changement de couleur";
                break;
            case QUITTER:
                msg = "Au revoir";
                break;
            default:
                msg = "Je n'ai pas compris, pouvez vous repeter ?";
                break;
        }
        return msg;
    }
}
