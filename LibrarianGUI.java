import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jason.architecture.*;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.ObjectTerm;
import jason.asSyntax.ObjectTermImpl;
import jason.asSyntax.VarTerm;

import javax.swing.*;

/** example of agent architecture's functions overriding */
public class LibrarianGUI extends AgArch {

    JTextArea jt;
    JTextField jf;
    JFrame    f;
    JButton search;
    HashMap<String, Object> results;
    HashMap<String, Double> papers;
    HashMap<String, ArrayList<String>> citationsFromScholar;
    int numberAgents;

    public LibrarianGUI() {
        jt = new JTextArea(10, 30);
        jf = new JTextField(10);
        
        results = new HashMap<String, Object>();
        papers = new HashMap<String, Double>();
        numberAgents = 0;
        
        jf.setText("Giuseppe Vizzari");
        
        search = new JButton("Start a new search");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String keyword = jf.getText();
            	jt.append("Search this keyword: "+keyword+"\n");
                Literal goal = ASSyntax.createLiteral("start_search", ASSyntax.createString(keyword));
                getTS().getC().addAchvGoal(goal, null);
                search.setEnabled(false);
            }
        });
        
        f = new JFrame("Librarian agent");
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(BorderLayout.NORTH, new JScrollPane(jt));
        f.getContentPane().add(BorderLayout.CENTER, new JScrollPane(jf));
        f.getContentPane().add(BorderLayout.SOUTH, search);
        f.pack();
        f.setVisible(true);
    }
    
    @Override
    public void act(ActionExec action, List<ActionExec> feedback) {
        if (action.getActionTerm().getFunctor().startsWith("show_index")) {
            jt.append("show index\n");
            
            for(Entry<String, Object> entry : results.entrySet()) {
            	jt.append("-------------------------------\n");
            	jt.append("agent: "+entry.getKey()+"\n");
            	jt.append("result: "+entry.getValue()+"\n");
            	jt.append("-------------------------------\n");
            }
            
            int m = papers.size();
            int n = numberAgents;
            double k;
            double pure_index = 0.0;
            
            for(Entry<String, Double> entry : papers.entrySet()) {
            	k = entry.getValue().doubleValue();
            	jt.append("paper: "+entry.getKey()+", value: "+(k/(m*n))+"\n");
            	pure_index += (k/(m*n));
            }
            
            jt.append("pure_index: "+pure_index);
            
            action.setResult(true);
            feedback.add(action);
            
            search.setEnabled(true); // enable GUI button
        } else if (action.getActionTerm().getFunctor().startsWith("get_index")) {
        	String agent = ((VarTerm) action.getActionTerm().getTerm(0)).toStringAsTerm();
        	Object result = ((ObjectTerm) action.getActionTerm().getTerm(2)).getObject();
        	
        	if(agent.equals("scholar")) {
        		citationsFromScholar = (HashMap<String, ArrayList<String>>) ((ObjectTerm) action.getActionTerm().getTerm(3)).getObject();
        	}
        	
        	if(result instanceof ArrayList<?>) {
        		ArrayList<String> papersFromAgent = (ArrayList<String>) result;
        		
	        	jt.append("get index for "+agent+"\n");
	        	
	        	numberAgents += 1;
	        	
	        	results.put(agent, result);
	        	
	        	for(String paper : papersFromAgent) {
	        		paper = paper.trim();
	        		Double value = papers.get(paper);
	        		
	        		if(value == null) {
	        			value = 0.0;
	        		}
	        		
	        		papers.put(paper, value+1.0);
	        	}
	        	
	        	action.setResult(true);
        	} else {
        		action.setFailureReason(Literal.LFalse, "result is not ArrayList");
        		action.setResult(true);
        	}
        	
        	feedback.add(action);
        } else if(action.getActionTerm().getFunctor().startsWith("show_filtered_citations")) {
    		action.setResult(true);
            feedback.add(action);
        } else {
        	super.act(action,feedback); // send the action to the environment to be performed.
        }
    }

    @Override
    public void stop() {
        f.dispose();
        super.stop();
    }
}
