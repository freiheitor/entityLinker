package com.LiuC.Dictionary;

import com.LiuC.common.ComUtil;
import com.LiuC.common.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Freiheiter on 2016/11/30.
 */
public class filterDic {
    public static  void filterNoise(String noiseIn, String filterOut,String filterFile){
        FileUtil fileUtil = new FileUtil();
        ArrayList<String> lines = new ArrayList<>();
        HashSet<String> filterMen = new HashSet<>();
        readFilterLines(noiseIn, lines,filterMen);
        fileUtil.writeLines(filterOut,lines);
        fileUtil.writeLines(filterFile,filterMen);
    }

    /**
     * filter by mention
     * @param file
     * @param lines
     * @param filterMen
     */
    public static void readFilterLines(String file, ArrayList<String> lines, HashSet<String> filterMen) {
        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(new File(file)));
            HashSet<String> orinalMen = new HashSet<>();
            HashSet<String> filtaferMen = new HashSet<>();

            int oriPair = 0;
            int filterPair = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                String [] lineArray = line.split("\t");
                orinalMen.add(lineArray[0]);
                oriPair++;
                if(FileUtil.StringFilter(lineArray[0]).isEmpty()){
                    filterMen.add(lineArray[0]);
                }
                else{
                    lines.add(line);
                    filtaferMen.add(lineArray[0]);
                    filterPair++;
                }
            }

            System.out.println("Original mention size: "+orinalMen.size());
            System.out.println("After filter mention size:"+filtaferMen.size());
            System.out.println("filter mention size: "+filterMen.size());
            System.out.println("Original mention-entity pair size: "+oriPair);
            System.out.println("After filter mention-entity pair size: "+filterPair);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * mention normalization
     * @param fileIn:input dictionary
     * @param fileOut:diction append norm1(abs(0,1)) and norm2(squad)
     */
    public static void nornMention(String fileIn,String fileOut){
        FileUtil fileUtil=new FileUtil();
        ArrayList<String> lines=new ArrayList<>();
        fileUtil.readLines(fileIn,lines);
        HashMap<String, Integer> men4num=new HashMap<>();
        //get mention num
        for(int i=0;i<lines.size();i++){
            try {
                String [] arrayLines=lines.get(i).split("\t");
                String mention=arrayLines[0];
                int num=Integer.parseInt(arrayLines[2]);
                if (men4num.containsKey(mention)){
                    num+=men4num.get(mention);
                }
                men4num.put(mention,num);
            }
            catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                System.out.println("this line in dic is error,discard it:"+lines.get(i));
                continue;
            }
        }

        ComUtil comUtil=new ComUtil();
        men4num = (HashMap<String, Integer>) comUtil.sortByValue(men4num, 0);
        //normalization2: the sum of square
        double squSum=fileUtil.getSquValue(men4num);
        //normalization1: (current-min)/(max-min)
        ArrayList<Integer> orderNum=fileUtil.orderValue(men4num);
        int sum=0;
        int min=orderNum.get(men4num.size()-1);
        int max=orderNum.get(0);
        int men4numSize=men4num.size();
        double maxSub=max - min;
        double maxLog=Math.log(max);
        ArrayList<String> normMenStr=new ArrayList<>();
        for (int i=0;i<lines.size();i++){
            try {
                String[] lineArray= lines.get(i).split("\t");
                String mention =lineArray[0];
                int men_num=men4num.get(mention);
                double norm1=(men_num-min)/maxSub;
                double norm2=men_num/squSum;
                double norm3=Math.log(men_num)/maxLog;
                normMenStr.add(lines.get(i)+"\t"+norm1+"\t"+norm2+"\t"+norm3);
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                System.out.println("read this line is error, discard it:"+lines.get(i));
            }
        }

        fileUtil.writeLines(fileOut,normMenStr);

        for (Map.Entry<String, Integer> entry : men4num.entrySet()) {
            int value=entry.getValue();
            sum+=value;
        }
        System.out.println("mention minimize:"+ min);
        System.out.println("mentin maximize:"+max);
        System.out.println("mention average:"+sum/men4num.size());

    }

    public static void main(String[] args) {
        //filterNoise("/data/liuc/entitylinking/dic/freeBaseDic","/data/liuc/entitylinking/dic/FBDicAfterfil","/data/liuc/entitylinking/dic/FBDicfilterred");
        String anchor="/data/liuc/entitylinking/wikipedia/zhwiki-20160501/anchor_entity_map_simple";
        String dic_primary="/data/liuc/entitylinking/data/wikiDic/dic_primary";
        String wikiDic_after_filter="/data/liuc/entitylinking/data/wikiDic/wikiDic_after_filter";
        String wikiDic_filterred="/data/liuc/entitylinking/data/wikiDic/wikiDic_filterred";
        String wikiDic_after_filter_menNorm="/data/liuc/entitylinking/data/wikiDic/wikiDic_after_filter_menNorm";
        //new WikiDic().getWikiDic(anchor,dic_primary);
        //filterNoise(dic_primary,wikiDic_after_filter,wikiDic_filterred);
        nornMention(wikiDic_after_filter,wikiDic_after_filter_menNorm);

    }
}
