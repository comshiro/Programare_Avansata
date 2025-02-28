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
   
   genMatrixClique(int n, int k){
      this.n=n;
      this.k=k;
   }
   
   public int getN()
   { return this.n;}

   public int getK()
   { return this.k;}
    
   public int[][] GenerateClique(int a, int b){
       
        int[][] randG = new int[a][a];
        int[] cliqueMembers = new int[k];
        
        for(int i =0 ; i<n;i++)
        for(int j =0; j< n; j++)
        randG[i][j]= (int)(Math.random() * 2);
  
            int cliqueSize = 0;
            
            int i = (int)(Math.random() * (k+1));
            int j = (int)(Math.random() * (k+1));
            randG[i][j] = 1; 
            cliqueMembers[0]=i; cliqueMembers[1]=j;
            cliqueSize++;
            
            while(cliqueSize < k){
            
            i = (int)(Math.random() * (k+1));
            cliqueMembers[cliqueSize + 1] = i;
            cliqueSize++;
            for(j = 0; j<cliqueSize;j++)
                randG[i][cliqueMembers[j]]=1; 
            }
          
        return randG;
    }
} 