package com.LiuC.ILP.tripleILP;

import com.LiuC.ILP.Common.StringControl;
import com.LiuC.common.NumStatistics;
import com.LiuC.common.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class CalTripILPdata {
    public static void main(String[] args) throws IOException{
        ArrayList<String> fileInList = new ArrayList<>();

        //windows
        String tripleFold="E:\\project\\entityLinking\\data\\triple\\";
        String[] tripleFileAppend = {"triple_all.noCon_100000","triple_all.noCon_100001"};
        String qaFile="E:\\project\\entityLinking\\data\\triple\\cqa_triple_all.test";
        String outFile="E:\\project\\entityLinking\\data\\triple\\cqa_result_no_combine_";
        String type="c";  //c:combine   //n:no combine
        for(String each:tripleFileAppend){
            fileInList.add(tripleFold+each);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("please input \"c\"(combine question) or \"n\"(no combine question)");
        type=br.readLine();
        if(!type.equalsIgnoreCase("c")&&!type.equalsIgnoreCase("n")){
            System.out.println("input error, exit!");
        }
//        ////windows
//        CalTripILPdata calTripILPdata=new CalTripILPdata();
//        calTripILPdata.tripleILPdata(fileInList,qaFile,outFile,type);




//// linux by command
////  java -jar entityLinker.jar /data/liuc/entityLinker/data/triple/triple_all.all /data/liuc/entityLinker/data/triple/cqa_triple_all.all /data/liuc/entityLinker/data/triple/cqa_result_no_combine_no_objectConflict_ > /data/liuc/entityLinker/data/triple/log_no_combine_no_objectConflict_20170101
//        tripleFile=args[0];
//        qaFile=args[1];
//        outFile=args[2];


//        // linux input by program
//
//        //original qa file
//        qaFile="/data/liuc/entityLinker/data/triple/cqa_triple_all.all";
//
//        //baidu zhidao qa file
//        qaFile="/data/liuc/entityLinker/data/triple/cqa_triple_baiduzhidao_all";
//
//
//        tripleFold="/data/liuc/entityLinker/data/triple_final/";
//        // 6 triples
//        String []tripleFileAppend2 ={"triple_baiduValueNormalized_noId"};//,"triple_bookValueNormalized_noId","triple_hudongValueNormalized_noId","triple_movieValueNormalized_noId","triple_musicValueNormalized_noId","triple_noCon_all"};//};//
//        outFile="/data/liuc/entityLinker/data/triple_final/cqa_result_baiduzhidao_all_noCombine_noObjectConflict_menObjOverOne_subNotEqualObj_noTripleRepeat_baiduTriples_";
//
////        // original triple
////        String [] tripleFileAppend2 ={"triple_all.all"};
////        outFile="/data/liuc/entityLinker/data/triple/cqa_result_no_combine_no_objectConflict_menOverOne_subNotEqualObj_originalTriples_";
//
//        fileInList=new ArrayList<>();
//        // merge triple
//        for(String each:tripleFileAppend2){
//            fileInList.add(tripleFold+each);
//        }
//
//        CalTripILPdata calTripILPdata=new CalTripILPdata();
//        calTripILPdata.tripleILPdata(fileInList,qaFile,outFile,type);





//        // load one triple file
//        String [] outfileAppends = {"baidu","book","hudong","movie","music"};
//        for(int i=0;i<tripleFileAppend2.length;i++){
//            String tripleAppend=tripleFileAppend2[i];
//            String outfile_append=outfileAppends[i];
//            outFile = "/data/liuc/entityLinker/data/triple/cqa_result_no_combine_no_objectConflict_menOverOne_subNotEqualObj_"+outfile_append+"Triples_";
//            fileInList=new ArrayList<>();
//            String triplefile = tripleFold+tripleAppend;
//            System.out.println("===========================");
//            System.out.println("start:"+triplefile);
//            System.out.println("===========================");
//            fileInList.add(triplefile);
//            CalTripILPdata calTripILPdata=new CalTripILPdata();
//            calTripILPdata.tripleILPdata(fileInList,qaFile,outFile,type);
//        }




        // linux input by program
        tripleFold="/data/liuc/entityLinker/data/triple_final/";
        String[] qa_files = {"cqa_triple_original_all","cqa_triple_baiduzhidao_all","cqa_triple_original_baiduzhidao_all"};
        // 6 triples
        String []tripleFileAppend2 ={"triple_original_noCon","triple_5","triple_original_5"};//,"triple_bookValueNormalized_noId","triple_hudongValueNormalized_noId","triple_movieValueNormalized_noId","triple_musicValueNormalized_noId","triple_noCon_all"};//};//
        //outFile="/data/liuc/entityLinker/data/triple_final/cqa_result_baiduzhidao_all_noCombine_noObjectConflict_menObjOverOne_subNotEqualObj_noTripleRepeat_baiduTriples_";

        for (String each_qa_file:qa_files){
            for(String each_tripleFileAppend2:tripleFileAppend2){
                System.out.println("***********************************************************************************");
                System.out.println("qa file start :"+each_qa_file);
                System.out.println("triple file start :"+each_tripleFileAppend2);
                fileInList=new ArrayList<>();
                fileInList.add(tripleFold+each_tripleFileAppend2);
                outFile = tripleFold+"result_"+each_qa_file+"_"+each_tripleFileAppend2+"_no_combine_no_objectConflict_menOverOne_subNotEqualObj_originalTriples_";
                CalTripILPdata calTripILPdata=new CalTripILPdata();
                calTripILPdata.tripleILPdata(fileInList,tripleFold+each_qa_file,outFile,type);
                System.out.println("qa file end :"+each_qa_file);
                System.out.println("triple file end :"+each_tripleFileAppend2);
            }
        }

    }

    /**
     * load file and calculate ILP
     * @param fileInList
     * @param qaFile
     * @param outFile
     * @param type
     */
    public static void tripleILPdata(ArrayList<String> fileInList,String qaFile,String outFile,String type){

        CalTripILPdata calTripILPdata=new CalTripILPdata();
        ArrayList<String> qa_triple_match=new ArrayList<>();
        ArrayList<String> qa_triple_no_match=new ArrayList<>();

        ArrayList<Triple> triples=new ArrayList<>();
        HashMap<String,ArrayList<Integer>> mention4tripID=new HashMap<>();
        LoadData loadData = new LoadData();
        loadData.loadTriple(fileInList,triples,mention4tripID);

        HashMap<String,ArrayList<String>> QApair=new HashMap<>();
        loadData.loadCqaTriple(qaFile,QApair);

        //statistic the mention length
        ArrayList<Double> menLengthStatic=new ArrayList<>();
        NumStatistics numStatistic=new NumStatistics();
        numStatistic.getMenLengthResult(mention4tripID,menLengthStatic);

        String questionTotalNum="questionTotalNum";
        String ansOneTriple="ansOneTriple";
        String ansOneMoreTriples="ansOneMoreTriples";
        String noAnsTriple="noAnsTriple";
        HashMap<String,Integer> statisNum=new HashMap<>();
        int initNum=0;
        statisNum.put(questionTotalNum,initNum);
        statisNum.put(ansOneTriple,initNum);
        statisNum.put(ansOneMoreTriples,initNum);
        statisNum.put(noAnsTriple,initNum);


        for(Map.Entry<String,ArrayList<String>> entry:QApair.entrySet()){
            String ques=entry.getKey();
            ArrayList<String> ansList=entry.getValue();
            if(type.equalsIgnoreCase("c")){
                StringBuffer ans_ILP=new StringBuffer();
                StringBuffer ans_write=new StringBuffer();
                for(int i=0;i<ansList.size();i++){
                    if (i!=0){
                        ans_ILP.append("\t");
                    }
                    ans_ILP.append(ansList.get(i));

                    ans_write.append("\t");
                    ans_write.append("answer");
                    ans_write.append(i);
                    ans_write.append(":");
                    ans_write.append(ansList.get(i));
                }
                ArrayList<Integer> tripleResult=new ArrayList<>();
                String answer_triple=calTripILPdata.ILPmodel(ques,ans_ILP.toString().toString(),mention4tripID,triples,tripleResult);

                statisNum.put(questionTotalNum,statisNum.get(questionTotalNum)+1);

                if (!answer_triple.equals("")&&answer_triple!=null){
                    calTripILPdata.ansTripleNum(tripleResult,statisNum,ansOneTriple,ansOneMoreTriples);
                    StringBuffer all_buffer=new StringBuffer();
                    all_buffer.append("question:");
                    all_buffer.append(ques);
                    all_buffer.append(ans_write);
                    all_buffer.append(answer_triple);
                    qa_triple_match.add(all_buffer.toString());
                }
                else {
                    statisNum.put(noAnsTriple,statisNum.get(noAnsTriple)+1);
                    qa_triple_no_match.add("question:"+ques+ans_write.toString());
                }
            }
            else if(type.equalsIgnoreCase("n")){
                ArrayList<String> no_repeat_triplt = new ArrayList<>();
                for(int i=0;i<ansList.size();i++){
                    String ans_origin=ansList.get(i);
                    ArrayList<Integer> tripleResult=new ArrayList<>();
                    String answer_triple=calTripILPdata.ILPmodel(ques,ans_origin,mention4tripID,triples,tripleResult);
                    if (no_repeat_triplt.contains(answer_triple)){
                        continue;
                    }
                    else{
                        no_repeat_triplt.add(answer_triple);
                    }

                        statisNum.put(questionTotalNum,statisNum.get(questionTotalNum)+1);
                    if (!answer_triple.equals("")&&answer_triple!=null){
                        calTripILPdata.ansTripleNum(tripleResult,statisNum,ansOneTriple,ansOneMoreTriples);

                        StringBuffer all_buffer=new StringBuffer();
                        all_buffer.append("question:");
                        all_buffer.append(ques);
                        all_buffer.append("\tanswer:");
                        all_buffer.append(ans_origin);
                        all_buffer.append(answer_triple);
                        qa_triple_match.add(all_buffer.toString());
                    }
                    else {
                        statisNum.put(noAnsTriple,statisNum.get(noAnsTriple)+1);
                        qa_triple_no_match.add("question:"+ques+"\tanswer:"+ans_origin);
                    }
                }
            }
        }

        FileUtil fileUtil=new FileUtil();
        String statisticNum="total question:"+statisNum.get(questionTotalNum)+"\tmore than one triple matched:"+statisNum.get(ansOneMoreTriples)+"\tone triple matched:"+statisNum.get(ansOneTriple)+"\tno triple matched:"+statisNum.get(noAnsTriple);
        System.out.println(statisticNum);
        fileUtil.writeLines(outFile+"match_triple",statisticNum);
        fileUtil.writeLinesAppend(outFile+"match_triple",qa_triple_match);
        fileUtil.writeLines(outFile+"no_match_triple",statisticNum);
        fileUtil.writeLinesAppend(outFile+"no_match_triple",qa_triple_no_match);
    }

    /**
     * static the number of one or more matched triple
     * @param tripleResultInteger
     * @param ansOneTriple
     * @param ansOneMoreTriples
     */
    public static void ansTripleNum(ArrayList<Integer> tripleResultInteger,HashMap<String,Integer> statisNum,String ansOneTriple,String ansOneMoreTriples){
        if (tripleResultInteger.size()==1){
            statisNum.put(ansOneTriple,statisNum.get(ansOneTriple)+1);
        }
        else if (tripleResultInteger.size()>1){
            statisNum.put(ansOneMoreTriples,statisNum.get(ansOneMoreTriples)+1);
        }
    }

    /**
     * deal with different model(combine the same question or not)
     * @param ques
     * @param ans
     * @param mention4tripID
     * @param triples
     * @return
     */
    public static String ILPmodel(String ques,String ans,HashMap<String,ArrayList<Integer>> mention4tripID,ArrayList<Triple> triples,ArrayList<Integer> tripleResult){
        ILPTripStru ilpTripStru=new ILPTripStru();
        CalTripILPdata calTripILPdata=new CalTripILPdata();
        calTripILPdata.calSentILP(ques,ans,triples,mention4tripID,ilpTripStru);
        calTripILPdata.calConflict(ilpTripStru);
        //System.out.println("ILP data prepared");
        StringBuffer sb_return=new StringBuffer();

        if (ilpTripStru.getMention_no_overlap().size()!=0){
//                System.out.println("ILP calculate starting");
            ILPtriple ilPtriple=new ILPtriple();
            //tripleResult = new ArrayList<>();
            ilPtriple.calILPtriple(ilpTripStru,tripleResult);
//            System.out.println("ILP calculate finished");
            if (tripleResult.size()!=0){
                ArrayList<Triple> triple_ = ilpTripStru.getTriples();
                for (int i=0;i<tripleResult.size();i++){
                    sb_return.append("\ttriple");
                    sb_return.append(i);
                    sb_return.append(":");
                    sb_return.append(triple_.get(tripleResult.get(i)).toString());
                }
            }
        }
        return sb_return.toString();
    }

    /**
     * calculate ILP date including ILP data structure and conflict
     * @param question
     * @param triples
     * @param mention4tripID
     * @param ilpTripStru
     */
    public static void tripleILPdata(String question,String answer,ArrayList<Triple> triples, HashMap<String,ArrayList<Integer>> mention4tripID,ILPTripStru ilpTripStru){
        CalTripILPdata calTripILPdata=new CalTripILPdata();
        calTripILPdata.calSentILP(question,answer,triples,mention4tripID,ilpTripStru);
        calTripILPdata.calConflict(ilpTripStru);

    }

    /**
     * calculate ILP giving one sentences
     * @param quesiton
     * @param allTriples
     * @param mention4tripID
     * @param ilpTripStru
     */
    public static void calSentILP(String quesiton, String answer,ArrayList<Triple> allTriples, HashMap<String,ArrayList<Integer>> mention4tripID,ILPTripStru ilpTripStru){
        ArrayList<ArrayList<String>> allsubStr = new ArrayList<>();
        StringControl stringControl=new StringControl();
        StringControl.getAllsubStr(quesiton,allsubStr);// each array is locations which the location of starting is same
        //int men4entstarInStrIndex = 0;
        int Men4entStart=0;
        for(int i=0;i<allsubStr.size();i++){
            ArrayList<String> subStrlist=allsubStr.get(i);// each array is locations which the location of starting is same
            for (int j=0;j<subStrlist.size();j++){
                String mention=subStrlist.get(j);
                // ***************the length of mention have to more than one

                if (mention4tripID.containsKey(mention)){
                    ArrayList<Integer> tripleIDlist=mention4tripID.get(mention);
                    int menEntSize=0;
                    for(int k=0;k<tripleIDlist.size();k++){
                        int tripleID=tripleIDlist.get(k);
                            Triple triple=allTriples.get(tripleID);
                            if(answer.contains(triple.getObject())){
                                ilpTripStru.addMen4entID(tripleID);
                                ilpTripStru.addTriples(allTriples.get(tripleID));
                                menEntSize++;
                        }
                    }
                    if (menEntSize!=0){
                        ilpTripStru.addMen4entNum(menEntSize);
                        ilpTripStru.addMen4entStart(Men4entStart);
                        Men4entStart+=menEntSize;

                        ilpTripStru.addMention_starInStr(i);
                        ilpTripStru.addMention_no_overlap(mention);
                        ilpTripStru.addMention_LengthWei(mention.length()*0.1+0.5);
                    }
                }
            }
        }
    }


    /**
     * calculate conflict in mention and object
     * @param ilpTripStru
     */
    public static void calConflict(ILPTripStru ilpTripStru) {
        ArrayList<Integer> men4entNum = ilpTripStru.getMen4entNum();//each mention to entity num:5,10,8,7
        ArrayList<Integer> men4entStart = ilpTripStru.getMen4entStart();//each mention start index,0,5,15,23
        ArrayList<Integer> mention_startInStr = ilpTripStru.getMention_starInStr();
        ArrayList<String> mention_no_overlap=ilpTripStru.getMention_no_overlap();

        //calculate conflict with mention overlap and contain
        for (int i = 0; i < men4entStart.size() - 1; i++) {  //one mention(i)
            for (int j = i + 1; j < men4entStart.size(); j++) { //another mention(j)
                int menIndex_i = i;
                int menIndex_j = j;
                String mention_i = mention_no_overlap.get(i);
                String mention_j = mention_no_overlap.get(j);

                //overlap conflict
                StringControl stringControl = new StringControl();
                if (stringControl.judgeOverlapIndex(mention_i, menIndex_i, mention_j, menIndex_j, mention_startInStr) || stringControl.judgeOverlapIndex(mention_j, menIndex_j, mention_i, menIndex_i, mention_startInStr)) {
                    ArrayList<Integer> over_men = new ArrayList<>();
                    over_men.add(i);
                    over_men.add(j);
                    ilpTripStru.addMention_overlap(over_men);
                }
                //contain conflict except overlap(such as "A" and "AB")
                if (stringControl.judgeContainIndex(mention_i, menIndex_i, mention_j, menIndex_j, mention_startInStr) || stringControl.judgeContainIndex(mention_j, menIndex_j, mention_i, menIndex_i, mention_startInStr)) {
                    ArrayList<Integer> contain_men = new ArrayList<>();
                    contain_men.add(i);
                    contain_men.add(j);
                    ilpTripStru.addMention_contain(contain_men);
                }
            }
        }

        //calculate the conflict which is object contain or contained
        CalTripILPdata calTripILPdata=new CalTripILPdata();
        calTripILPdata.calObjConflict(ilpTripStru);
    }

    /**
     * calculate the conflict which is object contain or contained
     * @param ilpTripStru
     */
    public static void calObjConflict(ILPTripStru ilpTripStru){
        ArrayList<Integer> men4entID=ilpTripStru.getMen4entID();
        ArrayList<Triple> triples=ilpTripStru.getTriples();
        for(int i=0;i<triples.size()-1;i++){
            for(int j=i+1; j<triples.size();j++){
                String obj_i=triples.get(i).getObject();
                String obj_2=triples.get(j).getObject();
                if(obj_i.contains(obj_2)||obj_2.contains(obj_i)){
                    ArrayList<Integer> obj_contain=new ArrayList<>();
                    obj_contain.add(i);
                    obj_contain.add(j);
                    ilpTripStru.addObject_contain(obj_contain);
                }
            }
        }
    }
}
