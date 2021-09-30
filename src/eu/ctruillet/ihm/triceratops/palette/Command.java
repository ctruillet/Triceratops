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
    protected Color color;
    protected PVector localisation;

    //Constructeur


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
    public void draw(){
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
        Palette.processing.text(action.name(),largeurRect/2,15);

        // ToDo Afficher Forme
        // ToDo Afficher Localisation
        // ToDo Afficher Couleur
    }
}
