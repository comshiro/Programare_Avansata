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
    private LocalDate brthdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBrthdate() {
        return brthdate;
    }

    public void setBrthdate(LocalDate brthdate) {
        this.brthdate = brthdate;
    }

    public Person(String name, LocalDate brthdate) {
        this.name = name;
        this.brthdate = brthdate;
    }
    
}
