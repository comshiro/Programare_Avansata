package org.example;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.example.entity.City;
import org.example.repository.CityRepository;

import java.util.List;

public class ChocoSolverDemo {
    public static void main(String[] args) {
        // Load cities from the database (limit to 10 for demo)
        CityRepository repo = new CityRepository();
        List<City> cities = repo.findByName("%"); // or use a method to get all cities

        if (cities.size() < 2) {
            System.out.println("Not enough cities in the database.");
            return;
        }

        Model model = new Model("City Latitude Sum");

        // Variables: indices of two different cities
        IntVar idx1 = model.intVar("city1", 0, cities.size() - 1);
        IntVar idx2 = model.intVar("city2", 0, cities.size() - 1);

        // Constraint: cities must be different
        model.arithm(idx1, "!=", idx2).post();

        // Constraint: sum of latitudes as close as possible to 100
        double[] latitudes = cities.stream().mapToDouble(City::getLatitude).toArray();
        IntVar sum = model.intVar("sum", 0, 200);
        model.scalar(new IntVar[]{idx1, idx2}, new int[]{(int)latitudes[0], (int)latitudes[1]}, "=", sum).post();
        // For a real constraint, you may need to use a custom propagator or search strategy

        // Find a solution
        if (model.getSolver().solve()) {
            int i1 = idx1.getValue();
            int i2 = idx2.getValue();
            System.out.println("City 1: " + cities.get(i1).getName() + ", lat=" + cities.get(i1).getLatitude());
            System.out.println("City 2: " + cities.get(i2).getName() + ", lat=" + cities.get(i2).getLatitude());
        } else {
            System.out.println("No solution found.");
        }
    }
}
