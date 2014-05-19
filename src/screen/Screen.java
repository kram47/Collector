package screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import processador.Processador;

/**
 *
 * @author Marc
 */
public class Screen extends JFrame {
    
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */  
    
    private JPanel          _container = new JPanel();    
    private JTabbedPane     _tabs = new JTabbedPane(SwingConstants.TOP);
    private JPanel          _tab1 = new JPanel();
    private JPanel          _tab2 = new JPanel();
    private JPanel          _tab3 = new JPanel();
    
    private JTextField      _procSearchTextField = new JTextField("");
    private JLabel          _procSearchLabel = new JLabel("Busca");
    private JButton         _procSearchButton = new JButton("Buscar");
    private JPanel          _procContent = new JPanel();
    

    
    

    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */        
  
    public Screen()
    {
        this.setTitle("Google");
        this.setSize(830, 670);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        _container.setBackground(Color.getHSBColor(180, (float)0, (float)0.9));
        _container.setLayout(new BorderLayout());
        
        createTab1();
        createTab2();
        createTab3();
        //_tabs.setOpaque(true);
        _container.add(_tabs);
                      
        this.getContentPane().add(_container);
        this.setVisible(true);
    }
    

    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */     

    
    private void    createTab1()
    {
        _tab1.setPreferredSize(new Dimension(800, 600));
        _tabs.addTab("Pesquisador", _tab1);
        
        _procSearchTextField.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        _procSearchTextField.setPreferredSize(new Dimension(400, 30));
        _procSearchTextField.setForeground(Color.GRAY);
              
        _procSearchButton.addActionListener(new ProcSearchButtonListener());
        
        JPanel top = new JPanel();
        top.add(_procSearchLabel);
        top.add(_procSearchTextField);
        top.add(_procSearchButton);
        
        
        
        JPanel test = new JPanel();
        test.add(new JLabel("Coucou les amis c'est un test"));
        test.setSize(800, 200);

        JPanel test2 = new JPanel();
        test2.add(new JLabel("Coucou les amis c'est un test"));
        test2.setSize(800, 200);

        _procContent.setLayout(new GridLayout(2,1));
        _procContent.add(test);
        _procContent.add(test2);
        
        _tab1.setLayout(new BorderLayout());
        _tab1.add(top, BorderLayout.NORTH);
        _tab1.add(_procContent, BorderLayout.CENTER);
        
    }
    
    private void    createTab2()
    {
        _tab2.add(new JLabel("This my indexador"));
        _tabs.addTab("Indexador", _tab2);
    }
    
    private void    createTab3()
    {
        _tab3.add(new JLabel("This my Collector"));
        _tabs.addTab("Coletor", _tab3);
    }
    
    
    class ProcSearchButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent event)
        {			
            String query = _procSearchTextField.getText();
            System.out.println("Consulta : " + query);
            
            Processador proc = new Processador(query);
            try 
            {
                proc.run(5);
            } 
            catch (SQLException ex) { Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex); }
        }
    }
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