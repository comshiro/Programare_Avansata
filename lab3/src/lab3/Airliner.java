/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;

/**
 *
 * @author Administrator
 */
public class Airliner extends Aircraft 
        implements PassengerCapable,
        CargoCapable
{
    private Long wingSpan;
    private Long seatCount;
    private double maximumPayLoad;
    
    public Airliner(Long wingSpan, Long seatCount, double maximumPayLoad, String model, Long tailNumber, String CallSign) {
        super(model, tailNumber, CallSign);
        this.wingSpan = wingSpan;
        this.seatCount = seatCount;
        this.maximumPayLoad = maximumPayLoad;
    }

    public Long getWingSpan() {
        return wingSpan;
    }

    public void setWingSpan(Long wingSpan) {
        this.wingSpan = wingSpan;
    }

    @Override
    public boolean hasBusinessClassSeats() {
        return true;
    }
    
    @Override
    public Long getSeatCount(){
        return seatCount;
    }
    
    public void setSeatCount(Long seatCount){
        this.seatCount = seatCount;
    }
    
    @Override
    public double getMaximumPayLoad(){
        return maximumPayLoad;
    }
    
    public void setMaximumPayLoad(double payLoad){
        this.maximumPayLoad = payLoad;
    }
}
