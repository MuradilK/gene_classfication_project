package com.example.demo.Entitys;

public class SssssElement {
    public int startLocInGeneFragment;  //ss在总的基因序列出现过的起始位置
    public String ss;   //起止序列
    public String locInFragment;    //fragment::ccattattgtgaatataagctatttgagaattattttacctttttacagccgccccc...
    public int fragmentStart;
    public int fragmentEnd;

    @Override
    public String toString() {
        return "SssssElement{" +
                "startLocInGeneFragment=" + startLocInGeneFragment +
                ", ss='" + ss + '\'' +
                '}';
    }
}
