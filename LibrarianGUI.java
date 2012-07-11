import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jason.architecture.*;
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
    public void stop() {
        f.dispose();
        super.stop();
    }
}
