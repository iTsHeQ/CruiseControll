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


/**
 *
 * @author Hekuran
 */
public class GUI extends Application {
    
    private double cp;
    private double ci;
    private double cd;
    private double desired_speed;
    PID pid = new PID();
    CruiseControll controlThread;
    

    private NumberAxis xAxis;
    VBox chart = new VBox();
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
         //Pane
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 800, 500);
        //Label
        Label label1 = new Label("Desired Speed:");
        Label label2 = new Label("CP:");
        Label label3 = new Label("CI: ");
        Label label4 = new Label("CD: ");
        Label label5 = new Label("Maxacceleration: ");
        //
        TextField txt1 = new TextField();
        TextField txt2 = new TextField();
        TextField txt3 = new TextField();
        TextField txt4 = new TextField();
        TextField txt5 = new TextField();
        //Buttons
        Button btn = new Button("Start Simulation");
        Button btn2 = new Button("Stop Simulation");
        Button btn3 = new Button("Restard");
        
        //set Gridposition
        GridPane.setColumnIndex(label1, 0);
        GridPane.setRowIndex(label2, 1);
        GridPane.setRowIndex(label3, 2);
        GridPane.setRowIndex(label4, 3);
        GridPane.setRowIndex(label5, 4);
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
        GridPane.setRowIndex(btn2, 5);
        GridPane.setColumnIndex(btn2, 1);
        GridPane.setRowIndex(btn3, 5);
        
        //
        GridPane.setColumnIndex(chart, 2);
        
        root.getChildren().addAll(label1,label2,label3, label4,label5, txt1, txt2, txt3, txt4,txt5, btn, btn2, chart );
        
        
        //TextHandler
        txt1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                desired_speed = Double.valueOf(txt1.getText());
            }
        });
        txt2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cp = Double.valueOf(txt2.getText());
            }
        });
        txt3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ci = Double.valueOf(txt3.getText());
            }
        });
        txt4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cd = Double.valueOf(txt4.getText());
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
         Thread thread = new Thread(){
                @Override
                public void run() {
                    pid.calculate(cp, cp, ci);
            }
         };
         Platform.runLater(thread);
            
        
        
        primaryStage.setTitle("PIDController");
  
        primaryStage.setScene(scene);
        primaryStage.show();


}
public static void main(String[] args) {
        launch(args);


    }
}