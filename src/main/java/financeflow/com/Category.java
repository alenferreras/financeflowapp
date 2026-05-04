package financeflow.com;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;

    public Category(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        this.name = name.trim();
    }

    @Override
    public String toString() {
        return name;
    }
}