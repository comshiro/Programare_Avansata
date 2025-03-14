/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab2;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import lab2.Project.projectType;
/**
 *
 * @author Naomi
 * 
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
        Student c3 = new Student();
        c3.setName("Student 1");
        c3.setBirthdate(LocalDate.of(1999, Month.MARCH, 31));
        c3.setRegNo(123_456_789L);
        System.out.println(c3.getName());
        
        Student c4 = new Student("Student 2");
        System.out.println(c4);
        Student c5 = new Student("Student 1");
        System.out.println(c3 == c5);
        System.out.println(c5.equals(c3));
        
        Teacher t1= new Teacher();
        t1.setName("Teacher 1");
        t1.setBirthdate(LocalDate.of(1978, Month.MARCH, 3));
        
        Project p = new Project("Some App", projectType.practical);
        Project p2= new Project("Some Other App", projectType.theoretical);
        Project p3= new Project("Lab3", projectType.theoretical);

        Teacher t2 = new Teacher("T2", LocalDate.of(1950, Month.AUGUST, 1) );
        t1.setProjects(new Project[] {p, p1});
        t2.setProjects(new Project[] {p2, p3});
        
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(p);
        System.out.println(c4.getName());
    
        p.setTeacher(t2);
        p1.setTeacher(t2);
        p2.setTeacher(t1);
        p3.setTeacher(t1);
        
        s1.setPreferances(new Project[] {p, p1});
        c3.setPreferances(new Project[] {p, p2});
        c4.setPreferances(new Project[] {p2, p3});
        c5.setPreferances(new Project[] {p, p3});
        
        Problem pb = new Problem();
        pb.addTeacher(t2);
        pb.addTeacher(t1);
        pb.addStudent(s1);
        pb.addStudent(c3);
        pb.addStudent(c4);
        pb.addStudent(c5);
        
        Solution solution = new Solution();
        solution.solveProblem(pb);
        solution.getSolution();
        
    
    }
    
}
