package eu.ctruillet.ihm.triceratops.palette;

public enum Couleur {
    ROUGE,
    VERT,
    BLEU;

    public static java.awt.Color getColor(Couleur c){
        switch (c){
            case ROUGE:
                return Couleur.getRed();
            case VERT:
                return Couleur.getGreen();
            case BLEU:
                return Couleur.getBlue();
            default:
                return new java.awt.Color(0, 0, 0);
        }
    }

    public static java.awt.Color getRed(){
        return new java.awt.Color(255,0,0);
    }

    public static java.awt.Color getGreen(){
        return new java.awt.Color(15, 136, 15);
    }

    public static java.awt.Color getBlue(){
        return new java.awt.Color(11, 11, 115);
    }
}
