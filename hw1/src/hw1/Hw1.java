/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package hw1;

import java.util.Scanner;
/**
 *
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
        
        long startTime = System.nanoTime();
        
        Scanner sc = new Scanner(System.in);
        int n=sc.nextInt();
        boolean displayOn = n>30_000;
        int k=sc.nextInt();
        
        genMatrixClique matrixC = new genMatrixClique(n,k);
        System.out.println(matrixC.getK());
        System.out.println(matrixC.getN());
        
        if(!displayOn)
            PrintMatrix.printMatrix(matrixC.getMatrix());
   
        System.out.println( "Numarul de muchii:"+ utilsMatrix.edgeNo(matrixC.getMatrix()));
        Pair.display(utilsMatrix.Δδ(matrixC.getMatrix()));
        
        if(utilsMatrix.degreeSum(matrixC.getMatrix()) == 2 * utilsMatrix.edgeNo(matrixC.getMatrix()))
            System.out.println("Da, Σd(G) = 2 * m ");
        else System.out.println("Nu, Σd(G) ≠  2 * m ");
        
        genMatrixStableSet matrixSS = new genMatrixStableSet(n,k);
        System.out.println(matrixC.getK());
        System.out.println(matrixC.getN());
        
        if(!displayOn)
            PrintMatrix.printMatrix(matrixSS.getMatrix());
        else
        {
            
        }
        System.out.println( "Numarul de muchii:"+ utilsMatrix.edgeNo(matrixSS.getMatrix()));
        Pair.display(utilsMatrix.Δδ(matrixSS.getMatrix()));
        
        if(utilsMatrix.degreeSum(matrixSS.getMatrix()) == 2 * utilsMatrix.edgeNo(matrixC.getMatrix()))
            System.out.println("Da, Σd(G) = 2 * m ");
        else System.out.println("Nu, Σd(G) ≠  2 * m ");
    
    long endTime = System.nanoTime();
    long totalTime = (endTime - startTime)/1_000_000_000;
    
    
        System.out.println("The running time of the application: " +totalTime);
}
}