package in.edconnect.TakeThisExam;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.Time;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.edconnect.Database.PassageDBHelper;
import in.edconnect.Database.QuestionDBHelper;
import in.edconnect.HomePageActivity;
import in.edconnect.ImageGetter.URLImageParser;
import in.edconnect.JsonRequest.NewCustomJsonRequest;
import in.edconnect.R;
import in.edconnect.SelectableTextView.SelectableTextView;
import in.edconnect.TextView.CustomTextView;
import in.edconnect.TouchImageView.TouchImageView;

/**
 * Created by admin on 7/19/2015.
 */
public class TakeThisExam extends Activity  {

    ArrayList<Question> questionArrayList;
    TextView question,termsandconditions,instructions;
    TextView reference,timer;
    RadioButton option1,option2,option3,option4;
    Button previous,skip,submit,calculator,protractor,highlight,scale,rotate,answerstats,termsandconditionsnext,instructionsnext;
    RelativeLayout termsandconditionslayout,instructionslayout,examlayout;
    RadioGroup options;
    private int position;
    ArrayList<Answers > answers;
    ImageView protractorImage;
    int _xDelta,_yDelta;
    SharedPreferences highlightText;
    LinearLayout match,rotationcontroller;
    int rotationCon=0;
    int dialerHeight,dialerWidth;
    Bitmap imageOriginal,imageScaled;
    double startAngle;
    Matrix matrix;
    int temp=0;
    ArrayList<HighlightText> highlightTextArrayList=new ArrayList<>();
    MalibuCountDownTimer countDownTimer ;
    private int r=0;
    ImageView mMainImg,mRotateImg;
    EditText match1,match2,match3,match4,match5;
    Button section1,section2,section3,section4,section5,section6;
    QuestionDBHelper dbHelper;
    int positionSec[]=new int[7];
    int currentSection=1;
    int sectionid=1;
    Spinner languageOptions;
    boolean section[]=new boolean[7];
    PassageDBHelper passageDBHelper=null;
    String startTime="0",examDuration="0";
    ScrollView scrollView;

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
        //protractorImage = (ImageView)findViewById(R.id.protractorimage);
        match = (LinearLayout)findViewById(R.id.match);
        match1=(EditText)findViewById(R.id.answer1);
        match2=(EditText)findViewById(R.id.answer2);
        match3=(EditText)findViewById(R.id.answer3);
        match4=(EditText)findViewById(R.id.answer4);
        match5=(EditText)findViewById(R.id.answer5);
        timer=(TextView)findViewById(R.id.timer);
        languageOptions=(Spinner)findViewById(R.id.languageOptions);
        termsandconditions=(TextView)findViewById(R.id.termsandconditions);
        termsandconditionsnext=(Button)findViewById(R.id.termsandconditionsnext);
        instructions=(TextView)findViewById(R.id.instructions);
        instructionsnext=(Button)findViewById(R.id.instructionsnext);
        termsandconditionslayout=(RelativeLayout)findViewById(R.id.termsandconditionslayout);
        instructionslayout=(RelativeLayout)findViewById(R.id.instructionslayout);
        examlayout=(RelativeLayout)findViewById(R.id.examlayout);

        examlayout.setVisibility(View.GONE);
        instructionslayout.setVisibility(View.GONE);
        termsandconditionslayout.setVisibility(View.VISIBLE);
        termsandconditionsnext.setEnabled(false);

        scrollView = (ScrollView)findViewById(R.id.scrollView);




        ////////////////////////////// Terms and conditions ////////////////////////////


        termsandconditions.setText("Business Standard Private Ltd allows you to access its websites (www.business-standard.com & www.businessstandard.com, www.smartinvestor.in, www.bshindi.com and www.bsmotoring,com) and all the content in them on certain terms and conditions specified below. By accessing or subscribing to any part of these sites, you are explicitly agreeing to the terms and conditions below.\n" +
                "\n" +
                "These terms and conditions were updated as on December 1, 2003, and Business Standard Private Ltd reserves all rights to change, modify or alter these terms and conditions at any point of time with or without notifying existing users and subscribers. If you do not agree to the new terms and conditions you should not use the business-standard.com and other associated sites mentioned above. Paid users will be refunded the unexpired portions of their subscriptions on a pro rata basis if they send us an email within 72 hours of changes made in the terms and conditions." +
                "Business Standard Private Ltd allows you to access its websites (www.business-standard.com & www.businessstandard.com, www.smartinvestor.in, www.bshindi.com and www.bsmotoring,com) and all the content in them on certain terms and conditions specified below. By accessing or subscribing to any part of these sites, you are explicitly agreeing to the terms and conditions below.\n" +
                "\" +\n" +
                "                    \"\\n\" +\n" +
                "                    \"These terms and conditions were updated as on December 1, 2003, and Business Standard Private Ltd reserves all ri");

        ////////////////////////////////////////////////////////////////////


        //////////////////////// Answer stats /////////////////


        answerstats = (Button)findViewById(R.id.answerstatsbutton);
        answerstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////// Send arraylist of answer and and also the section id //////////////
                Intent intent = new Intent();
                ArrayList<Integer> answered=new ArrayList<>();
                for(Answers answer:answers){
                    if(answer==null){
                        continue;
                    }
                    if(answer.sectionId==currentSection && !answer.ans.equals("0")){
                        answered.add(answer.position);
                    }
                }
                intent.putExtra("Total", dbHelper.getNoQuestionSectionId(currentSection));
                intent.putIntegerArrayListExtra("Answered", answered);
                intent.setClass(TakeThisExam.this,AnswerStats.class);
                startActivity(intent);   //// Check if the data of variable is preserved or not
            }
        });

        /////////////////////////////////////////////////////////

        //////////////////////// Section ///////////////////////

        section1 = (Button)findViewById(R.id.section1);
        section2 = (Button)findViewById(R.id.section2);
        section3 = (Button)findViewById(R.id.section3);
        section4 = (Button)findViewById(R.id.section4);
        section5 = (Button)findViewById(R.id.section5);
        section6 = (Button)findViewById(R.id.section6);




        SectionClick sectionClick =  new SectionClick();
        section1.setOnClickListener(sectionClick);
        section2.setOnClickListener(sectionClick);
        section3.setOnClickListener(sectionClick);
        section4.setOnClickListener(sectionClick);
        section5.setOnClickListener(sectionClick);
        section6.setOnClickListener(sectionClick);

        section1.setVisibility(View.GONE);
        section2.setVisibility(View.GONE);
        section3.setVisibility(View.GONE);
        section4.setVisibility(View.GONE);
        section5.setVisibility(View.GONE);
        section6.setVisibility(View.GONE);

        /////////////////////////////////////////////////////////////////////////



        /////////////////////Rotation//////////////////////////////////////

        mMainImg = (ImageView) findViewById(R.id.protractorimage);
        mRotateImg = (ImageView) findViewById(R.id.drager);

        mMainImg.setOnTouchListener(new SurfaceView.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eId = event.getAction();
                mRotateImg.setVisibility(ImageView.VISIBLE);
                if (v == mMainImg) {
                    switch (eId) {
                        case MotionEvent.ACTION_MOVE:
                            drag(v, event);
                            break;
                        case MotionEvent.ACTION_UP:
                            mRotateImg.setOnTouchListener(this);
                            break;
                        default:
                    }
                }
                if (v == mRotateImg) {
                    switch (eId) {
                        case MotionEvent.ACTION_MOVE:
                            r = r + 2;
                            rotate(v, event);
                            break;
                        case MotionEvent.ACTION_UP:
                            mRotateImg.setVisibility(ImageView.INVISIBLE);
                            break;
                        default:
                    }
                    if (v != mMainImg && v != mRotateImg)
                        mRotateImg.setVisibility(ImageView.INVISIBLE);
                }
                return true;
            }





        });
        ///////////////////////////////////////////////////////




       /* if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.protractor);
        }

        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }*/

        mMainImg.setVisibility(View.GONE);
        highlightText= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        highlightText.edit().clear().commit();

        Typeface faceGautami = Typeface.createFromAsset(getAssets(),
                "gautami.ttf");
        question.setTypeface(faceGautami);
        question.setText("M\'2. ??????????'");

        questionArrayList = new ArrayList<>();
        answers = new ArrayList<>();

        //Add here volley request to fetch all the question and options related to this quiz

        //Add all the question to arraylist
        //in that while loop check if the question is match the following or not if it is then set it
        try{

            dbHelper = new QuestionDBHelper(this);
            try {
                dbHelper.deleteAllData();
            }catch (Exception en){
                dbHelper=new QuestionDBHelper(this);
            }
            saveThisQuestionPaper();


            //if reference bar is not empty and if it is not already added then add it to array list
            questionArrayList.add(new Question(1,0,Html.fromHtml("M\'1. ?????????? ??????? ??????? ???????? ????'").toString(),"Gujarat","Delhi","UP","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(2,0,"The Centre for Cellular and Molecular Biology is situated at?","Patna","Jaipur","Jammu Kashmir","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(3,0,"The famous Dilwara Temples are situated in?","Rajasthan","Maharashtra","UP","Himachal","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(4,0,"What is the capital of India?", "Telangana", "Delhi", "UP", "Maharastra", "This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(5,1,"Grand Central Terminal, Park Avenue, New York is the world's?", "largest railway station", "highest railway station", "None", "longest railway station", "",0));
            highlightTextArrayList.add(new HighlightText(1));
            questionArrayList.add(new Question(6,2,"For which of the following disciplines is Nobel Prize awarded??", "Physics and Chemistry", " \tPhysiology or Medicine", "Literature, Peace and Economics", "All of the above", "",1));
            highlightTextArrayList.add(new HighlightText(2));
            questionArrayList.add(new Question(7,3,"Hitler party which came into power in 1933 is known as?", "Labour Party", "Nazi Party", "Democratic Party", "All of the above", "",0));
            highlightTextArrayList.add(new HighlightText(3));
            questionArrayList.get(5).setMatch("one", "two", "three", "four", "five", "2", "1", "5", "4", "3");

        }catch(Exception en){
            Log.e("5",highlightTextArrayList.size()+" "+en.toString());
        }



        //////////  assign time taken from web services

        countDownTimer= new MalibuCountDownTimer(600000,1000);
        countDownTimer.start();


        positionSec[currentSection]=1;
        //changeQuestion(positionSec[currentSection]);

        for(int k=0;k<100;k++){
            answers.add(null);
        }




       //////////////// Terms and conditions and instructions//////////////////////////////////

        termsandconditionsnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                examlayout.setVisibility(View.GONE);
                instructionslayout.setVisibility(View.VISIBLE);
                termsandconditionslayout.setVisibility(View.GONE);
            }
        });


        instructionsnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                String hourServer = startTime.substring(0,1);
                String hourMinute = startTime.substring(3,4);


                if(hour>=Integer.parseInt(hourServer)){
                    if(minute>=Integer.parseInt(hourMinute)) {
                        Log.e("Now","You can start the exam!!");
                        examlayout.setVisibility(View.VISIBLE);
                        instructionslayout.setVisibility(View.GONE);
                        termsandconditionslayout.setVisibility(View.GONE);

                    }
                    else{
                        Log.e("Now","You can not start an exam!");
                        examlayout.setVisibility(View.VISIBLE);
                        instructionslayout.setVisibility(View.GONE);
                        termsandconditionslayout.setVisibility(View.GONE);
                    }
                }else{
                    Log.e("Now","You can not start an exam!");
                    examlayout.setVisibility(View.VISIBLE);
                    instructionslayout.setVisibility(View.GONE);
                    termsandconditionslayout.setVisibility(View.GONE);
                }

            }
        });




        ///////////////////////////////////////////////////////////////////////////////

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(options.getCheckedRadioButtonId()==-1)) {

                    Log.e("AnswerId",Integer.parseInt(currentSection+positionSec[currentSection]+"")+"");
                    if(option1.getId()==(options.getCheckedRadioButtonId())){
                        answers.set( Integer.parseInt(currentSection+""+positionSec[currentSection]+""),new Answers("1",currentSection,positionSec[currentSection]));

                    }else if(option2.getId() == (options.getCheckedRadioButtonId())){
                        answers.set(Integer.parseInt(currentSection+""+positionSec[currentSection]+""),new Answers("2",currentSection,positionSec[currentSection]));

                    }else if(option3.getId() == (options.getCheckedRadioButtonId())){
                        answers.set(Integer.parseInt(currentSection+""+positionSec[currentSection]+""), new Answers("3",currentSection,positionSec[currentSection]));

                    }else if(option4.getId() == (options.getCheckedRadioButtonId())){
                        answers.set(Integer.parseInt(currentSection+""+positionSec[currentSection]+""),new Answers("4",currentSection,positionSec[currentSection]));

                    }
                    Log.e(positionSec[currentSection]+"Answers: ",answers.toString());


                    Log.e("NO OF QUESTION",dbHelper.getNoQuestionSectionId(currentSection)+"");
                    if(positionSec[currentSection]<dbHelper.getNoQuestionSectionId(currentSection)/2){
                        positionSec[currentSection]++;
                        changeQuestion(positionSec[currentSection]);
                    }else if(positionSec[currentSection] == dbHelper.getNoQuestionSectionId(currentSection)/2){
                          //showMeTheDialog();


                        section[currentSection]=true;
                        Log.e("Sections",section[1] + " " + section[2] + section[3] + section[4] + "");
                        int i=0;
                        boolean all=true;
                        for(boolean sec:section){
                            Log.e("i",i+""+sec);
                            if(i==0){

                            }else {
                                if (!sec) {
                                    Log.e("i", i + "");
                                    all = false;
                                    currentSection = i;
                                    changeQuestion(1);
                                    break;
                                }
                            }
                            i++;
                        }
                        if(all==true){
                            Intent intent = new Intent(TakeThisExam.this,CheckThisExam.class);
                            intent.putParcelableArrayListExtra("Answers",answers);
                            countDownTimer.stop();
                            startActivity(intent);
                        }
                        Log.e("END","OF SECTION"+currentSection);
                    }
                }else{
                    if(questionArrayList.get(position).matchFollowing==1){
                        Log.e("MAnswers: "," 1:" + match1.getText()+" 2:"+match2.getText()+" 3:"+match3.getText()+" 4:"+match4.getText()+" 5:"+match5.getText());
                        answers.add(position, new Answers("1995"));
                        answers.get(position).setMatch(match1.getText().toString(),match2.getText().toString(),match3.getText().toString(),match4.getText().toString(),match5.getText().toString());
                        if(positionSec[currentSection]<dbHelper.getNoQuestionSectionId(currentSection)/2){
                            positionSec[currentSection]++;
                            changeQuestion(positionSec[currentSection]);
                        }else if(positionSec[currentSection] == dbHelper.getNoQuestionSectionId(currentSection)/2){



                           // showMeTheDialog();
                            Log.e("END","OF SECTION"+currentSection);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Please Select One Option!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



         ///////////////////////////////////////////Onclicklistener
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answers.add(Integer.parseInt(currentSection+""+positionSec[currentSection]+""), new Answers("0",sectionid,positionSec[currentSection]));
                Log.e("NO OF QUESTIONS",dbHelper.getNoQuestionSectionId(currentSection)+"");
                if (positionSec[currentSection]  < dbHelper.getNoQuestionSectionId(currentSection)/2) {
                    positionSec[currentSection]++;
                    changeQuestion(positionSec[currentSection]);
                }else{

                    section[currentSection]=true;
                    Log.e("Sections",section[1] + " " + section[2] + section[3] + section[4] + "");
                    int i=0;
                    boolean all=true;
                    for(boolean sec:section){
                        Log.e("i",i+""+sec);
                        if(i==0){

                        }else {
                            if (!sec) {
                                Log.e("i", i + "");
                                all = false;
                                currentSection = i;
                                changeQuestion(0);
                                break;
                            }
                        }
                        i++;
                    }
                    if(all==true){
                        Intent intent = new Intent(TakeThisExam.this,CheckThisExam.class);
                        intent.putParcelableArrayListExtra("Answers",answers);
                        startActivity(intent);
                    }

                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (positionSec[currentSection] > 1) {
                    positionSec[currentSection]--;
                    changeQuestion(positionSec[currentSection]);
                    Log.e("InPre",positionSec[currentSection]+"");
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

     /*   rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rotationCon==0){
                    rotationCon=1;
                }else{
                    rotationCon=0;
                }
            }
        });*/


        protractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMainImg.getVisibility() == View.GONE) {
                    mMainImg.setVisibility(View.VISIBLE);
                } else {
                    mMainImg.setVisibility(View.GONE);
                    try{
                        mRotateImg.setVisibility(View.GONE);
                    }catch(Exception en){

                    }
                }
            }
        });



       /* protractorImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = protractorImage.getHeight();
                    dialerWidth = protractorImage.getWidth();

                    Log.e("ERROR-1",imageOriginal.getWidth()+" "+imageOriginal.getHeight());
                    Log.e("ERROR0",dialerHeight+ " "+dialerHeight);
                    Log.e("ERROR",((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth())+" "+ (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);


                    protractorImage.setImageBitmap(imageScaled);
                    protractorImage.setImageMatrix(matrix);
                }
            }
        });*/




       /* protractorImage.setOnTouchListener(new View.OnTouchListener() {
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
        });*/





        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int record=position,temp2=0;
                SharedPreferences.Editor editor = highlightText.edit();

                /*if(record!=0 && !questionArrayList.get(record).referencePar.equals(questionArrayList.get(record-1).referencePar)){
                    temp++;
                }
               while(highlightText.getInt(temp+"start"+temp2,-1)!=-1){
                    temp2++;
                }*/
                /*Toast.makeText(getApplicationContext(),"Storing "+record,Toast.LENGTH_SHORT).show();
                editor.putInt(record +  "start", reference.getSelectionStart());
                editor.putInt(record +  "end", reference.getSelectionEnd());
                editor.apply();*/

                //Toast.makeText(getApplicationContext(),"FROM "+ highlightText.getInt(record+temp+"start",10)+" TO "+highlightText.getInt(record+temp+"end",10),Toast.LENGTH_SHORT).show();
                /*editor.putInt(temp+"start"+temp2,reference.getSelectionStart());
                editor.putInt(temp+"end"+temp2,reference.getSelectionEnd());
                editor.apply();*/


                highlightTextArrayList.get(highlightTextArrayList.get(record).id).start.add(reference.getSelectionStart());
                highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.add(reference.getSelectionEnd());

                Spannable spannable = new SpannableString(reference.getText().toString());
                Log.e("10",highlightTextArrayList.get(highlightTextArrayList.get(record).id).start.toString());
                Log.e("20", highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.toString());
                int it=0;
                for(int start:highlightTextArrayList.get(highlightTextArrayList.get(record).id).start){
                    try {
                        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.get(it), 0);
                    }catch (Exception e){}
                    it++;
                }

                reference.setText(spannable);
            }
        });






    }








    public void changeQuestion(final int position) {

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageOptions.setAdapter(spinnerAdapter);

        Log.e("SECTION", currentSection + " " + positionSec[currentSection]);
        for (String langName : dbHelper.getThisLanguages(currentSection, positionSec[currentSection])) {
            spinnerAdapter.add(langName);
        }

        spinnerAdapter.notifyDataSetChanged();
        languageOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateQuestion(adapterView.getItemAtPosition(i).toString(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.e("Spinner",spinnerAdapter.getCount()+"");
        try {
            updateQuestion(spinnerAdapter.getItem(0), position);
        }catch (Exception e){
            Log.e("updateQ"," "+e.toString());
        }
    }

    public void updateQuestion(String lang,int position){



        Question questionCurrent = dbHelper.getThisQuestion(currentSection + "", (positionSec[currentSection] ) + "", lang);
        String referenceBar="";


        try {
            Log.e("PassageNo",Integer.parseInt(questionCurrent.passageNo)+"");
            if (Integer.parseInt(questionCurrent.passageNo) >= 0) {

                referenceBar = passageDBHelper.getPassage(questionCurrent.passageNo,lang);
                Log.e("Passage",referenceBar+"");
                reference.setVisibility(View.VISIBLE);
            }else{
                reference.setVisibility(View.GONE);
            }
        }catch (Exception e){
            referenceBar="";
            reference.setVisibility(View.GONE);
            Log.e("ErrorIn",e.toString());
        }
        questionCurrent.referencePar=referenceBar;

        if(questionCurrent.matchFollowing==1){
            options.setVisibility(View.GONE);
            match.setVisibility(View.VISIBLE);
            matchFollwing(questionCurrent);
            return;
        }

        match.setVisibility(View.GONE);
        options.setVisibility(View.VISIBLE);


        try {


            if(checkForLink(questionCurrent.question)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.question));
                question.setBackground(d);
            }else {
                URLImageParser p = new URLImageParser(question, this);
                Spanned sp=Html.fromHtml(questionCurrent.question+"<img src='www.google.com/1.png'/>",p,null);
                question.setText(sp);
            }
            if(checkForLink(questionCurrent.option1)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option1));
                option1.setBackground(d);
            }else {
                URLImageParser p = new URLImageParser(option1, this);
                Spanned sp=Html.fromHtml(questionCurrent.option1,p,null);
                option1.setText(sp);
            }
            if(checkForLink(questionCurrent.option2)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option2));
                option2.setBackground(d);
            }else {
                URLImageParser p = new URLImageParser(option2, this);
                Spanned sp=Html.fromHtml(questionCurrent.option2,p,null);
                option2.setText(sp);
            }
            if(checkForLink(questionCurrent.option3)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option3));
                option3.setBackground(d);
            }else {
                URLImageParser p = new URLImageParser(option3, this);
                Spanned sp=Html.fromHtml(questionCurrent.option3,p,null);
                option3.setText(sp);
            }
            if(checkForLink(questionCurrent.option4)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option4));
                option4.setBackground(d);
            }else {
                URLImageParser p = new URLImageParser(option4, this);
                Spanned sp=Html.fromHtml(questionCurrent.option4,p,null);
                option4.setText(sp);
            }

            URLImageParser p = new URLImageParser(reference, this);
            Spanned sp=Html.fromHtml(questionCurrent.referencePar,p,null);
            reference.setText(questionCurrent.referencePar);
        }catch (Exception en){}
        option1.setChecked(false);
        option2.setChecked(false);
        option3.setChecked(false);
        option4.setChecked(false);
        options.clearCheck();

        int record=position,temp2=0;
      /*  if(highlightText.getInt(record+"start",-1)!=-1){
            Toast.makeText(getApplicationContext(),"Getting "+record+temp,Toast.LENGTH_SHORT).show();
            Spannable spannable = new SpannableString(reference.getText().toString());
            try {
                int it=0;
                for(int start:highlightTextArrayList.get(highlightTextArrayList.get(position).id).start){
                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, highlightTextArrayList.get(highlightTextArrayList.get(position).id).end.get(it), 0);
                    it++;
                }

            }catch (Exception en){}
            //Toast.makeText(getApplicationContext(),"FROM "+ highlightText.getInt(record+"start",0)+" TO "+highlightText.getInt(record+"end",0),Toast.LENGTH_SHORT).show();
            reference.setText(spannable);
            temp++;
        }*/
        Spannable spannable = new SpannableString(reference.getText().toString());
        Log.e("21",highlightTextArrayList.get(highlightTextArrayList.get(record).id).start.toString());
        Log.e("22",highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.toString());
        int it=0;

        for(int start:highlightTextArrayList.get(highlightTextArrayList.get(record).id).start){
            try {
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.get(it), 0);
            }catch (Exception e){}
                it++;
        }

        reference.setText(spannable);

        try{
            switch (Integer.parseInt(answers.get(Integer.parseInt(currentSection+positionSec[position]+"")).ans)){
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



    private boolean checkForLink(String link){

        if(link.contains("http://") || link.contains("https://")){
            return true;
        }
        return false;
    }

    private Bitmap decodeThisUrl(String link){
        URL url=null;
        Bitmap bitmap=null;
        try {
            url = new URL(link);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (Exception en) {
            Log.e("Malformed", "URL EXCEPTION");
        }
        return bitmap;
    }


    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You can't go back from test!",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

    }


    public void matchFollwing(Question questionCurrent){

        TextView row12,row13,row14,row15,row21,row22,row23,row24,row25;
        CustomTextView row11;
        EditText answer1,answer2,answer3,answer4,answer5;

        Typeface typeface = Typeface.createFromAsset(getAssets(),"gautami.ttf");

        row11 =(CustomTextView)findViewById(R.id.row11);

        row12 =(TextView)findViewById(R.id.row12);
        row13 =(TextView)findViewById(R.id.row13);
        row14 =(TextView)findViewById(R.id.row14);
        row15 =(TextView)findViewById(R.id.row15);
        row21 =(TextView)findViewById(R.id.row21);
        row22 =(TextView)findViewById(R.id.row22);
        row23 =(TextView)findViewById(R.id.row23);
        row24 =(TextView)findViewById(R.id.row24);
        row25 =(TextView)findViewById(R.id.row25);




        row11.setText("Nahii???????");
        row12.setText(questionCurrent.row12);
        row13.setText(questionCurrent.row13);
        row14.setText(questionCurrent.row14);
        row15.setText(questionCurrent.row15);

        row21.setText(questionCurrent.row21);
        row22.setText(questionCurrent.row22);
        row23.setText(questionCurrent.row23);
        row24.setText(questionCurrent.row24);
        row25.setText(questionCurrent.row25);

    }


    public void showMeTheDialog(){
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

                //// Get the current section id stored in every questions ///////////
                //// And update it's sharedprefereces to true //////////////////////
                //// Check if every shared preferences is true then ////////////////
                //// Start Activity otherwise getSecQuestionAndRefresh /////////////

                dbHelper.deleteAllData();

                startActivity(new Intent(TakeThisExam.this, HomePageActivity.class));
                finish();
            }
        });
        dialog.show();
    }



    /////////////////////////////         TIMER                   ...////////////////////////////////////////////







    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            sendAnswers();
        }

        @Override
        public void onTick(long milliseconds)
        {
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
            timer.setText(hours + ":" + minutes + ":" + seconds);
        }

        public void stop(){
            super.cancel();
        }
    }












    //////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void sendAnswers(){
        startActivity(new Intent(TakeThisExam.this,HomePageActivity.class));
        finish();
    }

    public void updateTicker(long milliseconds){
        Toast.makeText(getApplicationContext(),milliseconds/1000+" ",Toast.LENGTH_SHORT).show();

    }
















    /////////////////////              Rotation            ///////////////////////



    private void rotate(View v, MotionEvent event) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.protractor);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(-r);
        Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
                true);
        BitmapDrawable bdr = new BitmapDrawable(rotaBitmap);
        mMainImg.setImageDrawable(bdr);
    }

    private void drag(View v, MotionEvent event) {
        LayoutParams mParams = (LayoutParams) mMainImg.getLayoutParams();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        mParams.leftMargin=x-150;
        mParams.topMargin =y-210;
        mMainImg.setLayoutParams(mParams);
        mRotateImg.setLayoutParams(mParams);



    }






    /////////////////////////////////////////////////////////////Save this question paper//////////////////////////////



    public void saveThisQuestionPaper(){
        ///// Send volley request and call savethisquestionpaperafterresponse after response
        String url="http://edconnect.in/home/exampapers/questionpaper.txt";
        /*NewCustomJsonRequest jsonRequest = new NewCustomJsonRequest(Request.Method.GET, url, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GettingRes",response);
                        saveThisQuestionPaperAfterResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Errror","Getting question paper!");
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);*/

        class RetrieveFeedTask extends AsyncTask<String, Void, String> {

            private Exception exception;
            StringBuilder str=new StringBuilder("");
            String temp="";
            String url = "http://edconnect.in/home/exampapers/questionpaper.txt";

            protected String doInBackground(String... urls) {
                Log.e("Here","123");
                try {
                    // Create a URL for the desired page
                    URL finalurl = new URL(url);

                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(finalurl.openStream()));
                    int i=0;
                    while ((( temp=in.readLine()) != null) && i!=16) {
                        // str is one line of text; readLine() strips the newline character(s)
                        if(temp==null){
                            i++;
                        }
                        Log.e("Iteration", "1" + temp);
                        str.append(temp);
                    }
                    in.close();
                    Log.e("BefRes",str.toString())
;                } catch (MalformedURLException e) {
                    Log.e("Errror",e.toString());
                } catch (IOException e) {
                    Log.e("Errror",e.toString());
                }
                Log.e("BefRes",str.toString());
                return str.toString();
            }

            protected void onPostExecute(String str) {
                // TODO: check this.exception
                // TODO: do something with the feed
                Log.e("Response", str);
                if(str.length()>15) {
                    saveThisQuestionPaperAfterResponse(str);
                }else{
                    Toast.makeText(getApplicationContext(),"Make sure you have working internet connection!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TakeThisExam.this,HomePageActivity.class));
                    finish();
                }
            }
        }
        new RetrieveFeedTask().execute("Doesn't matter!!");



    }


    public void saveThisQuestionPaperAfterResponse(String response){

        JSONObject questionPaper=null;
        JSONObject QuestionPaper=null;

        /*response = "{\n" +
                "  \"ExamQuestionPaper\": {\n" +
                "    \"ExamName\": \"Telangana General Knowledge\",\n" +
                "    \"ExamId\": \"000001\",\n" +
                "    \"MaximumMarks\": \"10\",\n" +
                "    \"StartDate\": \"8/18/2015\",\n" +
                "    \"EndDate\": \"8/18/2015\",\n" +
                "    \"StartTime\": \"15-00\",\n" +
                "    \"ExamDuration\": \"00-30\",\n" +
                "    \"Instructions\": \"some sequence of instructions\",\n" +
                "    \"StudentImage\": \"preet.jpg\",\n" +
                "    \"Sections\": {\n" +
                "      \"Section\": [\n" +
                "        {\n" +
                "          \"-id\": \"1\",\n" +
                "          \"-Name\": \"About Telangana\",\n" +
                "          \"SectionQuestions\": {\n" +
                "            \"Question\": [\n" +
                "              {\n" +
                "                \"-id\": \"1\",\n" +
                "                \"-CorrectAnswer\": \"3\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Which state is formed recently\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Chattishghar\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Jharkhand\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Telangana\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Uttarakhand\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"భారతదేశంలో నూతనంగా ఏర్పడిన రాష్ట్రం ఏది?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"చత్తీస్ ఘడ్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"జార్ఖండ్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"తెలంగాణ\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"ఉత్తరాఖండ్\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "            https://www.google.com\n" +
                "        \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"2\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Telangana formation day?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"June 1, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"June 2, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"June 3, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"June 4, 2014\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"తెలంగాణ రాష్ట్రావతరణ దినోత్సవం ఎప్పుడు?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"జూన్ 1, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"జూన్ 2, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"జూన్ 3, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"జూన్ 4, 2014\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"3\",\n" +
                "                \"-CorrectAnswer\": \"1\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Telangana captital\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Hyderabad\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Bangalore\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Vijayawada\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Chennai\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"తెలంగాణ రాజధాని ఏది?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"హైదరాబాద్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"బెంగుళూరు\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"విజయవాడ\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"చెన్నై\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"4\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Telangana first chief minister?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Devendra Fadnavis\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Chandra Sekhar Rao\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Sivaraj Chowhan\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Raman Singh\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"తెలంగాణ రాష్ట్ర మొదటి ముఖ్యమంత్రి ఎవరు?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"దేవేంద్ర ఫడ్నవిస్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"కే.చంద్రశేఖర రావ్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"శివరాజ్ చౌహాన్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"రమణ్ సింఘ్\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"5\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Which is border state of Telangana?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Bihar\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Maharastra\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Kerala\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Tamilnadu\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"ఈ క్రింది వానిలో ఏది తెలంగాణ సరిహద్దు రాష్ట్రాలలో ఒకటి?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"బీహార్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"మహారాష్ట్ర\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"కేరళ\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"తమిళనాడు\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"-id\": \"2\",\n" +
                "          \"-Name\": \"About Telugu\",\n" +
                "          \"SectionQuestions\": {\n" +
                "            \"Question\": [\n" +
                "              {\n" +
                "                \"-id\": \"1\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-PassageNo\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"How is Helen?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"cruel\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"good\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"innocent\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"mad\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"హెలెన్ ఎటువంటిది?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"కౄరమైనది\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"కరుణరస భరితమైన హృదయం కలిగినది\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"అమాయకమైనది\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"మతిస్థిమితం లేనిది\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"2\",\n" +
                "                \"-CorrectAnswer\": \"1\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-PassageNo\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Helen helped whom?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"poor\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"patients\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"disabled\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"unemployed\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"హెలెన్ ఎటువంటివాళ్ళ జీవితాలలో ఆమె అత్మవిశ్వాసాన్ని నింపింది?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"పేదవాళ్ళు\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"రోగులు\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"ప్రత్యేకావసరాలు ఉన్నవాళ్ళు\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"జీవనాధారం లేనివాళ్ళు\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"3\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-PassageNo\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Helen visiting following countries...\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Italy, Russia, France\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"England, Africa, America\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Japan, Koria, China\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Australia, Nuzeland, Malaysia\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"హెలెన్ ఏ ఏ దేశాలు పర్యటించింది?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"ఇటలి, రష్యా,ఫ్రాన్స్\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"ఇంగ్లాండ్, ఆఫ్రికా, అమెరికా\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"జపాన్, కొరియా, చైనా\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"ఆస్ట్రేలియా, న్యూజిలాండ్, మలేషియా\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"4\",\n" +
                "                \"-CorrectAnswer\": \"3\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": { \"p\": \"Which triangle is right angle triangle?\" },\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"1.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"2.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"3.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"4.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        { \"-id\": \"1\" },\n" +
                "                        { \"-id\": \"2\" },\n" +
                "                        { \"-id\": \"3\" },\n" +
                "                        { \"-id\": \"4\" }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"5\",\n" +
                "                \"-CorrectAnswer\": \"3\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": {\n" +
                "                      \"p\": [\n" +
                "                        \"Which pair of the following figures appears to be congruent (same size, same shape)?\",\n" +
                "                        {\n" +
                "                          \"-align\": \"center\",\n" +
                "                          \"-style\": \"text-align:center\",\n" +
                "                          \"span\": {\n" +
                "                            \"-style\": \"font-size:10.0pt\",\n" +
                "                            \"img\": {\n" +
                "                              \"-width\": \"143\",\n" +
                "                              \"-height\": \"136\",\n" +
                "                              \"-src\": \"5.gif\",\n" +
                "                              \"-style\": \"vertical-align: middle\"\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    },\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"aaaa\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"bbbb\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"cccc\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"dddd\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": {\n" +
                "                      \"p\": [\n" +
                "                        \"Which pair of the following figures appears to be congruent (same size, same shape)?\",\n" +
                "                        {\n" +
                "                          \"-align\": \"center\",\n" +
                "                          \"-style\": \"text-align:center\",\n" +
                "                          \"span\": {\n" +
                "                            \"-style\": \"font-size:10.0pt\",\n" +
                "                            \"img\": {\n" +
                "                              \"-width\": \"143\",\n" +
                "                              \"-height\": \"136\",\n" +
                "                              \"-src\": \"5.gif\",\n" +
                "                              \"-style\": \"vertical-align: middle\"\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    },\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"aaaa\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"bbbb\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"cccc\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"dddd\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ],\n" +
                "                \"Explanation\": {\n" +
                "                  \"Option\": [\n" +
                "                    {\n" +
                "                      \"-id\": \"1\",\n" +
                "                      \"#text\": \"Why did you answer Chattishghar??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"2\",\n" +
                "                      \"#text\": \"You selected Jharkhand??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"3\",\n" +
                "                      \"#text\": \"Why only Telangana??\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"-id\": \"4\",\n" +
                "                      \"#text\": \"You correctly selected Uttarakhand!\"\n" +
                "                    }\n" +
                "                  ]\n" +
                "                },\n" +
                "                \"ReferenceURL\": \"\n" +
                "              https://www.google.com\n" +
                "          \"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Images\": {\n" +
                "      \"Image\": [\n" +
                "        {\n" +
                "          \"-id\": \"1\",\n" +
                "          \"-Type\": \"Image\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"-id\": \"2\",\n" +
                "          \"-Type\": \"Image\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Passages\": {\n" +
                "      \"Passage\": {\n" +
                "        \"-id\": \"1\",\n" +
                "        \"Laungage\": [\n" +
                "          {\n" +
                "            \"-Name\": \"English\",\n" +
                "            \"PassageText\": \"\n" +
                "          Helen is a good girl. She feels pity by seeing poor. She loves nature. She worked for poor people. She visited England, America, Africa and then India. \n" +
                "        \"\n" +
                "          },\n" +
                "          {\n" +
                "            \"-Name\": \"Telugu\",\n" +
                "            \"PassageText\": \"\n" +
                "          హెలెన్ హృదయం కరుణరస భరితమయినది.దీనులను, దుఃఖితులను తలచుకుంటేనే ఆమె మనసు కరిగిపోయేది.పృకృతిలో ప్రతి అణువు ఆమెని పరవశింపచేసేవి.  మామూలు మనుషులకు కళ్ళు రెండే కాని ఆమె శరీరమంతా స్పర్శరూప నేత్రాలు ఉన్నాయి. ప్రతి స్పర్శకు ఆమె మనసారా అనుభూతి పొంది, తన భావాలను అనర్గళంగా ప్రకటించేది. తనలాంటి వాళ్ళ జీవితాలలో వెలుగులు నింపేందుకు జీవితాన్ని అంకితం చేసిన ఉన్నత వ్యక్తిత్వం హెలెన్ కెలర్ ది. ప్రపంచమంతా తిరిగి, 'ప్రత్యేకావసరాలూ ఉన్న పిల్లలను కలిసి వాళ్ళలో అత్మవిశ్వాసాన్ని నింపింది.ఈ నేపధ్యంలో ఇంగ్లాండ్, అమెరికా, ఆఫ్రిక దేశాలు పర్యటిస్తూ భరతదేశానికి కూడా వచ్చింది. ఆమె జీవితం, సాహిత్యం అందరికి ఆదర్శప్రాయమైనాయి.\n" +
                "        \"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
*/

        try {
            questionPaper = new JSONObject(response);
            QuestionPaper = questionPaper.getJSONObject("ExamQuestionPaper");
        }catch (JSONException e){
            Log.e("Parsing",e.toString());
        }


        //////////////////////// Images ////////////////////////////////





        //////////////////////////////////////////////////////////////

        //////////////////////// Passages //////////////////////////////


        try{
            try {
                passageDBHelper = new PassageDBHelper(this);
            }catch (Exception e){
                Log.e("passageDB",e.toString());
            }
            Log.e("1","this");
            JSONObject passages = QuestionPaper.getJSONObject("Passages");
            Log.e("2","this");
            Log.e("3","this");
            //JSONArray passage = passages.getJSONArray("Passage");

            //for(int i=0;i<passage.length();i++){

            //JSONObject passage=
                JSONObject passage = passages.getJSONObject("Passage");
                Log.e("4","this");
                //JSONObject passageOnes = passage.getJSONObject(i);
                JSONObject passageOnes=passage;
                Log.e("5","this");
                String id = passageOnes.getString("-id");
            Log.e("5.1","this");
                JSONArray passageLanguages = passageOnes.getJSONArray("Laungage");
            Log.e("6","this");
                for(int j=0;j<passageLanguages.length();j++){
                    JSONObject finalPassage = passageLanguages.getJSONObject(j);
                    Log.e("7","this");
                    String passagename = finalPassage.getString("-Name");
                    Log.e("8","this");
                    String passagetext = finalPassage.getString("PassageText");
                    passageDBHelper.insertPassage(id,passagename,passagetext);
                }

            //}


        }catch (Exception e){
            Log.e("ErrorIn","Storing Passages");
        }



        //////////////////////////////////////////////////////////////////////


        try{
            String examName = QuestionPaper.getString("ExamName");
            Log.e("ExamName",examName);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String examID = QuestionPaper.getString("ExamId");
            Log.e("ExamId",examID);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String maximumMarks = QuestionPaper.getString("MaximumMarks");
            Log.e("Max Marks",maximumMarks);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


        try{
            String startDate =  QuestionPaper.getString("StartDate");
            Log.e("Start Date",startDate);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String endDate = QuestionPaper.getString("EndDate");
            Log.e("End Date",endDate);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


        try{
            startTime = QuestionPaper.getString("StartTime");
            Log.e("Start Time ",startTime);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            examDuration = QuestionPaper.getString("ExamDuration");
            Log.e("Duration",examDuration);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


        try{
            String instruction = QuestionPaper.getString("Instructions");
            Log.e("Instruction", instruction);

            instruction+="Business Standard Private Ltd allows you to access its websites (www.business-standard.com & www.businessstandard.com, www.smartinvestor.in, www.bshindi.com and www.bsmotoring,com) and all the content in them on certain terms and conditions specified below. By accessing or subscribing to any part of these sites, you are explicitly agreeing to the terms and conditions below.\n" +
                    "\n" +
                    "These terms and conditions were updated as on December 1, 2003, and Business Standard Private Ltd reserves all rights to change, modify or alter these terms and conditions at any point of time with or without notifying existing users and subscribers. If you do not agree to the new terms and conditions you should not use the business-standard.com and other associated sites mentioned above. Paid users will be refunded the unexpired portions of their subscriptions on a pro rata basis if they send us an email within 72 hours of changes made in the terms and conditions.";
            instruction+=instruction;
            instructions.setText(instruction);
            termsandconditionsnext.setEnabled(true);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String studentImageURL = QuestionPaper.getString("StudentImage");
            Log.e("Student URL",studentImageURL);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            JSONObject sections = QuestionPaper.getJSONObject("Sections");
            JSONArray Section = sections.getJSONArray("Section");
            for(int i=0;i<Section.length();i++){

                JSONObject section = Section.getJSONObject(i);
                String secId="",secName="";
                try {
                     secId = section.getString("-id");
                }catch (JSONException en){
                    Log.e("Error at ",en.toString());
                }
                try{
                     secName = section.getString("-Name");
                }catch (JSONException en){
                    Log.e("Error at ",en.toString());
                }
                Log.e("Name:",secName);

                JSONObject sectionQuestions = section.getJSONObject("SectionQuestions");
                JSONArray Questions = sectionQuestions.getJSONArray("Question");

                String explanationOption1 = "",explanationOption2 = "",explanationOption3 = "",explanationOption4 = "";
                String referenceUrl = "";





                //Loop for each question
                for(int j=0;j<Questions.length();j++){

                    ////////////// Create your database from here (id + language = primery key) ///////////////
                    JSONArray questionLanguages=null;
                    String questionId="", questionCorrectAnswer="" , questionMarks="" , questionType="",passageNo="";
                    try {

                        JSONObject question = Questions.getJSONObject(j);
                        questionId = question.getString("-id");
                        questionCorrectAnswer = question.getString("-CorrectAnswer");



                        //////// Explanation //////

                        referenceUrl=question.getString("ReferenceURL");
                        try{
                            JSONObject explanations = question.getJSONObject("Explanation");
                            JSONArray explanationOptions = explanations.getJSONArray("Option");

                            for(int m=0;m<explanationOptions.length();m++){
                                JSONObject explanationOption = explanationOptions.getJSONObject(m);
                                switch(m){
                                    case 0:
                                        explanationOption1=explanationOption.getString("#text");
                                        break;

                                    case 1:
                                        explanationOption2=explanationOption.getString("#text");
                                        break;

                                    case 2:
                                        explanationOption3=explanationOption.getString("#text");
                                        break;

                                    case 3:
                                        explanationOption4=explanationOption.getString("#text");
                                        break;
                                }
                            }

                        }catch(JSONException en){
                            Log.e("ErrorIn","Explanation "+en.toString());
                        }




                        /////////////////////////////




                        try {
                            passageNo = question.getString("-PassageNo");
                            Log.e("PassageNo","Successfull"+passageNo);
                        }catch (Exception e){
                            Log.e("ErrorIn","NO passageNo");
                            passageNo = "-1";
                        }

                        /////////////// For hightlighting //////////////////////
                        // passageid can not be 0 //

                        if(passageNo.equals("-1")) {
                            highlightTextArrayList.add(new HighlightText(0));
                        }else{
                            highlightTextArrayList.add(new HighlightText(Integer.parseInt(passageNo)));
                        }

                        ///////////////////////////////////////////////////////

                        questionMarks = question.getString("-QuestionMarks");
                        questionType = question.getString("-Type");
                        Log.e("InfoQuestion", questionId + " " + questionCorrectAnswer + " " + questionMarks + " " + questionType);

                        questionLanguages = question.getJSONArray("Laungage");
                    }catch (JSONException en){
                        Log.e("Error At",en.toString());
                    }

                    for(int k=0;k<questionLanguages.length();k++){
                        JSONObject questionLanguage=null;
                        String languageName="",languageQuetionText="",optionId1="",optionText1="";
                        String optionId2="",optionText2="",optionId3="",optionText3="",optionId4="",optionText4="";
                        try {
                            questionLanguage = questionLanguages.getJSONObject(k);
                            languageName = questionLanguage.getString("-Name");
                            languageQuetionText = questionLanguage.getString("QuestionText");
                            Log.e("Language", languageName + " " + languageQuetionText);
                        }catch (JSONException en){
                            Log.e("Error at ",en.toString());
                        }

                        try {

                            JSONObject options = questionLanguage.getJSONObject("Options");
                            JSONArray option = options.getJSONArray("Option");

                            int optid=0;
                            for (int l = 0; l < option.length(); l++) {

                                JSONObject Option = option.getJSONObject(l);
                                Log.e("Option" + l, Option.toString());
                                switch(optid){
                                    case 0:

                                        try {
                                            optionId1 = Option.getString("-id");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        try {
                                            optionText1 = Option.getString("#text");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        optid++;
                                        break;
                                    case 1:

                                        try {
                                            optionId2 = Option.getString("-id");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        try {
                                            optionText2 = Option.getString("#text");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        optid++;
                                        break;
                                    case 2:

                                        try {
                                            optionId3 = Option.getString("-id");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        try {
                                            optionText3 = Option.getString("#text");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        optid++;
                                        break;
                                    case 3:

                                        try {
                                            optionId4 = Option.getString("-id");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        try {
                                            optionText4 = Option.getString("#text");
                                        } catch (JSONException e) {
                                            Log.e("Error At", e.toString());
                                        }
                                        optid++;
                                        break;

                                }
                            }
                        }catch (JSONException en){
                            Log.e("Error At",en.toString());
                        }

                        dbHelper.insertQuestion(questionId,secId,secName,questionCorrectAnswer,questionMarks,questionType,languageName,languageQuetionText,optionText1,optionText2,optionText3,optionText4,passageNo,explanationOption1,explanationOption2,explanationOption3,explanationOption4,referenceUrl);
                    }

                }

            }
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        changeQuestion(positionSec[currentSection]);
        makeThisSectionsVisible();


    }





    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    //////////////////////////////////////////// Section ////////////////////////////////////////////////////////




    private class SectionClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.section1:
                    getSecQuestionAndRefresh(1);
                    break;

                case R.id.section2:
                    getSecQuestionAndRefresh(2);
                    break;

                case R.id.section3:
                    getSecQuestionAndRefresh(3);
                    break;

                case R.id.section4:
                    getSecQuestionAndRefresh(4);
                    break;

                case R.id.section5:
                    getSecQuestionAndRefresh(5);
                    break;

                case R.id.section6:
                    getSecQuestionAndRefresh(6);
                    break;
            }
        }
    }


    public void getSecQuestionAndRefresh(int sectionid){

        currentSection=sectionid;
        Log.e("HEREGETSEC",positionSec[currentSection]+"");
        changeQuestion(positionSec[currentSection]);

    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////// Section ///////////////////////


    public void makeThisSectionsVisible(){

        int secCount = 0;
        Log.e("Sections: ",dbHelper.getThisSections().toString());
        for(int i=1;i<=6;i++){
            section[i]=true;
        }

        for(Sections sectio:dbHelper.getThisSections()){
            switch (secCount){
                case 0:
                    section1.setVisibility(View.VISIBLE);
                    section1.setText(sectio.sectionname);
                    positionSec[1]=1;
                    section[1]=false;
                    /// Store Section Id here //////////////
                    secCount++;
                    break;

                case 1:
                    section2.setVisibility(View.VISIBLE);
                    section2.setText(sectio.sectionname);
                    positionSec[2]=1;

                    section[2]=false;
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 3:
                    section3.setVisibility(View.VISIBLE);
                    section3.setText(sectio.sectionname);
                    positionSec[3]=1;
                    section[3]=false;
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 4:
                    section4.setVisibility(View.VISIBLE);
                    section4.setText(sectio.sectionname);
                    positionSec[4]=1;
                    section[4]=false;
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 5:
                    section5.setVisibility(View.VISIBLE);
                    section5.setText(sectio.sectionname);
                    positionSec[5]=1;
                    section[5]=false;
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 6:
                    section6.setVisibility(View.VISIBLE);
                    section6.setText(sectio.sectionname);
                    positionSec[6]=1;
                    section[6]=false;
                    /// Store Section Id here //////////////
                    secCount++;
                    break;

            }

        }



    }


}
