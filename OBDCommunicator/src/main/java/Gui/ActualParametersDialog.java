
package Gui;


import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Commands.Command;
import Commands.EngineCoolantTemperature;
import Commands.EngineLoadCommand;
import Commands.IgnitionTimingCommand;
import Commands.IntakeManifoldPressureCommand;
import Commands.MassAirFlowRateCommand;
import Commands.RPMCommand;
import Commands.ThrottlePositionCommand;
import Commands.VehicleSpeedCommand;
import Core.Service;
import Enums.OBDUnit;
import Utils.FactoryService;
import Utils.Response;

public class ActualParametersDialog extends JDialog{

    MainScreen displayer;
    private JTable table;
    private JScrollPane scrollPane;
    private Service service= FactoryService.getService();
    private Map<Command, BigDecimal> map= new HashMap<Command, BigDecimal>();
    private Map<Command, Integer> rowsMap= new HashMap<Command, Integer>();
    private Thread readingThrad;
    private boolean running= true;

    public ActualParametersDialog( JFrame frame ){
        super( frame );
        displayer= ( MainScreen ) frame;
        try{
            initParameters();
            initMap();
            readingThrad= new Thread( r );
            readingThrad.start();
        }catch (InterruptedException e){
            System.out.println( "ActualParametersDialog error constructor = " + e );
        }

    }


    private void initParameters() throws InterruptedException{
        setSize( 600, 500 );
        table= new JTable();
        table.setSize( 600, 500 );
        table.setModel( new MyTableModel() );
        scrollPane= new JScrollPane( table );
        add( scrollPane );
        addComponentListener( new ComponentAdapter(){

            public void componentHidden( ComponentEvent e ){
                running= false;
                dispose();
            }
        } );

        setVisible( true );
    }

    private void initMap(){

        RPMCommand rpmCommand= new RPMCommand();
        map.put( rpmCommand, new BigDecimal( 0 ) );
        rowsMap.put( rpmCommand, 0 );

        EngineCoolantTemperature engineCoolntTemperature= new EngineCoolantTemperature();
        map.put( engineCoolntTemperature, new BigDecimal( 0 ) );
        rowsMap.put( engineCoolntTemperature, 1 );

        ThrottlePositionCommand throttlePositionCommand= new ThrottlePositionCommand();
        map.put( throttlePositionCommand, new BigDecimal( 0 ) );
        rowsMap.put( throttlePositionCommand, 2 );
        
        VehicleSpeedCommand vehicleSpeedCommand= new VehicleSpeedCommand();
        map.put( vehicleSpeedCommand, new BigDecimal( 0 ) );
        rowsMap.put( vehicleSpeedCommand, 3 );
        
        MassAirFlowRateCommand massAirFlowRateCommand = new MassAirFlowRateCommand();
        map.put( massAirFlowRateCommand, BigDecimal.ZERO );
        rowsMap.put( massAirFlowRateCommand, 4 );
    }

    class MyTableModel extends AbstractTableModel{

        private static final long serialVersionUID= 1L;
        private String[] columnNames= {"Parametr", "Wartoœæ", "Jednostka" };
        private Object[][] data= new Object[ 5 ][ 3 ];

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
            data[ row ][ col ]= value;
            fireTableCellUpdated( row, col );
        }

    }

    Runnable r= new Runnable(){
        public void run(){
            while (running){
                for(Command c : map.keySet()){
                    Response response= service.sendAndGetResponse( c );
                    if(!response.hasErrors()){
                        map.put( c, response.getDecimalValue() );
                    }else{
                        System.out.println( "Actual Parameters Dialog run() method error -> "+response.getErrorAsString() );
                    }

                    int actualRow= rowsMap.get( c );
                    table.getModel().setValueAt( c.getParameterName(), actualRow, 0 );
                    table.getModel().setValueAt( map.get( c ), actualRow, 1 );
                    table.getModel().setValueAt( OBDUnit.getText( c.getUnit() ), actualRow, 2 );

                }

            }
        }
    };

}
