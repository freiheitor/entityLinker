package com.LiuC.ILP.tripleILP;

import com.LiuC.ILP.basicILP.ILPdataStru;

import java.util.ArrayList;

/**
 * Created by Freiheiter on 2016/12/19.
 */


/**
 * men4entNum responses to one subject（mention） for different triple //each mention to entity num:5,10,8,7
 * men4entityStart responses to each mention start index  //each mention start index,0,5,15,23
 * men4entStarInStr // each mention and entity pair stars location in String
 * mention //memory overlap mention if there are more than one mention to entity pair.
 * menOverlapMen
 * menCont
 *
 */
public class ILPTripStru extends ILPdataStru{
    public static void main(String[] args) {

    }

    private ArrayList<String> mention_no_overlap=new ArrayList<>(); // memorize mention without overlap
    private ArrayList<Integer> mention_starInStr = new ArrayList<>(); //mention start index in String without overlap

    private ArrayList<Integer> men4entID = new ArrayList<>(); //all mention for entity ID
    private ArrayList<Double> mention_LengthWei =new ArrayList<>(); //longer mention with larger weight, index with ID

    private ArrayList<Triple> triples = new ArrayList<>(); //all triples which match to question

    private ArrayList<ArrayList<Integer>> mention_overlap = new ArrayList<>();  //though mention is conflict, we can get a mention for different entity pairs(the conflict mention can get the only one)
    private ArrayList<ArrayList<Integer>> mention_contain = new ArrayList<>();//as blow
    private ArrayList<ArrayList<Integer>> object_contain = new ArrayList<>(); //if object contain or contained , conflict


    public ArrayList<String> getMention_no_overlap() {
        return mention_no_overlap;
    }

    public void addMention_no_overlap(String a_mention_no_overlap) {
        this.mention_no_overlap.add(a_mention_no_overlap);
    }

    public ArrayList<Integer> getMention_starInStr() {
        return mention_starInStr;
    }

    public void addMention_starInStr(Integer a_menStarInStr) {
        this.mention_starInStr.add(a_menStarInStr);
    }

    public ArrayList<ArrayList<Integer>> getMention_overlap() {
        return mention_overlap;
    }

    public void addMention_overlap(ArrayList<Integer> a_mention_overlap) {
        this.mention_overlap.add(a_mention_overlap);
    }

    public ArrayList<ArrayList<Integer>> getMention_contain() {
        return mention_contain;
    }

    public void addMention_contain(ArrayList<Integer> a_Mention_contain) {
        this.mention_contain.add(a_Mention_contain);
    }

    public ArrayList<Integer> getMen4entID() {
        return men4entID;
    }

    public void addMen4entID(int a_men4entID) {
        this.men4entID.add(a_men4entID);
    }


    public ArrayList<Triple> getTriples() {
        return triples;
    }

    public void addTriples(Triple a_triple) {
        this.triples.add(a_triple);
    }


    public ArrayList<ArrayList<Integer>> getObject_contain() {
        return object_contain;
    }

    public void addObject_contain(ArrayList<Integer> a_object_contain) {
        this.object_contain.add(a_object_contain);
    }


    public ArrayList<Double> getMention_LengthWei() {
        return mention_LengthWei;
    }

    public void addMention_LengthWei(double a_mentionLengthWei) {
        this.mention_LengthWei.add(a_mentionLengthWei);
    }



}
