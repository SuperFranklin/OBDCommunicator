
package Gui;


import gnu.io.*;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.w3c.dom.css.Counter;

import Core.Message;
import Core.Service;
import Utils.Response;

public class ConnectDialog extends JDialog{
    private MainScreen parent;
    // private JTextField fldPortName= new JTextField( 10 );
    private JComboBox<String> portsComboBox = initComboBoxParameters();
    private List<CommPortIdentifier> availablePorts;
    private JLabel lblPornName = new JLabel( "Nazwa portu" );
    private JLabel lblBoundRate = new JLabel( "Pr�dko�� transmisji" );
    private Service service;
    private JButton connectBtn = createConnectBtn();
    private Container contentPane;
    private SpringLayout layout;

    public ConnectDialog( JFrame frame ){
        super( frame );
        parent = ( MainScreen ) frame;
        service = parent.getService();
        setLocationRelativeTo( null );
        setLocation( 100, 100 );
        setSize( 250, 250 );
        prepareGui();

        getAvaiableSerialPorts();
        setVisible( true );
    }

    private void prepareGui(){
        contentPane = getContentPane();
        layout = new SpringLayout();
        setLayoutConstraints();

        contentPane.setLayout( layout );
        contentPane.setSize( 100, 100 );
        contentPane.add( lblPornName );

        contentPane.add( portsComboBox );
        contentPane.add( connectBtn );

    }

    private void setLayoutConstraints(){
        layout.putConstraint( SpringLayout.WEST, lblPornName, 5, SpringLayout.WEST, contentPane );
        layout.putConstraint( SpringLayout.NORTH, lblPornName, 5, SpringLayout.NORTH, contentPane );
        layout.putConstraint( SpringLayout.EAST, portsComboBox, 0, SpringLayout.EAST, contentPane );
        layout.putConstraint( SpringLayout.NORTH, portsComboBox, 5, SpringLayout.NORTH, contentPane );
        layout.putConstraint( SpringLayout.WEST, connectBtn, 80, SpringLayout.WEST, contentPane );
        layout.putConstraint( SpringLayout.NORTH, connectBtn, 35, SpringLayout.NORTH, contentPane );

    }

    private JComboBox<String> initComboBoxParameters(){
        availablePorts = getAvaiableSerialPorts();
        JComboBox<String> comboBox = new JComboBox<String>( createPortsComboBoxOptions() );

        return comboBox;
    }

    private String[] createPortsComboBoxOptions(){
        String[] options = new String[ availablePorts.size() ];
        Integer i = 0;
        for(CommPortIdentifier c : availablePorts){
            StringBuilder sb = new StringBuilder();
            sb.append( c.getName() );
            String currentOwner = c.getCurrentOwner();
            if(currentOwner != null){
                sb.append( "( " );
                sb.append( currentOwner );
                sb.append( " )" );
            }
            options[ i++ ] = (sb.toString());
        }

        return options;
    }

    private List<CommPortIdentifier> getAvaiableSerialPorts(){
        List<CommPortIdentifier> list = new ArrayList<>();
        CommPortIdentifier com;

        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()){
            com = ( CommPortIdentifier ) ports.nextElement();
            if(com.getPortType() == CommPortIdentifier.PORT_SERIAL){
                list.add( com );
            }
        }
        return list;
    }

    private JButton createConnectBtn(){
        JButton button = new JButton( Message.CONNECT );
        button.addActionListener( createConnectBtnAL() );
        return button;
    }

    private ActionListener createConnectBtnAL(){
        ActionListener listener = new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                String selectedItem = portsComboBox.getSelectedItem().toString();
                Response response = service.connect( selectedItem );
                if(!response.hasErrors()){
                    JOptionPane.showMessageDialog( parent, Message.CONNECTION_INITIALIZED_ON_PORT + selectedItem );
                    parent.setConnectionPanelParameters( selectedItem, Integer.toString( Core.Parameters.boudRate ),
                            Message.CONNECTED );
                    dispose();
                }else{
                    JOptionPane.showMessageDialog( parent, response.getErrorAsString(), Message.CONNECTION_ERROR,
                            JOptionPane.WARNING_MESSAGE );
                }
            }
        };
        return listener;
    }
}
