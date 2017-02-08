package com.LiuC.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Freiheiter on 2016/12/20.
 */
public class NumStatistics {
    public static void main(String[] args) {
//        double [] testData=new double[]{1,2,3,4,5,6,7,8,9};
//        System.out.println("最大值："+getMax(testData));
//        System.out.println("最小值："+getMin(testData));
//        System.out.println("计数："+getCount(testData));
//        System.out.println("求和："+getSum(testData));
//        System.out.println("求平均："+getAverage(testData));
//        System.out.println("方差："+getVariance(testData));
//        System.out.println("标准差："+getStandardDiviation(testData));

//        String tripleFile="E:\\project\\entityLinking\\data\\triple\\triple_all.all";
        String qaFile="E:\\project\\entityLinking\\data\\triple\\cqa_triple_all.all";

        FileUtil fileUtil = new FileUtil();
        NumStatistics numStatistics=new NumStatistics();
        ArrayList<String> linesList = new ArrayList<>();
        fileUtil.readLines(qaFile,linesList);
        System.out.println("statistic quesiton:");
        int index=0;
        ArrayList<Double> result=new ArrayList<>();
        numStatistics.num_statistic(linesList,index,result);

        System.out.println("statistic answer:");
        index=1;
        result=new ArrayList<>();
        numStatistics.num_statistic(linesList,index,result);

    }

    public static void num_statistic(ArrayList<String> linesList ,int index, ArrayList<Double> result){
        NumStatistics numStatistics=new NumStatistics();
        String [] questions=numStatistics.listToStringArray(linesList,index);
        double [] quesLengths=numStatistics.strTodoubleArray(questions);
        numStatistics.getLengthResult(quesLengths,result);
    }

    /**
     * from list<String> to split the index String
     * @param linesList
     * @param index
     * @return
     */
    public static String[] listToStringArray(ArrayList<String> linesList,int index){
        String[] returnArray = new String[linesList.size()];
        for(int i=0;i<linesList.size();i++){
            String [] strArray=linesList.get(i).split("\t");
            try {
                returnArray[i]=strArray[index];
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                returnArray[i]="";
                continue;
            }
        }
        return returnArray;
    }

    /**
     * string array to its length array
     * @param strArray
     * @return
     */
    public static double[] strTodoubleArray(String[] strArray){
        double[] doubleAaary = new double[strArray.length];
        for (int i=0;i<strArray.length;i++){
            doubleAaary[i]=(double)strArray[i].length();
        }
        return doubleAaary;
    }

    /**
     * statistic all length of array
     */
    public static void getLengthResult(double [] doubleAaary, ArrayList<Double> statMenLengthResult){

        NumStatistics numStatistics=new NumStatistics();
        statMenLengthResult=new ArrayList<>();
        double max=numStatistics.getMax(doubleAaary);
        double min=numStatistics.getMin(doubleAaary);
        double count=(double)numStatistics.getCount(doubleAaary);
        double sum=numStatistics.getSum(doubleAaary);
        double average=numStatistics.getAverage(doubleAaary);
        double variance=numStatistics.getVariance(doubleAaary);
        double standDeviation=numStatistics.getStandardDiviation(doubleAaary);
        System.out.println("max："+max);
        System.out.println("min："+min);
        System.out.println("count："+count);
        System.out.println("sum："+sum);
        System.out.println("average："+average);
        System.out.println("variance："+variance);
        System.out.println("standard deviation："+standDeviation);
        statMenLengthResult.add(max);
        statMenLengthResult.add(min);
        statMenLengthResult.add(count);
        statMenLengthResult.add(sum);
        statMenLengthResult.add(average);
        statMenLengthResult.add(variance);
        statMenLengthResult.add(standDeviation);
    }

    /**
     * 求给定双精度数组中值的最大值
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果,如果输入值不合法，返回为-1
     */
    public static double getMax(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double max = inputData[0];
        for (int i = 0; i < len; i++) {
            if (max < inputData[i])
                max = inputData[i];
        }
        return max;
    }
    /**
     * 求求给定双精度数组中值的最小值
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果,如果输入值不合法，返回为-1
     */
    public static double getMin(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double min = inputData[0];
        for (int i = 0; i < len; i++) {
            if (min > inputData[i])
                min = inputData[i];
        }
        return min;
    }
    /**
     * 求给定双精度数组中值的和
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getSum(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + inputData[i];
        }
        return sum;
    }
    /**
     * 求给定双精度数组中值的数目
     *
     * @param
     *            Data 输入数据数组
     * @return 运算结果
     */
    public static int getCount(double[] inputData) {
        if (inputData == null)
            return -1;
        return inputData.length;
    }
    /**
     * 求给定双精度数组中值的平均值
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getAverage(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double result;
        result = getSum(inputData) / len;

        return result;
    }
    /**
     * 求给定双精度数组中值的平方和
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getSquareSum(double[] inputData) {
        if(inputData==null||inputData.length==0)
            return -1;
        int len=inputData.length;
        double sqrsum = 0.0;
        for (int i = 0; i <len; i++) {
            sqrsum = sqrsum + inputData[i] * inputData[i];
        }

        return sqrsum;
    }
    /**
     * 求给定双精度数组中值的方差
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getVariance(double[] inputData) {
        int count = getCount(inputData);
        double sqrsum = getSquareSum(inputData);
        double average = getAverage(inputData);
        double result;
        result = (sqrsum - count * average * average) / count;
        return result;
    }
    /**
     * 求给定双精度数组中值的标准差
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getStandardDiviation(double[] inputData) {
        double result;
        //绝对值化很重要
        result = Math.sqrt(Math.abs(getVariance(inputData)));

        return result;
    }


    /**
     * statistic all length of mention(repeat if a mention has more than one triple )
     * @param mention4tripIDs
     */
    public static void getMenLengthResult(HashMap<String, ArrayList<Integer>> mention4tripIDs, ArrayList<Double> statMenLengthResult){
        double[] menLength=statMentLength(mention4tripIDs);

        NumStatistics numStatistics=new NumStatistics();
        statMenLengthResult=new ArrayList<>();
        double max=numStatistics.getMax(menLength);
        double min=numStatistics.getMin(menLength);
        double count=(double)numStatistics.getCount(menLength);
        double sum=numStatistics.getSum(menLength);
        double average=numStatistics.getAverage(menLength);
        double variance=numStatistics.getVariance(menLength);
        double standDeviation=numStatistics.getStandardDiviation(menLength);
        System.out.println("*******statistic information about mention length******");
        System.out.println("max："+max);
        System.out.println("min："+min);
        System.out.println("count："+count);
        System.out.println("sum："+sum);
        System.out.println("average："+average);
        System.out.println("variance："+variance);
        System.out.println("standard deviation："+standDeviation);
        statMenLengthResult.add(max);
        statMenLengthResult.add(min);
        statMenLengthResult.add(count);
        statMenLengthResult.add(sum);
        statMenLengthResult.add(average);
        statMenLengthResult.add(variance);
        statMenLengthResult.add(standDeviation);

    }
    /**
     * statistic all length of mention(repeat if a mention has more than one triple )
     * @param mention4tripID
     */
    public static double [] statMentLength(HashMap<String, ArrayList<Integer>> mention4tripID){
        int index=0;
        int allIndex=0;
        for (Map.Entry<String,ArrayList<Integer>> entry:mention4tripID.entrySet()){
            int strLength=entry.getValue().size();
            allIndex+=strLength;
        }
        double [] menLengths=new double[allIndex];

        for (Map.Entry<String,ArrayList<Integer>> entry:mention4tripID.entrySet()){
            int strLength=entry.getKey().length();
            for(int i=0;i<entry.getValue().size();i++){
                menLengths[index]=strLength;
                index++;
            }
        }
        return menLengths;
    }
}


