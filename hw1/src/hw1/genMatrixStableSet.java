/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

/**
 *
 * @author Administrator
 */
public class genMatrixStableSet {
    private final int n;
    private final int k;
    
     public int getN()
   { return this.n;}

   public int getK()
   { return this.k;}
   
    public genMatrixStableSet(int[][] matrix, int[] cliqueMembers)
    {
        int size = cliqueMembers.length; 
        this.k=size; this.n=(int)Math.sqrt(matrix.length);
        for(int i =0 ; i<size ;i++)
        for(int j =0; j<size-i; j++)
            matrix[i][j]=0;
       Generate(matrix);
    }
    
    private int[][] Generate(int[][] m)
    { return m;}
}
