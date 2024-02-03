package com.example.demo.Entitys;

public class Step4ResultElement {
    public String SGDId;
    public String locusTag;
    public String feature;
    public int order;
    public int groupId;
    public Boolean essential;
    public String oxpsym;
    public int currentBlockStartLoc;
    public int getCurrentBlockEndLoc;

    public Integer orfStartLoc;
    public Integer orfEndLoc;
    public String seq1;
    public String seq2;
    public boolean existComplement;

    @Override
    public String toString() {
        return "Step4ResultElement{" +
                "SGDId='" + SGDId + '\'' +
                ", locusTag='" + locusTag + '\'' +
                ", feature='" + feature + '\'' +
                ", order=" + order +
                ", groupId=" + groupId +
                ", essential=" + essential +
                ", oxpsym='" + oxpsym + '\'' +
                ", currentBlockStartLoc=" + currentBlockStartLoc +
                ", getCurrentBlockEndLoc=" + getCurrentBlockEndLoc +
                ", orfStartLoc=" + orfStartLoc +
                ", orfEndLoc=" + orfEndLoc +
                ", seq1='" + seq1 + '\'' +
                ", seq2='" + seq2 + '\'' +
                '}';
    }
}
