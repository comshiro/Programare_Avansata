/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;

/**
 *
 * @author Administrator
 */
public class Freighter extends Aircraft implements CargoCapable {
    
    private Long wingSpan;
    private double maximumPayLoad;
    
    public Freighter(Long wingSpan, double maximumPayLoad, String model, Long tailNumber, String CallSign) {
        super(model, tailNumber, CallSign);
        this.wingSpan = wingSpan;
        this.maximumPayLoad = maximumPayLoad;
    }

    public Long getWingSpan() {
        return wingSpan;
    }

    public void setWingSpan(Long wingSpan) {
        this.wingSpan = wingSpan;
    }
    
    @Override
    public double getMaximumPayLoad(){
        return maximumPayLoad;
    }
    
    public void setMaximumPayLoad(double payLoad){
        this.maximumPayLoad = payLoad;
    }
    
    @Override
    public String toString() {
        return "Freighter{" + "model=" + this.getModel()+ '}';
    }
}
