package com.example.demo.Entitys;

import java.util.List;

public class RowData {
    public List<Integer> I;    // subS在str1中出现过的位置列表
    public String subS;
    public int OriginalLocation;

    @Override
    public String toString() {
        return "RowData{" +
                "I=" + I +
                ", subS='" + subS + '\'' +
                ", OriginalLocation=" + OriginalLocation +
                '}';
    }
}
