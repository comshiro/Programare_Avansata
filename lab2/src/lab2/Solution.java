
package lab2;

/**
 *
 * @author Naomi
 */
public class Solution {
    private Project[] projects; //containing the projects that have setTeacher and student
    
    public Solution(){}
    
    void solveProblem(Problem pb)
    {
        Student[] students = pb.getStudents();
        Project[] availableProjects=new Project[0];
        
        System.out.println("Populating available projects...");
        
        for(Teacher teacher :pb.getTeachers()){
            System.out.println("Teacher "+teacher.getName());
            
           for(Project project: teacher.getProjects()){
              System.out.println("Project: " + project.getProjectTitle());
           boolean ok = false;
            for(Project p: availableProjects) {  
                if(p.equals(project)){
                    ok=true;
                    break;
                }
            }
                if(!ok){
                    availableProjects = addAvailableProject(availableProjects,project);
                    System.out.println("Project: " + project.getProjectTitle());

            }
       }
    }
        
        System.out.println("Number of available projects: "+ availableProjects.length);
        
        for(Student student : students){
            boolean assigned = false;
            for(Project project : student.getPreferances()){
                for(Project availableProject : availableProjects){
                    if(project.equals(availableProject) && availableProject.getStudent()==null){
                        availableProject.setStudent(student);
                        assigned = true;
                        break;       
                   }
            }
                if(assigned)
                    break;
        }
       
    }
        this.projects = availableProjects;
        System.out.println("Projects assigned");
    }

    public void getSolution()
    {
        System.out.println("Student-Project Allocations:");
        for(Project project : projects)
        {
            if(project.getStudent()!=null)
                System.out.println(project.getProjectTitle() + " is assigned to "+ project.getStudent());
            else
                System.out.println(project.getProjectTitle()+ "has no student assigned yet.");
        }
    }

    private Project[] addAvailableProject(Project[] availableProjects, Project project) {
        Project[] newP = new Project[availableProjects.length +1];
        System.arraycopy(availableProjects, 0, newP, 0, availableProjects.length);
        newP[availableProjects.length] = project;
        return newP;
    }
}
