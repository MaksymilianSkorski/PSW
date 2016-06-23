/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jade.behaviours;
import jade.agents.TestAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.Random;
/**
 *
 * @author Maksiu
 */
public class SendTokenBehaviour extends TickerBehaviour{
    
    
    private final TestAgent myAgent;
    
    
    public SendTokenBehaviour(TestAgent a, long period) {
        super(a, period);

        this.myAgent = a;
    }
    
    protected void askForId()
    {
        /*ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for(AID a : messageReceivers){
         cfp.addReceiver(a);
        }
        cfp.setContent("ask-ID");
        cfp.setPerformative(100);
        myAgent.send(cfp);*/
    }
    protected void sendToken(int id)
    {
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        AID a = (AID)myAgent.messageReceivers2.get(id);
        cfp.setContent("Sending-token");
        cfp.addReceiver(a);
        cfp.setPerformative(ACLMessage.REQUEST);
        myAgent.send(cfp);
    }
    @Override
    protected void onTick() {
        
        if(myAgent.isHasToken())
        {
            myAgent.getReceivers();
            askForId();
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
              //    System.out.println(myAgent.getAID().getLocalName() + " received: " + msg.getContent());
            }

            if(myAgent.isIsRandom())
            {
                Random random = new Random();
               int[] sorted = new int[myAgent.messageReceivers2.size()];
                int i =0;
                for( Object p : myAgent.messageReceivers2.keySet())
                {
                    sorted[i] = (int)p;
                    i++;
                } 
                sendToken(sorted[random.nextInt(sorted.length)]);
            }
            else
            {
                int[] sorted = new int[myAgent.messageReceivers2.size()];
                int i =0;
                for( Object p : myAgent.messageReceivers2.keySet())
                {
                    sorted[i] = (int)p;
                    i++;
                }
                Arrays.sort(sorted);
                boolean sended = false;
                for(int a :sorted)
                {
                    if( a > myAgent.getId()){ sendToken(a); sended = true; break;} 
                }
                if(sended == false) sendToken(sorted[0]);
                myAgent.setHasToken(false);
            }
        }
    }
}
