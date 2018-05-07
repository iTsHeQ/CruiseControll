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
    private double maxaccel = 10;
    private boolean run = true;
    
    CarSimulator car = new CarSimulator();
    Thread sim = new Thread(car);
    
    private double desired_speed;
    public CruiseControll(double speed, double cp,double ci, double cd){
        this.desired_speed = speed;
        this.cp = cp;
        this.ci = ci;
        this.cd = cd;
        this.maxaccel = maxaccel;
        
    }

    //double kp, double ci, double cd
    
    public double setaccel(double accel){
        if (accel > maxaccel){
            return maxaccel;
        }
        else{
            return accel;
        }
    }
    
    public double getSpeed(){
        return car.getSpeed();
    }
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
        PID pid = new PID(cp, ci, cd);
        
        sim.start();
        
        
        system_iteration = System.currentTimeMillis()/1000.0;
        system_iteration_old = system_iteration-1; //damit kein Nullwert entsteht für die Ableitung
        
        while(run){
            
            double actual_speed = car.getSpeed();
            
            system_iteration = System.currentTimeMillis()/1000.0;
            double iteration = (system_iteration - system_iteration_old);
            double accel = pid.calculate(desired_speed, actual_speed,iteration );
            System.out.println("ACCEL: " + accel);
            car.setAcceleration(setaccel(accel));
            if (accel > maxaccel){
            System.out.println("desiredspeed: " + desired_speed + " actual: " + actual_speed + " accel: " + maxaccel );
        }
        else{
            System.out.println("desiredspeed: " + desired_speed + " actual: " + actual_speed + " accel: " + accel );
        }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CruiseControll.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            system_iteration_old = system_iteration;
            system_iteration = System.currentTimeMillis()/1000;
            System.out.println("IterationTime: " + iteration);
        }
        
        
    }

    

    
}
