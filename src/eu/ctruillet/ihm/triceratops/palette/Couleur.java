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

    public static Couleur getColor(String c){
        switch (c){
            case "GREEN":
                return VERT;
            case "BLUE":
                return BLEU;
            case "RED":
                return ROUGE;
            default:
                return null;
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

    public boolean equals(Couleur c){
        if(c == null){
            return false;
        }
        return this.toString().equals(c.toString());
    }

    public String getName() {
        return this.toString();
    }
}
