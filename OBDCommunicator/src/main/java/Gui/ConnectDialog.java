
package Gui;


import gnu.io.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
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
import Core.ServiceImpl;
import Utils.Response;

public class ConnectDialog extends JDialog{

    private static final Point LOCATION = new Point( 300, 300 );
    private static final Dimension SIZE = new Dimension( 400, 250 );

    private MainScreen parent;
    private JComboBox<String> portsComboBox = initComboBoxParameters();
    private List<CommPortIdentifier> availablePorts;
    private JLabel lblPornName = new JLabel( Message.PORT_NAME );
    private JLabel lblBaudRate = new JLabel( "Baud Rate" );
    private JLabel lblProtocol = new JLabel( "Protokó³" );
    private JComboBox<String> protocolComboBox = createProtocolComboBox();
    private JComboBox<Integer> baudRateComboBox = initBaudRateComboBox();
    private ServiceImpl service;
    private JButton connectBtn = createConnectBtn();

    public ConnectDialog( JFrame frame ){
        super( frame );
        parent = ( MainScreen ) frame;
        service = parent.getService();

        setLocationRelativeTo( null );
        setLocation( LOCATION );
        setSize( SIZE );
        prepareGui();

        getAvaiableSerialPorts();
        setVisible( true );
    }

    private void prepareGui(){
        setLayout( null );
        lblPornName.setSize( 130, 35 );
        lblPornName.setLocation( 15, 15 );
        portsComboBox.setSize( 100, 35 );
        portsComboBox.setLocation( 95, 15 );
        lblBaudRate.setSize( 90, 35 );
        lblBaudRate.setLocation( 15, 60 );
        baudRateComboBox.setSize( 100, 35 );
        baudRateComboBox.setLocation( 95, 60 );
        lblProtocol.setSize( 95, 35 );
        lblProtocol.setLocation(15, 95);
        protocolComboBox.setSize( 255, 35 );
        protocolComboBox.setLocation( 95, 95 );
        connectBtn.setSize( 90, 35 );
        connectBtn.setLocation( 15, 150 );
        add( protocolComboBox);
        add( lblProtocol);
        add( baudRateComboBox );
        add( lblBaudRate );
        add( lblPornName );
        add( portsComboBox );
        add( connectBtn );
    }

    private JComboBox<String> initComboBoxParameters(){
        availablePorts = getAvaiableSerialPorts();
        JComboBox<String> comboBox = new JComboBox<String>( createPortsComboBoxOptions() );

        return comboBox;
    }

    private JComboBox<Integer> initBaudRateComboBox(){
        JComboBox<Integer> comboBox = new JComboBox<>( createBoudOptions() );

        return comboBox;
    }

    private Integer[] createBoudOptions(){
        Integer[] options = new Integer[ 2 ];
        options[ 0 ] = new Integer( 9600 );
        options[ 1 ] = new Integer( 38400 );
        return options;
    }
    
    private JComboBox<String> createProtocolComboBox(){
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem( "Automatic" );
        comboBox.addItem( "SAE J1850 PWM (41.6 kbit/s))" );
        comboBox.addItem( "SAE J1850 VPW (10.4 kbit/s) " );
        comboBox.addItem( "ISO 9141-2 (5 baud init, 10.4 kbit/s)" );
        comboBox.addItem( "ISO 14230-4 KWP (5 baud init, 10.4 kbit/s)" );
        comboBox.addItem( "ISO 14230-4 KWP (fast init, 10.4 kbit/s) " );
        comboBox.addItem( "ISO 15765-4 CAN (11 bit ID, 500 kbit/s) " );
        comboBox.addItem( "ISO 15765-4 CAN (29 bit ID, 500 kbit/s) " );
        comboBox.addItem( "ISO 15765-4 CAN (11 bit ID, 250 kbit/s) " );
        comboBox.addItem( "ISO 15765-4 CAN (29 bit ID, 250 kbit/s) " );

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
                Integer protocolNr = protocolComboBox.getSelectedIndex();
                Integer baudRate = baudRateComboBox.getSelectedIndex();
                Response response = service.connect( selectedItem, protocolNr, baudRate );
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
