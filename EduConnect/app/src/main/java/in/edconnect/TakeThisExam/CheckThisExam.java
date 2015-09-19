package in.edconnect.TakeThisExam;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.edconnect.Database.QuestionDBHelper;
import in.edconnect.HomePageActivity;
import in.edconnect.R;

public class CheckThisExam extends Activity{

    TextView totalmarks,totalscore,totalpercentage;
    RadioButton option1,option2,option3,option4;
    Button submit,previous;
    ArrayList<Question> questionsList = new ArrayList<>();
    ArrayList<Answers> answers = new ArrayList<>();
    ArrayList<Answers> answersSend = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>();
    ListView answerListView;
    int position=0;
    QuestionDBHelper dbHelper;
    private int totalMarks=0 , score=0 ;
    float percentage=0;
    MySimpleArrayAdapter arrayAdapter;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.checkthisexam_layout);
        totalmarks=(TextView)findViewById(R.id.totalmarks);
        totalscore=(TextView)findViewById(R.id.marksscored);
        totalpercentage=(TextView)findViewById(R.id.percentage);
        answerListView=(ListView)findViewById(R.id.answerlist);
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
                answersSend.add(answers.get(i));
            }catch (NullPointerException en){

            }
            i++;
        }

        percentage=((float)score / (float)(totalMarks))*100;
        totalmarks.setText("Total Marks :    "+totalMarks);
        totalscore.setText("Marks Scored :   "+score);
        totalpercentage.setText("Percentage Scored :    "+percentage+"%");
        Log.e("FinalSheet", score + " out of " + totalMarks);

        arrayAdapter=new MySimpleArrayAdapter(this,questions,answersSend);
        answerListView.setAdapter(arrayAdapter);
    }


    @Override
    public void onBackPressed(){
        startActivity(new Intent(CheckThisExam.this,HomePageActivity.class));
        finish();
    }
}
