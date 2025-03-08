/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab2;
import java.time.LocalDate;
import java.time.Month;
import lab2.Project.projectType;
/**
 *
 * @author Naomi
 * Compulsory (1p)

Create an object-oriented model of the problem. You should have (at least) the following classes: Student, Project.
A project may be of two types, thoretical or practical. Use an enum in order to implement this feature.
Each class should have appropriate constructors, getters and setters.
Use the IDE features for code generation, such as generating getters and setters. - USE ALT+INSERT
The toString method form the Object class must be properly overridden for all the classes.
Use the IDE features for code generation, for example (in NetBeans) press Alt+Ins or invoke the context menu, select "Insert Code" and then "toString()" (or simply start typing "toString" and then press Ctrl+Space).
Create and print on the screen an object of each class.
 */
public class Lab2 {

    /**
     * @param args the command line arguments
     * Initializam obiecte Student si Proiect
     * Si le printam
     */
    
    
    public static void main(String[] args) {
        
        LocalDate bday = LocalDate.of(2004, Month.MARCH, 12);
        Student s1 = new Student("Andrei", bday, 123242L);
        System.out.println(s1);

        Project p1 = new Project("EZ Java", projectType.practical);
        System.out.println(p1);
        //Student s2 = new Student('Ana', 12-01-2004, );
        
    }
    
}
