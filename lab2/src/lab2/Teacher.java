/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

/**
 *
 * @author Administrator
 */
public class Teacher {
    private String name;
    private Project[] projects;
    
    Teacher(String name)
    { this.name=name;}
    Teacher(String name, int n, Project[] projects)
    {
        this.name=name;
        projects = new Project[n];
        for(int i = 0; i< n; i++)
            this.projects[i] =projects[i]; 
    }
    
    
    public void setProjects(Project[] projects)
    {
        
    }
}
