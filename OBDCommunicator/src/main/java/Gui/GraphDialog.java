
package Gui;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import Commands.DecValueCommand;
import Commands.EngineLoadCommand;
import Commands.IntakeManifoldPressureCommand;
import Commands.MassAirFlowRateCommand;
import Commands.RPMCommand;
import Commands.VehicleSpeedCommand;
import Core.Message;
import Core.ServiceImpl;
import Utils.FactoryService;
import Utils.Response;

public class GraphDialog extends JDialog{

    private static int WIDTH = 250;
    private static int HIGHT = 250;

    private Map<String, DecValueCommand> commands = new HashMap<>();
    private JComboBox<String> comboBox;
    private GraphWorker graphWorker;
    private SwingWrapper<XYChart> sw;
    private ServiceImpl service = FactoryService.getService();
    private XYChart chart;
    private GridBagConstraints constraints;

    public GraphDialog( JFrame parent ) throws InterruptedException{
        initCommandMap();
        initParameters();
    }

    private void initParameters() throws InterruptedException{
        setSize( WIDTH, HIGHT );
        setLayout( new GridBagLayout() );
        constraints = new GridBagConstraints();
        comboBox = createComboBox();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add( comboBox, constraints );
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        add( createOpenBtn(), constraints );
        setVisible( true );
    }

    private void initCommandMap(){
        EngineLoadCommand engineLoadCommand = new EngineLoadCommand();
        commands.put( engineLoadCommand.getParameterName(), engineLoadCommand );
        RPMCommand rpmCommand = new RPMCommand();
        commands.put( rpmCommand.getParameterName(), rpmCommand );
        IntakeManifoldPressureCommand intakeManifoldPressureCommand = new IntakeManifoldPressureCommand();
        commands.put( intakeManifoldPressureCommand.getParameterName(), intakeManifoldPressureCommand );
        VehicleSpeedCommand vehicleSpeedCommand = new VehicleSpeedCommand();
        commands.put( vehicleSpeedCommand.getParameterName(), vehicleSpeedCommand );
        MassAirFlowRateCommand massAirFlowRateCommand = new MassAirFlowRateCommand();
        commands.put( massAirFlowRateCommand.getParameterName(), massAirFlowRateCommand );

    }

    private JButton createOpenBtn(){
        JButton button = new JButton( Message.SHOW_PARAMETERS_GRAPH );

        button.addActionListener( e->{
            Runnable r = ()->{
                startGraphWorker();
            };
            new Thread( r ).start();
        } );
        return button;
    }

    private JComboBox<String> createComboBox(){
        JComboBox<String> comboBox = new JComboBox<String>();

        commands.values().forEach( c->comboBox.addItem( c.getParameterName() ) );
        comboBox.setSize( 150, 90 );
        return comboBox;
    }

    private void startGraphWorker(){

        // Create Chart
        chart = QuickChart.getChart( comboBox.getSelectedItem().toString(), Message.TIME, Message.VALUE, "randomWalk",
                new double[]{0 }, new double[]{0 } );
        chart.getStyler().setLegendVisible( false );
        chart.getStyler().setXAxisTicksVisible( false );

        // Show it
        sw = new SwingWrapper<XYChart>( chart );
        sw.displayChart();

        graphWorker = new GraphWorker();
        graphWorker.execute();
    }

    private class GraphWorker extends SwingWorker<Boolean, double[]>{
        private static final int MAX_POINTS_NUMBER = 100;
        
        LinkedList<BigDecimal> fifo = new LinkedList<BigDecimal>();
        double[] arrayTopublished;
        Response responseFromSerialPort;

        public GraphWorker(){
            fifo.add( new BigDecimal( 0.0 ) );
        }

        @Override
        protected Boolean doInBackground(){
            
            while (!isCancelled()){
                responseFromSerialPort = service.sendAndGetResponse( commands.get( comboBox.getSelectedItem().toString() ) );
                sleep( 200 );
                if(responseFromSerialPort.getDecimalValue() != null){
                    addToFifoAndPublish();
                }
            }
            return true;
        }
       
        private void addToFifoAndPublish() {
            fifo.add( responseFromSerialPort.getDecimalValue() );
            if(fifo.size() > MAX_POINTS_NUMBER){
                fifo.removeFirst();
            }
            arrayTopublished = convertToDoubleArray( fifo );
            publish( arrayTopublished );
        }

        private double[] convertToDoubleArray(List<BigDecimal> list) {
            double[] array = new double[ fifo.size() ];
            for(int i = 0; i < fifo.size(); i++){
                array[ i ] = fifo.get( i ).doubleValue();
            }
            return array;
        }
        
        @Override
        protected void process( List<double[]> chunks ){
            double[] mostRecentDataSet = chunks.get( chunks.size() - 1 );
            chart.updateXYSeries( "randomWalk", null, mostRecentDataSet, null );
            sw.repaintChart();
        }
    }
    
    
    private void sleep( long mills ){
        try{
            Thread.sleep( mills );
        }catch (InterruptedException e){}
    }

}
