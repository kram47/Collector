package screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Marc
 */
public class Screen extends JFrame {
    
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */  
    
    private JPanel container = new JPanel();    
    private JTextField jtf = new JTextField("Pesquisa");
    private JLabel label = new JLabel("Busca");
    

    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */        

    public Screen()
    {
        this.setTitle("Google");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        container.setBackground(Color.getHSBColor(180, (float)0, (float)0.9));
        container.setLayout(new BorderLayout());
        
        JPanel top = new JPanel();
        Font police = new Font("Sans Serif", Font.PLAIN, 14);
        jtf.setFont(police);
        jtf.setPreferredSize(new Dimension(300, 30));
        jtf.setForeground(Color.LIGHT_GRAY);
        
        top.add(label);
        top.add(jtf);
        container.add(top, BorderLayout.NORTH);
        this.setContentPane(container);
        this.setVisible(true);  
    }


    /* ---------------------------------------------------------------- */
    /* --------------------- GETTER-SETTER ---------------------------- */        




    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */     




    /* ---------------------------------------------------------------- */
    /* ------------------------ OUTPUT -------------------------------- */   

    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        
        ret.append("Screen : {}");
        System.out.println(ret.toString());
        
        return ret.toString();
    }


}
