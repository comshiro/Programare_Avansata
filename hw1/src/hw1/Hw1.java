/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hw1;

/**
 *
 * @author Naomi
 */


   
public class Hw1 {
    public static void main(String[] args) 
    {
        
        /*
        n,k - integers from cmd args
        Create n*n matrix -adjacency matrix of random grapgh G
        G has the properties:
            -it contains at least 1 clique of k size
            -                       stable set
        
        
        
        */
        System.out.println(args[0]);
        System.out.println(args[1]);
        
        int n=Integer.parseInt(args[0]);
        int k=Integer.parseInt(args[1]);
        genMatrixClique matrixC = new genMatrixClique(n,k);
        matrixC.GenerateClique(n,k);
        
        genMatrixStableSet matrixSS=new genMatrixStableSet(matrixC, );
            
    }
    
}
