package in.edconnect.TakeThisExam;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.edconnect.R;

/**
 * Created by admin on 7/19/2015.
 */
public class TakeThisExam extends Activity {

    ArrayList<Question> questionArrayList;
    TextView question;
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

        questionArrayList = new ArrayList<>();
        answers = new ArrayList<>();

        //Add here volley request to fetch all the question and options related to this quiz

        //Add all the question to arraylist
        questionArrayList.add(new Question("What is the capital of India?","Gujarat","Delhi","UP","Kerala"));
        questionArrayList.add(new Question("What is the capital of India?","Gujarat","Delhi","Jammu Kashmir","Kerala"));
        questionArrayList.add(new Question("What is the capital of India?","Gujarat","Delhi","UP","Himachal"));
        questionArrayList.add(new Question("What is the capital of India?","Telangana","Delhi","UP","Maharastra"));



        position=0;
        changeQuestion(position);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(options.getCheckedRadioButtonId()==-1)) {

                    if("option1".equals(options.getCheckedRadioButtonId())){
                        answers.add(position,"1");
                    }else if("option2".equals(options.getCheckedRadioButtonId())){
                        answers.add(position,"2");
                    }else if("option3".equals(options.getCheckedRadioButtonId())){
                        answers.add(position,"3");
                    }else if("option4".equals(options.getCheckedRadioButtonId())){
                        answers.add(position,"4");
                    }


                    if(position+1<questionArrayList.size()){
                        position++;
                        changeQuestion(position);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select One Option!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position+1<questionArrayList.size()){
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
        question.setText(questionCurrent.question);
        option1.setText(questionCurrent.option1);
        option2.setText(questionCurrent.option2);
        option3.setText(questionCurrent.option3);
        option4.setText(questionCurrent.option4);
        option1.setChecked(false);
        option2.setChecked(false);
        option3.setChecked(false);
        option4.setChecked(false);
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You can't go back from test!",Toast.LENGTH_LONG).show();
    }
}
