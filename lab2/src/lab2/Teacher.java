/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

import java.time.LocalDate;
import java.util.Arrays;

/**
 *
 * @author Naomi
 * Consider that a project is proposed by a teacher. Both students and teachers are persons, 
 * having a name and a date of birth. 
 * Each student has a registration number and each teacher has a list of proposed projects.
 */
public class Teacher extends Person {
    private Project[] projects;

    public Teacher(String name, LocalDate brthdate) {
        super(name, brthdate);
    }
    
    public Teacher() {
    }
    
    public void setProjects(Project[] projects)
    {
        this.projects = projects;
        for(Project p : projects)
            p.setTeacher(this);
    }

    public Project[] getProjects() {
        return projects;
    }

    @Override
    public String toString() {
        return "Teacher{" + this.getName()+ " "+ this.getBirthdate() + "}";
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
        final Teacher other = (Teacher) obj;
        return Arrays.deepEquals(this.projects, other.projects);
    }
    
    
}
