package com.example.demo.Entitys;

public class ResultEndIntact {
    public String SGDID;
    public String locusTag;//1
    public String feature;
    public boolean existComplement;//2
    public int startLoc;//3
    public int overLoc;//4
    public String geneNote;
    //public String repeat_region;
    public boolean isNote;
    public String poLeft1;//5
    public String poLeft2;//6
    public String poRight1;//7
    public String poRight2;//8
    public int val1;//9
    public boolean isEssential;
    public int xlsx_data_7;
    public int xlsx_data_8;
    public String str;//10
    public String str2;//11


    @Override
    public String toString() {
        return "ResultEndIntact{" +
                "SGDID='" + SGDID + '\'' +
                ", locusTag='" + locusTag + '\'' +
                ", feature='" + feature + '\'' +
                ", existComplement=" + existComplement +
                ", startLoc=" + startLoc +
                ", overLoc=" + overLoc +
                ", geneNote='" + geneNote + '\'' +
                ", isNote=" + isNote +
                ", poLeft1='" + poLeft1 + '\'' +
                ", poLeft2='" + poLeft2 + '\'' +
                ", poRight1='" + poRight1 + '\'' +
                ", poRight2='" + poRight2 + '\'' +
                ", val1=" + val1 +
                ", isEssential=" + isEssential +
                ", xlsx_data_7=" + xlsx_data_7 +
                ", xlsx_data_8=" + xlsx_data_8 +
                ", str='" + str + '\'' +
                ", str2='" + str2 + '\'' +
                '}';
    }
}
