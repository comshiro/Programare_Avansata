package lab4;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello, World! (in Maven)");
        Location[] locs = new Location[4];
        locs[0] = new Location("P9", Type.FRIENDLY);
        locs[1] = new Location("P1", Type.ENEMY);
        locs[2] = new Location("P2", Type.ENEMY);
        locs[3] = new Location("P3", Type.FRIENDLY);

        var locs2 = IntStream.rangeClosed(0, 3)
                .mapToObj(i -> new Location("P" + i, Type.ENEMY) )
                .toArray(Location[]::new);

        /*List<String> listOfNames = Arrays.stream(locs)
                .filter(loc -> loc.isFriendly())
                .map(loc -> loc.getName())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
*/

        TreeSet<Location> orderedFriendlies = new TreeSet<>();
        for (Location loc : locs) {
            if(loc.isFriendly())
                orderedFriendlies.add(loc);
        }
        System.out.println(orderedFriendlies); //TreeSet le sorteaza automat!

        TreeSet<Location> sortedFriendlies = Arrays.stream(locs)
                .filter(l -> l.getType() == Type.FRIENDLY)
                .collect(Collectors.toCollection(TreeSet::new));

        System.out.println(sortedFriendlies);

        LinkedList<Location> orderedEnemies = new LinkedList<>();
        for (Location loc : locs) {
            if(loc.isEnemy())
                orderedEnemies.add(loc);
        }
        Collections.sort(orderedEnemies);
        System.out.println(orderedEnemies);

        LinkedList<Location> sortedEnemies = Arrays.stream(locs)
                .filter(l -> l.getType() == Type.ENEMY)
                .sorted(Comparator.comparing(Location::getName))
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println(sortedEnemies);

    }
}