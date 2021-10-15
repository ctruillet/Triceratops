package eu.ctruillet.ihm.triceratops.ivy;

import eu.ctruillet.ihm.triceratops.palette.Action;
import eu.ctruillet.ihm.triceratops.palette.Command;
import eu.ctruillet.ihm.triceratops.palette.Shape;
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


    //Constructeur
    public IvyListener (){
        this.bus = initBus("Triceratops", "Roar");
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
                    System.out.println("> RECEIVE " + strings[0] + " " + strings[1]);

                    //ToDo Gerer la confidence
                    command.setConfidenceOneDollar(Float.parseFloat(strings[1].replace(',','.')));


                    switch (strings[0]){
                        case "cancel":
                            command.setAction(Action.SUPPRIMER);
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

                    if (command.isValidCommand()){
                        System.out.println("ADD COMMAND " + command);
                        commands.add(command);
                        command = new Command();
                    }


                }
            });

            //Abonnement à SRA5
            bus.bindMsg("^sra5" + "$", new IvyMessageListener() {
                @Override
                public void receive(IvyClient ivyClient, String[] strings) {
                    //ToDo SRA5
                }
            });

        } catch (IvyException e) {
            e.printStackTrace();
        }

        return bus;
    }

    public Command getNextCommand(){
        Command command;

        if (this.commands.isEmpty() || i >= this.commands.size()){
            return null;
        }

        command = this.commands.get(i);
        i++;

        return command;
    }

}
