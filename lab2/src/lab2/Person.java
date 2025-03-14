/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

import java.time.LocalDate;

/**
 *
 * @author Administrator
 */
public class Person {
    private String name;
    private LocalDate birthdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate brthdate) {
        this.birthdate = brthdate;
    }

    public Person(String name, LocalDate brthdate) {
        this.name = name;
        this.birthdate = brthdate;
    }
    
    public Person(){}
    
}
