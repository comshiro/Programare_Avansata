/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2;

/**
 *
 * @author Naomi
 */
public class Project {
    
    /**
     * A project may be of two types, thoretical or practical. Use an enum in order to implement this feature.
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
    public String toString(){
        return this.projectTitle;
    }
            
}
