package lab4;

public class Location implements Comparable<Location>{

    private String name;
    private Type type;

    public boolean isFriendly() {
        return type == Type.FRIENDLY;
    }

    public boolean isEnemy() {
        return type == Type.ENEMY;
    }

    public Location(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(Location other) {
        if(name != null && other.getName() != null)
            return this.name.compareTo(other.name);
        return -1;
    }
}
