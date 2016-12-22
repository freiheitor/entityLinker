package com.LiuC.ILP.tripleILP;

import com.LiuC.common.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class LoadData {
    public static void main(String[] args) {
        String tripleFile="E:\\project\\entityLinking\\data\\triple\\triple_all.all_100000";
        ArrayList<Triple> triples=new ArrayList<>();
        HashMap<String,ArrayList<Integer>> subject4tripID=new HashMap<>();
        LoadData loadData = new LoadData();
        loadData.loadTriple(tripleFile,triples,subject4tripID);

        String QAtriple="E:\\project\\entityLinking\\data\\triple\\cqa_triple_all.all";
        HashMap<String, String> QApair=new HashMap<>();
        loadData.loadCqaTriple(QAtriple,QApair);
        System.out.println("finished!");
    }

    /**
     * load triple file to get ArrayList<triple> and subject for different triple ID
     * @param fileInput:
     * @param triples: ArrayList<triple>
     * @param mention4tripID: subject for different triple
     */
    public static void loadTriple(String fileInput, ArrayList<Triple> triples, HashMap<String,ArrayList<Integer>> mention4tripID){
        System.out.println("load triple start:");
        FileUtil fileUtil=new FileUtil();
        ArrayList<String> linesList = new ArrayList<>();
        fileUtil.readLines(fileInput,linesList);
        int ID=0;
        for(int i=0; i<linesList.size();i++){
            String[] lines = linesList.get(i).split("\t");
            try{
                String subject =lines[0];
                String relation = lines[1];
                String object  = lines[2];

                //set triple
                Triple triple = new Triple(ID);

                triple.setSubject(subject);
                triple.setRelation(relation);
                triple.setObject(object);
                triples.add(triple);

                //get subject for different triple
                fileUtil.addMapList(subject,ID,mention4tripID);

                ID++;

            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                continue;
            }
        }
        System.out.println("load triple finished!");
    }

    public static void loadCqaTriple(String input, HashMap<String, String> quesAns){
        System.out.println("load cqa_triple starts:");
        FileUtil fileUtil=new FileUtil();
        ArrayList<String> lineList=new ArrayList<>();
        fileUtil.readLines(input,lineList);
        for (int i=0;i<lineList.size();i++){
            try{
                String [] lines=lineList.get(i).split("\t");
                String question=lines[0];
                String answer=lines[1];
                if(quesAns.containsKey(question)){
                    String answer_before=quesAns.get(question);
                    String[] answer_array=answer_before.split("\t");
                    quesAns.put(question,quesAns.get(question)+"\tanswer"+answer_array.length+":"+answer);
                }
                else {
                    quesAns.put(question,"\tanswer0:"+answer);
                }

            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                System.out.println("line number:"+i+","+lineList.get(i)+",read error");
                continue;
            }
        }
        System.out.println("load cqa_triple finished");
    }

}
