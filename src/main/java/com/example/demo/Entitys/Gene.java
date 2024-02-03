package com.example.demo.Entitys;


import java.util.Objects;

public class Gene {
    public String locusTag;
    public int startLoc;
    public int overLoc;
    public String geneNote;
    //public String repeat_region;
    public boolean isNote;
    public boolean existComplement;
    public String type;
    public int startLoc2;
    public int overLoc2;


    @Override
    public String toString() {
        return "Gene{" +
                "locusTag='" + locusTag + '\'' +
                ", startLoc=" + startLoc +
                ", overLoc=" + overLoc +
                ", geneNote='" + geneNote + '\'' +
                ", isNote=" + isNote +
                ", existComplement=" + existComplement +
                ", type='" + type + '\'' +
                ", startLoc2=" + startLoc2 +
                ", overLoc2=" + overLoc2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene gene = (Gene) o;
        if((this.locusTag==null && gene.locusTag!=null) || (this.locusTag!=null && gene.locusTag==null))  return false;
        if(this.geneNote==null && ((Gene) o).geneNote!=null || this.geneNote!=null&& ((Gene) o).geneNote==null) return false;
        if(this.type==null && gene.geneNote != null ||this.type!=null && gene.geneNote == null ) return  false;

        else if(this.locusTag==null){
            return this.isNote==gene.isNote && this.startLoc==gene.startLoc && this.overLoc == gene.overLoc && ((this.geneNote==null&&gene.geneNote==null)||this.geneNote.equals(gene.geneNote)) && this.existComplement==gene.existComplement &&((this.type==null&&gene.type==null)||this.type.equals(gene.type));
        }
        else return this.locusTag.equals(gene.locusTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locusTag, startLoc, overLoc, geneNote, isNote, existComplement);
    }
}
