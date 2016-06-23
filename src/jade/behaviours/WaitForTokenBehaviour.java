/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jade.behaviours;

import jade.agents.TestAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;

/**
 *
 * @author Maksiu
 */
public class WaitForTokenBehaviour extends CyclicBehaviour {
    TestAgent myAgent;
    
    public WaitForTokenBehaviour(TestAgent a) {
        super(a);
        this.myAgent = a;
    }
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg == null) {
            //myAgent.setArguments(args);
            block();
            
            
        }
        else if( msg.getPerformative() == ACLMessage.INFORM)
        {
            System.out.println(myAgent.getAID().getLocalName() + " received id: " + msg.getContent() + " from: " + msg.getSender().getLocalName());
            myAgent.messageReceivers2.put( Integer.parseInt((String)msg.getContent()),msg.getSender());
        } else if(msg.getPerformative() == ACLMessage.REQUEST)
        {
                System.out.println(myAgent.getAID().getLocalName() + " received token from: " + msg.getSender().getLocalName());
                myAgent.setHasToken(true);
        }
    }
}
