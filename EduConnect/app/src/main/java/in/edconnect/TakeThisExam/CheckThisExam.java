package in.edconnect.TakeThisExam;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import in.edconnect.Database.QuestionDBHelper;
import in.edconnect.HomePageActivity;
import in.edconnect.R;

public class CheckThisExam extends Activity{

    TextView question,reference;
    RadioButton option1,option2,option3,option4;
    Button submit,previous;
    ArrayList<Question> questionsList = new ArrayList<>();
    ArrayList<Answers> answers = new ArrayList<>();;
    ArrayList<Question> questions = new ArrayList<>();
    int position=0;
    QuestionDBHelper dbHelper;
    private int totalMarks=0 , score=0 , percentage=0;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.checkthisexam_layout);

        question = (TextView) findViewById(R.id.question);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);
        reference = (TextView) findViewById(R.id.referencePar);
        submit = (Button) findViewById(R.id.submit);
        previous = (Button) findViewById(R.id.previous);
        dbHelper=new QuestionDBHelper(this);

        //////Get this answersheet from web services

        answers = getIntent().getParcelableArrayListExtra("Answers");
        Log.e("Answers", answers.toString());

        int i=0;
        Question question;
        while(i<answers.size()){
            try{
                Log.e("Ans" + i, answers.get(i).sectionId + " " + answers.get(i).position + "  " + answers.get(i).ans);
                question=dbHelper.getThisQuestion(answers.get(i).sectionId,answers.get(i).position);
                totalMarks+=Integer.parseInt(question.questionMarks);
                Log.e("Ans",answers.get(i).ans + " and "+question.correctAnswer);
                if(answers.get(i).ans.equals(question.correctAnswer)){
                    score+=Integer.parseInt(question.questionMarks);
                }
                questions.add(question);
            }catch (NullPointerException en){

            }
            i++;
        }

        Log.e("FinalSheet",score+" out of "+totalMarks);




        //changeQuestion(0);






    }



    public void changeQuestion(int position) {

        Question questionCurrent = questionsList.get(position);


        try {

            question.setText(questionCurrent.question);
            option1.setText(questionCurrent.option1);
            option2.setText(questionCurrent.option2);
            option3.setText(questionCurrent.option3);
            option4.setText(questionCurrent.option4);
            reference.setText(questionCurrent.referencePar);
            option1.setEnabled(false);
            option2.setEnabled(false);
            option3.setEnabled(false);
            option4.setEnabled(false);

            switch (Integer.parseInt(answers.get(position).ans)) {
                case 0:
                    break;
                case 1:
                    option1.setChecked(true);
                    break;
                case 2:
                    option2.setChecked(true);
                    break;
                case 3:
                    option3.setChecked(true);
                    break;
                case 4:
                    option4.setChecked(true);
                    break;

            }

        }catch (Exception en){}

    }
}
