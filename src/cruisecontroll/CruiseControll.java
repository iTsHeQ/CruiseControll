/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cruisecontroll;
import CarSimulator.CarSimulator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


    
/**
 *
 * @author Hekuran Mulaki
 */
public class CruiseControll {
    
    private double cp;
    private double ci;
    private double cd;
    private double desired_speed;
//    CarSimulator car = new CarSimulator();
//    Thread sim = new Thread(car);
    
    /**
     * @param args the command line arguments
     */
    
    
    
    //double kp, double ci, double cd
    public void launch_sim(double speed, double cp, double ci, double cd){
        this.cp = cp;
        this.ci = ci;
        this.cd = cd;
        desired_speed = speed;
        double system_iteration = 0;
        double system_iteration_old = 0;
        //PID pid = new PID(0.5, 0.1, 0.000001);
        PID pid = new PID(cp, ci, cd);
        
        CarSimulator car = new CarSimulator();
        Thread sim = new Thread(car);
        //sim.start();
        //start_sim();
        
        
        system_iteration = System.currentTimeMillis()/1000.0;
        system_iteration_old = system_iteration - 0.01; //-0.01 weil sonst erster wert 0 ist.
        while(true){ //neuer Thread erstellen damit zugegriffen werden kann
            double iteration = system_iteration - system_iteration_old;
            double actual_speed = car.getSpeed();
            
            double accel = pid.calculate(desired_speed, actual_speed,iteration );
            car.setAcceleration(accel);
            try {
                Thread.sleep( 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CruiseControll.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("desiredspeed: " + desired_speed + " actual: " + actual_speed + " accel: " + accel);
            
            system_iteration_old = system_iteration;
            system_iteration = System.currentTimeMillis()/1000.0;
        }
    }
    
    public static void main(String[] args) {
        //launch(args);
        Application.launch(GUI.class, args);
        
        
    }

    

    
}
