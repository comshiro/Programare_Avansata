/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pa_lab1.lab1;

/**
 *
 * @author Naomi
 */
public class Lab1 {

    public static void main(String[] args) {
        System.out.println("Hello World");
        
        String[] languages = {"C", "C++", "C#", "Python", "Go",
        "Rust", "JavaScript", "PHP", "Swift", "Java"};
        System.out.println( languages[3]);
        int n = (int) (Math.random() * 1_000_000);
        System.out.println(n);
        n = 6*((n*3) + 0b10101 + 0xFF); // 6*((n*3) + 21 + 255) = 6*3*(n + 92) = 2*9(n+92)
        System.out.println(n);

        while (n >= 10) { 
            int sum = 0;
            while (n > 0) {
                sum += n % 10;  
                n /= 10;
            }
            n = sum;
        }
        
        System.out.println(n); //DC DA MEREU 9??? am aflat

        System.out.println("Willy-nilly, this semester I will learn");
        System.out.println(languages[n]); ///  System.out.println(languages);

        System.out.print("Willy-nilly, this semester I will learn "); 
        System.out.print(languages[n]); ///  System.out.println(languages);

    }
}
