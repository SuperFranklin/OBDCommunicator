
package Gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Core.Parameters;
import Core.ServiceImpl;
import Utils.Response;
import Utils.ByteUtils;

public class TerminalDialog extends JDialog{

    public MainScreen parent;
    private ServiceImpl service;
    private JTextField fldMessage= new JTextField( 10 );
    private JLabel lblResponse= new JLabel( "Odpowiedzi" );
    private JTextArea responseTxtArea= new JTextArea( 12, 20 );
    private JScrollPane responseScrollPane= new JScrollPane( responseTxtArea );
    private JButton saveToFileBtn= createSaveToFileBtn();
    private JButton sendBtn= createSendBtn();

    public TerminalDialog( JFrame frame ){
        super( frame );
        parent= ( MainScreen ) frame;
        service= parent.getService();
        setLocationRelativeTo( frame );
        setResizable( false );

        prepareGui();
        setVisible( true );
    }

    private void prepareGui(){
        setLayout( null );
        fldMessage.setLocation( 20, 10 );
        fldMessage.setSize( 140, 30 );
        sendBtn.setLocation( 170, 10 );
        sendBtn.setSize( 110, 30 );
        responseScrollPane.setLocation( 20, 55 );
        responseScrollPane.setSize( 260, 255 );
        saveToFileBtn.setLocation( 20, 320 );
        saveToFileBtn.setSize( 260, 40 );
        add( fldMessage );
        add( sendBtn );
        add( responseScrollPane );
        add( saveToFileBtn );
        setSize( 300, 400 );
    }

    private JButton createSendBtn(){
        JButton button= new JButton( "Wyœlij kod" );
        button.addActionListener( new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                Response response= service.sendAndGetResponse( fldMessage.getText() );
                String txt= ByteUtils.getStringFromBytes( response.getBytes() );
                setText( txt );
            }
        } );

        return button;
    }

    private JButton createSaveToFileBtn(){
        JButton button= new JButton( "Zapisz do pliku tekstowego" );
        button.addActionListener( e -> {
            String fileName= createFileName();
            File file= new File( Parameters.terminalFilePath + fileName );
            PrintWriter printWriter= null;
            try{
                printWriter= new PrintWriter( file );
            }catch (FileNotFoundException e1){
                JOptionPane.showMessageDialog( this, e1.toString() );
            }
            String header = createFileHeader();
            String txt= responseTxtArea.getText();
            printWriter.println(header);
            printWriter.println( txt );
            printWriter.close();
            JOptionPane.showMessageDialog( this, "Zapisano w katalogu" + Parameters.terminalFilePath );
        } );

        return button;
    }

    private String createFileName(){
        StringBuilder sb= new StringBuilder();
        //sb.append( new Date().toString() );
        sb.append( "TerminalFile.txt" );

        return sb.toString();
    }
    
    private String createFileHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append( "OBDExplorer zapis danych z terminalu \n" );
        sb.append( "Data :" + new Date().toString() );
        sb.append( new Date().toString() );
        
        return sb.toString();
    }

    /*
     * private void setLayoutConstraints(){ layout.putConstraint( SpringLayout.WEST, fldMessage, 10, SpringLayout.WEST,
     * contentPane ); layout.putConstraint( SpringLayout.NORTH, fldMessage, 5, SpringLayout.NORTH, contentPane );
     * layout.putConstraint( SpringLayout.EAST, sendBtn, 100, SpringLayout.EAST, fldMessage ); layout.putConstraint(
     * SpringLayout.NORTH, sendBtn, 5, SpringLayout.NORTH, contentPane ); layout.putConstraint( SpringLayout.WEST,
     * responseScrollPane, 10, SpringLayout.WEST, contentPane ); layout.putConstraint( SpringLayout.NORTH,
     * responseScrollPane, 35, SpringLayout.NORTH, contentPane ); }
     */

    public void setText( String txt ){
        responseTxtArea.append( txt );
        responseTxtArea.append( "\n" );
    }
}
