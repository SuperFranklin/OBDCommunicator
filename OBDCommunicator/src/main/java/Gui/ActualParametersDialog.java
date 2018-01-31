
package Gui;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import Commands.DecValueCommand;
import Commands.EngineCoolantTemperature;
import Commands.MassAirFlowRateCommand;
import Commands.RPMCommand;
import Commands.ThrottlePositionCommand;
import Commands.VehicleSpeedCommand;
import Core.Message;
import Core.ServiceImpl;
import Enums.OBDUnit;
import Utils.FactoryService;
import Utils.Response;

public class ActualParametersDialog extends JDialog{

    private static final int WIDTH = 600;
    private static final int HIGHT = 500;

    private MainScreen parent;
    private JTable table;
    private JScrollPane scrollPane;
    private ServiceImpl service = FactoryService.getService();
    private Map<DecValueCommand, BigDecimal> parametersMap = new HashMap<DecValueCommand, BigDecimal>();
    private Map<DecValueCommand, Integer> rowsMap = new HashMap<DecValueCommand, Integer>();
    private Thread readingThrad;
    private boolean running = true;

    public ActualParametersDialog( JFrame frame ){
        super( frame );
        parent = ( MainScreen ) frame;
        try{
            initParameters();
            initParametersMap();
            readingThrad = new Thread( r );
            readingThrad.start();
        }catch (InterruptedException e){
            JOptionPane.showMessageDialog( parent, e.toString() );
        }
    }

    private void initParameters() throws InterruptedException{
        setSize( WIDTH, HIGHT );
        initTable();
        initWindowsListener();

        setVisible( true );
    }

    private void initTable(){
        table = new JTable();
        table.setSize( WIDTH, HIGHT );
        table.setModel( new MyTableModel() );
        scrollPane = new JScrollPane( table );
        add( scrollPane );

    }

    private void initWindowsListener(){
        addComponentListener( new ComponentAdapter(){

            public void componentHidden( ComponentEvent e ){
                running = false;
                dispose();
            }
        } );
    }

    private void initParametersMap(){

        RPMCommand rpmCommand = new RPMCommand();
        parametersMap.put( rpmCommand, new BigDecimal( 0 ) );
        rowsMap.put( rpmCommand, 0 );

        EngineCoolantTemperature engineCoolntTemperature = new EngineCoolantTemperature();
        parametersMap.put( engineCoolntTemperature, new BigDecimal( 0 ) );
        rowsMap.put( engineCoolntTemperature, 1 );

        ThrottlePositionCommand throttlePositionCommand = new ThrottlePositionCommand();
        parametersMap.put( throttlePositionCommand, new BigDecimal( 0 ) );
        rowsMap.put( throttlePositionCommand, 2 );

        VehicleSpeedCommand vehicleSpeedCommand = new VehicleSpeedCommand();
        parametersMap.put( vehicleSpeedCommand, new BigDecimal( 0 ) );
        rowsMap.put( vehicleSpeedCommand, 3 );

        MassAirFlowRateCommand massAirFlowRateCommand = new MassAirFlowRateCommand();
        parametersMap.put( massAirFlowRateCommand, BigDecimal.ZERO );
        rowsMap.put( massAirFlowRateCommand, 4 );
    }

    class MyTableModel extends AbstractTableModel{

        private static final long serialVersionUID = 1L;
        private static final int ROWS = 5;
        private static final int COLUMNS = 3;
        private String[] columnNames = {Message.PARAMETER, Message.VALUE, Message.UNIT };
        private Object[][] data = new Object[ ROWS ][ COLUMNS ];

        public int getColumnCount(){
            return columnNames.length;
        }

        public int getRowCount(){
            return data.length;
        }

        public String getColumnName( int col ){
            return columnNames[ col ];
        }

        public Object getValueAt( int row, int col ){
            return data[ row ][ col ];
        }

        public boolean isCellEditable( int row, int col ){
            return false;
        }

        public void setValueAt( Object value, int row, int col ){
            data[ row ][ col ] = value;
            fireTableCellUpdated( row, col );
        }

    }

    Runnable r = new Runnable(){
        public void run(){
            while (running){
                for(DecValueCommand c : parametersMap.keySet()){
                    Response response = service.sendAndGetResponse( c );
                   
                    if(!response.hasErrors()){
                        parametersMap.put( c, response.getDecimalValue() );
                    }

                    int actualRow = rowsMap.get( c );
                    table.getModel().setValueAt( c.getParameterName(), actualRow, 0 );
                    table.getModel().setValueAt( parametersMap.get( c ), actualRow, 1 );
                    table.getModel().setValueAt( OBDUnit.getText( c.getUnit() ), actualRow, 2 );

                }

            }
        }
    };

}
