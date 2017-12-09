
package Gui;


import javax.imageio.ImageIO;
import javax.swing.*;

import Core.Service;
import Utils.FactoryService;
import Utils.Response;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainScreen extends JFrame{
    JFrame frame;
    private Service service= FactoryService.getService();
    private TerminalDialog terminalDialog;
    private ActualParametersDialog actualParametersDialog;
    private Container container;
    private JPanel centralPanel;
    private JPanel connectionPanel;

    /// connectionPanel
    private JLabel fldPortName;
    private JLabel fldConnectionStatus;
    private JLabel fldBoudRate;

    private JButton troubleCodesBtn;

    public MainScreen(){

        super( "OBD Explorer" );
        
        setLayout( new BorderLayout() );
        setUIManagerParameters();
        container= getContentPane();
        container.setLayout( new BorderLayout() );
        frame= this;

        setSize( 800, 600 );
        setResizable( false );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        add( createMenuBar(), BorderLayout.NORTH );
        add( createCenterPanel(), BorderLayout.CENTER );
        add( createNavigationPanel(), BorderLayout.WEST );
        setLocationRelativeTo( null );
        setVisible( true );

    }

    private JPanel createNorthPanel(){
        JPanel panel= new JPanel();

        return panel;
    }

    private JPanel createConnectionPanel(){
        JPanel panel= new JPanel( new GridLayout( 3, 2 ) );
        JLabel lblPortName= new JLabel( "Nazwa portu: " );
        panel.add( lblPortName );
        fldPortName= new JLabel();
        panel.add( fldPortName );
        JLabel lblConnectionStatus= new JLabel( "Status po³¹czenia: " );
        panel.add( lblConnectionStatus );
        fldConnectionStatus= new JLabel();
        panel.add( fldConnectionStatus );
        JLabel lblBaudRate= new JLabel( "Prêdkoœæ transmisji: " );
        panel.add( lblBaudRate );
        fldBoudRate= new JLabel();
        panel.add( fldBoudRate );
        panel.setSize( new Dimension( 270, 120 ) );

        return panel;
    }

    private JPanel createNavigationPanel(){
        JPanel panel= new JPanel( new GridLayout( 6, 1 ) );
        panel.setSize( 70, 750 );

        Image monitorIcon, graphIcon, settingsIcon, troubleCodesIcon, exitIcon, terminalIcon;
        try{
            monitorIcon= ImageIO.read( new File( "Monitor.png" ) );
            JButton monitorBtn= new JButton(
                    new ImageIcon( monitorIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
            graphIcon= ImageIO.read( new File( "Graph.png" ) );
            JButton graphBtn=
                    new JButton( new ImageIcon( graphIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
            settingsIcon= ImageIO.read( new File( "Settings.png" ) );
            JButton settingsBtn= new JButton(
                    new ImageIcon( settingsIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
            troubleCodesIcon= ImageIO.read( new File( "TroubleCodes.png" ) );
            troubleCodesBtn= new JButton(
                    new ImageIcon( troubleCodesIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
            troubleCodesBtn.addActionListener( listener -> {
                new TroubleCodesDialog( this );
            } );
            exitIcon= ImageIO.read( new File( "exit.png" ) );
            JButton exitBtn=
                    new JButton( new ImageIcon( exitIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
            terminalIcon= ImageIO.read( new File( "Terminal.png" ) );
            JButton terminalBtn= new JButton(
                    new ImageIcon( terminalIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );

            panel.add( troubleCodesBtn );
            panel.add( monitorBtn );
            panel.add( graphBtn );
            panel.add( terminalBtn );
            panel.add( settingsBtn );
            panel.add( exitBtn );

        }catch (IOException e){
            System.out.println( "B³¹d podczas dodawania ikon do panelu nawigacji" );
            e.printStackTrace();
        }

        return panel;
    }

    private void setUIManagerParameters(){
        UIManager.put( "Button.background", Color.yellow );
        UIManager.put( "Button.higlight", Color.darkGray );

    }

    private JPanel createCenterPanel(){
        centralPanel= new JPanel();
        centralPanel.setLayout( null );
        connectionPanel= createConnectionPanel();
        connectionPanel.setLocation( 460, 0 );
        centralPanel.add( connectionPanel );

        return centralPanel;
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar= new JMenuBar();
        JMenu connectionMenu= new JMenu( "Po³¹czenia" );
        JMenu serviceMenu= new JMenu( "Us³ugi" );
        JMenu settingsMenu= new JMenu( "Ustawienia" );
        JMenu helpMenu= new JMenu( "Pomoc" );
        UIManager.put( "MenuItem.font", new Font( Font.SERIF, Font.BOLD, 14 ) );
        UIManager.put( "MenuBar.font", new Font( Font.SERIF, Font.BOLD, 16 ) );
        SwingUtilities.updateComponentTreeUI( frame );
        connectionMenu.add( createConnectMenuItem() );
        connectionMenu.add( createCloseConnectionMenuItem() );

        serviceMenu.add( createTerminalMenuItem() );
        serviceMenu.add( createActualParametersMenuItem() );
        serviceMenu.add( createGraphsMenuItem() );
        menuBar.add( connectionMenu );
        menuBar.add( serviceMenu );
        menuBar.add( settingsMenu );
        menuBar.add( helpMenu );

        return menuBar;
    }

    private JMenuItem createCloseConnectionMenuItem(){
        JMenuItem menuItem= new JMenuItem( "Zakoñcz po³¹czenie" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                Response result = service.closePort();
                if(result.hasErrors()) {
                    JOptionPane.showMessageDialog( null, result.getErrorAsString(), "OBDExplorer communicate",
                            JOptionPane.WARNING_MESSAGE );
                }

            }
        } );
        return menuItem;
    }

    private JMenuItem createConnectMenuItem(){
        JMenuItem menuItem= new JMenuItem( "Po³¹cz" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                new ConnectDialog( frame );
            }
        } );

        return menuItem;
    }

    private JMenuItem createGraphsMenuItem(){
        JMenuItem menuItem= new JMenuItem( "Wykresy" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                try{
                    new GraphDialog( frame );
                }catch (InterruptedException e1){
                    e1.printStackTrace();
                }
            }
        } );

        return menuItem;

    }

    private JMenuItem createTerminalMenuItem(){
        JMenuItem menuItem= new JMenuItem( "Terminal" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                terminalDialog= new TerminalDialog( frame );
            }
        } );

        return menuItem;
    }

    private JMenuItem createActualParametersMenuItem(){
        JMenuItem menuItem= new JMenuItem( "Bierz¹ce parametry" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                actualParametersDialog= new ActualParametersDialog( frame );
            }
        } );

        return menuItem;
    }

    public Service getService(){
        return service;
    }

    public TerminalDialog getTerminalDialog(){
        return terminalDialog;
    }

    public void setConnectionPanelParameters( String portName, String boudRate, String status ){

        if( portName != null ) fldPortName.setText( portName );
        if( boudRate != null ) fldBoudRate.setText( boudRate);
        if( status != null ) fldConnectionStatus.setText( status );
    }

}
