/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class utilsMatrix {
    
    
    public static int edgeNo(int[][] matrix)
    {
        int m =0;
        int n = matrix.length;
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i < j && matrix[i][j] == 1)
                    m++;
            }
         }
         return m;
    }
    
    private static int calculateDegree(int[][] matrix, int i)
    {
        int n = matrix.length, degree = 0;
        for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    degree++;
                }
            }
        return degree;
    }
    
    public static int degreeSum(int[][] matrix)
    {
        int n = matrix.length, sum = 0;
        for(int i = 0; i<n;i++)
            sum+=calculateDegree(matrix,i);
        return sum;
    }
    
    public static Pair Δδ(int[][] matrix)
    {
        int n = matrix.length, degree;
        Pair d = new Pair(n,-1);
        
        for (int i = 0; i < n; i++){
            degree = calculateDegree(matrix, i);
            if(degree>d.δ)
                d.δ= degree;
            if(degree < d.Δ)
                d.Δ = degree;
        } 
       return d; 
    }
    
    public static boolean existsClique(int k, int[][] matrix)
    {
        List<Integer> nodes = new ArrayList<>();
        for(int i = 0; i< matrix.length; i++){
            nodes.add(i);
        }
        return findClique(new ArrayList<>(),nodes, k, matrix);
    }
    
    private static boolean findClique(List<Integer> currentClique, List<Integer>cliqueMembers, int k, int[][] matrix)
    {
        if(currentClique.size() >=k)
                return true;
        
        int j = cliqueMembers.size();
        for(int i = 0; i <j; i++)
        {
            int node = cliqueMembers.get(i);
                List<Integer> newClique = new ArrayList<>(currentClique);
                newClique.add(node);
                List<Integer> newCandidates = new ArrayList<>();
                for(int neighbour : cliqueMembers)
                {
                    if(neighbour != node && matrix[node][neighbour] == 1)
                        newCandidates.add(neighbour);
                }
                if(findClique(newClique, newCandidates,k, matrix))
                        return true;
        }
    
        return false;
    } 
}
