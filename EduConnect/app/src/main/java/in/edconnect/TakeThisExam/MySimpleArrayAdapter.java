package in.edconnect.TakeThisExam;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.edconnect.R;
import in.edconnect.TextView.CustomTextView;

public class MySimpleArrayAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Question> values;
    private final ArrayList<Answers> answers;
    LinearLayout match;
    RadioGroup options;

    public MySimpleArrayAdapter(Context context, ArrayList<Question> values,ArrayList<Answers> answers) {
        this.context = context;
        this.values = values;
        this.answers = answers;

    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);




        View rowView = inflater.inflate(R.layout.comfirmation_popup_question, parent, false);

        match = (LinearLayout)rowView.findViewById(R.id.match);
        options=(RadioGroup)rowView.findViewById(R.id.options);


        Question questionCurrent = values.get(position);


        if(questionCurrent.matchFollowing==1){
            options.setVisibility(View.GONE);
            match.setVisibility(View.VISIBLE);

            TextView row12,row13,row14,row15,row21,row22,row23,row24,row25;
            CustomTextView row11;
            EditText answer1,answer2,answer3,answer4,answer5;



            row11 =(CustomTextView)rowView.findViewById(R.id.row11);

            row12 =(TextView)rowView.findViewById(R.id.row12);
            row13 =(TextView)rowView.findViewById(R.id.row13);
            row14 =(TextView)rowView.findViewById(R.id.row14);
            row15 =(TextView)rowView.findViewById(R.id.row15);
            row21 =(TextView)rowView.findViewById(R.id.row21);
            row22 =(TextView)rowView.findViewById(R.id.row22);
            row23 =(TextView)rowView.findViewById(R.id.row23);
            row24 =(TextView)rowView.findViewById(R.id.row24);
            row25 =(TextView)rowView.findViewById(R.id.row25);




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

            answer1=(EditText)rowView.findViewById(R.id.answer1);
            answer2=(EditText)rowView.findViewById(R.id.answer2);
            answer3=(EditText)rowView.findViewById(R.id.answer3);
            answer4=(EditText)rowView.findViewById(R.id.answer4);
            answer5=(EditText)rowView.findViewById(R.id.answer5);

            answer1.setText(answers.get(position).match1);
            answer2.setText(answers.get(position).match2);
            answer3.setText(answers.get(position).match3);
            answer4.setText(answers.get(position).match4);
            answer5.setText(answers.get(position).match5);

            answer1.setEnabled(false);
            answer2.setEnabled(false);
            answer3.setEnabled(false);
            answer4.setEnabled(false);
            answer5.setEnabled(false);
            return rowView;
        }

        match.setVisibility(View.GONE);
        options.setVisibility(View.VISIBLE);

        TextView question=(TextView)rowView.findViewById(R.id.question);
        RadioButton option1=(RadioButton)rowView.findViewById(R.id.option1);
        RadioButton option2=(RadioButton)rowView.findViewById(R.id.option2);
        RadioButton option3=(RadioButton)rowView.findViewById(R.id.option3);
        RadioButton option4=(RadioButton)rowView.findViewById(R.id.option4);
        Button explanation1=(Button)rowView.findViewById(R.id.explanation1);
        Button explanation2=(Button)rowView.findViewById(R.id.explanation2);
        Button explanation3=(Button)rowView.findViewById(R.id.explanation3);
        Button explanation4=(Button)rowView.findViewById(R.id.explanation4);
        final TextView explanationinfo1=(TextView)rowView.findViewById(R.id.explanationinfo1);
        final TextView explanationinfo2=(TextView)rowView.findViewById(R.id.explanationinfo2);
        final TextView explanationinfo3=(TextView)rowView.findViewById(R.id.explanationinfo3);
        final TextView explanationinfo4=(TextView)rowView.findViewById(R.id.explanationinfo4);
        Button referenceUrl = (Button)rowView.findViewById(R.id.referenceurl);
        TextView answerStatus = (TextView)rowView.findViewById(R.id.answerstatus);

        question.setText(values.get(position).question);
        option1.setText(values.get(position).option1);
        option2.setText(values.get(position).option2);
        option3.setText(values.get(position).option3);
        option4.setText(values.get(position).option4);


        explanation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (explanationinfo1.getVisibility() == View.GONE) {
                    explanationinfo1.setText(values.get(position).explanation1);
                    explanationinfo1.setVisibility(View.VISIBLE);
                } else {
                    explanationinfo1.setText("");
                    explanationinfo1.setVisibility(View.GONE);
                }
            }
        });


        explanation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(explanationinfo2.getVisibility()==View.GONE){
                    explanationinfo2.setText(values.get(position).explanation2);
                    explanationinfo2.setVisibility(View.VISIBLE);
                }else{
                    explanationinfo2.setText("");
                    explanationinfo2.setVisibility(View.GONE);
                }
            }
        });


        explanation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(explanationinfo3.getVisibility()==View.GONE){
                    explanationinfo3.setText(values.get(position).explanation3);
                    explanationinfo3.setVisibility(View.VISIBLE);
                }else{
                    explanationinfo3.setText("");
                    explanationinfo3.setVisibility(View.GONE);
                }
            }
        });


        explanation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(explanationinfo4.getVisibility()==View.GONE){
                    explanationinfo4.setText(values.get(position).explanation4);
                    explanationinfo4.setVisibility(View.VISIBLE);
                }else{
                    explanationinfo4.setText("");
                    explanationinfo4.setVisibility(View.GONE);
                }
            }
        });

        referenceUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Uri uri = Uri.parse(values.get(position).referenceUrl.trim()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }catch (Exception en){
                    Toast.makeText(context,"Problem starting web browser!"+en.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(values.get(position).correctAnswer.equals(answers.get(position).ans)){
            answerStatus.setText("You answered it correctly!");
        }else if(answers.get(position).ans.equals("0")){
            answerStatus.setText("You did not answer this question!");
        }else{
            answerStatus.setText("You did not answered it correctly!");
        }


        try {


            switch (Integer.parseInt(values.get(position).correctAnswer)) {
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
        }catch (Exception en){
            Log.e("Pos", " " + position + " " + answers.size());
        }


        option1.setTextColor(Color.BLACK);
        option2.setTextColor(Color.BLACK);
        option3.setTextColor(Color.BLACK);
        option4.setTextColor(Color.BLACK);

        try {


            switch (Integer.parseInt(answers.get(position).ans)) {
                case 0:
                    break;
                case 1:
                    option1.setTextColor(Color.RED);
                    break;
                case 2:
                    option2.setTextColor(Color.RED);
                    break;
                case 3:
                    option3.setTextColor(Color.RED);
                    break;
                case 4:
                    option4.setTextColor(Color.RED);
                    break;

            }
        }catch (Exception en){
            Log.e("Pos", " " + position + " " + answers.size());
        }


        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);

        return rowView;
    }

}