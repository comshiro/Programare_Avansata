/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

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
}
