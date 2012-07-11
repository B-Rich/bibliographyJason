import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

    int auctionId = 0;

    public LibrarianGUI() {
        jt = new JTextArea(10, 30);
        jf = new JTextField(10);
        search = new JButton("Start a new search");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String keyword = jf.getText();
            	jt.append("Search this keyword: "+keyword);
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
        if (action.getActionTerm().getFunctor().startsWith("show_winner")) {
            //jt.append("Winner of auction  " + action.getActionTerm().getTerm(0));
            //jt.append(" is " + action.getActionTerm().getTerm(1) + "\n");
            //action.setResult(true);
        	//feedback.add(action);
            
        	//search.setEnabled(true); // enable GUI button
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
