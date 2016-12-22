package com.LiuC.ILP.tripleILP;

import com.LiuC.ILP.Common.StringControl;
import com.LiuC.common.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class CalTripILPdata {
    public static void main(String[] args) {

        String tripleFile="E:\\project\\entityLinking\\data\\triple\\triple_all.all_100000";
        String qaFile="E:\\project\\entityLinking\\data\\triple\\cqa_triple_all.test";
        String outFile="E:\\project\\entityLinking\\data\\triple\\cqa_result_";

//        tripleFile=args[0];
//        qaFile=args[1];
//        outFile=args[2];

        CalTripILPdata calTripILPdata=new CalTripILPdata();
        calTripILPdata.tripleILPdata(tripleFile,qaFile,outFile);

    }

    public static void tripleILPdata(String tripleFile,String qaFile,String outFile){

        CalTripILPdata calTripILPdata=new CalTripILPdata();
        ArrayList<String> qa_triple_match=new ArrayList<>();
        ArrayList<String> qa_triple_no_match=new ArrayList<>();

        ArrayList<Triple> triples=new ArrayList<>();
        HashMap<String,ArrayList<Integer>> mention4tripID=new HashMap<>();
        LoadData loadData = new LoadData();
        loadData.loadTriple(tripleFile,triples,mention4tripID);

        HashMap<String,String> QApair=new HashMap<>();
        loadData.loadCqaTriple(qaFile,QApair);

//        //statistic the mention length
//        ArrayList<Double> menLengthStatic=new ArrayList<>();
//        NumStatistics numStatistic=new NumStatistics();
//        numStatistic.getMenLengthResult(mention4tripID,menLengthStatic);

        for(Map.Entry<String,String> entry:QApair.entrySet()){
            String ques=entry.getKey();
            String ans=entry.getValue();
            ILPTripStru ilpTripStru=new ILPTripStru();
            calTripILPdata.calSentILP(ques,ans,triples,mention4tripID,ilpTripStru);
            calTripILPdata.calConflict(ilpTripStru);
            //System.out.println("ILP data prepared");
            StringBuffer sb=new StringBuffer();
            sb.append("question:");
            sb.append(ques);
//            sb.append("\tanswer:");
            sb.append(ans);
            if (ilpTripStru.getMention_no_overlap().size()!=0){
                StringBuffer qa_match_sb=sb;
                qa_match_sb.append("\ttriple:");

//                System.out.println("ILP calculate starting");
                ILPtriple ilPtriple=new ILPtriple();
                ArrayList<Integer> tripleResult=new ArrayList<>();
                ilPtriple.calILPtriple(ilpTripStru,tripleResult);
                System.out.println("ILP calculate finished");
                if (tripleResult.size()!=0){
                    ArrayList<Triple> triple_ = ilpTripStru.getTriples();
                    for (int i=0;i<tripleResult.size();i++){
                        qa_match_sb.append(triple_.get(tripleResult.get(i)).toString());
                        qa_match_sb.append("\t");
                    }
                }
                qa_triple_match.add(qa_match_sb.toString());
            }
            else{
                qa_triple_no_match.add(sb.toString());
            }
        }

        FileUtil fileUtil=new FileUtil();
        System.out.println("Total QA triple: "+QApair.size()+" match QA triple in KB: "+qa_triple_match.size()+" no match QA triple in KB:"+qa_triple_no_match.size());
        fileUtil.writeLines(outFile+"match_triple",qa_triple_match);
        fileUtil.writeLines(outFile+"no_match_triple",qa_triple_no_match);
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
        //int men4entstarInStrIndex=0;
        int Men4entStart=0;
        for(int i=0;i<allsubStr.size();i++){
            ArrayList<String> subStrlist=allsubStr.get(i);// each array is locations which the location of starting is same
            for (int j=0;j<subStrlist.size();j++){
                String mention=subStrlist.get(j);
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
