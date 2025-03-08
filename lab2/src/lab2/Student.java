/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author Naomi
 */
public class Student extends Person {
    private String name;
    private LocalDate birthdate;
    private Long regNumber;
    
    public Student(){}
    
    public Student(String name){
        this(name, null,null);
    }

    public Student(String name, LocalDate birthdate, Long regNumber) {
        super(name, birthdate);
        this.regNumber = regNumber;
    }

    public void setRegNo(Long regNumber) {
        this.regNumber = regNumber;
    }
    
    public Long getRegNo(){
        return this.regNumber;
    }

    @Override
    public String toString() {
        return "Student{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.regNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        return Objects.equals(this.regNumber, other.regNumber);
    }
    
}
