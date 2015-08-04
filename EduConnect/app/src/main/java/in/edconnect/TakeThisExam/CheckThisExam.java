package in.edconnect.TakeThisExam;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import in.edconnect.HomePageActivity;
import in.edconnect.R;

public class CheckThisExam extends Activity{

    TextView question,reference;
    RadioButton option1,option2,option3,option4;
    Button submit,previous;
    ArrayList<Question> questionsList = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();;
    int position=0;

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

        //////Get this answersheet from web services

        questionsList.add(new Question(1,"What is the capital of India?", "Gujarat", "Delhi", "UP", "Kerala", "This is a reference paragraph for you . You have to answer the questions after reading this paragraph"));
        answers.add(0,"1");
        questionsList.add(new Question(2,"The Centre for Cellular and Molecular Biology is situated at?", "Patna", "Jaipur", "Jammu Kashmir", "Kerala", "This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
        answers.add(1,"2");
        questionsList.add(new Question(3,"The famous Dilwara Temples are situated in?","Rajasthan","Maharashtra","UP","Himachal","This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
        answers.add(2,"1");
        questionsList.add(new Question(4,"What is the capital of India?", "Telangana", "Delhi", "UP", "Maharastra", "This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
        answers.add(3,"4");
        questionsList.add(new Question(5,"Grand Central Terminal, Park Avenue, New York is the world's?", "largest railway station", "highest railway station", "None", "longest railway station", ""));
        answers.add(4,"3");
        questionsList.add(new Question(6,"For which of the following disciplines is Nobel Prize awarded??", "Physics and Chemistry", " \tPhysiology or Medicine", "Literature, Peace and Economics", "All of the above", ""));
        answers.add(5, "2");
        questionsList.add(new Question(7,"Hitler party which came into power in 1933 is known as?", "Labour Party", "Nazi Party", "Democratic Party", "All of the above", ""));


        changeQuestion(0);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position + 1 >= questionsList.size()) {
                    startActivity(new Intent(CheckThisExam.this, HomePageActivity.class));
                    finish();
                } else {
                    position++;
                    changeQuestion(position);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position<=0){

                }else{
                    position--;
                    changeQuestion(position);
                }
            }
        });

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

            switch (Integer.parseInt(answers.get(position))) {
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
