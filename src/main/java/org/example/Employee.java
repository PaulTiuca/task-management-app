package org.example;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Employee {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private int idEmployee;
    private String name;

    public Employee(String name){
        this.idEmployee = idGenerator.getAndIncrement();
        this.name = name;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode(){
        return Objects.hash(idEmployee);
    }

    @Override
    public String toString(){
        return name;
    }
}
