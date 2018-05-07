/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cruisecontroll;


import javafx.application.Application;
import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.scene.chart.NumberAxis;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Application.launch;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 *
 * @author Hekuran
 */
public class GUI extends Application {
    
    private double cp;
    private double ci;
    private double cd;
    private double desired_speed;
    private double maxaccel;
    PID pid = new PID();
    CruiseControll controlThread;
    private double speed = 30;
    

    private static final int MAX_DATA_POINTS = 200;
    private int xSeriesData = 0;
    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Number> dataQ1 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ2 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ3 = new ConcurrentLinkedQueue<>();

    private NumberAxis xAxis;

    VBox chart = new VBox();
    
    private void init(Stage primaryStage) {

        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {
            }
        };

        lineChart.setAnimated(false);
        lineChart.setTitle("PID in Action");
        lineChart.setHorizontalGridLinesVisible(true);

        // Set Name for Series
        series1.setName("Desired Speed");
        series2.setName("Actual Speed");
        series3.setName("Series 3");

        // Add Chart Series
        lineChart.getData().addAll(series1, series2, series3);

        chart.getChildren().add(lineChart);
    }

    private class AddToQueue implements Runnable {

        @Override
        public void run() {
            try {
                // add a item of random data to queue
                dataQ1.add(desired_speed);
                dataQ2.add(speed);
                dataQ3.add(0);

                Thread.sleep(100);
                executor.execute(this);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void prepareTimeline() {

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { 
            if (dataQ1.isEmpty()) {
                break;
            }
            series1.getData().add(new XYChart.Data<>(xSeriesData++, dataQ1.remove()));
            series2.getData().add(new XYChart.Data<>(xSeriesData++, dataQ2.remove()));
            series3.getData().add(new XYChart.Data<>(xSeriesData++, dataQ3.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series1.getData().size() > MAX_DATA_POINTS) {
            series1.getData().remove(0, series1.getData().size() - MAX_DATA_POINTS);
        }
        if (series2.getData().size() > MAX_DATA_POINTS) {
            series2.getData().remove(0, series2.getData().size() - MAX_DATA_POINTS);
        }
        if (series3.getData().size() > MAX_DATA_POINTS) {
            series3.getData().remove(0, series3.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
    }   
    
    @Override
    public void start(Stage primaryStage) throws Exception {
         //Pane
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 600, 400);
        //Label
        Label label1 = new Label("Desired Speed:");
        Label label2 = new Label("CP:");
        Label label3 = new Label("CI: ");
        Label label4 = new Label("CD: ");
        //
        TextField txt1 = new TextField();
        TextField txt2 = new TextField();
        TextField txt3 = new TextField();
        TextField txt4 = new TextField();
        TextField txt5 = new TextField();
        //Buttons
        Button btn = new Button("Start Simulation");
        Button btn2 = new Button("Close Simulation");
        Button btn3 = new Button("Stop Simulation");
        
        //set Gridposition
        GridPane.setColumnIndex(label1, 0);
        GridPane.setRowIndex(label2, 1);
        GridPane.setRowIndex(label3, 2);
        GridPane.setRowIndex(label4, 3);
        
        //Textfieldlocation
        GridPane.setColumnIndex(txt1, 1);
        GridPane.setColumnIndex(txt2, 1);
        GridPane.setRowIndex(txt2, 1);
        GridPane.setRowIndex(txt3, 2);
        GridPane.setColumnIndex(txt3, 1);
        GridPane.setRowIndex(txt4, 3);
        GridPane.setColumnIndex(txt4, 1);
        GridPane.setRowIndex(txt5, 4);
        GridPane.setColumnIndex(txt5, 1);
        //Button
        GridPane.setRowIndex(btn, 5);
        GridPane.setRowIndex(btn2, 6);
        GridPane.setColumnIndex(btn2, 1);
        GridPane.setRowIndex(btn3, 5);
        GridPane.setColumnIndex(btn3, 1);
        
        //
        GridPane.setColumnIndex(chart, 2);
        
        root.getChildren().addAll(label1,label2,label3, label4, txt1, txt2, txt3, txt4,txt5, btn, btn2, 
                btn3, chart );
        
        
        //TextHandler
        txt1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txt1.getText().equals("")){
                    desired_speed = 0;
                }else{
                    desired_speed = Double.valueOf(txt1.getText());
                }
            }
        });
        txt2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txt2.getText().equals("")){
                    cp = 0;
                }else{
                    cp = Double.valueOf(txt2.getText());
                }
                
            }
        });
        txt3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txt3.getText().equals("")){
                    ci = 0;
                }else{
                    ci = Double.valueOf(txt3.getText());
                }
                
            }
        });
        txt4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txt4.getText().equals("")){
                    cd = 0;
                }else{
                    cd = Double.valueOf(txt4.getText());
                }
                
            }
        });
        
        //Buttonhandler
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controlThread = new CruiseControll(desired_speed, cp, cd, ci);
                new Thread(controlThread).start();
            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
                controlThread.stop_sim();
               
            }
        });
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controlThread.stop_sim();
               
            }
        });
            
        
        
        primaryStage.setTitle("PIDController");
        init(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), (ActionEvent event) -> {
            
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });

        AddToQueue addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();


}
  
public static void main(String[] args) {
        launch(args);


    }
}