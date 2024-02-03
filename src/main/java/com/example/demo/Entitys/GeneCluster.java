package com.example.demo.Entitys;

import java.util.Arrays;


public class GeneCluster {
    public String name;
    public double score;
    public String [] records;

    @Override
    public String toString() {
        return "GeneCluster{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", records=" + Arrays.toString(records) +
                '}';
    }
}
