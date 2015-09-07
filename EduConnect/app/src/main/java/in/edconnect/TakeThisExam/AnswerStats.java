package in.edconnect.TakeThisExam;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.edconnect.R;

/**
 * Created by admin on 8/28/2015.
 */
public class AnswerStats extends Activity {


    private ListView answerStatsList;
    private int total;
    private ArrayList<Integer> answersArrayList;
    private TextView totalQuestions;
    private ArrayList<String> answered = new ArrayList<>();


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.answerstats_layout);
        answersArrayList=getIntent().getExtras().getIntegerArrayList("Answered");
        total=getIntent().getExtras().getInt("Total");

        answerStatsList=(ListView)findViewById(R.id.ListAnswerStats);
        totalQuestions=(TextView)findViewById(R.id.totalQuestions);
        answered.clear();
        for(int answer:answersArrayList){
            answered.add(" Q."+answer+"     Answered!");
        }

        Log.e("RECEIVED",answersArrayList.toString());
        Log.e("ARRAY", answered.toString());
        totalQuestions.setText(total/2+"");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,answered);

        answerStatsList.setAdapter(arrayAdapter);

    }





    private class AnswerStatsAdapter extends BaseAdapter{

        public int getCount(){
            return answersArrayList.size();
        }

        public Object getItem(int item){
            return answersArrayList.get(item);
        }

        public long getItemId(int item){
            return 0;
        }


        public View getView(int itemId , View item , ViewGroup parent){

            View anAnswer=item;
            if(item==null) {
                LayoutInflater inflater = (LayoutInflater) AnswerStats.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                anAnswer = inflater.inflate(R.layout.ananswer_layout, parent, false);
            }

            TextView answerText;

            answerText=(TextView)anAnswer.findViewById(R.id.answer);

            ///////////// DO SOME PROCESSING HERE /////////////////

            ///////////////////////////////////////////////////////

            answerText.setText(answersArrayList.get(itemId).toString());
            return anAnswer;
        }
    }
}
