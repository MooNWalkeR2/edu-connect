package in.edconnect.TakeThisExam;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.edconnect.HomePageActivity;
import in.edconnect.R;
import in.edconnect.SelectableTextView.SelectableTextView;
import in.edconnect.TouchImageView.TouchImageView;

/**
 * Created by admin on 7/19/2015.
 */
public class TakeThisExam extends Activity {

    ArrayList<Question> questionArrayList;
    TextView question;
    TextView reference;
    RadioButton option1,option2,option3,option4;
    Button previous,skip,submit,calculator,protractor,highlight,scale;
    RadioGroup options;
    private int position;
    ArrayList<String> answers;
    ImageView protractorImage;
    int _xDelta,_yDelta;
    SharedPreferences highlightText;

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
        calculator = (Button)findViewById(R.id.calculator);
        protractor = (Button)findViewById(R.id.protractor);
        highlight = (Button)findViewById(R.id.highlight);
        scale = (Button)findViewById(R.id.scale);
        protractorImage = (ImageView)findViewById(R.id.protractorimage);

        protractorImage.setVisibility(View.INVISIBLE);
        highlightText= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        highlightText.edit().clear().commit();

        Typeface faceGautami = Typeface.createFromAsset(getAssets(),
                "gautami.ttf");
        question.setTypeface(faceGautami);
        question.setText("2. ??????????");

        questionArrayList = new ArrayList<>();
        answers = new ArrayList<>();

        //Add here volley request to fetch all the question and options related to this quiz

        //Add all the question to arraylist\
        try{

        questionArrayList.add(new Question(1,"What is the capital of India?","Gujarat","Delhi","UP","Kerala","This is a reference paragraph for you . You have to answer the questions after reading this paragraph"));
        questionArrayList.add(new Question(2,"The Centre for Cellular and Molecular Biology is situated at?","Patna","Jaipur","Jammu Kashmir","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
        questionArrayList.add(new Question(3,"The famous Dilwara Temples are situated in?","Rajasthan","Maharashtra","UP","Himachal","This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
        questionArrayList.add(new Question(4,"What is the capital of India?", "Telangana", "Delhi", "UP", "Maharastra", "This is reference paragraph for you . You have to answer the questions after reading this paragraph"));
        questionArrayList.add(new Question(5,"Grand Central Terminal, Park Avenue, New York is the world's?", "largest railway station", "highest railway station", "None", "longest railway station", ""));
        questionArrayList.add(new Question(6,"For which of the following disciplines is Nobel Prize awarded??", "Physics and Chemistry", " \tPhysiology or Medicine", "Literature, Peace and Economics", "All of the above", ""));
        questionArrayList.add(new Question(7,"Hitler party which came into power in 1933 is known as?", "Labour Party", "Nazi Party", "Democratic Party", "All of the above", ""));
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
                        int go=0;
                        for(String answer:answers){
                            if(!answer.equals("0")){
                                go=1;
                            }
                        }
                        if(go==0){
                            Toast.makeText(getApplicationContext(),"Please answer at least one question!",Toast.LENGTH_SHORT).show();
                        }else {
                            final Dialog dialog = new Dialog(TakeThisExam.this);
                            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = li.inflate(R.layout.comfirmation_popup, null, false);
                            dialog.setContentView(v);
                            ListView listQuestions = (ListView) v.findViewById(R.id.comfirmation_questions);
                            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(TakeThisExam.this, questionArrayList, answers);
                            listQuestions.setAdapter(adapter);
                            dialog.setTitle("Confirm Answers!");

                            Button goback = (Button) v.findViewById(R.id.goback);
                            Button submit = (Button) v.findViewById(R.id.submit);

                            goback.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    highlightText.edit().clear().commit();
                                    startActivity(new Intent(TakeThisExam.this, HomePageActivity.class));
                                    finish();
                                }
                            });
                            dialog.show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select One Option!", Toast.LENGTH_SHORT).show();
                }
            }
        });



         ///////////////////////////////////////////Onclicklistener
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answers.add(position, "0");
                if (position + 1 < questionArrayList.size()) {
                    position++;
                    changeQuestion(position);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                    changeQuestion(position);
                }
            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName("com.android.calculator2", "com.android.calculator2.Calculator"));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException noSuchActivity) {
                    // handle exception where calculator intent filter is not registered
                }
            }
        });


        protractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (protractorImage.getVisibility() == View.INVISIBLE) {
                    Toast.makeText(getApplicationContext(),"VISIBLE",Toast.LENGTH_SHORT).show();
                    protractorImage.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(),"INVISIBLE",Toast.LENGTH_SHORT).show();
                    protractorImage.setVisibility(View.VISIBLE);
                }
            }
        });


        protractorImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                ImageView j = (ImageView) findViewById(R.id.protractorimage);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        _xDelta = (int) (X - j.getTranslationX());
                        _yDelta = (int) (Y - j.getTranslationY());
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                        j.setTranslationX(X - _xDelta);
                        j.setTranslationY(Y - _yDelta);
                        break;
                }

                return true;
            }
        });




        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spannable spannable = new SpannableString(reference.getText().toString());
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), reference.getSelectionStart(), reference.getSelectionEnd(), 0);

                int record=position,temp=0;
                SharedPreferences.Editor editor = highlightText.edit();
               /* while(highlightText.getInt(record+temp+"start",-1)!=-1){
                    temp++;
                }*/
                Toast.makeText(getApplicationContext(),"Storing "+record,Toast.LENGTH_SHORT).show();
                editor.putInt(record +  "start", reference.getSelectionStart());
                editor.putInt(record +  "end", reference.getSelectionEnd());
                editor.apply();
                Toast.makeText(getApplicationContext(),"FROM "+ highlightText.getInt(record+temp+"start",10)+" TO "+highlightText.getInt(record+temp+"end",10),Toast.LENGTH_SHORT).show();
                reference.setText(spannable);
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

        int record=position,temp=0;
        if(highlightText.getInt(record+"start",-1)!=-1){
            Toast.makeText(getApplicationContext(),"Getting "+record+temp,Toast.LENGTH_SHORT).show();
            Spannable spannable = new SpannableString(reference.getText().toString());
            try {
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), highlightText.getInt(record +  "start", 0), highlightText.getInt(record  + "end", 0), 0);

            }catch (Exception en){}
            Toast.makeText(getApplicationContext(),"FROM "+ highlightText.getInt(record+"start",0)+" TO "+highlightText.getInt(record+"end",0),Toast.LENGTH_SHORT).show();
            reference.setText(spannable);
            temp++;
        }

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
                    break;
            }
        }catch (Exception en){}

    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You can't go back from test!",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}
