/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

/**
 *
 * @author Naomi
 */
public class PrintMatrix {
    
    
    public static void printMatrixUnicode(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                if(val == 1)
                System.out.print("\u25C9 ");
                else System.out.print("\u25A2 ");
            }
            System.out.println();
        }
    }
    
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
