package com.LiuC.ILP.tripleILP;

import com.LiuC.common.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class LoadData {
    public static void main(String[] args) {
        filterLast("E:\\\\project\\\\entityLinking\\\\data\\\\triple\\\\test_qa_all_50.pair","E:\\\\project\\\\entityLinking\\\\data\\\\triple\\\\test_qa_pair_50_noCon_pair");
//        String tripleFile="E:\\project\\entityLinking\\data\\triple\\triple_all.all_100000";
//        ArrayList<Triple> triples=new ArrayList<>();
//        HashMap<String,ArrayList<Integer>> subject4tripID=new HashMap<>();
//        LoadData loadData = new LoadData();
////        loadData.loadTriple(tripleFile,triples,subject4tripID);
//
//        String QAtriple = "E:\\project\\entityLinking\\data\\triple\\cqa_triple_all.test";
//        HashMap<String, ArrayList<String>> QApair=new HashMap<>();
//        loadData.loadCqaTriple(QAtriple,QApair);
//        System.out.println("finished!");
    }

    /**
     * load triple file to get ArrayList<triple> and subject for different triple ID
     * @param fileInList:Arraylist<input file string>
     * @param triples: ArrayList<triple>
     * @param mention4tripID: subject for different triple
     */
    public static void loadTriple(ArrayList<String> fileInList, ArrayList<Triple> triples, HashMap<String,ArrayList<Integer>> mention4tripID){
        System.out.println("load triple start:");
        FileUtil fileUtil=new FileUtil();
//        ArrayList<String> linesList = new ArrayList<>();
        Set<String> linesSet = new HashSet<>();
        for(int i=0;i<fileInList.size();i++){
            fileUtil.readLines(fileInList.get(i),linesSet);
        }
        int ID=0;
        for(String line_set:linesSet){
            String[] lines = line_set.split("\t");
            try{
                if(lines.length>=3){
                    String subject =lines[0];
                    String relation = lines[1];
                    String object  = lines[2];


                    // ***************the length of mention (subject) and object have to more than one
                    if (subject.length()<2 || object.length()<2){
                        continue;
                    }

                    // ************** remove those triple that subject equals object
                    if (subject.equals(object))
                    {
                        continue;
                    }

                    //set triple
                    Triple triple = new Triple(ID);

                    triple.setSubject(subject);
                    triple.setRelation(relation);
                    triple.setObject(object);
                    triples.add(triple);

                    //get subject for different triple
                    fileUtil.addMapList(subject,ID,mention4tripID);

                    ID++;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                continue;
            }
        }
        System.out.println("load triple finished!");
    }

    /**
     * load aqa_triple, return the map<qeustion,List<answer>>, filter repeat question and answer
     * @param input
     * @param quesAns
     */
    public static void loadCqaTriple(String input, HashMap<String, ArrayList<String>> quesAns){
        System.out.println("load cqa_triple starts:");
        FileUtil fileUtil=new FileUtil();
        ArrayList<String> lineList=new ArrayList<>();
        fileUtil.readLines(input,lineList);
        for (int i=0;i<lineList.size();i++){
            try{
                if (lineList.get(i).isEmpty()){
                    continue;
                }
                String [] lines=lineList.get(i).split("\t");
                if (lines.length<2){
                    continue;
                }
                String question=lines[0];
                String answer=lines[1];
                ArrayList<String> ansList=new ArrayList<>();
                if(quesAns.containsKey(question)){
                    ansList=quesAns.get(question);
                    if(!ansList.contains(answer)){
                        ansList.add(answer);
                    }
                }else{
                    ansList.add(answer);
                }
                quesAns.put(question,ansList);
            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                System.out.println("line number:"+i+","+lineList.get(i)+",read error");
                continue;
            }
        }
        System.out.println("load cqa_triple finished");
    }

    /**
     * filter the last one after '\t'
     * @param fileIn
     * @param fileOut
     */
    public static void filterLast(String fileIn,String fileOut){
        ArrayList<String> linesArray=new ArrayList<>();
        FileUtil fileUtil=new FileUtil();
        fileUtil.readLines(fileIn,linesArray);
        ArrayList<String> linesResult=new ArrayList<>();
        for (int i=0;i<linesArray.size();i++){
            String[] lines=linesArray.get(i).split("\t");
            try{
                String subject=lines[0];
                String predicate=lines[1];
//                String object=lines[2];
//                linesResult.add(subject+"\t"+predicate+"\t"+object);
                linesResult.add(subject+"\t"+predicate);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                continue;
            }
        }
        fileUtil.writeLines(fileOut,linesResult);
    }

}
