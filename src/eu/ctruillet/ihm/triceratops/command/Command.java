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

import java.util.Collection;

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

    //Constructeur
    public Command(){
        //ToDo peut être pas une bonne idée de mettre ici les params par défaut -> fout en l'air le check si la commande est valide
        this(null, Shape.RECTANGLE, Couleur.ROUGE, new PVector((float) (Math.random() * 640), (float) (Math.random() * 480)), 0.f, 0.f);
    }

    public Command(Action action, Shape shape) {
        // ToDo Gestion de la couleur, la localisation et confidence par defaut
        this(action,shape,null, null, 0.f, 0.f);
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
        this(action,shape, null, localisation, confidenceShape, 0.f);
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

        switch (this.shape){
            case RECTANGLE:
                Palette.processing.rectMode(CENTER);
                Palette.processing.fill(Couleur.getColor(this.color).getRGB());
                Palette.processing.rect(this.localisation.x, this.localisation.y, 100,60);
                Palette.processing.rectMode(CORNER);
                break;
            case TRIANGLE:
                Palette.processing.fill(Couleur.getColor(this.color).getRGB());
                Palette.processing.triangle(this.localisation.x - 50, this.localisation.y + 30 , this.localisation.x + 50, this.localisation.y + 30 , this.localisation.x , this.localisation.y - 50 );
                break;
            case CIRCLE:
                Palette.processing.fill(Couleur.getColor(this.color).getRGB());
                Palette.processing.circle(this.localisation.x, this.localisation.y, 100);
                break;
            default:
                break;
        }

    }

    public void drawFeedback(){
        Palette.processing.noStroke();
        switch (action){
            case CREER:
                Palette.processing.fill(new java.awt.Color(255, 234, 0).getRGB(),255);
                break;
            case DEPLACER:
                Palette.processing.fill(new java.awt.Color(62, 133, 159).getRGB(),255);
                break;
            case CANCEL:
            case SUPPRIMER:
                Palette.processing.fill(new java.awt.Color(255, 119, 70).getRGB(),255);
                break;
            case MODIFIER:
                Palette.processing.fill(new java.awt.Color(15, 136, 15).getRGB(),255);
                break;
            case ERREUR:
                Palette.processing.fill(new java.awt.Color(199, 45, 45).getRGB(),255);
                break;
            default:
                break;
        }
        Palette.processing.textSize(20);

        int largeurRect = (int)Palette.processing.textWidth(action.name()) + 30 ;
        Palette.processing.rect(0,0,largeurRect,30);
        Palette.processing.triangle(largeurRect,0,largeurRect,30,largeurRect+15,15);
        Palette.processing.textAlign(CENTER, CENTER);
        Palette.processing.fill(new java.awt.Color(0, 0,0).getRGB(),255);
        Palette.processing.text(action.name(), (float) (largeurRect/2.0),15);

        // ToDo Afficher Forme
        if(shape != null) {
            switch (shape) {
                case CIRCLE:
                    break;
                case TRIANGLE:
                    break;
                case RECTANGLE:
                    break;
                default:
                    break;
            }
        }

        // ToDo Afficher Localisation
        // ToDo Afficher Couleur

    }
    //ToDo
    public boolean isValidCommand(){
        if(this.action == null) return false;

        switch (this.action){
            case SUPPRIMER:
                // supprimer quoi ? Forme + Couleur (?) / Position
                //  Supprimer ce triangle
                //  Supprimer le triangle rouge
                //  Supprimer ça

                break;
            case CANCEL:
                return true;
            case CREER:
//                Créer un rectangle
//                Créer un rectangle rouge
//                Créer un rectangle rouge ici
//
//                 FORME [+ COULEUR] [+ POSITION]
                return (this.shape != null);

            case DEPLACER:
                //  Déplacer ça là
                //  Déplacer le triangle ici
                //  Déplacer ce triangle là
                //  Déplacer le triangle rouge là bas

                //
                break;
            case MODIFIER:
                break;
            case ERREUR:
                return true;

            default:
                return false;
        }

        return false;
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

    public boolean isMouseOnShape(float mouseX, float mouseY) {
        boolean isOnShape = false;

        switch (this.getShape()) {
            case TRIANGLE:
                PVector AB = new PVector( (int) (this.localisation.x + 50 - (this.localisation.x - 50)), 0);
                PVector AC = new PVector( (int) (this.localisation.x - (this.localisation.x - 50)),(int) ((this.localisation.y - 50) - (this.localisation.y + 30)));
                PVector AM = new PVector( (int) (mouseX - (this.localisation.x - 50)),(int) (mouseY - (this.localisation.y + 30)));

                PVector BA = new PVector( (int) ((this.localisation.x - 50) - (this.localisation.x + 50)), 0);
                PVector BC = new PVector( (int) (this.localisation.x - (this.localisation.x + 50)),(int) ((this.localisation.y - 50) - (this.localisation.y + 30)));
                PVector BM = new PVector( (int) (mouseX - (this.localisation.x + 50)),(int) (mouseY - (this.localisation.y + 30)));

                PVector CA = new PVector( (int) ((this.localisation.x - 50) - this.localisation.x), (int) ((this.localisation.y + 30) - (this.localisation.y - 50)));
                PVector CB = new PVector( (int) ((this.localisation.x + 50) - this.localisation.x), (int) ((this.localisation.y + 30) - (this.localisation.y - 50)));
                PVector CM = new PVector( (int) (mouseX - this.localisation.x), (int) (mouseY - (this.localisation.y - 50)));

                if ( ((AB.cross(AM)).dot(AM.cross(AC)) >=0) && ((BA.cross(BM)).dot(BM.cross(BC)) >=0) && ((CA.cross(CM)).dot(CM.cross(CB)) >=0) ) {
                    isOnShape = true;
                }
                break;
            case RECTANGLE:
                // RectMode CENTER
                if (mouseX > this.localisation.x - 50 && mouseX < this.localisation.x + 50 && mouseY > this.localisation.y - 30 && mouseY < this.localisation.y + 30) {
                    isOnShape = true;
                }
                break;
            case CIRCLE:
                // Radius = 50
                if (sqrt(pow(mouseX - this.localisation.x, 2) + pow(mouseY - this.localisation.y, 2)) < 50) {
                    isOnShape = true;
                }
                break;
            default:
                break;
        }

        return isOnShape;
    }

}
