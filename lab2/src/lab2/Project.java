/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

import java.util.Objects;

/**
 *
 * @author Naomi
 */
public class Project {
    private Teacher t;
    private Student s;
    
    public void setTeacher(Teacher t) {
        this.t = t;
    }
    
    void setStudent(Student s)
    {
        this.s = s;
    }

    public Student getStudent() {
        return this.s;
    }
    /**
     * A project may be of two types, theoretical or practical. Use an enum in order to implement this feature.
     */
    public enum projectType
    {practical, theoretical}
    
    private String projectTitle;
    private projectType type;
    private Teacher teacher;
    
    Project(String projectTitle, projectType type)
    {
        this.projectTitle = projectTitle;
        this.type = type;
    }
    
    public String getProjectTitle()
    {
        return this.projectTitle;
    }
    public projectType getProjectType(){
        return this.type;
    }
    
    public void setProjectTitle(String title)
    {
        this.projectTitle=title;
    }
    
    public void setProjectType(projectType type){
        this.type = type;
    }

    @Override
    public String toString() {
        return "Project{" + "projectTitle=" + projectTitle + ", type=" + type + ", teacher=" + teacher + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.projectTitle);
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
        final Project other = (Project) obj;
        return Objects.equals(this.projectTitle, other.projectTitle);
    }
   
            
}
