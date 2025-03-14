/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class Problem {
    private Student[] students;
    private Teacher[] teachers; //containing the projects
    
    public Problem()
    {
        this.students = new Student[0];
        this.teachers= new Teacher[0];
    }
    
    public void addStudent(Student s)
    {
        for(Student student: students)
        {
            if(student.equals(s))
                System.out.println("Student already exists");
            else
            {
                students = Arrays.copyOf(students, students.length +1);
                students[students.length - 1]=s;
            }
                
        }
       
    }
    
    public void addTeacher(Teacher t)
    {
        for(Teacher teacher: teachers)
            if(teacher.equals(t))
                System.out.println("Teacher already exists");
            else
            {
                teachers = Arrays.copyOf(teachers, teachers.length +1);
                teachers[teachers.length - 1]=t;
            }
    }
    
   
    public Person[] getPersons()
    {
        Person[] persons = new Person[students.length + teachers.length];
        int i=0;
        
        for(Student student : students)
            persons[i++]=student;
        
        for(Teacher teacher : teachers)
            persons[i++]=teacher;
        
        return persons;
    }
   
}
