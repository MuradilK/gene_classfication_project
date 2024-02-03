package com.example.demo.Services;

import com.example.demo.Entitys.*;
import com.example.demo.Util.GeneClusterReportClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;

import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Step1Service {

    //@Autowired
    private final GeneClusterReportClient geneClusterReportClient=new GeneClusterReportClient();

    public Step1Result s1(List<Gene> addList, int chromosome) {
        //File file  = new File("D:\\git base\\train\\mq-redis-practice\\src\\main\\resources\\static\\NC_001138.5.gb");

        String GBFileName = switch (chromosome) {
            case 1 -> "static\\1.gb";
            case 2 -> "static\\2.gb";
            case 3 -> "static\\3.gb";
            case 4 -> "static\\4.gb";
            case 5 -> "static\\5.gb";
            case 6 -> "static\\6.gb";
            case 7 -> "static\\7.gb";
            case 8 -> "static\\8.gb";
            case 9 -> "static\\9.gb";
            case 10 -> "static\\10.gb";
            case 11 -> "static\\11.gb";
            case 12 -> "static\\12.gb";
            case 13 -> "static\\13.gb";
            case 14 -> "static\\14.gb";
            case 15 -> "static\\15.gb";
            case 16 -> "static\\16.gb";
            default -> "static\\wt I .gb";
        };
        Set<Gene> allGene = processGBFile(GBFileName);
        Set<Gene>   intronSet = new HashSet<>();

        allGene.forEach(gene -> {
            if(gene.isNote){
                intronSet.add(gene);
            }
        });
        allGene.forEach(System.out::println);
        System.out.println(allGene.size());
        System.out.println("intron set:");
        intronSet.forEach(System.out::println);
        //String[] delList = {"YFL068W", "YFL067W", "YFL066C", "YFL065C", "YFL064C", "YFL062W", "YFL061W", "YFL060C", "YFL059W", "YFL058W", "YFL056C", "YNCF0001C", "YNCF0002W", "YNCF0003C", "YNCF0004C", "YFL002W-A", "YFL002W-B", "YNCF0005C", "YNCF0006W", "YNCF0007W", "YNCF0008C", "YNCF0009C", "YNCF0010C", "YNCF0011C", "YNCF0012C", "YNCF0013W", "YNCF0014C", "YFR057W"};
        String[] delList={"YAL067W-A",
                "YAL068W-A",
                "YAR009C",
                "YAR010C",
                "YAR050W",
                "YAR061W",
                "YAR064W",
                "YAR066W",
                "YAR068W",
                "YAR071W",
                "YNCA0001W",
                "YNCA0002W",
                "YNCA0003W",
                "YNCA0004W",
                "YNCA0005W",
                "YNCA0006C"
        };
        //delList = new String[]{""};
        ArrayList<String> deleteList = new ArrayList<>(Arrays.asList(delList));

        List<Gene> geneEnd = this.checkGene(deleteList,allGene,false);
        ArrayList<Gene> geneEndList = new ArrayList<>(geneEnd);
        ArrayList<Gene> remList = new ArrayList<>();
        ArrayList<Gene> intactList = new ArrayList<>();
        //addlist新逻辑的循环，需验证
        for (Gene add : addList) {
            for (Gene auto : geneEndList) {
                if(add.locusTag!=null && add.locusTag.equals(auto.locusTag)){
                    remList.add(add);
                    intactList.add(auto);
                }
            }
        }
        addList.removeAll(remList);
        addList.addAll(intactList);


        System.out.println("after remove repeat gene,remains:");
        for (Gene gene : addList) {
            System.out.println(gene);
        }


        //geneEndList.addAll(addList);  8/12


        System.out.println("排好序的基因序列为:");

        geneEndList.sort(new Comparator<>() {//此处创建了一个匿名内部类
            @Override
            public int compare(Gene o1, Gene o2) {
                if(o1.startLoc - o2.startLoc!=0)
                    return o1.startLoc - o2.startLoc;
                else
                    return o1.overLoc - o2.overLoc;
            }
        });
//        for (Gene gene : geneEndList) {
//            System.out.println(gene);
//        }

        List<Gene> intron = this.getIntron(geneEndList);
        List<Gene> tRNAs = this.getTRNAs(geneEndList);
        List<Gene> ARS = this.getARS(geneEndList);
        List<Gene> ncRNA = this.getNcRNA(geneEndList);
        List<Gene> mRNA = this.getmRNAs(geneEndList);
        List<Gene> CDS = this.getCDS(geneEndList);
        System.out.print("tRNA :");
        System.out.println(tRNAs.size());
        for (Gene tRNA : tRNAs) {
            System.out.println(tRNA);
        }

        System.out.print("ARS :");
        System.out.println(ARS.size());
        for (Gene ars : ARS) {
            System.out.println(ars);
        }

        System.out.print("ncRNA :");System.out.println(ncRNA.size());
        for (Gene nc : ncRNA) {
            System.out.println(nc);
        }

        System.out.print("CDS :");
        System.out.println(CDS.size());
        for (Gene ars : CDS) {
            System.out.println(ars);
        }


        System.out.print("mRNA :");
        System.out.println(mRNA.size());
        for (Gene nc : mRNA) {
            System.out.println(nc);
        }
        System.out.print("intron：");
        System.out.println(intron.size());
        for (Gene nc : intron) {
            System.out.println(nc);
        }
        geneEndList.clear();
        geneEndList.addAll(addList);
        geneEndList.sort(new Comparator<>() {//此处创建了一个匿名内部类
            @Override
            public int compare(Gene o1, Gene o2) {
                if(o1.startLoc - o2.startLoc!=0)
                    return o1.startLoc - o2.startLoc;
                else
                    return o1.overLoc - o2.overLoc;
            }
        });

        String fastaFileName = "static\\wt I .fasta";
        fastaFileName = switch (chromosome) {
            case 1 -> "static\\1.fasta";
            case 2 -> "static\\2.fasta";
            case 3 -> "static\\3.fasta";
            case 4 -> "static\\4.fasta";
            case 5 -> "static\\5.fasta";
            case 6 -> "static\\6.fasta";
            case 7 -> "static\\7.fasta";
            case 8 -> "static\\8.fasta";
            case 9 -> "static\\9.fasta";
            case 10 -> "static\\10.fasta";
            case 11 -> "static\\11.fasta";
            case 12 -> "static\\12.fasta";
            case 13 -> "static\\13.fasta";
            case 14 -> "static\\14.fasta";
            case 15 -> "static\\15.fasta";
            case 16 -> "static\\16.fasta";
            default -> "static\\wt I .fasta";
        };
        ArrayList<String> tagList = fastaBufferedReader(fastaFileName,"tag");
        ArrayList<String> textList = fastaBufferedReader(fastaFileName,"text");
        geneEndList.removeAll(intron);
        geneEndList.removeAll(tRNAs);
        geneEndList.removeAll(ARS);
        geneEndList.removeAll(ncRNA);
        geneEndList.removeAll(CDS);
        geneEndList.removeAll(mRNA);

        System.out.println("移除intron之后的end_gene_list为");
        for (Gene gene : geneEndList) {
            System.out.println(gene);
        }
        System.out.println("geneEndList size");
        System.out.println(geneEndList.size());
        List<SssssElement> result_end = this.extractGene(geneEndList,textList,intron);
        System.out.println(result_end);
        Step1Result step1Result = new Step1Result();
        step1Result.result_end = result_end;
        step1Result.geneEndList = geneEndList;
        step1Result.ARS = ARS;
        step1Result.tRNAs = tRNAs;
        step1Result.ncRNA = ncRNA;
        step1Result.intron = intron;
        step1Result.CDS = CDS;
        step1Result.mRNA = mRNA;
        step1Result.textList = textList;
//        System.out.println(tagList);
//        System.out.println(textList);
        return step1Result;
        //return s2(geneEndList,textList,ARS,tRNAs,ncRNA,intron,CDS,mRNA);
    }

    private List<Gene> getNcRNA(ArrayList<Gene> geneEndList) {
        List<Gene> ARS = new ArrayList<>();
        for (Gene g : geneEndList) {
            if(g.type!=null&&g.type.equals("ncRNA"))
                ARS.add(g);
        }
        return ARS;
    }

    private List<Gene> getARS(ArrayList<Gene> geneEndList) {
        List<Gene> ARS = new ArrayList<>();
        for (Gene g : geneEndList) {
            if(g.type!=null&&g.type.equals("ARS"))
                ARS.add(g);
        }
        return ARS;
    }

    private List<Gene> getTRNAs(ArrayList<Gene> geneEndList) {
        List<Gene> tRNAs = new ArrayList<>();
        for (Gene g : geneEndList) {
            if(g.type!=null&&g.type.equals("tRNA"))
                tRNAs.add(g);
        }
        return tRNAs;
    }
    private List<Gene> getmRNAs(ArrayList<Gene> geneEndList) {
        List<Gene> tRNAs = new ArrayList<>();
        for (Gene g : geneEndList) {
            if(g.type!=null&&g.type.equals("mRNA"))
                tRNAs.add(g);
        }
        return tRNAs;
    }
    private List<Gene> getCDS(ArrayList<Gene> geneEndList) {
        List<Gene> tRNAs = new ArrayList<>();
        for (Gene g : geneEndList) {
            if(g.type!=null&&g.type.equals("CDS"))
                tRNAs.add(g);
        }
        return tRNAs;
    }

    public Step2Result s2(ArrayList<Gene> geneEndList, List<String> textList, List<Gene> ARS, List<Gene> tRNAs, List<Gene> ncRNA, List<Gene> intron, List<Gene> CDS, List<Gene> mRNA){



        List<ResultEndElement> resultEnd = new ArrayList();
        StringBuilder sb = new StringBuilder();
        System.out.println("finish step1 , now start step2!");
        for(int i=0;i<textList.size();i++){
            sb.append(textList.get(i));
        }

        String [] poLeft = {"1","start"};
        String poRight1 = "";
        int i;
        for(i=0;i<geneEndList.size() - 1;i++){
            System.out.println(i);
            //下一个基因的起始位置大于当前基因的终止位置，无重叠情况
            if(geneEndList.get(i + 1).startLoc > geneEndList.get(i).overLoc){
                if(i==0){
                    boolean p1 = geneEndList.get(i).existComplement;
                    boolean p2 = geneEndList.get(i + 1).existComplement;
                    int currentOver = geneEndList.get(i).overLoc;
                    int nextStart = geneEndList.get(i + 1).startLoc;
                    List<PosElement> pos = extractPos(p1, p2, currentOver,nextStart,sb);
//                    System.out.print("pos:");
//                    System.out.println(pos);
                    pos.get(0).startLoc = pos.get(0).startLoc + currentOver;
                    pos.get(0).endLoc = pos.get(0).endLoc + currentOver;
                    ResultEndElement resultEndElement = new ResultEndElement();
                    resultEndElement.poLeft1 = poLeft[0];
                    resultEndElement.poLeft2 = poLeft[1];
                    resultEndElement.poRight1 = String.valueOf(pos.get(0).startLoc);
                    poRight1 = resultEndElement.poRight1;
                    resultEndElement.poRight2 = pos.get(0).str;
                    resultEndElement.val1 = pos.get(0).startLoc - Integer.parseInt(poLeft[0]) + 1;
                    resultEndElement.str = sb.substring(Integer.parseInt(poLeft[0])-1,pos.get(0).startLoc);
                    resultEnd.add(resultEndElement);
                    poLeft[0] = String.valueOf(pos.get(0).endLoc);
                    poLeft[1] = String.valueOf(pos.get(0).str);
//                    System.out.print("result end element:");
//                    System.out.println(resultEndElement);


                }
                else{
                    //if(i==82) System.out.println(1);
                    if(geneEndList.get(i-1).overLoc < geneEndList.get(i).overLoc){
                        boolean p1 = geneEndList.get(i).existComplement;
                        boolean p2 = geneEndList.get(i + 1).existComplement;
                        int currentOver = geneEndList.get(i).overLoc;
                        int nextStart = geneEndList.get(i + 1).startLoc;
                        List<PosElement> pos = extractPos(p1, p2, currentOver,nextStart,sb);
//                        System.out.print("pos:");
//                        System.out.println(pos);
                        pos.get(0).startLoc = pos.get(0).startLoc + currentOver;
                        pos.get(0).endLoc = pos.get(0).endLoc + currentOver;
                        ResultEndElement resultEndElement = new ResultEndElement();
                        resultEndElement.poLeft1 = poLeft[0];
                        resultEndElement.poLeft2 = poLeft[1];
                        resultEndElement.poRight1 = String.valueOf(pos.get(0).startLoc);
                        poRight1 = resultEndElement.poRight1;
                        resultEndElement.poRight2 = pos.get(0).str;
                        resultEndElement.val1 = pos.get(0).startLoc - Integer.parseInt(poLeft[0]) + 1;
                        resultEndElement.str = sb.substring(Integer.parseInt(poLeft[0])-1,pos.get(0).startLoc);
                        poLeft[0] = String.valueOf(pos.get(0).endLoc);
                        poLeft[1] = String.valueOf(pos.get(0).str);
//                        System.out.print("result end element:");
//                        System.out.println(resultEndElement);
                        resultEnd.add(resultEndElement);
                    }
                    else{
                        boolean p1 = geneEndList.get(i).existComplement;
                        boolean p2 = geneEndList.get(i + 1).existComplement;
                        int currentOver = geneEndList.get(i).overLoc;
                        int nextStart = geneEndList.get(i + 1).startLoc;
                        List<PosElement> pos  = extractPos(p1,p2,currentOver,nextStart,sb);
                        pos.get(0).startLoc = pos.get(0).startLoc + currentOver;
                        pos.get(0).endLoc = pos.get(0).endLoc + currentOver;
                        ResultEndElement resultEndElement = new ResultEndElement();
                        resultEndElement.poLeft1 = poLeft[0];
                        resultEndElement.poLeft2 = poLeft[1];
                        resultEndElement.poRight1 = String.valueOf(pos.get(0).startLoc);
                        poRight1 = resultEndElement.poRight1;
                        resultEndElement.poRight2 = pos.get(0).str;
                        resultEndElement.val1 = pos.get(0).startLoc - Integer.parseInt(poLeft[0]) + 1;
                        resultEndElement.str = sb.substring(Integer.parseInt(poLeft[0])-1,pos.get(0).startLoc);
//                        System.out.print("result end element:");
//                        System.out.println(resultEndElement);
                        p1 = geneEndList.get(i-1).existComplement;
                        p2 = geneEndList.get(i + 1).existComplement;
                        currentOver = geneEndList.get(i).overLoc;
                        nextStart = geneEndList.get(i + 1).startLoc;
                        List<PosElement> pos1 = extractPos(p1,p2,currentOver,nextStart,sb);
                        pos1.get(0).startLoc = pos1.get(0).startLoc + currentOver;
                        pos1.get(0).endLoc = pos1.get(0).endLoc + currentOver;
                        poLeft[0] = String.valueOf(pos1.get(0).endLoc);
                        poLeft[1] = String.valueOf(pos1.get(0).str);
                        resultEnd.add(resultEndElement);
                    }
                }
            }
            else{   // 有overlap
                boolean p1;
                boolean p2;
                int currentOver;
                int nextStart;
                if(i + 2 < geneEndList.size()){
                        p1 = geneEndList.get(i).existComplement;
                        p2 = geneEndList.get(i + 2).existComplement;
                        currentOver = geneEndList.get(i).overLoc;//670297
                        nextStart = geneEndList.get(i + 2).startLoc;//670125
                        //2023年9月23日修改，需求为当nextStart小于currentOver时，将nextStart一直改到下一个基因的startLoc大于为止。
                        if(nextStart < currentOver){
                            for(int j=i+3;j<geneEndList.size();j++){
                                nextStart = geneEndList.get(j).startLoc;
                                if(nextStart>currentOver)
                                    break;
                            }
                        }
                }
                else{
                        p1 = geneEndList.get(i).existComplement;
                        p2 = false;
                        currentOver = geneEndList.get(i).overLoc;
                        nextStart = sb.length();
                }
                List<PosElement> pos  = extractPos(p1,p2,currentOver,nextStart,sb);
                pos.get(0).startLoc = pos.get(0).startLoc + currentOver;
                pos.get(0).endLoc = pos.get(0).endLoc + currentOver;
                ResultEndElement resultEndElement = new ResultEndElement();
                resultEndElement.poLeft1 = poLeft[0];
                resultEndElement.poLeft2 = poLeft[1];
                resultEndElement.poRight1 = String.valueOf(pos.get(0).startLoc);
                poRight1 = resultEndElement.poRight1;
                resultEndElement.poRight2 = pos.get(0).str;
                resultEndElement.val1 = pos.get(0).startLoc - Integer.parseInt(poLeft[0]) + 1;
                resultEndElement.str = sb.substring(Integer.parseInt(poLeft[0])-1,pos.get(0).startLoc);
//                System.out.print("result end element:");
//                System.out.println(resultEndElement);
                resultEnd.add(resultEndElement);

                p1 = geneEndList.get(i-1).existComplement;
                p2 = geneEndList.get(i + 1).existComplement;
                currentOver = geneEndList.get(i-1).overLoc;
                nextStart = geneEndList.get(i + 1).startLoc;
                //2023年9月25日修改，添加if语句
                if(nextStart < currentOver){
                    for(int j=i+2;j<geneEndList.size();j++){
                        nextStart = geneEndList.get(j).startLoc;
                        if(nextStart>currentOver)
                            break;
                    }
                }
                List<PosElement> pos1 = extractPos(p1,p2,currentOver,nextStart,sb);
                pos1.get(0).startLoc = pos1.get(0).startLoc + currentOver;
                pos1.get(0).endLoc = pos1.get(0).endLoc + currentOver;
                poLeft[0] = String.valueOf(pos1.get(0).endLoc);
                poLeft[1] = String.valueOf(pos1.get(0).str);
            }
        }
        i = geneEndList.size();
        ResultEndElement resultEndElement = new ResultEndElement();
        resultEndElement.poLeft1 = poLeft[0];
        resultEndElement.poLeft2 = poLeft[1];
        resultEndElement.poRight1 = String.valueOf(sb.length());
        //resultEndElement.poRight1 = String.valueOf(202595);
        resultEndElement.poRight2 = "end";
        resultEndElement.val1 = Integer.parseInt(poRight1) - Integer.parseInt(poLeft[0]) + 1;
        resultEndElement.str = sb.substring(Integer.parseInt(poLeft[0])-1,sb.length());
        resultEnd.add(resultEndElement);
//        for (ResultEndElement endElement : resultEnd) {
//            System.out.println(endElement);
//        }
        List <ResultEndIntact> step2FinalResultEnd = new ArrayList<>();
        for(int j=0;j<resultEnd.size();j++){
            ResultEndIntact r = new ResultEndIntact();
            Gene g = geneEndList.get(j);
            ResultEndElement re = resultEnd.get(j);
            r.locusTag = g.locusTag;
            r.startLoc = g.startLoc;
            r.overLoc = g.overLoc;
            r.geneNote = g.geneNote;
            //public String repeat_region;
            r.isNote = g.isNote;
            r.existComplement = g.existComplement;
            r.poLeft1 = re.poLeft1;
            r.poLeft2 = re.poLeft2;
            r.poRight1 = re.poRight1;
            r.poRight2 = re.poRight2;
            r.val1 = re.val1+1;
            r.str = re.str;
            step2FinalResultEnd.add(r);

        }
        for (ResultEndIntact resultEndIntact : step2FinalResultEnd) {
            System.out.println(resultEndIntact);
        }

        System.out.println("step2 is finished, now start step3!");

        Step2Result step2Result = new Step2Result();
        step2Result.step2FinalResultEnd = step2FinalResultEnd;
        step2Result.sb = sb;
        step2Result.ARS = ARS;
        step2Result.tRNAs = tRNAs;
        step2Result.ncRNA = ncRNA;
        step2Result.intron = intron;
        step2Result.CDS = CDS;
        step2Result.mRNA = mRNA;
        return step2Result;
        //return s3(step2FinalResultEnd,sb,ARS,tRNAs,ncRNA,intron,CDS,mRNA);
    }



    public List<XlsxDataElement> autoListGenerator(List<ResultEndIntact> copyResult,List<Gene> ARS,List<ResultEndIntact> originResult,StringBuilder sb){
        List<XlsxDataElement> autoList = new ArrayList<>();
        for(int i=0;i<copyResult.size()-1;i++){
            XlsxDataElement x = new XlsxDataElement();

            int currentSegStart = Integer.parseInt(copyResult.get(i).poLeft1);
            int currentSegOver = Integer.parseInt(copyResult.get(i).poRight1);
            int originCurrentSegStart = Integer.parseInt(originResult.get(i).poLeft1);
            int originCurrentSegOver = Integer.parseInt(originResult.get(i).poRight1);
            int originNextSegStart = Integer.parseInt(originResult.get(i+1).poLeft1);
            int originNextSegOver = Integer.parseInt(originResult.get(i+1).poRight1);
            int nextSegStart = Integer.parseInt(copyResult.get(i+1).poLeft1);
            int nextSegOver = Integer.parseInt(copyResult.get(i+1).poRight1);
            for(int j=0;j<ARS.size();j++){
                if(ARS.get(j).startLoc >= currentSegStart && ARS.get(j).startLoc <= currentSegOver && ARS.get(j).overLoc >= currentSegOver){
                    //System.out.println("detect 1111");
                    int La = currentSegOver - ARS.get(j).startLoc;
                    int Lb = ARS.get(j).overLoc - currentSegOver;
                    if(La >= Lb){
                        //System.out.println("La>=Lb");
                        x.locusTag = copyResult.get(i).locusTag;
                        x.coordinate = copyResult.get(i).startLoc;
                        x.varName4 = copyResult.get(i).overLoc;
                        x.LTRARS1LeftNum = currentSegStart;
                        x.LTRARS1RightNum = ARS.get(j).overLoc;
                    }
                    else{
                        if(!copyResult.get(i).existComplement){
                            if(ARS.get(j).startLoc - copyResult.get(i).overLoc >= 300){
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = currentSegStart;
                                x.LTRARS1RightNum = ARS.get(j).startLoc;
                            }
                            else if(ARS.get(j).startLoc - copyResult.get(i).overLoc < 300 && ARS.get(j).startLoc - copyResult.get(i).overLoc>=0){
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = currentSegStart;
                                x.LTRARS1RightNum = copyResult.get(i).overLoc + 300;
                            }
                            else{
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = currentSegStart;
                                x.LTRARS1RightNum = copyResult.get(i).overLoc + 300;
                            }

                        }
                        else{
                            if(ARS.get(j).startLoc - copyResult.get(i).overLoc >= 500){
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = currentSegStart;
                                x.LTRARS1RightNum = ARS.get(j).startLoc;
                            }
                            else {
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = currentSegStart;
                                x.LTRARS1RightNum = copyResult.get(i).overLoc + 500;
                            }
                        }
                    }
                }
                if(currentSegStart <= ARS.get(j).overLoc && ARS.get(j).overLoc <= currentSegOver && ARS.get(j).startLoc <= currentSegStart){
                    //System.out.println("detect 2222");
                    int La = currentSegStart - ARS.get(j).startLoc;
                    int Lb = ARS.get(j).overLoc - currentSegStart;
                    if(La >= Lb){
                        if(!copyResult.get(i).existComplement){
                            if(copyResult.get(i).startLoc - ARS.get(j).overLoc >= 500){
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = ARS.get(j).overLoc;
                                x.LTRARS1RightNum = currentSegOver;
                            }
                            if(copyResult.get(i).startLoc - ARS.get(j).overLoc < 500){
                                x.locusTag = copyResult.get(i).locusTag;
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = copyResult.get(i).overLoc - 500;
                                x.LTRARS1RightNum = currentSegOver;
                            }
                        }
                        else{
                            if(copyResult.get(i).startLoc - ARS.get(j).overLoc >= 0){
                                System.out.println("a");
                                x.locusTag = copyResult.get(i).locusTag;
                                System.out.println(x.locusTag);
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = copyResult.get(i).overLoc;
                                x.LTRARS1RightNum = currentSegOver;
                            }
                            else{
                                System.out.println("b");
                                x.locusTag = copyResult.get(i).locusTag;
                                System.out.println(x.locusTag);
                                x.coordinate = copyResult.get(i).startLoc;
                                x.varName4 = copyResult.get(i).overLoc;
                                x.LTRARS1LeftNum = copyResult.get(i).startLoc;
                                x.LTRARS1RightNum = currentSegOver;
                            }
                        }
                    }

                }
                if(ARS.get(j).overLoc <= Integer.parseInt(copyResult.get(i).poLeft1) && ARS.get(j).startLoc >= Integer.parseInt(originResult.get(i).poLeft1) || ARS.get(j).startLoc >= Integer.parseInt(copyResult.get(i).poRight1) && ARS.get(j).overLoc <= Integer.parseInt(originResult.get(i).poRight1)){
                    //System.out.println("detect ARS bench:");
                    if(ARS.get(j).overLoc <= Integer.parseInt(copyResult.get(i).poLeft1) && ARS.get(j).startLoc >= Integer.parseInt(originResult.get(i).poLeft1)){
                        //System.out.println("cd bench1 in line 500");
                        XlsxDataElement x1 = new XlsxDataElement();
                        x1.str = sb.substring(ARS.get(j).startLoc,ARS.get(j).overLoc)+sb.substring(Integer.parseInt(copyResult.get(i).poLeft1),Integer.parseInt(copyResult.get(i).poRight1));
                        x1.locusTag = copyResult.get(i).locusTag;
                        x1.LTRARS1LeftNum = Integer.parseInt(copyResult.get(i).poLeft1) - (ARS.get(j).overLoc - ARS.get(j).startLoc);
                        x1.LTRARS1RightNum = Integer.parseInt(copyResult.get(i).poRight1);
                        x1.coordinate = copyResult.get(i).startLoc;
                        x1.varName4 = copyResult.get(i).overLoc;
                        autoList.add(x1);
                    }
                    if(ARS.get(j).startLoc >= Integer.parseInt(copyResult.get(i).poRight1) && ARS.get(j).overLoc <= Integer.parseInt(originResult.get(i).poRight1)){
                        //System.out.println(ARS.get(j).locusTag);
                        //System.out.println("cd bench2 in line 511");
                        XlsxDataElement x1 = new XlsxDataElement();
                        x1.str = sb.substring(Integer.parseInt(copyResult.get(i).poLeft1),Integer.parseInt(copyResult.get(i).poRight1)) + sb.substring(ARS.get(j).startLoc,ARS.get(j).overLoc);
                        x1.locusTag = copyResult.get(i).locusTag;
                        x1.LTRARS1LeftNum = Integer.parseInt(copyResult.get(i).poLeft1);
                        x1.LTRARS1RightNum = Integer.parseInt(copyResult.get(i).poRight1)+ (ARS.get(j).overLoc - ARS.get(j).startLoc);
                        x1.coordinate = copyResult.get(i).startLoc;
                        x1.varName4 = copyResult.get(i).overLoc;
                        autoList.add(x1);
                    }
                }

            }
            if(x.locusTag!=null)
                autoList.add(x);
        }
        System.out.println("finish ARS insert, List:");
        for (XlsxDataElement xlsxDataElement : autoList) {
            System.out.println(xlsxDataElement);
        }
        return autoList;
    }
    private List<XlsxDataElement> tyAndTRNARemover(List<ResultEndIntact> resultEnd, List<Gene> sortedTRNAAndTy,List<Gene> CDS,List<Gene> mRNA,List<Gene>ARS) {
        List<XlsxDataElement> autoList = new ArrayList<>();

        for (ResultEndIntact r : resultEnd) {
            int checkRegionStart = Integer.parseInt(r.poLeft1);
            int checkRegionEnd = Integer.parseInt(r.poRight1);  // 当前基因起始和终止位置
            int removeLength = 0;
            ArrayList<Gene> tysInCurrentGene = new ArrayList<>();
            for(int i=0;i< sortedTRNAAndTy.size();i++) {
                Gene tyCurrent = sortedTRNAAndTy.get(i);
                if ((tyCurrent.startLoc >= checkRegionStart && tyCurrent.startLoc <= checkRegionEnd)||(tyCurrent.overLoc >= checkRegionStart && tyCurrent.overLoc <= checkRegionEnd)||(tyCurrent.startLoc2 >= checkRegionStart && tyCurrent.startLoc2 <= checkRegionEnd)||(tyCurrent.overLoc2 >= checkRegionStart && tyCurrent.overLoc2 <= checkRegionEnd)) {
                    tysInCurrentGene.add(tyCurrent);
                }
            }// ty/ltr中只要在切割位置内部，就将该ty/ltr加入List中
            if(tysInCurrentGene.size()>0)
            {
               // System.out.print("detect list size > 0 :");
               // System.out.println(r);
                //System.out.println("ty-ltr:");
                for (Gene gene : tysInCurrentGene) {
                    System.out.println(gene);
                }
            }
            XlsxDataElement x = new XlsxDataElement();
            x.LTRARS = r.poLeft2;
            x.LTRARS1 = r.poRight2;
            if(tysInCurrentGene.size()==0)
                continue;
            ArrayList<Gene> validGenes = new ArrayList<>();
//            System.out.println("################################");
//            System.out.println(r);
            //ArrayList<Gene> startTys = new ArrayList<>();
            if(!r.existComplement){
                for (Gene ty : tysInCurrentGene) {
                    if(ty.startLoc <= r.startLoc && ty.overLoc >= r.startLoc){
                        //System.out.println("ty.startLoc <= r.startLoc && ty.overLoc >= r.startLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        x.LTRARS1LeftNum = r.startLoc - 500;
                    }
                    if(ty.overLoc <= r.startLoc){
                        //System.out.println("ty.overLoc <= r.startLoc");
                        if(r.startLoc - ty.overLoc >= 500){
                            System.out.println("r.startLoc - ty.overLoc >= 500");
                            x.locusTag = r.locusTag;
                            x.coordinate = r.startLoc;
                            x.varName4 = r.overLoc;
                            x.LTRARS1LeftNum = ty.overLoc;
                        }
                        else{
                            System.out.println("r.startLoc - ty.overLoc < 500");
                            System.out.println("r.locustag");
                            System.out.println(r.locusTag);
                            x.locusTag = r.locusTag;
                            x.coordinate = r.startLoc;
                            x.varName4 = r.overLoc;
                            x.LTRARS1LeftNum = r.startLoc - 500;
                        }
                    }

                    if(ty.overLoc >= r.overLoc){
                        //System.out.println("ty.overLoc >= r.overLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        //System.out.println(x.LTRARS1LeftNum);
                        if(x.LTRARS1RightNum == 0){
                            x.LTRARS1RightNum = ty.startLoc;
                            System.out.print("update leftNum:");
                            System.out.println(x.LTRARS1LeftNum);
                        }
                    }
                    if(ty.startLoc <= r.overLoc && ty.overLoc >= r.overLoc){
                        //System.out.println("ty.startLoc <= r.overLoc && ty.overLoc >= r.overLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        x.LTRARS1RightNum = r.overLoc;
                    }
                    if(ty.startLoc < r.startLoc && ty.overLoc > r.overLoc){
                        //System.out.println("ty.startLoc < r.startLoc && ty.overLoc > r.overLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        x.LTRARS1LeftNum = checkRegionStart;
                        x.LTRARS1RightNum = checkRegionEnd;
                    }
                }
                boolean special = false;
                if(x.locusTag==null)
                    special = true;
                if(x.LTRARS1LeftNum==0) x.LTRARS1LeftNum = checkRegionStart;
                if(x.LTRARS1RightNum==0)    x.LTRARS1RightNum = checkRegionEnd;
                if(!special)    autoList.add(x);
                System.out.println(" + new position list:");
                for (XlsxDataElement xlsx : autoList) {
                    //System.out.println(xlsx);
                }
                //System.out.println("<<<<<<<<<<<<<<<<<<<<<");
            }
            else{
                for (Gene ty : tysInCurrentGene) {
                    if(ty.startLoc <= r.startLoc && ty.overLoc >= r.startLoc){
                        //System.out.println("ty.startLoc <= r.startLoc && ty.overLoc >= r.startLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        x.LTRARS1LeftNum = r.startLoc;
                    }
                    if(ty.overLoc <= r.startLoc){
                        //System.out.println("ty.overLoc <= r.startLoc");
                            x.locusTag = r.locusTag;
                            x.coordinate = r.startLoc;
                            x.varName4 = r.overLoc;
                            x.LTRARS1LeftNum = ty.overLoc;
                    }

                    if(ty.startLoc >= r.overLoc){
                        //System.out.println("ty.overLoc >= r.overLoc");
                        if(ty.startLoc - r.overLoc >= 500){
                            x.locusTag = r.locusTag;
                            x.coordinate = r.startLoc;
                            x.varName4 = r.overLoc;
                            x.LTRARS1RightNum = ty.startLoc;
                        }
                        else if(ty.startLoc - r.overLoc < 500 && ty.startLoc - r.overLoc>0){
                            //System.out.println("ty.startLoc - r.overLoc < 500");
                            x.locusTag = r.locusTag;
                            x.coordinate = r.startLoc;
                            x.varName4 = r.overLoc;
                            x.LTRARS1RightNum = r.overLoc + 500;// YAL005C在这里因为tRNA有join更新时不对的地方
                        }
                    }
                    if(ty.startLoc <= r.overLoc && ty.overLoc >= r.overLoc){
                        //System.out.println("ty.startLoc <= r.overLoc && ty.overLoc >= r.overLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        x.LTRARS1RightNum = r.overLoc + 500;
                    }
                    if(ty.startLoc < r.startLoc && ty.overLoc > r.overLoc){//System.out.println("ty.startLoc < r.startLoc && ty.overLoc > r.overLoc");
                        x.locusTag = r.locusTag;
                        x.coordinate = r.startLoc;
                        x.varName4 = r.overLoc;
                        x.LTRARS1LeftNum = r.startLoc;
                        x.LTRARS1RightNum = Integer.parseInt(r.poRight1);
                    }
                }
                boolean special = false;
                if(x.locusTag==null)    special = true;
                if(x.LTRARS1LeftNum==0) x.LTRARS1LeftNum = checkRegionStart;
                if(x.LTRARS1RightNum==0)    x.LTRARS1RightNum = checkRegionEnd;
                if(!special)    autoList.add(x);
                System.out.println(" - new position list:");
                for (XlsxDataElement xlsx : autoList) {
                    System.out.println(xlsx);
                }
                System.out.println("<<<<<<<<<<<<<<<<<<<<<");
            }

        }
        return autoList;
    }
    public Step3Result s3(List<ResultEndIntact> resultEnd, StringBuilder sb, List<Gene> ARS, List<Gene> tRNAs, List<Gene> ncRNA, List<Gene> intron, List<Gene> CDS, List<Gene> mRNA){
        System.out.println("sb length:");
        System.out.println(sb.length());
        List<Gene> sortedTRNAAndTy = new ArrayList<>();
        sortedTRNAAndTy.addAll(tRNAs);
        sortedTRNAAndTy.addAll(intron);
        sortedTRNAAndTy.sort(new Comparator<>() {
            @Override
            public int compare(Gene o1, Gene o2) {
                return o1.startLoc - o2.startLoc;
            }
        });
        List<ResultEndIntact> copyResult1 = new ArrayList<>();
        for (ResultEndIntact r : resultEnd) {
            ResultEndIntact originResult = new ResultEndIntact();
            originResult.locusTag = r.locusTag;
            originResult.poLeft1 = r.poLeft1;
            originResult.poRight1 = r.poRight1;
            originResult.poLeft1 = r.poLeft1;
            originResult.existComplement = r.existComplement;
            originResult.startLoc = r.startLoc;
            originResult.overLoc = r.overLoc;
            originResult.val1 = r.val1;
            originResult.str = r.str;
            originResult.str2 = r.str2;
            copyResult1.add(originResult);
        }
        List<XlsxDataElement> tyRemoveList = this.tyAndTRNARemover(copyResult1,sortedTRNAAndTy,CDS,mRNA,ARS);
        System.out.println("ty RemoveList:");
        for (XlsxDataElement x : tyRemoveList) {
            if(x.LTRARS1RightNum>x.LTRARS1LeftNum-1)
            x.str = sb.substring(x.LTRARS1LeftNum-1,x.LTRARS1RightNum);System.out.println(x);
        }
        for (ResultEndIntact r : copyResult1) {
            for (XlsxDataElement x : tyRemoveList) {
                if(r.locusTag.equals(x.locusTag)){
                    r.poLeft1 = String.valueOf(x.LTRARS1LeftNum);
                    r.poRight1 = String.valueOf(x.LTRARS1RightNum);
                }
            }
        }
        int addTemp = 0;
        List<ResultEndIntact> copyResult2 = new ArrayList<>();
        for (ResultEndIntact r : resultEnd) {
            ResultEndIntact originResult = new ResultEndIntact();
            originResult.locusTag = r.locusTag;
            originResult.poLeft1 = r.poLeft1;
            originResult.poRight1 = r.poRight1;
            originResult.poLeft1 = r.poLeft1;
            originResult.existComplement = r.existComplement;
            originResult.startLoc = r.startLoc;
            originResult.overLoc = r.overLoc;
            originResult.val1 = r.val1;
            originResult.str = r.str;
            originResult.str2 = r.str2;
            copyResult2.add(originResult);
        }
        List<ResultEndIntact> copyResult3 = new ArrayList<>();
        for (ResultEndIntact r : resultEnd) {
            ResultEndIntact originResult = new ResultEndIntact();
            originResult.locusTag = r.locusTag;
            originResult.poLeft1 = r.poLeft1;
            originResult.poRight1 = r.poRight1;
            originResult.poLeft1 = r.poLeft1;
            originResult.existComplement = r.existComplement;
            originResult.startLoc = r.startLoc;
            originResult.overLoc = r.overLoc;
            originResult.val1 = r.val1;
            originResult.str = r.str;
            originResult.str2 = r.str2;
            copyResult3.add(originResult);
        }
//        ARS.addAll(ncRNA);
//        Gene checkARS = new Gene();
//        checkARS.startLoc = 164000;
//        checkARS.overLoc = 164010;
//        checkARS.locusTag = "checkARS";
//        ARS.add(checkARS);
//        Gene checkARS1 = new Gene();
//        checkARS1.startLoc = 138850;
//        checkARS1.overLoc = 138860;
//        checkARS1.locusTag = "checkARS1";
//        ARS.add(checkARS1);

        ARS.sort(new Comparator<>() {//此处创建了一个匿名内部类
            @Override
            public int compare(Gene o1, Gene o2) {
                return o1.startLoc - o2.startLoc;
            }
        });
        for (Gene ars : ARS) {
            if(ars.overLoc2!=0)
                ars.overLoc = ars.overLoc2;
        }
        System.out.println(copyResult1.size());
        System.out.println(ARS.size());
        System.out.println(resultEnd.size());
        List<XlsxDataElement> autoList = autoListGenerator(copyResult1,ARS,resultEnd,sb);

        for (XlsxDataElement x : autoList) {
            tyRemoveList.removeIf(t -> t.locusTag.equals(x.locusTag));
        }

        autoList.addAll(tyRemoveList);
        for (XlsxDataElement x : autoList) {
            for (ResultEndIntact r : resultEnd) {
                if(x.locusTag.equals(r.locusTag)) {
                    x.LTRARS = r.poLeft2;
                    x.LTRARS1 = r.poRight2;
                }
            }
        }
        System.out.println(autoList.size());
        List<XlsxDataElement> originList = new ArrayList<>(autoList);

//        System.out.println("originList:::::::");
//        for (XlsxDataElement z : originList) {
//            System.out.println(z);
//        }
        List<XlsxDataElement> additionalList = new ArrayList<>();
        for (ResultEndIntact resultEndIntact : resultEnd) {
            boolean exist = false;
            for (XlsxDataElement x : autoList) {
                if(resultEndIntact.locusTag!=null && resultEndIntact.locusTag.equals(x.locusTag))
                    exist = true;
            }
            if(!exist){
                XlsxDataElement xlsxDataElement = new XlsxDataElement();
                xlsxDataElement.locusTag = resultEndIntact.locusTag;
                xlsxDataElement.coordinate = resultEndIntact.startLoc;
                xlsxDataElement.varName4 = resultEndIntact.overLoc;
                xlsxDataElement.LTRARS = resultEndIntact.poLeft2;
                xlsxDataElement.LTRARS1 = resultEndIntact.poRight2;
                xlsxDataElement.LTRARS1LeftNum = Integer.parseInt(resultEndIntact.poLeft1);
                xlsxDataElement.LTRARS1RightNum = Integer.parseInt(resultEndIntact.poRight1);
                xlsxDataElement.str = sb.substring(xlsxDataElement.LTRARS1LeftNum-1,xlsxDataElement.LTRARS1RightNum);
                additionalList.add(xlsxDataElement);
            }
        }
        autoList.addAll(additionalList);
        autoList.sort((o1, o2) -> (int) (o1.coordinate-o2.coordinate));
        System.out.println("final copyResult:");
        for (XlsxDataElement x : autoList) {
            System.out.println(x);
        }

        System.out.println("auto generate list:");
        for (XlsxDataElement x : autoList) {
            x.len = x.LTRARS1RightNum - x.LTRARS1LeftNum;
            System.out.println(x);
        }
        List<XlsxDataElement> tyAndARSFinList = new ArrayList<>();
        tyAndARSFinList.addAll(autoList);
        //tyAndARSFinList.addAll(tyRemoveList);
        tyAndARSFinList.sort(new Comparator<>() {//此处创建了一个匿名内部类
            @Override
            public int compare(XlsxDataElement o1, XlsxDataElement o2) {
                return (int) (o1.coordinate - o2.coordinate);
            }
        });
        System.out.println("finish all cut:");
        for (XlsxDataElement tA: tyAndARSFinList) {
            System.out.println(tA);
        }

        List<XlsxDataElement> list = new ArrayList<>();


        //list.clear();
        list.addAll(tyAndARSFinList);
        System.out.println(list.size());
        for (XlsxDataElement x : list) {
            System.out.println(x);
            String geneName = x.locusTag;
            int geneIndex = -1;
            for(int j=0;j<resultEnd.size();j++){
                if(geneName.equals(resultEnd.get(j).locusTag))
                    geneIndex = j;
            }
            System.out.println("geneName"+geneName);
            ResultEndIntact r = resultEnd.get(geneIndex);

            r.poLeft1 = String.valueOf(x.LTRARS1LeftNum);
            r.poRight1 = String.valueOf(x.LTRARS1RightNum);
            r.val1 = x.LTRARS1RightNum - x.LTRARS1LeftNum;
            if(x.str==null)
                r.str = sb.substring(x.LTRARS1LeftNum-1,x.LTRARS1RightNum);
            else r.str = x.str;
        }
        for (ResultEndIntact r : resultEnd) {
            System.out.println(r);
            r.str2 = r.str;
        }
        for(int i=0;i<resultEnd.size();i++){
            //System.out.print("i:");
            //System.out.println(i);
            List <Integer> stopCodons = new ArrayList<>();
            for(int j=0;j<resultEnd.size();j++){
                if(resultEnd.get(i).existComplement!=resultEnd.get(j).existComplement)
                    continue;
                if(!resultEnd.get(i).existComplement
                        && Integer.parseInt(resultEnd.get(i).poLeft1) <= (resultEnd.get(j).overLoc - 2)
                        && resultEnd.get(j).overLoc <= Integer.parseInt(resultEnd.get(i).poRight1)){
                    stopCodons.add(resultEnd.get(j).overLoc);
                }
                else if(resultEnd.get(j).existComplement
                && Integer.parseInt(resultEnd.get(i).poLeft1) <= resultEnd.get(j).startLoc
                && (resultEnd.get(j).startLoc + 2) <= Integer.parseInt(resultEnd.get(i).poRight1)){
                    stopCodons.add(resultEnd.get(j).startLoc);
                }
            }
            for(int j=0;j<stopCodons.size();j++){
                //System.out.print("j:");
                //System.out.println(j);
                //System.out.println(sb.substring(stopCodons.get(j) - 3, stopCodons.get(j)));
                if("tag".equals(sb.substring(stopCodons.get(j) - 3, stopCodons.get(j)))){
                    StringBuilder localSb = new StringBuilder(resultEnd.get(i).str2);
                    localSb.replace(stopCodons.get(j) - 1 - Integer.parseInt(resultEnd.get(i).poLeft1)-1 ,stopCodons.get(j) + 1 - Integer.parseInt(resultEnd.get(i).poLeft1),"taa");
                    resultEnd.get(i).str2 = localSb.toString();
                    //System.out.print("*****tag:");
                    //System.out.println(localSb.toString());
                }
                if("cta".equals(sb.substring(stopCodons.get(j)-1,stopCodons.get(j)+2))){
                    StringBuilder localSb = new StringBuilder(resultEnd.get(i).str2);
                    localSb.replace(stopCodons.get(j)+1-Integer.parseInt(resultEnd.get(i).poLeft1)-1,stopCodons.get(j)+3-Integer.parseInt(resultEnd.get(i).poLeft1),"tta");
                    resultEnd.get(i).str2 = localSb.toString();
                    //System.out.print("*****cta:");
                    //System.out.println(localSb.toString());
                }
                //if(!resultEnd.get(i).existComplement){
                    //if("tag".equals(sb.substring(stopCodons.get(j)-2,stopCodons.get(j))))
                //}

            }
        }
        System.out.println("step3 is finished,now start step4！");
        Step3Result step3Result = new Step3Result();
        step3Result.originList = originList;
        step3Result.autoGenerateList = list;
        step3Result.needAddTelomere = false;
        step3Result.resultEnd = resultEnd;
        step3Result.sb = sb;

        return step3Result;
        //return s4(resultEnd,sb,false);
    }

    public Step4Result s4(List<ResultEndIntact> resultEnd, StringBuilder sb, boolean needAddTelomere, DavidParam davidParam, DavidParam davidParamNotEssential,String telomere,String RTelomere) {
        System.out.println("step4 received result_end : ");
        List<Step4ResultElement> step4ResultEnd = new ArrayList<>();
        StringBuilder finalSeq = new StringBuilder();

//        for (ResultEndIntact r : resultEnd) {
//            System.out.println(r);
//        }

        Step4Result step4Result = new Step4Result();
        String step4Sequence = "";
        String loxpsym = "ataacttcgtataatgtacattatacgaagttat";  // 非必需
        String roxpsym2 = "taactttaaataatgcgcattatttaaagtta";  // 必需

        String csvPath = "static/fusion_ess.csv";
        String SGDIdCsvPath = "static/results.csv";
        //BufferedReader b = new BufferedReader(new InputStreamReader())
        try (BufferedReader rbr = new BufferedReader(new InputStreamReader(new ClassPathResource(SGDIdCsvPath).getInputStream()))) {
            // CSV文件的分隔符
            String RDELIMITER = ",";
            // 按行读取
            String rline;
            //rline = rbr.readLine();
            //System.out.println(rline);
            int i = 0;
            while ((rline = rbr.readLine()) != null) {

                // 分割
                String[] columns = rline.split(RDELIMITER);
                for (ResultEndIntact r : resultEnd) {
                    if (r.locusTag.equals(columns[1].substring(1,columns[1].length()-1))) {
                        r.SGDID = columns[0].substring(1,columns[0].length()-1);
                        System.out.println("current SGD ID:");
                        System.out.println(r.SGDID);

                    }
                }

            }
        }catch (IOException e) {
            System.out.println("step4 read csv failed");
            e.printStackTrace();
        }
        System.out.println("\n\nfinish add SGD ID,now result end :");

        for (ResultEndIntact r : resultEnd) {
            System.out.println(r);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(csvPath).getInputStream()))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line;
            line = br.readLine();
            int i = 0;
            while ((line = br.readLine()) != null) {
                XlsxDataElement xlsxDataElement = new XlsxDataElement();
                // 分割
                String[] columns = line.split(DELIMITER);
                for (ResultEndIntact r : resultEnd) {
                    if (r.locusTag.equals(columns[1])) {
                        System.out.println("current locus tag：");
                        System.out.println(r.locusTag);
                        r.feature = columns[0];
                        if (columns[7].equals("not essential")) {
                            System.out.println("not essential");
                            r.isEssential = false;
                        }
                        if (columns[7].equals("essential")) {
                            System.out.println("essential");
                            r.isEssential = true;
                        }

                    }
                }
            }
            List<ResultEndIntact> essentialList = new ArrayList<>();
            List<ResultEndIntact> notEssentialList = new ArrayList<>();
            for (ResultEndIntact r : resultEnd) {
                if(r.isEssential)
                    essentialList.add(r);
                else notEssentialList.add(r);
            }
            System.out.println("Essential list:");
            for (ResultEndIntact resultEndIntact : essentialList) {
                System.out.println(resultEndIntact);
            }
            List<String> customEssentialStringList = Arrays.asList("S000000030",
                    "S000000041",
                    "S000000001",
                    "S000000072",
                    "S000000032",
                    "S000000036",
                    "S000000066",
                    "S000000031",
                    "S000000039",
                    "S000000065",
                    "S000000023",
                    "S000000003");
            List<ResultEndIntact> customEssentialList = new ArrayList<>(essentialList);

            for(int j=0;j<essentialList.size();j++){
                for(int k=0;k<customEssentialStringList.size();k++){
                    if(essentialList.get(j).SGDID.equals(customEssentialStringList.get(k)))
                        customEssentialList.set(k,essentialList.get(j));
                }
            }
            System.out.println("custom order:");
            for (ResultEndIntact intact : customEssentialList) {
                System.out.println(intact.SGDID);
            }
            essentialList = customEssentialList;
            System.out.println("Not essential list:");
            for (ResultEndIntact resultEndIntact : notEssentialList) {
                System.out.println(resultEndIntact.SGDID);
            }
            List<GeneCluster> essentialCluster = this.geneClusterReportClient.invokeService(essentialList, "LOCUS_TAG",davidParam.overlap,davidParam.initialSeed,davidParam.finalSeed,davidParam.linkage,davidParam.kappa);
            List<GeneCluster> notEssentialCluster = this.geneClusterReportClient.invokeService(notEssentialList,"LOCUS_TAG",davidParamNotEssential.overlap,davidParamNotEssential.initialSeed,davidParamNotEssential.finalSeed,davidParamNotEssential.linkage,davidParamNotEssential.kappa);
            HashSet<String> eMap = new HashSet<>();

            essentialCluster.sort(new Comparator<GeneCluster>() {
                @Override
                public int compare(GeneCluster o1, GeneCluster o2) {
                    return (int)(o1.score- o2.score);
                }
            });
            for (GeneCluster cluster : essentialCluster) {
                int rIndex = 0;
                for (String record : cluster.records) {
                    boolean notExist = eMap.add(record);
                    if(!notExist){
                        String [] newRecords = new String[cluster.records.length-1];
                        int newRIndex = 0;
                        for (int i1 = 0; i1 < cluster.records.length; i1++) {
                            if(cluster.records[i1]!=null && !cluster.records[i1].equals(record))
                                newRecords[newRIndex++] = cluster.records[i1];
                        }
                        cluster.records = newRecords;
                    }
                    rIndex++;
                }
            }
            for (GeneCluster cluster : notEssentialCluster) {
                int rIndex = 0;
                for (String record : cluster.records) {
                    boolean notExist = eMap.add(record);
                    if(!notExist){
                        String [] newRecords = new String[cluster.records.length-1];
                        int newRIndex = 0;
                        for (int i1 = 0; i1 < cluster.records.length; i1++) {
                            if(cluster.records[i1]!=null && !cluster.records[i1].equals(record))
                                newRecords[newRIndex++] = cluster.records[i1];
                        }
                        cluster.records = newRecords;
                    }
                    rIndex++;
                }
            }
            System.out.println("after remove repeat SGD_ID");
            System.out.println("essential cluster:");
            for (GeneCluster cluster : essentialCluster) {
                System.out.println(cluster);
            }
            System.out.println("not essential cluster:");
            for (GeneCluster cluster : notEssentialCluster) {
                System.out.println(cluster);
            }

            //List<GeneCluster> notEssentialCluster = this.geneClusterReportClient.invokeService(notEssentialList,"LOCUS_TAG",4,4,4,0.5,35);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(new ClassPathResource(csvPath).getInputStream()));//Files.newBufferedReader(Paths.get(csvPath));
            line = br1.readLine();
            i = 0;
            while ((line = br1.readLine()) != null) {   //  生成ResultEndIntact对象的xlsx_7和xlsx_8的值
                XlsxDataElement xlsxDataElement = new XlsxDataElement();
                // 分割
                String[] columns = line.split(DELIMITER);
                for (ResultEndIntact r : resultEnd) {
                    if (r.locusTag.equals(columns[1])) {

                        String orf = sb.substring(r.startLoc,r.overLoc-1);

                        List<Integer> I = new ArrayList<>(); // 用于存储子字符串在原字符串中的起始位置
                        int index = r.str2.indexOf(orf); // 查找子字符串在原字符串中的起始位置
                        while (index != -1) { // 当子字符串存在于原字符串中时
                            I.add(index); // 将子字符串的起始位置添加到列表中
                            index = r.str2.indexOf(orf, index + 1); // 继续查找子字符串在原字符串中的起始位置
                        }

                        if(I.size()==1){
                            r.xlsx_data_7 = I.get(0) - 1 + 1;
                            r.xlsx_data_8 = orf.length() + 2;
                            System.out.print("only 1 orf location: ");
                            System.out.println(r.locusTag + " "+ r.SGDID + ":"+ r.xlsx_data_7+" " + r.xlsx_data_8);

                        }
                        else{

                            System.out.print("multi orf location: ");
                            if(I.size()==0) System.out.println("(0)");
                            System.out.println(r);
                            for(int j=100;j>0;j--){
                                List<Integer> localI = new ArrayList<>(); // 用于存储子字符串在原字符串中的起始位置
                                System.out.println(orf.substring(0, j));
                                int local_index = r.str2.indexOf(orf.substring(0,j)); // 查找子字符串在原字符串中的起始位置
                                while (local_index != -1) { // 当子字符串存在于原字符串中时
                                    localI.add(local_index); // 将子字符串的起始位置添加到列表中
                                    local_index = r.str2.indexOf(orf.substring(0,j), local_index + 1); // 继续查找子字符串在原字符串中的起始位置
                                }
                                System.out.println(localI);
                                if(localI.size()==1){
                                    r.xlsx_data_7 = localI.get(0) - 1 + 1;
                                    for(int x=99;x>=0;x--){
                                        System.out.println(x);
                                        List<Integer> localIJ = new ArrayList<>();
                                        int local_index_j = r.str2.indexOf(orf.substring(orf.length()-x-1,orf.length()));
                                        while(local_index_j!=-1){
                                            localIJ.add(local_index_j);
                                            local_index_j = r.str2.indexOf(orf.substring(orf.length()-x-1,orf.length()),local_index_j+1);
                                        }
                                        if(localIJ.size()==1){
                                            r.xlsx_data_8 = localIJ.get(localIJ.size()-1) + x - r.xlsx_data_7 + 2;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                //原代码此处有个else，先忽略
                }

                // 打印行
                //System.out.println("line["+ String.join(", ", columns) +"]");
                i++;
            }
//            for (ResultEndIntact resultEndIntact : resultEnd) {
//                System.out.println(resultEndIntact);
//            }




            int currentBlockAt = 0;
            if(needAddTelomere){    // 需要添加端粒
                int currentBlockStartIndex=1;
                int currentBlockLength=0;
                int currentBlockEndIndex=0;
                int order=0;
                boolean test=false;
                for(int j=0;j<essentialCluster.size();j++){
                    for(int k=0;k<essentialCluster.get(j).records.length;k++){
                        ResultEndIntact target=null;
                        for(int m=0;m<resultEnd.size();m++){
                            if(resultEnd.get(m).SGDID.equals(essentialCluster.get(j).records[k]))
                                target = resultEnd.get(m);
                        }

                        if(target==null) {
                            System.out.print("未在所有基因列表中找到该基因的相关信息:");
                            System.out.println(essentialCluster.get(k).records[k]);
                            continue;
                        }
//                        if(target.SGDID.equals("S000000030")&& !test){//测试用，需删除
//                            k--;
//                            test=true;
//                        }

                        currentBlockLength = target.str2.length();  // 当前基因的切割序列的长度
                        currentBlockEndIndex = currentBlockStartIndex + currentBlockLength - 1;
                        Step4ResultElement sr = new Step4ResultElement();
                        sr.existComplement = target.existComplement;
                        sr.SGDId = target.SGDID;
                        sr.locusTag = target.locusTag;
                        sr.feature = target.feature;
                        sr.order = order++;
                        sr.groupId = j;
                        sr.essential = target.isEssential;
                        sr.oxpsym = roxpsym2;
                        sr.currentBlockStartLoc = currentBlockStartIndex;
                        sr.getCurrentBlockEndLoc = currentBlockEndIndex;
                        System.out.println(target);
                        String orf = target.str2.substring(target.xlsx_data_7-1,target.xlsx_data_7+ target.xlsx_data_8-1);
                        sr.seq1 =  target.str2;
                        step4ResultEnd.add(sr);
                        sr.seq2 = orf;
                        sr.orfStartLoc = currentBlockStartIndex + target.xlsx_data_7 - 1;
                        sr.orfEndLoc = sr.orfStartLoc + target.xlsx_data_8 - 1;
                        currentBlockStartIndex = currentBlockEndIndex + roxpsym2.length() + 1;
                    }
                }

                for(int j=0;j<notEssentialCluster.size();j++){
                    for(int k=0;k<notEssentialCluster.get(j).records.length;k++){
                        ResultEndIntact target=null;
                        for(int m=0;m<resultEnd.size();m++){
                            if(resultEnd.get(m).SGDID.equals(notEssentialCluster.get(j).records[k]))
                                target = resultEnd.get(m);
                        }

                        if(target==null) {
                            System.out.print("未在所有基因列表中找到该基因的相关信息:");
                            System.out.println(notEssentialCluster.get(k).records[k]);
                            continue;
                        }
//                        if(target.SGDID.equals("S000000030")&& !test){//测试用，需删除
//                            k--;
//                            test=true;
//                        }

                        currentBlockLength = target.str2.length();  // 当前基因的切割序列的长度
                        currentBlockEndIndex = currentBlockStartIndex + currentBlockLength - 1;
                        Step4ResultElement sr = new Step4ResultElement();
                        sr.SGDId = target.SGDID;
                        sr.locusTag = target.locusTag;
                        sr.feature = target.feature;
                        sr.order = order++;
                        sr.groupId = j;
                        sr.essential = target.isEssential;
                        sr.oxpsym = loxpsym;
                        sr.currentBlockStartLoc = currentBlockStartIndex;
                        sr.getCurrentBlockEndLoc = currentBlockEndIndex;
                        System.out.println(target);
                        String orf = target.str2.substring(target.xlsx_data_7-1,target.xlsx_data_7+ target.xlsx_data_8-1);
                        sr.seq1 =  target.str2;
                        sr.seq2 = orf;
                        sr.orfStartLoc = currentBlockStartIndex + target.xlsx_data_7 - 1;
                        sr.orfEndLoc = sr.orfStartLoc + target.xlsx_data_8 - 1;
                        step4ResultEnd.add(sr);
                        currentBlockStartIndex = currentBlockEndIndex + loxpsym.length() + 1;
                    }
                }
                Step4ResultElement s = new Step4ResultElement();
                s.locusTag = "TEL_L";
                s.currentBlockStartLoc = 1;
                s.getCurrentBlockEndLoc = s.currentBlockStartLoc + telomere.length();
                s.oxpsym = roxpsym2;
                s.seq1 = telomere;

                for (Step4ResultElement sr : step4ResultEnd) {
                    System.out.println(sr);
                    sr.currentBlockStartLoc += telomere.length()+33;
                    sr.getCurrentBlockEndLoc += telomere.length()+33;
                    sr.orfStartLoc += telomere.length()+33;
                    sr.orfEndLoc += telomere.length()+33;
                }


                Step4ResultElement last = step4ResultEnd.get(step4ResultEnd.size()-1);


                Step4ResultElement st = new Step4ResultElement();
                st.locusTag = "TEL_R";
                st.currentBlockStartLoc = last.getCurrentBlockEndLoc+1;
                st.getCurrentBlockEndLoc = last.getCurrentBlockEndLoc+RTelomere.length();
                st.seq1 = RTelomere;
                st.oxpsym = "";

                step4ResultEnd.add(0,s);
                step4ResultEnd.add(st);

                for (Step4ResultElement sa : step4ResultEnd) {
                    finalSeq.append(sa.seq1);
                    finalSeq.append(sa.oxpsym);
                }



//
//                }
//                String lTelomere = "cacacacaccacacccacaccacacacacaccacacccacaccacacccacacacacccacacccacacaccacacccacacacacacccacacccacacaccacacccacaccacacaccacacccacacaccacacccacacaccacacccacaccacacacccacacaccacacccacacacaccacacccacacccacacacacacacaccacacccaccacacccacacacccacacccacacaccacacccacacacaccacacccacacaccacacccacacacccacacccacaccacatcattatgcacggcacttgcctcagcggtctataccctgtgccatttacccataactcccacgattatccacattttaatatctatatctcattcggcggccccaaatattgtataactgctcttaatacatacgttataccacttttacaccatatactaaccactcaatttatatacacttatgtcaatattacaaaaaaatcaccactaaaatcacctaaacataaaaatattctattcttcaacaataatacataaacacactaccctaataacttcgtatagcatacattatacgaagttatcggtccg";
//                int currentBlockStartAt = 1;
//                int currentBlockLength = lTelomere.length();
//                int currentBlockEndAt = currentBlockStartAt + currentBlockLength - 1;
//                Step4ResultElement sre = new Step4ResultElement();
//                ResultEndIntact r1 = resultEnd.get(0);
//                ArrayList<String> step4Seq = new ArrayList<>();
//                step4Seq.add(r1.str2);
//                sre.SGDId = "";
//                sre.locusTag = "TELL";
//                sre.feature = "telL";
//                sre.order = 0;
//                sre.groupId = 0;
//                sre.essential = null;
//                sre.oxpsym = roxpsym2;
//                sre.currentBlockStartLoc = currentBlockStartAt;
//                sre.getCurrentBlockEndLoc = currentBlockEndAt;
//                sre.seq1 = lTelomere;
//                sre.orfStartLoc = null;
//                sre.orfEndLoc = null;
//                sre.seq2 = null;
//                step4ResultEnd.add(sre);
//                ArrayList<OxpsymLocs> oxpsymLocs = new ArrayList<>();
//                currentBlockStartAt++;
//                int currentOrder = 1;
//                int currentGroupId = 1;
//                for(int k=0;k<essentialCluster.size();k++){
//                    if(k==0){
//                        step4Seq.add(roxpsym2);
//                        currentBlockLength = roxpsym2.length();
//                        currentBlockEndAt = currentBlockStartAt + currentBlockLength - 1;
//                        OxpsymLocs oxp = new OxpsymLocs();
//                        oxp.start = currentBlockStartAt;
//                        oxp.end = currentBlockEndAt;
//                        oxp.str = roxpsym2;
//                        oxpsymLocs.add(oxp);
//                        currentBlockStartAt = currentBlockEndAt + 1;
//                    }
//                    for(int l=0;l<essentialCluster.get(k).records.length;l++){
//                        if(essentialCluster.get(k).records[l]==null)    continue;
//                        ResultEndIntact target = null;
//                        for(int m=0;m<resultEnd.size();m++){
////                            System.out.println("************");
////                            System.out.println(resultEnd.get(m).SGDID);
////                            System.out.println(essentialCluster.get(k).records[l]);
////                            System.out.println("#############");
//                            if(resultEnd.get(m).SGDID.equals(essentialCluster.get(k).records[l]))
//                                target = resultEnd.get(m);
//                        }
//                        if(target==null)
//                            System.out.println("Can not find" + essentialCluster.get(k).records[l] + "from result end list!");
//                        currentBlockLength = target.str2.length();
//                        currentBlockEndAt = currentBlockStartAt + currentBlockLength - 1;
//                        int orfStartAt = currentBlockStartAt + target.xlsx_data_7 - 1;
//                        int orfEndAt = orfStartAt + target.xlsx_data_8 - 1;
//                        String orf = target.str2.substring(target.xlsx_data_7-1,target.xlsx_data_7+ target.xlsx_data_8-1);
//                        Step4ResultElement sreL= new Step4ResultElement();
//                        sreL.SGDId = target.SGDID;
//                        sreL.locusTag = target.locusTag;
//                        sreL.feature = target.feature;
//                        sreL.order = currentOrder;
//                        sreL.groupId = currentGroupId;
//                        sreL.essential = true;
//                        sreL.oxpsym = roxpsym2;
//                        sreL.currentBlockStartLoc = currentBlockStartAt;
//                        sreL.getCurrentBlockEndLoc = currentBlockEndAt;
//                        sreL.seq1 = target.str2;
//                        sreL.orfStartLoc = orfStartAt;
//                        sreL.orfEndLoc = orfEndAt;
//                        sreL.seq2 = orf;
//                        step4ResultEnd.add(sreL);
//                        currentOrder++;
//                        step4Seq.add(target.str2);
//                        currentBlockStartAt = currentBlockEndAt + 1;
//                        step4Seq.add(roxpsym2);
//                        currentBlockLength = roxpsym2.length();
//                        currentBlockEndAt = currentBlockStartAt+currentBlockLength-1;
//                        OxpsymLocs o1 = new OxpsymLocs();
//                        o1.start = currentBlockStartAt;
//                        o1.end = currentBlockEndAt;
//                        o1.str = roxpsym2;
//                        oxpsymLocs.add(o1);
//                        currentBlockStartAt = currentBlockEndAt + 1;
//                    }
//                    currentGroupId++;
//                }
//                //非必需基因
//                int m=0;
//                for(int k=0;k<notEssentialCluster.size();k++){
//                    if(k==0){
//                        step4Seq.add(loxpsym);
//                        currentBlockLength = loxpsym.length();
//                        currentBlockEndAt = currentBlockStartAt + currentBlockLength - 1;
//                        OxpsymLocs oxp = new OxpsymLocs();
//                        oxp.start = currentBlockStartAt;
//                        oxp.end = currentBlockEndAt;
//                        oxp.str = loxpsym;
//                        oxpsymLocs.add(oxp);
//                        currentBlockStartAt = currentBlockEndAt + 1;
//                    }
//                    for(int l=0;l<notEssentialCluster.get(k).records.length;l++){
//                        if(notEssentialCluster.get(k).records[l]==null)    continue;
//                        ResultEndIntact target = null;
//                        for(m=0;m<resultEnd.size();m++){
//                            if(resultEnd.get(m).SGDID.equals(notEssentialCluster.get(k).records[l]))
//                                target = resultEnd.get(m);
//                        }
//                        if(target==null)
//                            System.out.println("Can not find" + notEssentialCluster.get(k).records[l] + "from result end list!");
//                        currentBlockLength = target.str2.length();
//                        currentBlockEndAt = currentBlockStartAt + currentBlockLength - 1;
//                        int orfStartAt = currentBlockStartAt + target.xlsx_data_7 - 1;
//                        int orfEndAt = orfStartAt + target.xlsx_data_8 - 1;
//                        String orf = target.str2.substring(target.xlsx_data_7-1,target.xlsx_data_7+ target.xlsx_data_8-1);
//                        Step4ResultElement sreL= new Step4ResultElement();
//                        sreL.SGDId = target.SGDID;
//                        sreL.locusTag = target.locusTag;
//                        sreL.feature = target.feature;
//                        sreL.order = currentOrder;
//                        sreL.groupId = currentGroupId;
//                        sreL.essential = true;
//                        sreL.oxpsym = loxpsym;
//                        sreL.currentBlockStartLoc = currentBlockStartAt;
//                        sreL.getCurrentBlockEndLoc = currentBlockEndAt;
//                        sreL.seq1 = target.str2;
//                        sreL.orfStartLoc = orfStartAt;
//                        sreL.orfEndLoc = orfEndAt;
//                        sreL.seq2 = orf;
//                        step4ResultEnd.add(sreL);
//                        currentOrder++;
//                        step4Seq.add(target.str2);
//                        currentBlockStartAt = currentBlockEndAt + 1;
//                        step4Seq.add(loxpsym);
//                        currentBlockLength = loxpsym.length();
//                        currentBlockEndAt = currentBlockStartAt+currentBlockLength-1;
//                        OxpsymLocs o1 = new OxpsymLocs();
//                        o1.start = currentBlockStartAt;
//                        o1.end = currentBlockEndAt;
//                        o1.str = loxpsym;
//                        oxpsymLocs.add(o1);
//                        currentBlockStartAt = currentBlockEndAt + 1;
//                    }
//                    currentGroupId++;
//                }
//                currentBlockLength = resultEnd.get(m-1).str2.length();
//                currentBlockEndAt = currentBlockStartAt + currentBlockLength - 1;
//                Step4ResultElement sreR= new Step4ResultElement();
//                sreR.SGDId = "";
//                sreR.locusTag = "TELR";
//                sreR.feature = "telR";
//                sreR.order = 0;
//                sreR.groupId = 0;
//                sreR.essential = null;
//                sreR.oxpsym = "";
//                sreR.currentBlockStartLoc = currentBlockStartAt;
//                sreR.getCurrentBlockEndLoc = currentBlockEndAt;
//                sreR.seq1 = resultEnd.get(m-1).str2;
//                sreR.orfStartLoc = null;
//                sreR.orfEndLoc = null;
//                sreR.seq2 = "";
//                step4ResultEnd.add(sreR);
//                step4Seq.add(resultEnd.get(resultEnd.size()-1).str2);
//                StringBuilder sb1 = new StringBuilder();
//                for(int seqNum=0;seqNum<step4Seq.size();seqNum++){
//                    sb1.append(step4Seq.get(seqNum));
//                }
//                step4Sequence = sb1.toString();
//                for (Step4ResultElement s : step4ResultEnd) {
//                    System.out.println(s);
//                }
            }else{
                int currentBlockStartIndex=1;
                int currentBlockLength=0;
                int currentBlockEndIndex=0;
                int order=0;
                boolean test=false;
                for(int j=0;j<essentialCluster.size();j++){
                    for(int k=0;k<essentialCluster.get(j).records.length;k++){
                        ResultEndIntact target=null;
                        for(int m=0;m<resultEnd.size();m++){
                            if(resultEnd.get(m).SGDID.equals(essentialCluster.get(j).records[k]))
                                 target = resultEnd.get(m);
                        }

                        if(target==null) {
                            System.out.print("未在所有基因列表中找到该基因的相关信息:");
                            System.out.println(essentialCluster.get(k).records[k]);
                            continue;
                        }
//                        if(target.SGDID.equals("S000000030")&& !test){//测试用，需删除
//                            k--;
//                            test=true;
//                        }

                        currentBlockLength = target.str2.length();  // 当前基因的切割序列的长度
                        currentBlockEndIndex = currentBlockStartIndex + currentBlockLength - 1;
                        Step4ResultElement sr = new Step4ResultElement();
                        sr.existComplement = target.existComplement;
                        sr.SGDId = target.SGDID;
                        sr.locusTag = target.locusTag;
                        sr.feature = target.feature;
                        sr.order = order++;
                        sr.groupId = j;
                        sr.essential = target.isEssential;
                        sr.oxpsym = roxpsym2;
                        sr.currentBlockStartLoc = currentBlockStartIndex;
                        sr.getCurrentBlockEndLoc = currentBlockEndIndex;

                        String orf = target.str2.substring(target.xlsx_data_7-1,target.xlsx_data_7+ target.xlsx_data_8-1);
                        sr.seq1 =  target.str2;
                        step4ResultEnd.add(sr);
                        sr.seq2 = orf;
                        sr.orfStartLoc = currentBlockStartIndex + target.xlsx_data_7 - 1;
                        sr.orfEndLoc = sr.orfStartLoc + target.xlsx_data_8 - 1;
                        currentBlockStartIndex = currentBlockEndIndex + roxpsym2.length() + 1;
                    }
                }

                for(int j=0;j<notEssentialCluster.size();j++){
                    for(int k=0;k<notEssentialCluster.get(j).records.length;k++){
                        ResultEndIntact target=null;
                        for(int m=0;m<resultEnd.size();m++){
                            if(resultEnd.get(m).SGDID.equals(notEssentialCluster.get(j).records[k]))
                                target = resultEnd.get(m);
                        }
                        System.out.println(target);
                        if(target==null) {
                            System.out.print("未在所有基因列表中找到该基因的相关信息:");
                            System.out.println(notEssentialCluster.get(k).records[k]);
                            continue;
                        }
                        if(target.SGDID.equals("S000000030")&& !test){//测试用，需删除
                            k--;
                            test=true;
                        }

                        currentBlockLength = target.str2.length();  // 当前基因的切割序列的长度
                        currentBlockEndIndex = currentBlockStartIndex + currentBlockLength - 1;
                        Step4ResultElement sr = new Step4ResultElement();
                        sr.SGDId = target.SGDID;
                        sr.locusTag = target.locusTag;
                        sr.feature = target.feature;
                        sr.order = order++;
                        sr.groupId = j;
                        sr.essential = target.isEssential;
                        sr.oxpsym = loxpsym;
                        sr.currentBlockStartLoc = currentBlockStartIndex;
                        sr.getCurrentBlockEndLoc = currentBlockEndIndex;
                        String orf = target.str2.substring(target.xlsx_data_7-1,target.xlsx_data_7+ target.xlsx_data_8-1);
                        sr.seq1 =  target.str2;
                        sr.seq2 = orf;
                        sr.orfStartLoc = currentBlockStartIndex + target.xlsx_data_7 - 1;
                        sr.orfEndLoc = sr.orfStartLoc + target.xlsx_data_8 - 1;
                        step4ResultEnd.add(sr);
                        currentBlockStartIndex = currentBlockEndIndex + loxpsym.length() + 1;
                    }
                }
                for (Step4ResultElement sr : step4ResultEnd) {
                    System.out.println(sr);
                }
            }

            step4Result.step4ResultEnd = step4ResultEnd;
            step4Result.essentialCluster = essentialCluster;
            step4Result.notEssentialCluster = notEssentialCluster;
            step4Result.finalSeq = finalSeq.toString();

        } catch (IOException ex) {
            System.out.println("read csv failed!");
            ex.printStackTrace();
        }
//        for (Step4ResultElement s : step4Result.step4ResultEnd) {
//            if(s.existComplement){
//                s.
//            }
//        }
        for (Step4ResultElement step4ResultElement : step4Result.step4ResultEnd) {
            if(step4ResultElement.existComplement){
                StringBuilder sb1 = new StringBuilder(step4ResultElement.seq1);
                StringBuilder sb2 = new StringBuilder(step4ResultElement.seq2);
                step4ResultElement.seq1 = sb1.reverse().toString();;
                step4ResultElement.seq2 = sb2.reverse().toString();
            }
        }
        return step4Result;
    }

        private List<PosElement> extractPos(boolean p1, boolean p2, int currentOver, int nextStart, StringBuilder sb) {
        String str1 = sb.substring(currentOver,nextStart-1);
        int flag = 0;
        if(!p1 && !p2)
            flag = 1;
        if(!p1 && p2)
            flag = 2;
        if(p1 && !p2)
            flag = 3;
        if(p1 && p2)
            flag = 4;

//        System.out.println(str1);
//        System.out.print("\tflag:");
//        System.out.println(flag);
        return filterSplit(flag,str1);
    }

    private List<PosElement> filterSplit(int flag, String str1) {
        String[][] ss = {{"aataaa", "aaaaaa", "aaaaa", "aataa", "aagaa"},{"tttatt", "tttttt", "ttttt", "ttatt", "ttctt"}};
        List<PosElement> posList = new ArrayList<>();

        // flag为1的情况所有情况已检查，全部正确
        if(flag == 1){

            String []subSs = {ss[0][0],ss[0][1]};
            String []subSss = {ss[0][2],ss[0][3],ss[0][4]};
            //System.out.println(Arrays.toString(subSs));
            //System.out.println(Arrays.toString(subSss));
            List<RowData> stri2 = new ArrayList<>();
            if(str1.length() < 500) {
                PosElement pos = new PosElement();
                pos.startLoc = 300;
                pos.endLoc = str1.length() - 499;
                pos.str = "null_00";
                posList.add(pos);
            }
            else{
                stri2 = fiterSs(flag,str1,subSs,subSss);
                if(!stri2.isEmpty()){
                    Collections.sort(stri2, new Comparator<RowData>() {
                        @Override
                        public int compare(RowData o1, RowData o2) {
                            return o1.I.get(0) - o2.I.get(0);
                        }
                    });
//                    System.out.print("stri2:");
//                    System.out.println(stri2);  //3.31日：到这里的结果是正确的
                    List<Integer> arg1 = new ArrayList<>();

                    //arg的size和stri2等长
                    for (RowData rowData : stri2) {
                        arg1.add(rowData.I.get(0));
                    }
                    List<Integer> I = fiterNum(arg1,flag,str1); //从这里找到了唯一的一个位置I
//                    System.out.print("I in fiter_split");
//                    System.out.println(I);  //到这里是正确的
                    if(!I.isEmpty()){
                        List<Integer> SS = new ArrayList<Integer>();
                        //SS的size和
                        for (int i = 0; i < stri2.size(); i++) {
                            if (stri2.get(i).I.get(0) == I.get(0)) {    //找到stri2中I的：下标位置+1
                                SS.add(i+1); // 注意Matlab中下标从1开始，Java中下标从0开始
                            }
                        }
//                        System.out.print("SS in function fiter_split:");
//                        System.out.println(SS);
                        StringBuilder sb = new StringBuilder();
                        for (int i : SS) {
                            sb.append(stri2.get(i-1).subS);
                        }
                        int KKK = sb.toString().length();
//                        System.out.print("KKK in function fiter_split:");
//                        System.out.println(KKK);
                        for(int i=0;i<SS.size();i++){
                            PosElement posElement = new PosElement();
                            posElement.startLoc = I.get(0) + KKK - 1;
                            posElement.endLoc = I.get(0) + KKK;
                            posElement.str = stri2.get(SS.get(i)-1).subS;
                            posList.add(posElement);
                        }
//                        System.out.print("posList in filterSplit 182");
//                        System.out.println(posList);
                    }
                }
                if(posList.isEmpty()){
                    PosElement posElement = new PosElement();
                    if(str1.length() > 800){
                        posElement.startLoc = 300;
                        posElement.endLoc = 301;
                    }
                    else{
                        posElement.startLoc = str1.length() - 500;
                        posElement.endLoc = str1.length() - 499;
                    }
                    posElement.str = "null_00";
                    posList.add(posElement);
                }
            }
        }
        // flag为4的情况所有情况已检查，全部正确
        if(flag == 4){
            if(str1.length() < 500){
                PosElement posElement = new PosElement();
                posElement.startLoc = 500;
                posElement.endLoc = str1.length()-299;
                posElement.str = "null_11";
                posList.add(posElement);
            }
            else{
                String []subSs = {ss[1][0],ss[1][1]};
                String []subSss = {ss[1][2],ss[1][3],ss[1][4]};
                List<RowData> stri2 = fiterSs(flag,str1,subSs,subSss);

                if(!stri2.isEmpty()){
                    Collections.sort(stri2, new Comparator<>() {
                        @Override
                        public int compare(RowData o1, RowData o2) {
                            return o1.I.get(0) - o2.I.get(0);
                        }
                    });
                    List<Integer> arg1 = new ArrayList<>();

                    //arg的size和stri2等长
                    for (RowData rowData : stri2) {
                        arg1.add(rowData.I.get(0));
                    }
                    List<Integer> I = fiterNum(arg1,flag,str1); //从这里找到了唯一的一个位置I
                    if(!I.isEmpty()) {
                        List<Integer> SS = new ArrayList<>();
                        //SS的size和
                        for (int i = 0; i < stri2.size(); i++) {
                            if (stri2.get(i).I.get(0) == I.get(0)) {    //找到stri2中I的：下标位置+1
                                SS.add(i + 1); // 注意Matlab中下标从1开始，Java中下标从0开始
                            }
                        }
//                        System.out.print("SS in function fiter_split:");
//                        System.out.println(SS);
                        StringBuilder sb = new StringBuilder();
                        for (int i : SS) {
                            sb.append(stri2.get(i - 1).subS);
                        }
                        for(int i=0;i<SS.size();i++){
                            PosElement posElement = new PosElement();
                            posElement.startLoc = I.get(0)  - 1;
                            posElement.endLoc = I.get(0);
                            posElement.str = stri2.get(SS.get(i)-1).subS;
                            posList.add(posElement);
                        }
                    }
                }
                if(posList.isEmpty()){
                    PosElement posElement = new PosElement();
                    if(str1.length() > 800){
                        posElement.startLoc = str1.length() - 300;
                        posElement.endLoc = str1.length() - 299;
                    }
                    else{
                        posElement.startLoc = 500;
                        posElement.endLoc = 501;
                    }
                    posElement.str = "null_11";
                    posList.add(posElement);
                }
            }
//            System.out.print("posList in filterSplit 237");
//            System.out.println(posList);
        }
        if(flag==2){
            String []subSs = {ss[0][0],ss[0][1],ss[1][0],ss[1][1]};
            String []subSss = {ss[0][2],ss[0][3],ss[0][4],ss[1][2],ss[1][3],ss[1][4]};
            if(str1.length() > 600){
                PosElement posElement = new PosElement();
                double len = str1.length();
                posElement.startLoc = Math.toIntExact(Math.round(len / 2));
                posElement.endLoc = Math.toIntExact(Math.round(len / 2)) + 1;
                posElement.str = "null_01";
                posList.add(posElement);
            }
            else{
                List<RowData> stri2 = fiterSs(flag,str1,subSs,subSss);
                List<PosElement> posElements = new ArrayList<>();
                if(!stri2.isEmpty()){
                    Collections.sort(stri2, new Comparator<RowData>() {
                        @Override
                        public int compare(RowData o1, RowData o2) {
                            return o1.I.get(0) - o2.I.get(0);
                        }
                    });
                    posElements = find_RL(stri2,flag,str1);


                }
                //PosElement posElement = posElements.get(0);
                if(posElements.isEmpty()){
                    PosElement posElement = new PosElement();
                    double len = str1.length();
                    posElement.startLoc = Math.toIntExact(Math.round(len / 2));
                    posElement.endLoc = Math.toIntExact(Math.round(len / 2)) + 1;
                    posElement.str = "null_01";
                    posList.add(posElement);
                }
                posList.addAll(posElements);
            }
        }
        if(flag==3){
            PosElement posElement = new PosElement();
            if(str1.length()>=1000){
                double len = str1.length();
                posElement.startLoc = Math.toIntExact(Math.round(len / 2));
                posElement.endLoc = Math.toIntExact(Math.round(len / 2)) + 1;
            }
            else{
                posElement.startLoc = 500;
                posElement.endLoc = str1.length()-499;
            }
            posElement.str = "null_10";
            posList.add(posElement);
        }

        return posList;
    }

    //4月1日进度
    private List<PosElement> find_RL(List<RowData> stri2, int flag, String str1) {
        String [][] ss = {{"aataaa", "aaaaaa", "aaaaa", "aataa", "aagaa"},{"tttatt", "tttttt", "ttttt", "ttatt", "ttctt"}};
        List<Integer> arg1 = new ArrayList<>();
//        System.out.print("find_RL received stri2:");
//        System.out.println(stri2);

        List<PosElement> posList = new ArrayList<>();
        //arg的size和stri2等长
        for (RowData rowData : stri2) {
            arg1.add(rowData.I.get(0));
        }
        //SS的size和
        List<Integer> I = fiterNum(arg1,flag,str1); //从这里找到了唯一的一个位置I

        if(I.size() > 0){
            List<Integer> SS = new ArrayList<Integer>();

            for (int i = 0; i < arg1.size(); i++) {
                if (stri2.get(i).I.get(0) == I.get(0)) {    //找到stri2中I的：下标位置+1
                    SS.add(i + 1); // 注意Matlab中下标从1开始，Java中下标从0开始
                }
            }
//            System.out.print("^^^^^^^^^SS in find_RL:");
//            System.out.println(SS);
            int SSVal = SS.get(0);
            String findStr = stri2.get(SSVal - 1).subS;
            int kk = -1;
            int kkk = -1;
            for(int i=0;i<ss.length;i++){
                for(int j=0;j<ss[i].length;j++){
                    if(ss[i][j].equals(findStr)){
                        kk = i + 1;
                        kkk = j + 1;
                    }
                }
            }
            if(kk==1){
                int KKK = findStr.length();
                PosElement posElement = new PosElement();
                posElement.startLoc = I.get(0) + KKK -1;
                posElement.endLoc = I.get(0) + KKK;
                posElement.str = findStr;
                posList.add(posElement);
            }
            else{
                int KKK = findStr.length();
                PosElement posElement = new PosElement();
                posElement.startLoc = I.get(0) - 1;
                posElement.endLoc = I.get(0);
                posElement.str = findStr;
                posList.add(posElement);
            }
        }
//        System.out.print("posList will return at findRL:");
//        System.out.println(posList);
        return posList;
    }

    private List<RowData> fiterSs(int flag, String str1, String[] subSs, String[] subSss) {
//        System.out.print("subSs in fiterSs");
//        System.out.println(Arrays.toString(subSs));
//        System.out.print("subSss in fiterSss");
//        System.out.println(Arrays.toString(subSss));
        List<RowData> stri2 = new ArrayList<RowData>();
        for(int i=0;i<subSs.length;i++){
            List<Integer> I = new ArrayList<Integer>(); // 用于存储子字符串在原字符串中的起始位置
            int index = str1.indexOf(subSs[i]); // 查找子字符串在原字符串中的起始位置
            while (index != -1) { // 当子字符串存在于原字符串中时
                I.add(index); // 将子字符串的起始位置添加到列表中
                index = str1.indexOf(subSs[i], index + 1); // 继续查找子字符串在原字符串中的起始位置
            }
            for(int j=0;j<I.size();j++){
                I.set(j,I.get(j)+1);
            }
//            System.out.print("line 207 display I:");
//            System.out.println(I);
            if(!I.isEmpty()){
                I = fiterNum(I,flag,str1);
            }
            if(!I.isEmpty()){
                RowData rowData = new RowData(); // 创建一个列表，用于存储一行数据
                rowData.I = I; // 将子字符串在原字符串中的起始位置添加到列表中
                rowData.subS = subSs[i]; // 将子字符串添加到列表中
                rowData.OriginalLocation = stri2.size();
                stri2.add(rowData); // 将新的一行数据添加到矩阵末尾
            }
        }
        if(stri2.isEmpty()){
            for(int i=0;i<subSss.length;i++){
                List<Integer> I = new ArrayList<Integer>(); // 用于存储子字符串在原字符串中的起始位置
                int index = str1.indexOf(subSss[i]); // 查找子字符串在原字符串中的起始位置
                while (index != -1) { // 当子字符串存在于原字符串中时
                    I.add(index); // 将子字符串的起始位置添加到列表中
                    index = str1.indexOf(subSss[i], index + 1); // 继续查找子字符串在原字符串中的起始位置
                }
//                System.out.print("line 228 display I:");
//                System.out.println(I);
                for(int j=0;j<I.size();j++){
                    I.set(j,I.get(j)+1);
                }
                if(!I.isEmpty()){
                    I = fiterNum(I,flag,str1);
                }
                if(!I.isEmpty()){
                    RowData rowData = new RowData(); // 创建一个列表，用于存储一行数据
                    rowData.I = I; // 将子字符串在原字符串中的起始位置添加到列表中
                    rowData.subS = subSss[i]; // 将子字符串添加到列表中
                    rowData.OriginalLocation = stri2.size();
                    stri2.add(rowData); // 将新的一行数据添加到矩阵末尾
                }
            }
        }
        return stri2;
    }

    private List<Integer> fiterNum(List<Integer> I, int flag, String str1) {
//        System.out.print("fiterNum received I:");
//        System.out.println(I);
        ArrayList<Integer> pos = new ArrayList<>();
        List<Integer> II = new ArrayList<>();
        for(int i=0;i<I.size();i++){
            II.add(str1.length() - I.get(i));
        }
        if(flag==1){
            if(str1.length() > 800){
                ArrayList<Integer> J = new ArrayList<>();
                for(int j = 0;j < II.size();j++){
                    if (II.get(j) >= 500 && I.get(j) >= 300) {
                        J.add(j);
                    }
                }
                if(!J.isEmpty())
                    pos.add(I.get(J.get(0)));
            }
            else if(str1.length() < 800){
                ArrayList<Integer> J = new ArrayList<>();   //J为符合条件的下标集合
                for(int j = 0;j < II.size();j++){
                    if (II.get(j) >= 500) {
                        J.add(j);
                    }
                }
                if(!J.isEmpty()){
                    pos.add(I.get(J.get(J.size()-1)));  //取I中第（J的最后一个下标）的元素
                }
            }
        }
        if(flag==4){
            if(str1.length() >= 800){
                ArrayList<Integer> J = new ArrayList<>();
                for(int j = 0;j < II.size();j++){
                    if (I.get(j) >= 500 && II.get(j) >= 300) {
                        J.add(j);
                    }
                }
                if(!J.isEmpty()){
                    pos.add(I.get(J.get(J.size()-1)));  //取I中第（J的最后一个下标）的元素
                }
            }
            if(str1.length() >= 500 && str1.length() < 800) {
                ArrayList<Integer> J = new ArrayList<>();
                for (int j = 0; j < I.size(); j++) {
                    if (I.get(j) >= 500)
                        J.add(j);
                }
                if (!J.isEmpty()) {
                    pos.add(I.get(J.get(0)));
                }
            }
            if(str1.length() < 500){
                pos.clear();
            }

        }
        if(flag==2){
            if(str1.length() < 600){
                int index = -1;
                int minDiff = Integer.MAX_VALUE;
                for (int i = 0; i < I.size(); i++) {
                    int diff = Math.abs(I.get(i) - str1.length() / 2);
                    if (diff < minDiff) {
                        minDiff = diff;
                        index = i;
                    }
                }
                int III = minDiff; // 数组I中与长度为str1一半的差值最小的元素的绝对值
                int J = index; // 该元素在数组I中的下标
                if(J!=-1){
                    pos.add(I.get(J));
                }
            }
        }
//        System.out.print("II:");
//        System.out.println(II);
//        System.out.print("fiterNum return pos:");
//        System.out.println(pos);
        return pos;
    }

    //该方法对应matlab代码的157行至
    public ArrayList<SssssElement> extractGene(List<Gene> geneList,List<String> textList,List<Gene> intron){
        ArrayList<SssssElement> result_end = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        System.out.println("textList size:"+textList.size());
        for(int i=0;i<textList.size();i++){
            sb.append(textList.get(i));
        }
        //System.out.println(sb);
        int flag = 0;
        for(int i=0;i < geneList.size() - 1;i++){
            ArrayList<SssssElement> sssss = new ArrayList<>();
            Gene current = geneList.get(i);
            Gene next = geneList.get(i+1);

            if(current.overLoc > next.startLoc+1)
                continue;
            String fragment = sb.substring(current.overLoc,next.startLoc + 1);
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//            System.out.print("fragment start location:");
//            System.out.println(current.overLoc);
//            System.out.print("count:");
//            System.out.println(i);
//            System.out.print("fragment:");
//            System.out.println(fragment);
            ArrayList<Integer> s1 = new ArrayList<Integer>();
            ArrayList<Integer> s2 = new ArrayList<Integer>();
            ArrayList<Integer> s3 = new ArrayList<Integer>();
            ArrayList<Integer> s4 = new ArrayList<Integer>();
            // 在 str1 中查找子串，并将子串出现的位置添加到 s1 中
            int indexs1 = -1;
            while ((indexs1 = fragment.indexOf("aataaa", indexs1 + 1)) != -1) {
                s1.add(indexs1);
            }
            //int s2 = fragment.indexOf("aaaaaa");
            int indexs2 = -1;
            while ((indexs2 = fragment.indexOf("aaaaaa", indexs2 + 1)) != -1) {
                s2.add(indexs2);
            }
            //int s3 = fragment.indexOf("tttttt");
            int indexs3 = -1;
            while ((indexs3 = fragment.indexOf("tttttt", indexs3 + 1)) != -1) {
                s3.add(indexs3);
            }
            //int s4 = fragment.indexOf("tttatt");
            int indexs4 = -1;
            while ((indexs4 = fragment.indexOf("tttatt", indexs4 + 1)) != -1) {
                s4.add(indexs4);
            }

            if(!current.existComplement && !next.existComplement){
                //System.out.print("\tComplement status:");
                //System.out.println("\t0 0");
                ArrayList<Integer> num = new ArrayList<>();
                num.add(s1.size());
                num.add(s2.size());
                //System.out.print("\tnum array:");
                //System.out.println("\t"+num);
                String[] ss = {"aataaa","aaaaaa"};
                ArrayList<SssssElement> sssssloc = new ArrayList<>();
                for(int j=0;j<num.size();j++)
                {
                    if(num.get(j) >= 0){
                        flag = 1;
                        ArrayList<String> sss = new ArrayList<>();
                        if(j==0){
                            for (Integer integer : s1) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        if(j==1){
                            for (Integer integer : s2) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        List<Integer> label = findIntron(sss,intron);

                        //此时得到label之后需要对sssss进行操作，预留***

                        for(int k = 0;k < sss.size();k++){
                            if(label.get(k)==1) {
                                SssssElement sssssElement = new SssssElement();
                                sssssElement.startLocInGeneFragment = Integer.parseInt(sss.get(k)) - 1;
                                sssssElement.ss = ss[j];
                                sssssElement.locInFragment = fragment;
                                sssssElement.fragmentStart = current.overLoc;
                                sssssElement.fragmentEnd = next.overLoc;
                                sssssloc.add(sssssElement);
                            }
                        }
                        System.out.println("\t\t"+sssssloc);
                        System.out.println();
//                        ArrayList<Integer> idx = new ArrayList<Integer>();
//                        for (int k = 0; k < label.size(); k++) {
//                            if (label.get(k) == 1) {
//                                idx.add(k);
//                            }
//                        }
//                        // 提取 sss 中所有等于 1 的位置处的元素，并将其转换为单元格数组
//                        ArrayList<Object> ss1 = new ArrayList<Object>();
//                        for (int k = 0; k < idx.size(); k++) {
//                            int f = idx.get(k);
//                            ss1.add(sss.get(f));
//
//                        }
//                        ArrayList<ArrayList<Object>> sss1 = new ArrayList<ArrayList<Object>>();
//                        sss1.add(ss1);
//                        // 从 ss 向量中提取第 jjj 个元素，并添加到 sss1 中
//                        sss1.add(new ArrayList<Object>());
//                        sss1.get(1).add(ss[j]);
                    }
                }

                if(flag==0){
                    sssss.clear();
                }
                sssss.addAll(sssssloc);
//                for(int j=0;j<2;j++){
//                    if(num[j] >= 0){
//                        int flag = 1;
//                        String sss="";
//                        if(j==0)    sss = String.valueOf(s1 + current.overLoc);
//                        if(j==1)    sss = String.valueOf(s2 + current.overLoc);
//                        this.findIntron(sss);
//                    }
//                }
            }
            if(!current.existComplement && next.existComplement){
                //System.out.print("\tComplement status:");
                //System.out.println("\t0 1");
                String[] ss = {"aataaa", "aaaaaa", "tttttt", "tttatt"};
                ArrayList<Integer> num = new ArrayList<>();
                num.add(s1.size());
                num.add(s2.size());
                num.add(s3.size());
                num.add(s4.size());
                System.out.println("\t"+num);
                ArrayList<SssssElement> sssssloc = new ArrayList<>();
                for(int j=0;j<num.size();j++)
                {
                    if(num.get(j) >= 0){
                        flag = 1;
                        ArrayList<String> sss = new ArrayList<>();
                        if(j==0){
                            for (Integer integer : s1) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        if(j==1){
                            for (Integer integer : s2) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        if(j==2){
                            for (Integer integer : s3) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        if(j==3){
                            for (Integer integer : s4) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        List<Integer> label = findIntron(sss,intron);
                        //预留sssss***
                        for(int k = 0;k < sss.size();k++){
                            if(label.get(k)==1) {
                                SssssElement sssssElement = new SssssElement();
                                sssssElement.startLocInGeneFragment = Integer.parseInt(sss.get(k)) - 1;
                                sssssElement.ss = ss[j];
                                sssssElement.locInFragment = fragment;
                                sssssElement.fragmentStart = current.overLoc;
                                sssssElement.fragmentEnd = next.overLoc;
                                sssssloc.add(sssssElement);
                            }
                        }
                        System.out.println("\t\t"+sssssloc);
                        System.out.println();
                    }
                }
                sssss.addAll(sssssloc);

            }
            if(current.existComplement && !next.existComplement){
                //System.out.print("\tComplement status:");
                //System.out.println("\t1 0");
                ArrayList<SssssElement> sssssloc = new ArrayList<>();
                //ArrayList<SssssElement> sssss = new ArrayList<>();
                SssssElement sssssElement = new SssssElement();
                sssssElement.startLocInGeneFragment = fragment.length() / 2 + current.overLoc;
                sssssElement.ss = "none";
                sssssElement.locInFragment = fragment;
                sssssElement.fragmentStart = current.overLoc;
                sssssElement.fragmentEnd = next.overLoc;
                sssssloc.add(sssssElement);

                //System.out.println("\t\t"+sssssloc);
                sssss.addAll(sssssloc);
                //System.out.println();
            }
            if(current.existComplement && next.existComplement){
                //System.out.print("\tComplement status:");
                //System.out.println("\t1 1");
                String[] ss = {"tttttt", "tttatt"};
                ArrayList<Integer> num = new ArrayList<>();
                num.add(s3.size());
                num.add(s4.size());
                System.out.println("\t"+num);
                ArrayList<SssssElement> sssssloc = new ArrayList<>();
                for(int j=0;j<num.size();j++)
                {
                    if(num.get(j) >= 0){
                        flag = 1;
                        ArrayList<String> sss = new ArrayList<>();
                        if(j==0){
                            for (Integer integer : s1) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }
                        if(j==1){
                            for (Integer integer : s2) {
                                sss.add(String.valueOf(integer + current.overLoc + 1));
                            }
                        }

                        List<Integer> label = findIntron(sss,intron);
                        //预留sssss***
                        for(int k = 0;k < sss.size();k++){
                            if(label.get(k)==1) {
                                SssssElement sssssElement = new SssssElement();
                                sssssElement.startLocInGeneFragment = Integer.parseInt(sss.get(k)) - 1;
                                sssssElement.ss = ss[j];
                                sssssElement.locInFragment = fragment;
                                sssssElement.fragmentStart = current.overLoc;
                                sssssElement.fragmentEnd = next.overLoc;
                                sssssloc.add(sssssElement);
                            }
                        }
//                        System.out.println("\t\t"+sssssloc);
//                        System.out.println();
                    }
                }
                sssss.addAll(sssssloc);
            }
            result_end.addAll(sssss);

//            System.out.print("result_end length:");
//            System.out.println(result_end.size());

        }
        return result_end;
    }

    public List<Integer> findIntron(ArrayList<String> sss,List<Gene> ty_end){
        System.out.print("\t\tReceived sss:");
        System.out.println(sss);
        //ArrayList<Gene> ty_end = new ArrayList<>();
//        String tyEndFileName = "src\\main\\resources\\static\\ty_end.txt";
//        File file  = new File(tyEndFileName);
//        try {
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);
//            String line = br.readLine();
//            while(line!=null){
//                String []lineData = line.trim().split("  ");
//                //System.out.println("read line from ty_end.txt , current line is :");
//                //System.out.println(Arrays.toString(lineData));
//                Gene gene  = new Gene();
//                gene.geneNote = lineData[0].trim().substring(8,15);
//                //System.out.println(lineData[1]);
//                //System.out.println(lineData[2]);
//                //System.out.println(lineData[1].trim().charAt(1));
//                gene.existComplement = lineData[2].trim().charAt(1)=='1';
//                gene.startLoc = Integer.parseInt(lineData[4].trim().substring(1,lineData[4].trim().length()-1));
//                //System.out.println(gene.startLoc);
//                gene.overLoc = Integer.parseInt(lineData[6].trim().substring(1,lineData[6].trim().length()-1));
//                //System.out.println(gene.overLoc);
//                //System.out.println(gene);
//                ty_end.add(gene);
//                line = br.readLine();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("\t\tRead ty_end.txt finished!");
        ArrayList<Integer> label = new ArrayList<>();
        for(int i=0;i<sss.size();i++){
            int lag = 1;
            for(int j=0;j<ty_end.size();j++){
                if(Integer.parseInt(sss.get(i)) > ty_end.get(j).startLoc && Integer.parseInt(sss.get(i)) < ty_end.get(j).overLoc)
                    lag = 0;
            }
            label.add(lag);
        }
        System.out.print("\t\tlabel array finally returned:");
        System.out.println(label);
        return label;
    }

    public List<Gene> getIntron(List<Gene> allGenes){
        ArrayList<Gene> intron = new ArrayList<>();
        for (Gene gene : allGenes) {
            if(gene.isNote)
                intron.add(gene);
        }
        return intron;
    }

    //给定一个被删除的基因列表，若geneSet里有基因在这个列表里，则将其从geneSet移除，放到deleteSet中，根据retDeleteSet的值返回对应的集合
    //retDeleteSet为true，则返回原本存在与geneSet但被移除的基因集合，否则返回移除了被删除基因的基因集合
    public ArrayList<Gene> checkGene(ArrayList<String> deletedGenes, Set<Gene> geneSet, boolean retDeleteSet){
        ArrayList <Gene> deleteSet = new ArrayList<>();
        System.out.println(geneSet.size());
        System.out.println("geneSet in method:");
        System.out.println(geneSet);
        ArrayList<Gene> newGeneSet = new ArrayList<>(geneSet);
        System.out.println(newGeneSet.size());
//        for(Gene gene:geneSet){
//            if(gene.locusTag==null) continue;
//            boolean ctn = deletedGenes.contains(gene.locusTag);
//            if (ctn) {
//                deleteSet.add(gene);
//                geneSet.remove(gene);
//            }
//        }
        System.out.println("******************");
        for(int i=0;i<newGeneSet.size();i++){
            System.out.println(i);
            System.out.println(newGeneSet.get(i).locusTag);
            if(newGeneSet.get(i).locusTag==null) continue;
            boolean ctn = deletedGenes.contains(newGeneSet.get(i).locusTag);
            //System.out.println(newGeneSet.get(i).locusTag);
            if("YFL056C".equals(newGeneSet.get(i).locusTag))
                System.out.println("true");
            if (ctn) {
                deleteSet.add(newGeneSet.get(i));
                //newGeneSet.remove(newGeneSet.get(i));
            }
        }
        newGeneSet.removeAll(deleteSet);
        System.out.println("####################");
        System.out.println(deleteSet.size());
        if(retDeleteSet)    return deleteSet;
        else return newGeneSet;
    }


    public Set<Gene> processGBFile(String GBFileName){
        //ArrayList<String> geneName = new ArrayList<>();
        Set<Gene> geneSet = new HashSet<Gene>();
        //Set set;
        //set.re
        try {
            ClassPathResource classPathResource = new ClassPathResource(GBFileName);
            InputStream inputStream =classPathResource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            String singleGeneName = "";


            while(line != null){
                line = line.trim();
                if(line.contains("gene  ")){
                    singleGeneName = line;
                }
                if(line.contains("/locus_tag")){
                    Gene gene = new Gene();
                    StringBuilder startLoc = new StringBuilder();
                    StringBuilder overLoc = new StringBuilder();
                    boolean isFirstNum = true;

                    //找到起始位置和终止位置
                    for(int i=0;i<singleGeneName.length();i++){
                        char a = singleGeneName.charAt(i);
                        if(a>='0' && a<='9' && isFirstNum) {
                            startLoc.append(a);
                        }
                        if(a=='.')
                            isFirstNum = false;
                        if( a>='0' && a<='9' && !isFirstNum)
                            overLoc.append(a);
                    }
                    if(singleGeneName.contains("complement"))
                        gene.existComplement = true;
                    else gene.existComplement = false;

                    gene.startLoc = Integer.parseInt(startLoc.toString());
                    gene.overLoc = Integer.parseInt(overLoc.toString());
                    StringBuilder locusTag = new StringBuilder();
                    boolean canStart = false;
                    line = line.trim();
                    //获取locusTag的值
                    for(int i=0;i<line.length()-1;i++){
                        if(line.charAt(i)=='"'){
                            canStart = true;
                            continue;
                        }
                        if(canStart)
                            locusTag.append(line.charAt(i));
                    }
                    gene.locusTag = locusTag.toString();
                    gene.isNote = false;
                    geneSet.add(gene);
                }
                if(line.contains("repeat_region")) {
                    System.out.println(line);
                    Gene gene = new Gene();
                    String repeatGeneName = line;
                    line = reader.readLine();
                    line = line.trim();
                    gene.existComplement = repeatGeneName.contains("complement");
                    if(line.contains("/note"))
                    {
                        StringBuilder startLoc = new StringBuilder();
                        StringBuilder overLoc = new StringBuilder();
                        boolean isFirstNum = true;

                        for(int i=0;i<repeatGeneName.length();i++){
                            char a = repeatGeneName.charAt(i);
                            if(a>='0' && a<='9' && isFirstNum) {
                                startLoc.append(a);
                            }
                            if(a=='.')
                                isFirstNum = false;
                            if( a>='0' && a<='9' && !isFirstNum)
                                overLoc.append(a);
                        }
                        gene.startLoc = Integer.parseInt(startLoc.toString());
                        gene.overLoc = Integer.parseInt(overLoc.toString());
                        StringBuilder note = new StringBuilder();
                        boolean canStart = false;
                        line = line.trim();
                        //获取locusTag的值
                        for(int i=0;i<line.length()-1;i++){
                            if(line.charAt(i)=='"')
                                canStart = true;
                            if(canStart)
                                note.append(line.charAt(i));
                        }
                        gene.geneNote = note.toString();

                        gene.isNote = true;
                        geneSet.add(gene);


                    }
                }
                if(line.contains("mobile_element  ")){
                    Gene g = new Gene();
                    g.isNote = true;
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(line);
                    int i=0;
                    while (matcher.find()) {
                        if(i==0) {
                            g.startLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                        else if(i==1) {
                            g.overLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                    }
                    geneSet.add(g);
                }
                if(line.contains("tRNA  ")){
                    Gene g = new Gene();
                    g.type = "tRNA";
                    if(line.contains("complement"))
                        g.existComplement = true;
                    if(line.contains("join")){
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        int i=0;
                        while (matcher.find()) {
                            if(i==0) {
                                g.startLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==1) {
                                g.overLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==2){
                                g.startLoc2 = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==3){
                                g.overLoc2 = Integer.parseInt(matcher.group());
                                i++;
                            }
                        }
                    }
                    else{
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        int i=0;
                        while (matcher.find()) {
                            if(i==0) {
                                g.startLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==1) {
                                g.overLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                        }
                    }

                    geneSet.add(g);
                }
                if(line.contains("rep_origin")){
                    Gene g = new Gene();
                    g.type = "ARS";
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(line);
                    int i=0;
                    while (matcher.find()) {
                        if(i==0) {
                            g.startLoc = Integer.parseInt(matcher.group());
                            i++;

                        }
                        else if(i==1) {
                            g.overLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                    }
                    if(line.contains("complement"))
                        g.existComplement=true;
                    geneSet.add(g);
                }
                if(line.contains("ncRNA  ")){
                    Gene g = new Gene();
                    g.type = "ncRNA";
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(line);
                    int i=0;
                    while (matcher.find()) {
                        if(i==0) {
                            g.startLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                        else if(i==1) {
                            g.overLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                    }
                    if(line.contains("complement"))
                        g.existComplement=true;
                    geneSet.add(g);
                }

                if(line.contains("snoRNA  ")){
                    Gene g = new Gene();
                    g.type = "snoRNA";
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(line);
                    int i=0;
                    while (matcher.find()) {
                        if(i==0) {
                            g.startLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                        else if(i==1) {
                            g.overLoc = Integer.parseInt(matcher.group());
                            i++;
                        }
                    }
                    if(line.contains("complement"))
                        g.existComplement=true;
                }
                if(line.contains("CDS  ")){
                    Gene g = new Gene();
                    //.geneNote = false;
                    g.type = "CDS";
                    if(line.contains("complement"))
                        g.existComplement = true;
                    if(line.contains("join")){
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        int i=0;
                        while (matcher.find()) {
                            if(i==0) {
                                g.startLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==1) {
                                g.overLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==2){
                                g.startLoc2 = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==3){
                                g.overLoc2 = Integer.parseInt(matcher.group());
                                i++;
                            }
                        }
                    }
                    else{
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        int i=0;
                        while (matcher.find()) {
                            if(i==0) {
                                g.startLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==1) {
                                g.overLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                        }
                    }

                    geneSet.add(g);
                }
                if(line.contains("mRNA  ")){
                    Gene g = new Gene();
                    g.type = "mRNA";
                    if(line.contains("complement"))
                        g.existComplement = true;
                    if(line.contains("join")){
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        int i=0;
                        while (matcher.find()) {
                            if(i==0) {
                                g.startLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==1) {
                                g.overLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==2){
                                g.startLoc2 = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==3){
                                g.overLoc2 = Integer.parseInt(matcher.group());
                                i++;
                            }
                        }
                    }
                    else{
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        int i=0;
                        while (matcher.find()) {
                            if(i==0) {
                                g.startLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                            else if(i==1) {
                                g.overLoc = Integer.parseInt(matcher.group());
                                i++;
                            }
                        }
                    }

                    geneSet.add(g);
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("打开.gb文件发生错误!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(".gb文件读取错误");
            e.printStackTrace();
        }
        return geneSet;
    }


    //读取fasta文件，若choose参数为"tag"，则返回该文件所有的注释行，否则返回所有数据行
    public static ArrayList<String> fastaBufferedReader(String path, String choose) {//返回值类型是新建集合大类，此处是Set而非哈希。
        BufferedReader reader;
        ArrayList<String> tag = new java.util.ArrayList<String>();
        ArrayList<String> fasta = new java.util.ArrayList<String>();
        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            InputStream inputStream =classPathResource.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while (line != null) {//多次匹配带有“>”的行，\w代表0—9A—Z_a—z，需要转义。\W代表非0—9A—Z_a—z。
                if (line.matches(">[\\w*|\\W*]*")){
                    tag.add(line);
                }else{
                    String lowLine = line.toLowerCase();
                    fasta.add(lowLine);
                }
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (choose.equals("tag")){
            return tag;
        }
        return fasta;
    }

    public static String httpRequest(String requestUrl,Map params) {
        RestTemplate restTemplate = new RestTemplate();
        String str = restTemplate.getForObject("https://david.ncifcrf.gov/api.jsp?type=ENTREZ_GENE_ID&ids=2919,6347,6348,6364&tool=summary",String.class);
        System.out.println(str);
        return "";
    }

    public static String urlencode(Map<String,Object>data) {
        //将map里的参数变成像 showapi_appid=###&showapi_sign=###&的样子
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
