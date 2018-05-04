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
    
    private double data[] = new double[500];
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
        data[index] = (setpoint - input);
        index = ++index % 500;
        sum = 0;
        sum_error = 0;
        for (int i = 0; i < 500; i++) {
            sum += data[i] * iteration;
            sum_error += data[i];
        }
        ableitung = ((sum_error - errorold)/ iteration);
        
        double result = cp * sum_error + ci * sum + cd * ableitung;
        errorold = sum_error;
        return result;
    }
}
