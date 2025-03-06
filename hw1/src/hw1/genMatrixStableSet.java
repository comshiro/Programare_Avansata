/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

/**
 *
 * @author Naomi 
 */
class genMatrixStableSet {   
   private final int n;
   private final int k;
   private int[][] adjMatrix;
   
   public genMatrixStableSet(int n, int k){
      this.n=n;
      this.k=k;
      generateStableSet();
   }
   
   public int getN()
   { return this.n;}

   public int getK()
   { return this.k;}
   
   public int[][] getMatrix()
   {return this.adjMatrix;}
    
   private void generateStableSet(){
       
        int[][] randG = new int[n][n];
        int[] stableSetMembers = new int[k];
        int stableSetSize = 0;
        
        while(stableSetSize<k)
        {
            int candidate = (int)(Math.random()*n);
            boolean exists = false;
            
            for(int i =0; i<stableSetSize; i++)
                if(stableSetMembers[i] == candidate)
            {
                    exists=true;
                    break;
            }
            
            if(!exists)
                stableSetMembers[stableSetSize++]=candidate;
        }
  
           for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    randG[stableSetMembers[i]][stableSetMembers[j]] = 0;
                    randG[stableSetMembers[j]][stableSetMembers[i]] = 0;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (randG[i][j] == 0) {
                    randG[i][j] = (int) (Math.random() * 2);
                    randG[j][i] = randG[i][j];
                }
            }
        }
        
        this.adjMatrix = randG;
    }
}