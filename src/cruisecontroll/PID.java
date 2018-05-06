/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cruisecontroll;

/**
 *
 * @author Hekuran Mulaki
 */
public class PID {
    private double cp;
    private double ci;
    private double cd;
    private double errorold = 0 ;
    //private double integral  ;
    private double ableitung = 0 ;
    private int value = 1000; //length of the array
    private double data[] = new double[value];
    private double sum; 
    private double sum_error = 0;
    private int index = 0;
    
    
    public PID(double cp, double ci, double cd){
        this.cp = cp;
        this.ci = ci;
        this.cd = cd;
    }
    
    public PID(){
        this.cp = 0;
        this.ci = 0;
        this.cd = 0;
    }
    
    public double calculate(double setpoint, double input, double iteration){
        double error = setpoint - input;

        sum += error * iteration;
        
        ableitung = ((error - errorold)/ iteration);
        
        double result = cp * error + ci * sum + cd * ableitung;
        errorold = error;
        return result;
    }
    
}
