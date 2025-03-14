
package lab3;

/**
 *
 * @author Naomi
 */
public class Lab3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Drone d = new Drone(120L, 1000D, "AEX", 201L, "23K"); 
        Airliner a = new Airliner(1000L, 200L, 1000_000_000L, "324D", 342L, "LMAO");
        Freighter f = new Freighter(12043L, 20000000L, "DEF", 1234L, "ABC");
        
        Aircraft[] cargo = new Aircraft[2];
        cargo[0]=d;
        cargo[1]=f;
        System.out.println(cargo[0] + " "+ cargo[1]);
    }
    
}
