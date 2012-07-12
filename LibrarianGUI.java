import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jason.architecture.*;
import jason.asSemantics.ActionExec;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;

import javax.swing.*;

/** example of agent architecture's functions overriding */
public class LibrarianGUI extends AgArch {

    JTextArea jt;
    JTextField jf;
    JFrame    f;
    JButton search;
    HashMap<String, String> results;

    int auctionId = 0;

    public LibrarianGUI() {
        jt = new JTextArea(10, 30);
        jf = new JTextField(10);
        results = new HashMap<String, String>();
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
            
            for(Entry<String, String> entry : results.entrySet()) {
            	jt.append("-------------------------------\n");
            	jt.append("agent: "+entry.getKey()+"\n");
            	jt.append("result: "+entry.getValue()+"\n");
            	jt.append("-------------------------------\n");
            }
            
            action.setResult(true);
            feedback.add(action);
            
            search.setEnabled(true); // enable GUI button
        } else if (action.getActionTerm().getFunctor().startsWith("get_index")) {
        	String agent = "" + action.getActionTerm().getTerm(0);
        	String result = "" + action.getActionTerm().getTerm(2);
        	
        	jt.append("get index for "+agent+"\n");
        	
        	results.put(agent, result);
        	
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
