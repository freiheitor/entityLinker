package com.LiuC.ILP.tripleILP;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class Triple {
    public int id;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    private String subject;
    private String relation;
    private String object;

    public Triple(int id){
        this.id=id;
    }

    public String toString(){
        return this.subject+"\t"+this.relation+"\t"+this.object;
    }
}
