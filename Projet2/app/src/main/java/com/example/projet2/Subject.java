package com.example.projet2;
import java.io.Serializable;
import java.util.Objects;

public class Subject implements Serializable {
    private String name;

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj){
        Subject subject = (Subject) obj;
        return Objects.equals(this.name,subject.name);
    }
    @Override
    public String toString() {
        return name;
    }
}