package in.edconnect.TakeThisExam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.edconnect.HomePageActivity;
import in.edconnect.R;

/**
 * Created by admin on 7/19/2015.
 */
public class CheckThisExam extends Activity {

    ArrayList<Question> questionArrayList;
    TextView question,reference;
    RadioButton option1,option2,option3,option4;
    Button previous,skip,submit;
    RadioGroup options;
    private int position;
    ArrayList<String> answers;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.takethisexam_layout);

        question = (TextView) findViewById(R.id.question);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);
        previous = (Button) findViewById(R.id.previous);
        skip = (Button)findViewById(R.id.skip);
        submit = (Button)findViewById(R.id.submit);
        options = (RadioGroup)findViewById(R.id.options);
        reference = (TextView)findViewById(R.id.referencePar);
        option3.setChecked(true);


        Typeface faceGautami = Typeface.createFromAsset(getAssets(),
                "gautami.ttf");
        question.setTypeface(faceGautami);
        question.setText("2. ??????????");

        questionArrayList = new ArrayList<>();
        answers = new ArrayList<>();

        //Add here volley request to fetch all the question and options related to this quiz

        //Add all the question to arraylist\
        try{

            questionArrayList.add(new Question("What is the capital of India?","Gujarat","Delhi","UP","Kerala","This is a reference paragraph for you . You have to answer the questions after reading this paragraph"));
            questionArrayList.add(new Question("The Centre for Cellular and Molecular Biology is situated at?","Patna","Jaipur","Jammu Kashmir","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
            questionArrayList.add(new Question("The famous Dilwara Temples are situated in?","Rajasthan","Maharashtra","UP","Himachal","This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
            questionArrayList.add(new Question("What is the capital of India?", "Telangana", "Delhi", "UP", "Maharastra", "This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
            questionArrayList.add(new Question("Grand Central Terminal, Park Avenue, New York is the world's?", "largest railway station", "highest railway station", "None", "longest railway station", ""));
            questionArrayList.add(new Question("For which of the following disciplines is Nobel Prize awarded??", "Physics and Chemistry", " \tPhysiology or Medicine", "Literature, Peace and Economics", "All of the above", ""));
            questionArrayList.add(new Question("Hitler party which came into power in 1933 is known as?", "Labour Party", "Nazi Party", "Democratic Party", "All of the above", ""));
        }catch(Exception en){}


        position=0;
        changeQuestion(position);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(options.getCheckedRadioButtonId()==-1)) {


                    if(option1.getId()==(options.getCheckedRadioButtonId())){
                        answers.add(position, "1");

                    }else if(option2.getId() == (options.getCheckedRadioButtonId())){
                        answers.add(position, "2");

                    }else if(option3.getId() == (options.getCheckedRadioButtonId())){
                        answers.add(position, "3");

                    }else if(option4.getId() == (options.getCheckedRadioButtonId())){
                        answers.add(position, "4");

                    }


                    if(position+1<questionArrayList.size()){
                        position++;
                        changeQuestion(position);
                    }else if(position+1 == questionArrayList.size()){
                        final Dialog dialog = new Dialog(CheckThisExam.this);
                        LayoutInflater li =(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = li.inflate(R.layout.comfirmation_popup, null, false);
                        dialog.setContentView(v);
                        ListView listQuestions = (ListView)v.findViewById(R.id.comfirmation_questions);
                        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(CheckThisExam.this,questionArrayList,answers);
                        listQuestions.setAdapter(adapter);
                        dialog.setTitle("Confirm Answers!");

                        Button goback = (Button)v.findViewById(R.id.goback);
                        Button submit = (Button)v.findViewById(R.id.submit);

                        goback.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(CheckThisExam.this, HomePageActivity.class));
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select One Option!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position + 1 < questionArrayList.size()) {
                    position++;
                    changeQuestion(position);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>0){
                    position--;
                    changeQuestion(position);
                }
            }
        });
    }



    public void changeQuestion(int position){

        Question questionCurrent = questionArrayList.get(position);


        try {

            question.setText(questionCurrent.question);
            option1.setText(questionCurrent.option1);
            option2.setText(questionCurrent.option2);
            option3.setText(questionCurrent.option3);
            option4.setText(questionCurrent.option4);
            reference.setText(questionCurrent.referencePar);
        }catch (Exception en){}
        option1.setChecked(false);
        option2.setChecked(false);
        option3.setChecked(false);
        option4.setChecked(false);

        options.clearCheck();
        option3.setChecked(true);

        try{
            switch (Integer.parseInt(answers.get(position))){
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
                default:
                    option3.setChecked(true);
                    break;
            }
        }catch (Exception en){}

    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You can't go back from test!",Toast.LENGTH_LONG).show();
    }
}
