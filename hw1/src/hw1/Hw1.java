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
    
        Scanner sc = new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        
        genMatrixClique matrixC = new genMatrixClique(n,k);
        System.out.println(matrixC.getK());
        System.out.println(matrixC.getN());
        PrintMatrix.printMatrix(matrixC.getMatrix());
   
        System.out.println( "Numarul de muchii:"+ utilsMatrix.edgeNo(matrixC.getMatrix()));
        utilsMatrix.Pair.display(utilsMatrix.Δδ(matrixC.getMatrix()));
        genMatrixStableSet matrixSS = new genMatrixStableSet(n,k);
        System.out.println(matrixC.getK());
        System.out.println(matrixC.getN());
        
        PrintMatrix.printMatrix(matrixSS.getMatrix());
        
        System.out.println( "Numarul de muchii:"+ utilsMatrix.edgeNo(matrixC.getMatrix()));
        
        
    }
    
}
