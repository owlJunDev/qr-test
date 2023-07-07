package com.example.qr.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cell")
public class Cell {

    @Id
    private String name;
    private String value;
    private String formula;

    public Cell() {
    }

    public Cell(String name, String formula) {
        this.name = name;
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
