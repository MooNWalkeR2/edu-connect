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
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import in.edconnect.TextView.CustomTextView;
import in.edconnect.TouchImageView.TouchImageView;

/**
 * Created by admin on 7/19/2015.
 */
public class TakeThisExam extends Activity {

    ArrayList<Question> questionArrayList;
    TextView question;
    TextView reference;
    RadioButton option1,option2,option3,option4;
    Button previous,skip,submit,calculator,protractor,highlight,scale,rotate;
    RadioGroup options;
    private int position;
    ArrayList<String> answers;
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
        match = (LinearLayout)findViewById(R.id.match);
        rotationcontroller = (LinearLayout)findViewById(R.id.protractorcontroller);
        rotate = (Button)findViewById(R.id.rotate);




        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.protractor);
        }

        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

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

        //Add all the question to arraylist
        //in that while loop check if the question is match the following or not if it is then set it
        try{


            //if reference bar is not empty and if it is not already added then add it to array list
            questionArrayList.add(new Question(1,0,"What is the capital of India?","Gujarat","Delhi","UP","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
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

        countDownTimer= new MalibuCountDownTimer(100000,1000);
        countDownTimer.start();


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


                          showMeTheDialog();

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
                }else{
                    int check=0;
                    for(String ans:answers){
                        if(ans.equals("0")){

                        }else{
                            check=1;
                        }
                    }
                    if(check==0){
                        Toast.makeText(getApplicationContext(),"You haven't selected any answers!",Toast.LENGTH_SHORT).show();
                        showMeTheDialog();
                    }
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

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rotationCon==0){
                    rotationCon=1;
                }else{
                    rotationCon=0;
                }
            }
        });


        protractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rotationcontroller.getVisibility() == View.INVISIBLE) {

                    rotationcontroller.setVisibility(View.VISIBLE);
                } else {

                    rotationcontroller.setVisibility(View.INVISIBLE);
                }
            }
        });



        protractorImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

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
        });



        protractorImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                ///Rotation

                if(rotationCon==1){


                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            startAngle = getAngle(event.getX(), event.getY());
                            break;

                        case MotionEvent.ACTION_MOVE:
                            double currentAngle = getAngle(event.getX(), event.getY());
                            rotateDialer((float) (startAngle - currentAngle));
                            startAngle = currentAngle;
                            break;

                        case MotionEvent.ACTION_UP:

                            break;
                    }
                    return true;


                }


                ///////////////




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








    public void changeQuestion(int position){

        Question questionCurrent = questionArrayList.get(position);


        if(questionCurrent.matchFollowing==1){
            options.setVisibility(View.GONE);
            match.setVisibility(View.VISIBLE);
            matchFollwing(questionCurrent);
            return;
        }

        match.setVisibility(View.GONE);
        options.setVisibility(View.VISIBLE);


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



    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    private void rotateDialer(float degrees) {
        matrix.postRotate(degrees);

        protractorImage.setImageMatrix(matrix);
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
        public void onTick(long millisUntilFinished)
        {
            updateTicker((millisUntilFinished / 1000));
        }
    }












    //////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void sendAnswers(){
        startActivity(new Intent(TakeThisExam.this,HomePageActivity.class));
        finish();
    }

    public void updateTicker(long remaining){
        Toast.makeText(getApplicationContext(),remaining+" ",Toast.LENGTH_SHORT).show();
    }
}
