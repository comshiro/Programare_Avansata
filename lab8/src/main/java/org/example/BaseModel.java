package org.example;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String getName();
}
