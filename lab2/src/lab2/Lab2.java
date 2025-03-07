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
