package eu.ctruillet.ihm.triceratops.palette;

public enum Color {
    ROUGE,
    VERT,
    BLEU;

    public java.awt.Color getColor(Color c) throws Exception {
        switch (c){
            case ROUGE:
                return new java.awt.Color(255,0,0);
            case VERT:
                return new java.awt.Color(15, 136, 15);
            case BLEU:
                return new java.awt.Color(11, 11, 115);
            default:
                throw new Exception("Couleur invalide");
        }
    }
}
