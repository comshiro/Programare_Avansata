/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;

/**
 *
 * @author Naomi
 */
public abstract class Aircraft
    implements Comparable<Aircraft>
{
    private String model;
    private Long tailNumber;
    private String CallSign;

    public Aircraft(String model, Long tailNumber, String CallSign) {
        this.model = model;
        this.tailNumber = tailNumber;
        this.CallSign = CallSign;
    }
   
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(Long tailNumber) {
        this.tailNumber = tailNumber;
    }

    public String getCallSign() {
        return CallSign;
    }

    public void setCallSign(String CallSign) {
        this.CallSign = CallSign;
    }

    @Override
    public int compareTo(Aircraft other){
        if(model != null && other.getModel() != null)
            return this.model.compareTo(other.model);
        else return -1;
    }
     

}
