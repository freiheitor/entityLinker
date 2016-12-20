package com.LiuC.ILP.tripleILP;

import com.LiuC.ILP.Common.StringControl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class CalTripILPdata {
    public static void main(String[] args) {
        CalTripILPdata calTripILPdata=new CalTripILPdata();

        String fileIn="E:\\project\\entityLinking\\data\\triple\\triple_all.all_100000";
        String question="孙悟空出生在哪？";
//        question=args[1];
//        fileIn=args[0];

        ArrayList<Triple> triples=new ArrayList<>();
        HashMap<String,ArrayList<Integer>> mention4tripID=new HashMap<>();
        LoadData loadData = new LoadData();
        loadData.loadTriple(fileIn,triples,mention4tripID);

//        //statistic the mention length
//        ArrayList<Double> menLengthStatic=new ArrayList<>();
//        NumStatistics numStatistic=new NumStatistics();
//        numStatistic.getMenLengthResult(mention4tripID,menLengthStatic);

        ILPTripStru ilpTripStru=new ILPTripStru();
        calTripILPdata.tripleILPdata(question,triples,mention4tripID,ilpTripStru);
        System.out.println("ILP data prepared");
        if (ilpTripStru.getMention_no_overlap().size()!=0){
            System.out.println("ILP calculate starting");
            ILPtriple ilPtriple=new ILPtriple();
            ilPtriple.calILPtriple(ilpTripStru);
            System.out.println("ILP calculate finished");
        }
        else{
            System.out.println(question+" has no mention to match");
        }


    }


    /**
     * calculate ILP date including ILP data structure and conflict
     * @param question
     * @param triples
     * @param mention4tripID
     * @param ilpTripStru
     */
    public static void tripleILPdata(String question,ArrayList<Triple> triples, HashMap<String,ArrayList<Integer>> mention4tripID,ILPTripStru ilpTripStru){
        CalTripILPdata calTripILPdata=new CalTripILPdata();
        calTripILPdata.calSentILP(question,triples,mention4tripID,ilpTripStru);
        calTripILPdata.calConflict(ilpTripStru);

    }

    /**
     * calculate ILP giving one sentences
     * @param quesiton
     * @param allTriples
     * @param mention4tripID
     * @param ilpTripStru
     */
    public static void calSentILP(String quesiton, ArrayList<Triple> allTriples, HashMap<String,ArrayList<Integer>> mention4tripID,ILPTripStru ilpTripStru){
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
                    ilpTripStru.addMen4entNum(tripleIDlist.size());
                    ilpTripStru.addMen4entStart(Men4entStart);
                    Men4entStart+=tripleIDlist.size();

                    ilpTripStru.addMention_starInStr(i);
                    ilpTripStru.addMention_no_overlap(mention);

                    ilpTripStru.addMention_LengthWei(mention.length()*0.1+0.5);

                    for(int k=0;k<tripleIDlist.size();k++){
                        int tripleID=tripleIDlist.get(k);
                        ilpTripStru.addMen4entID(tripleID);
                        ilpTripStru.addTriples(allTriples.get(tripleID));
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
