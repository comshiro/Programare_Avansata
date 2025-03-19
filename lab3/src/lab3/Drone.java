/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;

/**
 *
 * @author Administrator
 */
public class Drone extends Aircraft implements CargoCapable 
{
    private final Long batteryLife;

    public Drone(Long batteryLife, double maximumPayLoad, String model, Long tailNumber, String CallSign) {
        super(model, tailNumber, CallSign);
        this.batteryLife = batteryLife;
        this.maximumPayLoad = maximumPayLoad;
    }
    
    private double maximumPayLoad;

    @Override
    public double getMaximumPayLoad(){
        return maximumPayLoad;
    }
    
    public void setMaximumPayLoad(double payLoad){
        this.maximumPayLoad = payLoad;
    }

    public Long getBatteryLife() {
        return batteryLife;
    }

    @Override
    public String toString() {
        return "Drone{" + "model=" + this.getModel()+ '}';
    }
    
    
}