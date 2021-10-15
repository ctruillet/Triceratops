package eu.ctruillet.ihm.triceratops.palette;

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

import processing.core.PVector;

import static processing.core.PConstants.CENTER;

public class Command {
    //Attributs
    protected Action action;
    protected Shape shape;
    protected Forme forme;
    protected Couleur color;
    protected PVector localisation;

    //Constructeur
    public Command(){
        this(null, null, null, 0.f, 0.f);
    }



    public Command(Action action, Shape shape) {
        // ToDo Gestion de la couleur, la localisation et confidence par defaut
        this(action,shape,null, null, 0.f, 0.f);
    }

    public Command(Action action, Shape shape) {
        // ToDo Gestion de la couleur et de la localisation par defaut
        this(action,shape,null, null);
    }

    public Command(Action action, Shape shape, Color color) {
        // ToDo Gestion de la localisation par defaut
        this(action,shape,color, null);
    }

    public Command(Action action, Shape shape, PVector localisation) {
        //ToDo Gestion de la couleur
        this(action,shape,null, localisation);
    }

    public Command(Action action, Shape shape, Color color, PVector localisation) {
        this.action = action;
        this.shape = shape;
        this.color = color;
        this.localisation = localisation;
    }

    //Méthodes

    public void drawCommand(){
        //ToDo Afficher le résultat de la commande sur la palette

        System.out.println(this.toString());
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
        switch (shape){
            case CIRCLE:
                break;
            case TRIANGLE:
                break;
            case RECTANGLE:
                break;
            default:
                break;
        }

        // ToDo Afficher Localisation
        // ToDo Afficher Couleur

    }
    //ToDo
    public boolean isValidCommand(){
        //return true;
        switch (this.action){
            case SUPPRIMER:
                // supprimer quoi ? Forme + Couleur (?) / Position
                //  Supprimer ce triangle
                //  Supprimer le triangle rouge
                //  Supprimer ça

                break;
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
            case MODIFIER:
            case ERREUR:
                return true;
        }

        return false;
    }

    public void creerForme(Couleur color, Shape shape, PVector position) {
        this.forme = new Forme(color, shape, position);
        forme.drawForme();
    }

    public void setAction(Action action) {
        this.action = action;
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
}
