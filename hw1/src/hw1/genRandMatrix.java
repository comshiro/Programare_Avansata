/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

/**
 *
 * @author Administrator
 */
public class genRandMatrix { 
   private final int n;
   private final int k;
   private int[][] adjMatrix;
   
   public genRandMatrix(int n, int k){
      this.n=n;
      this.k=k;
      adjMatrix = new int[n][n];
      generateRand();
   }
   
   public int getN()
   { return this.n;}

   public int getK()
   { return this.k;}
   
   public int[][] getMatrix()
   {return this.adjMatrix;}
    
   private void generateRand()
   {
       for(int i=0; i<n;i++)
          for(int j=i+1; j<n;j++){
              adjMatrix[i][j]=(int)(Math.random()*2);
              adjMatrix[j][i]=adjMatrix[i][j];
   }
   }
}