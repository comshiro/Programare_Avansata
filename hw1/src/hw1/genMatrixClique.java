/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

/**
 *
 * @author Naomi 
 */
class genMatrixClique {   
   private final int n;
   private final int k;
   private int[][] adjMatrix;
   
   public genMatrixClique(int n, int k){
      this.n=n;
      this.k=k;
      generateClique();
   }
   
   public int getN()
   { return this.n;}

   public int getK()
   { return this.k;}
   
   public int[][] getMatrix()
   {return this.adjMatrix;}
    
   private void generateClique(){
       
        int[][] randG = new int[n][n];
        int[] cliqueMembers = new int[k];
        int cliqueSize = 0;
        
        while(cliqueSize<k)
        {
            int candidate = (int)(Math.random()*n);
            boolean exists = false;
            
            for(int i =0; i<cliqueSize; i++)
                if(cliqueMembers[i] == candidate)
            {
                    exists=true;
                    break;
            }
            
            if(!exists)
                cliqueMembers[cliqueSize++]=candidate;
        }
  
          for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    randG[cliqueMembers[i]][cliqueMembers[j]] = 1;
                    randG[cliqueMembers[j]][cliqueMembers[i]] = 1; // Ensure symmetry
                }
            }
        }
          
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (randG[i][j] == 0) { // Don't overwrite clique edges
                    randG[i][j] = (int) (Math.random() * 2); // 50% chance of being connected
                    randG[j][i] = randG[i][j]; // Ensure symmetry
                }
            }
        }
          
        this.adjMatrix = randG;
    }
} 