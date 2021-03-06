/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cruisecontroll;
import CarSimulator.CarSimulator;
import java.util.logging.Level;
import java.util.logging.Logger;



    
/**
 *
 * @author Hekuran Mulaki
 */
public class CruiseControll implements Runnable{
    
    private double cp;
    private double ci;
    private double cd;
    private double maxaccel = 10; // maximale Beschleunigung
    private double brems = -15; //maximale Bremsgeschwindkeit
    private boolean run = true;
    
    CarSimulator car = new CarSimulator();
    Thread sim = new Thread(car);
    
    private double desired_speed;
    public CruiseControll(double speed, double cp,double ci, double cd){
        this.desired_speed = speed;
        this.cp = cp;
        this.ci = ci;
        this.cd = cd;
    }

    //Bedingunge falls Beschleunigung/Bremsung zu hoch/tief ist    
    public double setaccel(double accel){
        if (accel > maxaccel && accel > 0){
            return maxaccel;
        }
        else if(accel < brems){
            return brems;
        }
        else{
            return accel;
        }
    }
    
    public double getSpeed(){
        return car.getSpeed();
    }
    //Wird im GUI benötigt um den Thread zu stoppen
    public void stop_sim(){
            car.stop();
            run = false;
            
        }

    @Override
    public void run() {
        this.cp = cp;
        this.ci = ci;
        this.cd = cd;
        desired_speed = desired_speed;
        double system_iteration = 0;
        double system_iteration_old = System.currentTimeMillis()/1000;
        int index=0;
        PID pid = new PID(cp, ci, cd);
        
        sim.start();
        
        system_iteration = System.currentTimeMillis()/1000.0;
        system_iteration_old = system_iteration-1; //damit kein Nullwert entsteht für die Ableitung
        
        while(run){
            
            double actual_speed = car.getSpeed();
            
            system_iteration = System.currentTimeMillis()/1000.0;
            double iteration = (system_iteration - system_iteration_old);
            double accel = pid.calculate(desired_speed, actual_speed,iteration );
            car.setAcceleration(setaccel(accel));
            index++; //für die Anzahl Iterationen
            //Output um die Werte zu sehen
            if (accel > maxaccel){
            System.out.println("(" + index + ")" +"desiredspeed: " + desired_speed + " actual: " + actual_speed + " accel: " + maxaccel );
        }
            else if(accel < brems){
                System.out.println("(" + index + ")" +"desiredspeed: " + desired_speed + " actual: " + actual_speed + " accel: " + brems );
            }
        else{
            System.out.println("(" + index + ")" +"desiredspeed: " + desired_speed + " actual: " + actual_speed + " accel: " + accel );
        }
            
            
            system_iteration_old = system_iteration;
            system_iteration = System.currentTimeMillis()/1000;
            
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(CruiseControll.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        
    }

    

    
}
