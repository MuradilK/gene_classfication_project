package com.example.demo.Entitys;

public class XlsxDataElement {
    public String feature;
    public String locusTag;
    public float coordinate;
    public float varName4;
    public boolean strand;                  // +为1，-为0
    public String LTRARS;
    public String LTRARS1;
    public int len;



    public int LTRARS1LeftNum;
    public int LTRARS1RightNum;
    public String str;

    @Override
    public String toString() {
        return "XlsxDataElement{" +
                "feature='" + feature + '\'' +
                ", locusTag='" + locusTag + '\'' +
                ", coordinate=" + coordinate +
                ", varName4=" + varName4 +
                ", strand=" + strand +
                ", LTRARS='" + LTRARS + '\'' +
                ", LTRARS1='" + LTRARS1 + '\'' +
                ", LTRARS1LeftNum=" + LTRARS1LeftNum +
                ", LTRARS1RightNum=" + LTRARS1RightNum +
                ", str='" + str + '\'' +
                '}';
    }
}
