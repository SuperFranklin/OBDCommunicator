
package Gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.bind.Marshaller.Listener;

import Commands.MILCommand;
import Core.Message;
import Core.Service;
import Utils.FactoryService;
import Utils.Response;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainScreen extends JFrame{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Dimension CONNECTION_PANEL_DIMENSION = new Dimension( 270, 120 );
    private static final Dimension NAVIGATION_PANEL_DIMENSION = new Dimension( 70, 750 );
    private static final String LOGO_FILEPATH ="Logo.png";

    JFrame frame;

    private Service service = FactoryService.getService();
    private TerminalDialog terminalDialog;
    private ActualParametersDialog actualParametersDialog;
    private Container container;
    private JPanel centralPanel;
    private Image logo;


    /// connectionPanel
    private JPanel connectionPanel;
    private JLabel fldPortName;
    private JLabel fldConnectionStatus;
    private JLabel fldBoudRate;
    
    /// TitlePanel
    private JPanel titlePanel;
    
    // MIL PANEL
    private JPanel milPanel;
    private JTextField fldMILLamp;
    private JTextField fldDetectedDTCNumber;

    // NavigationPanel
    private JPanel navigationPanel;
    private Image monitorIcon , graphIcon , settingsIcon , troubleCodesIcon , exitIcon , terminalIcon;
    private JButton monitorBtn;
    private JButton troubleCodesBtn;
    private JButton graphBtn;
    private JButton settingsBtn;
    private JButton terminalBtn;
    private JButton exitBtn;

    public MainScreen(){
        super( "OBD Explorer" );
        frame = this;

        initGui();
        addComponentsFrame();

        setVisible( true );
    }

    private void initGui(){
        setLayout( new BorderLayout() );
        setUIManagerParameters();
        container = getContentPane();
        container.setLayout( new BorderLayout() );
        setSize( WIDTH, HEIGHT );
        setResizable( false );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        setLocationRelativeTo( null );
        addLogo();

    }

    private void addComponentsFrame(){
        add( createMenuBar(), BorderLayout.NORTH );
        add( createCenterPanel(), BorderLayout.CENTER );
        add( createNavigationPanel(), BorderLayout.WEST );

    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        JLabel titleLabel = null;
        Image titleImage;
        try{
            titleImage = ImageIO.read( new File( "TITLE.png" ) );
            titleLabel = new JLabel(new ImageIcon( titleImage ));
        }catch (IOException e){
            //TODO wypisac problem z ladowanie ikony
            e.printStackTrace();
        }
        
        panel.add( titleLabel );
        panel.setSize( 370, 110 );
        return panel;
    }
    
    private JPanel createMILPanel() {
        JPanel panel = new JPanel();
        panel.setLayout( new GridLayout( 3, 2 ) );
        panel.setSize( 300, 120 );
        /*JLabel milLampLabel = null;
        try{
            Image lamp = ImageIO.read( new File( "MILLAMP.png" ) );
            milLampLabel = new JLabel( new ImageIcon( lamp ) );
            milLampLabel.setSize( 64, 64 );
            milLampLabel.setLocation( 0, 0 );
            //panel.add( milLampLabel );
            
            
        }catch (IOException e){
            e.printStackTrace();
        }*/
        JLabel lblMILLamp = new JLabel( "Lampka MIL" );
        JLabel lblDetectedDTCNumber = new JLabel(" Wykryte kody b³êdów");
        JLabel lblFreezedFrames = new JLabel(" Freezed Frames");
        fldMILLamp = new JTextField( "NO DATA" );
        fldDetectedDTCNumber = new JTextField( "NO DATA" );
        JTextField fldFreezedFrames = new JTextField( "NO DATA" );
        panel.add( lblMILLamp );
        panel.add( fldMILLamp);
        panel.add( lblDetectedDTCNumber );
        panel.add( fldDetectedDTCNumber );
        panel.add( lblFreezedFrames );
        fldFreezedFrames.setEditable( false );
        fldMILLamp.setEditable( false );
        fldDetectedDTCNumber.setEditable( false );
        panel.add( fldFreezedFrames );
        return panel;
    }
    private JPanel createConnectionPanel(){
        JPanel panel = new JPanel( new GridLayout( 3, 2 ) );
        fldPortName = new JLabel();
        fldConnectionStatus = new JLabel();
        fldBoudRate = new JLabel();
        JLabel lblPortName = new JLabel( Message.PORT_NAME );
        JLabel lblConnectionStatus = new JLabel( Message.CONNECTION_STATUS );
        JLabel lblBaudRate = new JLabel( Message.BAUD_RATE );

        fldConnectionStatus.setText( Message.DISCONNECTED );

        panel.add( lblPortName );
        panel.add( fldPortName );
        panel.add( lblConnectionStatus );
        panel.add( fldConnectionStatus );
        panel.add( lblBaudRate );
        panel.add( fldBoudRate );
        panel.setSize( CONNECTION_PANEL_DIMENSION );

        return panel;
    }

    private JPanel createNavigationPanel(){
        navigationPanel = new JPanel( new GridLayout( 6, 1 ) );
        navigationPanel.setSize( NAVIGATION_PANEL_DIMENSION );
        loadNavigationPanelImages();
        initNavigationPanelButtons();
        addActionListenersToNavigationPanel();
        addComponentsToNavigationPanel();

        return navigationPanel;
    }

    private void loadNavigationPanelImages(){
        try{
            monitorIcon = ImageIO.read( new File( "Monitor.png" ) );
            graphIcon = ImageIO.read( new File( "Graph.png" ) );
            settingsIcon = ImageIO.read( new File( "Settings.png" ) );
            troubleCodesIcon = ImageIO.read( new File( "TroubleCodes.png" ) );
            terminalIcon = ImageIO.read( new File( "Terminal.png" ) );
            exitIcon = ImageIO.read( new File( "exit.png" ) );
        }catch (IOException e){
            JOptionPane.showMessageDialog( this, "B³¹d podczas dodawania ikon do panelu nawigacji" );
            e.printStackTrace();
        }
    }

    private void initNavigationPanelButtons(){
        monitorBtn =
                new JButton( new ImageIcon( monitorIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
        graphBtn = new JButton( new ImageIcon( graphIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
        settingsBtn =
                new JButton( new ImageIcon( settingsIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
        troubleCodesBtn = new JButton(
                new ImageIcon( troubleCodesIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
        exitBtn = new JButton( new ImageIcon( exitIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
        terminalBtn =
                new JButton( new ImageIcon( terminalIcon.getScaledInstance( 84, 84, java.awt.Image.SCALE_SMOOTH ) ) );
    }

    // to Navigation Panel
    private void addActionListenersToNavigationPanel(){
        
        monitorBtn.addActionListener( listener ->{
            new ActualParametersDialog( frame );
        });
        troubleCodesBtn.addActionListener( listener->{
            new TroubleCodesDialog( this );
        } );
        graphBtn.addActionListener( listener ->{
            try{
                new GraphDialog( frame );
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        troubleCodesBtn.addActionListener( listener ->{
            new TroubleCodesDialog( frame );
        });
        exitBtn.addActionListener( listener -> {
            dispose();
        });
        terminalBtn.addActionListener( listener -> {
            new TerminalDialog( frame );
        });
        
    }

    // navigationPanel
    private void addComponentsToNavigationPanel(){
        navigationPanel.add( troubleCodesBtn );
        navigationPanel.add( monitorBtn );
        navigationPanel.add( graphBtn );
        navigationPanel.add( terminalBtn );
        navigationPanel.add( settingsBtn );
        navigationPanel.add( exitBtn );
    }

    private void setUIManagerParameters(){
        UIManager.put( "Button.background", Color.yellow );
        UIManager.put( "Button.higlight", Color.darkGray );
        UIManager.put( "MenuItem.font", new Font( Font.SERIF, Font.BOLD, 14 ) );
        UIManager.put( "MenuBar.font", new Font( Font.SERIF, Font.BOLD, 16 ) );
    }

    private JPanel createCenterPanel(){
        centralPanel = new JPanel();
        centralPanel.setLayout( null );
        connectionPanel = createConnectionPanel();
        connectionPanel.setLocation( 460, 0 );
        centralPanel.add( connectionPanel );
        titlePanel = createTitlePanel();
        titlePanel.setLocation(25,5);
        centralPanel.add( titlePanel );
        milPanel = createMILPanel();
        milPanel.setLocation(20,120);
        centralPanel.add( milPanel);
        JPanel downloadInfoPanel= createDownloadMILInfoPanel();
        downloadInfoPanel.setLocation( 90, 250 );
        centralPanel.add(downloadInfoPanel);
        return centralPanel;
    }
    
    private JPanel createDownloadMILInfoPanel(){
        JPanel panel = new JPanel();
        panel.setSize( 280, 90 );
        JButton downloadBtn = new JButton( "Pobierz dane" );
        downloadBtn.addActionListener( listener ->{
            MILCommand command = new MILCommand();
            if( command.milIsLighted()) {
                fldMILLamp.setText( "TRUE" );
            }else {
                fldMILLamp.setText( "FALSE" );
            }
            fldDetectedDTCNumber.setText( Integer.toString( command.getNumberOfDetectedError() ));
        });
        downloadBtn.setSize( 280, 90 );
        downloadBtn.setLocation( 5, 5 );
        panel.add( downloadBtn );
        return panel;
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu connectionMenu = new JMenu( "Po³¹czenia" );
        JMenu serviceMenu = new JMenu( "Us³ugi" );
        JMenu settingsMenu = new JMenu( "Ustawienia" );
        JMenu helpMenu = new JMenu( "Pomoc" );

        SwingUtilities.updateComponentTreeUI( this );
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
        JMenuItem menuItem = new JMenuItem( "Zakoñcz po³¹czenie" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                Response result = service.closePort();
                if(result.hasErrors()){
                    JOptionPane.showMessageDialog( null, result.getErrorAsString(), "OBDExplorer communicate",
                            JOptionPane.WARNING_MESSAGE );
                }

            }
        } );
        return menuItem;
    }

    private JMenuItem createConnectMenuItem(){
        JMenuItem menuItem = new JMenuItem( "Po³¹cz" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                new ConnectDialog( frame );
            }
        } );

        return menuItem;
    }

    private JMenuItem createGraphsMenuItem(){
        JMenuItem menuItem = new JMenuItem( "Wykresy" );
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
        JMenuItem menuItem = new JMenuItem( "Terminal" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                terminalDialog = new TerminalDialog( frame );
            }
        } );

        return menuItem;
    }

    private JMenuItem createActualParametersMenuItem(){
        JMenuItem menuItem = new JMenuItem( "Bierz¹ce parametry" );
        menuItem.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                actualParametersDialog = new ActualParametersDialog( frame );
            }
        } );

        return menuItem;
    }

    private void addLogo(){

        try{
            logo = ImageIO.read( new File( LOGO_FILEPATH ) );
            setIconImage( logo );
        }catch (IOException e){
            JOptionPane.showMessageDialog( this, Message.EMPTY_APPLICATION_LOGO_FILE );
        }

    }

    public Service getService(){
        return service;
    }

    public TerminalDialog getTerminalDialog(){
        return terminalDialog;
    }

    public void setConnectionPanelParameters( String portName, String boudRate, String status ){

        if(portName != null) fldPortName.setText( portName );
        if(boudRate != null) fldBoudRate.setText( boudRate );
        if(status != null) fldConnectionStatus.setText( status );
    }

    public void setService( Service service ){
        this.service = service;
    }

}
