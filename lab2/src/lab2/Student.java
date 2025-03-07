/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

import java.time.LocalDate;

/**
 *
 * @author Naomi
 */
public class Student {
    private String name;
    private LocalDate birthdate;
    private Long regNumber;
    public Student(){}
    public Student(String name){
        this(name, null,null);
    }
    
    public Student(String name, LocalDate birthdate, Long regNumber){
        this.name=name;
        ///
    }
    //get set
    void setName(String name){
        this.name=name;
    }
    
    void setBirthdate(LocalDate birthdate){
        this.birthdate=birthdate;
    }
    
    void setRegNo(Long regNumber){
        this.regNumber=regNumber;
    }
    
    String getName(){
        return this.name;
    }
    
    public LocalDate getBirthdate(){
        return this.birthdate;
    }
    
    public Long getRegNo(){
        return this.regNumber;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
