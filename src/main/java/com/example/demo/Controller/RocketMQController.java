package com.example.demo.Controller;

import com.example.demo.Entitys.*;
import com.example.demo.Services.Step1Service;
import com.example.demo.Util.CommonResult;
import com.example.demo.Util.TokenHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class RocketMQController {
    @Autowired
    Step1Service step1Service;
    @Autowired
    TokenHelper th;

//    @GetMapping
//    @ResponseBody
//    public CommonResult keepGene(String geneName,int startLoc,int endLoc,int complement){
//
//    }

    @PostMapping("/test")
    @ResponseBody
    public CommonResult readMission(@RequestBody ReadMissionEntity re){
        boolean tokenExist = th.searchToken(re.token);
        if(!tokenExist){
            return CommonResult.failed("token失效，请重新登录");
        }
        System.out.println("print addList in controller");
        for (Gene gene : re.addList) {
            System.out.println(gene);
        }
        Step1Result step1Result = step1Service.s1(re.addList,re.chromosome);
        Step2Result step2Result = step1Service.s2(step1Result.geneEndList,step1Result.textList,step1Result.ARS,step1Result.tRNAs,step1Result.ncRNA,step1Result.intron,step1Result.CDS,step1Result.mRNA);
        Step3Result step3Result = step1Service.s3(step2Result.step2FinalResultEnd,step2Result.sb,step2Result.ARS,step2Result.tRNAs,step2Result.ncRNA,step2Result.intron,step2Result.CDS,step2Result.mRNA);
        DavidParam dpEssential = new DavidParam();
        dpEssential.overlap = 4;
        dpEssential.finalSeed = 4;
        dpEssential.initialSeed = 4;
        dpEssential.kappa = 35;
        dpEssential.linkage = 0.5;


        Step4Result step4Result = step1Service.s4(step3Result.resultEnd,step3Result.sb,false,dpEssential,dpEssential,"","");
        AllStepResult allStepResult = new AllStepResult();
        allStepResult.step1Result = step1Result;
        allStepResult.step2Result = step2Result;
        allStepResult.step3Result = step3Result;
        allStepResult.step4Result = step4Result;
        return CommonResult.success(allStepResult,"成功");
    }

    @PostMapping("/step4")
    @ResponseBody
    public CommonResult step4ReadMission(@RequestBody Step4ReadMissionEntity re){
        boolean tokenExist = th.searchToken(re.token);
        if(!tokenExist){
            return CommonResult.failed("token失效，请重新登录");
        }
        System.out.println("print addList in controller");
        for (Gene gene : re.addList) {
            System.out.println(gene);
        }
        Step1Result step1Result = step1Service.s1(re.addList, re.chromosome);
        Step2Result step2Result = step1Service.s2(step1Result.geneEndList,step1Result.textList,step1Result.ARS,step1Result.tRNAs,step1Result.ncRNA,step1Result.intron,step1Result.CDS,step1Result.mRNA);
        Step3Result step3Result = step1Service.s3(step2Result.step2FinalResultEnd,step2Result.sb,step2Result.ARS,step2Result.tRNAs,step2Result.ncRNA,step2Result.intron,step2Result.CDS,step2Result.mRNA);
        DavidParam dpEssential = new DavidParam();
        dpEssential.overlap = re.essentialGeneParam.overlap;
        dpEssential.finalSeed = re.essentialGeneParam.finalSeed;
        dpEssential.initialSeed = re.essentialGeneParam.initialSeed;
        dpEssential.kappa = re.essentialGeneParam.kappa;
        dpEssential.linkage = re.essentialGeneParam.linkage;

        DavidParam dpNotEssential = new DavidParam();
        dpNotEssential.overlap = re.notEssentialGeneParam.overlap;
        dpNotEssential.finalSeed = re.notEssentialGeneParam.finalSeed;
        dpNotEssential.initialSeed = re.notEssentialGeneParam.initialSeed;
        dpNotEssential.kappa = re.notEssentialGeneParam.kappa;
        dpNotEssential.linkage = re.notEssentialGeneParam.linkage;
        Step4Result step4Result = step1Service.s4(step3Result.resultEnd,step3Result.sb,false,dpEssential,dpNotEssential,"","");
        AllStepResult allStepResult = new AllStepResult();
        allStepResult.step1Result = step1Result;
        allStepResult.step2Result = step2Result;
        allStepResult.step3Result = step3Result;
        allStepResult.step4Result = step4Result;
        return CommonResult.success(allStepResult,"成功");
    }

    @PostMapping("/step4/tel")
    @ResponseBody
    public CommonResult step4TelReadMission(@RequestBody Step4TelReadMissionEntity re){
        boolean tokenExist = th.searchToken(re.token);
        if(!tokenExist){
            return CommonResult.failed("token失效，请重新登录");
        }
        System.out.println("print addList in controller");
        for (Gene gene : re.addList) {
            System.out.println(gene);
        }
        Step1Result step1Result = step1Service.s1(re.addList, re.chromosome);
        Step2Result step2Result = step1Service.s2(step1Result.geneEndList,step1Result.textList,step1Result.ARS,step1Result.tRNAs,step1Result.ncRNA,step1Result.intron,step1Result.CDS,step1Result.mRNA);
        Step3Result step3Result = step1Service.s3(step2Result.step2FinalResultEnd,step2Result.sb,step2Result.ARS,step2Result.tRNAs,step2Result.ncRNA,step2Result.intron,step2Result.CDS,step2Result.mRNA);
        DavidParam dpEssential = new DavidParam();
        dpEssential.overlap = re.essentialGeneParam.overlap;
        dpEssential.finalSeed = re.essentialGeneParam.finalSeed;
        dpEssential.initialSeed = re.essentialGeneParam.initialSeed;
        dpEssential.kappa = re.essentialGeneParam.kappa;
        dpEssential.linkage = re.essentialGeneParam.linkage;

        DavidParam dpNotEssential = new DavidParam();
        dpNotEssential.overlap = re.notEssentialGeneParam.overlap;
        dpNotEssential.finalSeed = re.notEssentialGeneParam.finalSeed;
        dpNotEssential.initialSeed = re.notEssentialGeneParam.initialSeed;
        dpNotEssential.kappa = re.notEssentialGeneParam.kappa;
        dpNotEssential.linkage = re.notEssentialGeneParam.linkage;
        Step4Result step4Result = step1Service.s4(step3Result.resultEnd,step3Result.sb,true,dpEssential,dpNotEssential,re.LTelomere,re.RTelomere);
        AllStepResult allStepResult = new AllStepResult();
        allStepResult.step1Result = step1Result;
        allStepResult.step2Result = step2Result;
        allStepResult.step3Result = step3Result;
        allStepResult.step4Result = step4Result;
        return CommonResult.success(allStepResult,"成功");
    }



    @GetMapping("/logTest")
    public String myLogAndAspectTest(){
        //Logger logger = LoggerFactory.getLogger(WriteLog.class);
        //logger.info("记录前置通知");
        String requestUrl = "https://david.ncifcrf.gov/api.jsp?type=ENTREZ_GENE_ID&ids=2919,6347,6348,6364&tool=gene2gene";
        //params用于存储要请求的参数
        Map params = new HashMap();

        String string = step1Service.httpRequest(requestUrl,params);
        //处理返回的JSON数据并返回
        //JSONObject pageBean = JSONObject.fromObject(string).getJSONObject("showapi_res_body");
        System.out.println(string);


        System.out.println(this.getClass().toString()+"Receive a request know!");
        return "TestOver";
    }

//    @ResponseBody
//    @GetMapping("/cluster")
//    public List<GeneCluster> getGeneClassfication()
//    {
//        GeneClusterReportClient geneClusterReportClient = new GeneClusterReportClient();
//        List<GeneCluster> result = geneClusterReportClient.invokeService();
//        return result;
//    }


}
