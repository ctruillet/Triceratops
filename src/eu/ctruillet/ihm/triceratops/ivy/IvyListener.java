package eu.ctruillet.ihm.triceratops.ivy;

import eu.ctruillet.ihm.triceratops.command.Command;
import eu.ctruillet.ihm.triceratops.command.CommandMerger;
import eu.ctruillet.ihm.triceratops.palette.*;
import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

import java.util.ArrayList;

public class IvyListener {
    //Attribut
    protected Ivy bus;
    protected ArrayList<Command> commands = new ArrayList<>();
    protected Command command = new Command();
    protected int i = 0;
    protected CommandMerger commandMerger;


    //Constructeur
    public IvyListener (CommandMerger commandMerger){
        this.bus = initBus("Triceratops", "Roar");
        this.commandMerger = commandMerger;
    }

    //Méthodes
    protected Ivy initBus(String name, String msg) {
        bus = new Ivy(name, msg, null);
        //Lancement du Bus
        try {
            bus.start("127.255.255.255.2010");
        } catch (IvyException ie) {
            System.err.println("Error : " + ie.getMessage());
        }

        //Abonnements
        try {
            //Abonnement à OneDollarIvy
            bus.bindMsg("^OneDollarIvy" +" Template=(.*) Confidence=(.*)$", new IvyMessageListener() {
                @Override
                public void receive(IvyClient ivyClient, String[] strings) {
                    System.out.println("> OneDollarIvy \n" +
                            "\tTemplate=" + strings[0] + " \n" +
                            "\tConfidence=" + strings[1]);

                    //ToDo Gerer la confidence
                    command.setConfidenceOneDollar(Float.parseFloat(strings[1].replace(',','.')));


                    switch (strings[0]){
                        case "cancel":
                            //command.setAction(Action.CANCEL);
                            commandMerger.addCommandOneDollar(Action.CANCEL, confidence);
                            break;

                        case "circle":
                            command.setAction(Action.CREER);
                            command.setShape(Shape.CIRCLE);
                            break;
                        case "rectangle":
                            command.setAction(Action.CREER);
                            command.setShape(Shape.RECTANGLE);
                            break;
                        case "triangle":
                            command.setAction(Action.CREER);
                            command.setShape(Shape.TRIANGLE);
                            break;
                        default:
                            break;
                    }
                }
            });

            //Abonnement à SRA5
            //sra5 Parsed=action=MOVE what=undefined form=undefined color=RED localisation=undefined Confidence=0,8282248 NP=11 Num_A=0
            //^sra5 Parsed=action=(.*) what=(.*) form=(.*) color=(.*) localisation=(.*) Confidence=(.*) NP=(.*)$
            //sra5 Parsed=action=CREATE what=undefined form=RECTANGLE color=BLUE localisation=THERE Confidence=0,8282248 NP=11 Num_A=0
            //sra5 Parsed=action=CREATE what=THIS form=undefined color=BLUE localisation=THERE Confidence=0,8282248 NP=11 Num_A=0
            bus.bindMsg("^sra5 Parsed=action=(.*) what=(.*) form=(.*) color=(.*) localisation=(.*) Confidence=(.*) NP=(.*) Num_A=(.*)$", new IvyMessageListener() {
                @Override
                public void receive(IvyClient ivyClient, String[] strings) {
                    System.out.println("> SRA5 \n"
                                    + "\taction=" + strings[0] + "\n"
                                    + "\twhat=" + strings[1] + "\n"
                                    + "\tform=" + strings[2] + "\n"
                                    + "\tcolor=" + strings[3] + "\n"
                                    + "\tlocalisation=" + strings[4] + "\n"
                                    + "\tConfidence=" + strings[5] + "\n"
                                    + "\tNP=" + strings[6] + "\n"
                                    + "\tNP=" + strings[7] + "\n"
                            );

                    float confidence = Float.parseFloat(strings[5].replace(',','.'));

                    switch (strings[0]){
                        case "CREATE":
                            commandMerger.addCommandSRA(Action.CREER, strings[1], Shape.getShape(strings[2]), Couleur.getColor(strings[3]), strings[4], confidence);
                            break;

                        case "DELETE":
                            // ToDo
                            //commandMerger.addCommandSRA(Action.SUPPRIMER, strings[1], Shape.getShape(strings[2]),);
                            break;

                        case "MOVE":
                            // ToDo
                            //commandMerger.addCommandSRA(Action.DEPLACER, strings[1], Shape.getShape(strings[2]),);
                            break;

                        case "ANNULER":
                            // ToDo
                            //commandMerger.addCommandSRA(Action.CANCEL, Float.parseFloat(strings[5]));
                            break;

                        case "QUIT":
                            commandMerger.addCommandSRA(Action.QUITTER, confidence);
                            break;

                        default:
                            break;
                    }


                }
            });

            //Event ("met ça ici" ) -> click souris

        } catch (IvyException e) {
            e.printStackTrace();
        }

        return bus;
    }
}
