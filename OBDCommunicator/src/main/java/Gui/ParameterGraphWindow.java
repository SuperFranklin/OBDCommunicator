package Gui;

import java.util.stream.IntStream;

import Commands.Command;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.layout.Pane;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ParameterGraphWindow extends Application{

    
    public ParameterGraphWindow(Command command) {
        launch( );
    }
    public void start( Stage stage ) throws Exception{
        stage.setTitle( "Neural networks 3" );
        XYChart.Series<Number, Number> series1= new XYChart.Series<>();
        series1.setName( "Pomiar" );

        NumberAxis xAxis= new NumberAxis( 0, 20, 1 );
        NumberAxis yAxis= new NumberAxis( 0, 20, 1 );
        ScatterChart<Number, Number> scatterChart= new ScatterChart<>( xAxis, yAxis );
        //scatterChart.setTitle( title );
        scatterChart.getData().add( series1 );
        XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
        LineChart<Number, Number> lineChart = new LineChart<>( xAxis, yAxis );
        lineChart.setOpacity( 0.1 );
        Pane pane = new Pane();
        pane.getChildren().addAll( scatterChart, lineChart );
        stage.setScene( new Scene( pane, 500, 400 ) );
        stage.show();
    }

}
