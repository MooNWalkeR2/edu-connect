package in.edconnect.TakeThisExam;

/**
 * Created by admin on 7/19/2015.
 */
public class Question {


    int num;
    public String question,option1,option2,option3,option4,language,id;
    public String  correctAnswer,questionMarks,sectionid;
    public String sectionname,passageNo;
    public String referencePar;
    int matchFollowing=0;
    String row11,row12,row13,row14,row15,row21,row22,row23,row24,row25;
    public int groupID;
    public String explanation1="",explanation2="",explanation3="",explanation4="",referenceUrl="";

    public Question(){}

    public  Question(int num,int groupID,String question,String option1,String option2,String option3,String option4,String referencePar,int matchFollowing){
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.referencePar=referencePar;
        this.matchFollowing=matchFollowing;
        this.groupID=groupID;

    }


    public void setMatch(String row11,String row12,String row13,String row14,String row15,String row21,String row22,String row23,String row24,String row25){
        this.row11=row11;
        this.row12=row12;
        this.row13=row13;
        this.row14=row14;
        this.row15=row15;
        this.row21=row21;
        this.row22=row22;
        this.row23=row23;
        this.row24=row24;
        this.row25=row25;

    }
}
