/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw1;

/**
 *
 * @author Administrator
 */
public class Pair {
    public int Δ;
    public int δ;
    Pair(int x, int y){
            this.Δ= x;
            this.δ = y;
        }
    public static void display(Pair p)
        {
            System.out.println("Δ(G)=" + p.Δ + " δ(G)=" + p.δ);
        }
   }
