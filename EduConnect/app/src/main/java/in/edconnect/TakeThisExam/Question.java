package in.edconnect.TakeThisExam;

/**
 * Created by admin on 7/19/2015.
 */
public class Question {

    String question,option1,option2,option3,option4;
    String referencePar;
    byte[] ques;

    public  Question(String question,String option1,String option2,String option3,String option4,String referencePar){
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.referencePar=referencePar;
        try {
            ques = "?????????? ??????? ??????? ???????? ????".getBytes("UTF-16LE");
        }catch (Exception en){}

    }


}
