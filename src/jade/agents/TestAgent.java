/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jade.agents;

import jade.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maksio
 */
public class TestAgent extends jade.core.Agent{
    
    private boolean isRandom = false;
    private boolean hasToken = false;
    private int id = 0;

    /**
     *
     */
    public Map messageReceivers2 = new HashMap();
    public List<AID> messageReceivers;
    @Override
    public void setup()
    {
       
        Object[] args = getArguments();
        try{
            if(args.length >= 3)
            {
                if(((String)args[1]).equals("1")) setHasToken(true);
                if(((String)args[2]).equals("1")) setIsRandom(true);
                setId(Integer.parseInt((String)args[0]));
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        getAID().addUserDefinedSlot("id", Integer.toString(id));
         this.registerService();
        System.out.println("Agent: " + getLocalName() + " token: " + isHasToken() + " random: " + isIsRandom() + " id: " + getId());
        getReceivers();
        informAboutExistance();
        addBehaviour(new SendTokenBehaviour(this, 2500));
        addBehaviour(new WaitForTokenBehaviour(this));
    }
    
    private void informAboutExistance()
    {
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for(AID a : messageReceivers){
         cfp.addReceiver(a);
        }
        cfp.setContent(Integer.toString(id));
        cfp.setPerformative(ACLMessage.INFORM);
        send(cfp);
    }
    public void getReceivers() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Message-Receiver");

        DFAgentDescription template = new DFAgentDescription();
        template.addServices(sd);
        

        try {
            DFAgentDescription[] result = DFService.search(this, template);
            messageReceivers = new ArrayList<AID>();
            for (int i = 0; i < result.length; ++i) {
                if(!(result[i].getName().getLocalName().equals(this.getLocalName()))) 
                {
                   //System.out.println(result[i].getName().getAllUserDefinedSlot().getProperty("asdasd"));
                    //messageReceivers2.put(Integer.parseInt(result[i].getName().getAllUserDefinedSlot().get("id").toString()),result[i].getName() );
                    messageReceivers.add(result[i].getName());
                }            
                }
                //System.out.println(result[i].getName().;
            }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
    private void registerService() {
        Object[] args = getArguments();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Message-Receiver");
        sd.setName((String)args[0]);

        DFAgentDescription dfd = new DFAgentDescription();
        
      //  d.addUserDefinedSlot("id", Integer.toString(id));
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            dfd.setName(getAID());
            dfd = DFService.register(this, dfd);
           /* System.out.println(dfd.getName().getAllUserDefinedSlot().getProperty("id"));
            System.out.println(getAID().getAllUserDefinedSlot().getProperty("id"));*/
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
    
    
    
    
    /**
     * @return the isRandom
     */
    public boolean isIsRandom() {
        return isRandom;
    }

    /**
     * @param isRandom the isRandom to set
     */
    public void setIsRandom(boolean isRandom) {
        this.isRandom = isRandom;
    }

    /**
     * @return the hasToken
     */
    public boolean isHasToken() {
        return hasToken;
    }

    /**
     * @param hasToken the hasToken to set
     */
    public void setHasToken(boolean hasToken) {
        this.hasToken = hasToken;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
}
