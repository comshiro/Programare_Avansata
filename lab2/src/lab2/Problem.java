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
       Student[] newStudents = new Student[students.length +1];
       System.arraycopy(students, 0, newStudents, 0, students.length);
       newStudents[students.length] = s;
       students = newStudents;
       
    }
    
    public void addTeacher(Teacher t)
    {
       Teacher[] newTeachers = new Teacher[teachers.length +1];
       System.arraycopy(teachers, 0, newTeachers, 0, teachers.length);
       newTeachers[teachers.length] = t;
       teachers = newTeachers;
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

    public Student[] getStudents() {
        return students;
    }
    
    public Teacher[] getTeachers(){
        return teachers;
    }
    
    
   
}
