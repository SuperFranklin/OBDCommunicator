
package Gui;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import Commands.RPMCommand;
import Core.Service;
import Utils.Response;
import Utils.Utils;

public class TerminalDialog extends JDialog{

    public MainScreen parent;
    private Service service;
    private JTextField fldMessage= new JTextField( 10 );
    private JLabel lblResponse= new JLabel( "Odpowiedzi" );
    private JTextArea responseTxtArea= new JTextArea( 12, 20 );
    private JScrollPane responseScrollPane= new JScrollPane( responseTxtArea );

    private JButton sendBtn= createSendBtn();
    private Container contentPane;
    private SpringLayout layout;

    public TerminalDialog( JFrame frame ){
        super( frame );
        parent= ( MainScreen ) frame;
        service= parent.getService();
        setLocation( 100, 200 );
        setSize( 260, 310 );

        prepareGui();
        setVisible( true );
    }

    private void prepareGui(){
        contentPane= getContentPane();
        layout= new SpringLayout();
        setLayoutConstraints();
        contentPane.add( fldMessage );
        contentPane.add( sendBtn );
        contentPane.add( responseScrollPane );
        contentPane.setLayout( layout );
        contentPane.setSize( 300, 400 );
    }

    private JButton createSendBtn(){
        JButton button= new JButton( "Wy�lij kod" );
        button.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                Response response= service.sendAndGetResponse( fldMessage.getText() );
                String txt= Utils.getStringFromBytes( response.getBytes() );
                setText( txt );
            }
        } );

        return button;
    }

    private void setLayoutConstraints(){
        layout.putConstraint( SpringLayout.WEST, fldMessage, 10, SpringLayout.WEST, contentPane );
        layout.putConstraint( SpringLayout.NORTH, fldMessage, 5, SpringLayout.NORTH, contentPane );
        layout.putConstraint( SpringLayout.EAST, sendBtn, 100, SpringLayout.EAST, fldMessage );
        layout.putConstraint( SpringLayout.NORTH, sendBtn, 5, SpringLayout.NORTH, contentPane );
        layout.putConstraint( SpringLayout.WEST, responseScrollPane, 10, SpringLayout.WEST, contentPane );
        layout.putConstraint( SpringLayout.NORTH, responseScrollPane, 35, SpringLayout.NORTH, contentPane );
    }

    public void setText( String txt ){
        responseTxtArea.append( txt );
        responseTxtArea.append( "\n" );
    }
}
