package in.edconnect.TakeThisExam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.edconnect.R;

public class MySimpleArrayAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Question> values;
    private final ArrayList<String> answers;

    public MySimpleArrayAdapter(Context context, ArrayList<Question> values,ArrayList<String> answers) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.comfirmation_popup_question, parent, false);

        TextView question=(TextView)rowView.findViewById(R.id.question);
        RadioButton option1=(RadioButton)rowView.findViewById(R.id.option1);
        RadioButton option2=(RadioButton)rowView.findViewById(R.id.option2);
        RadioButton option3=(RadioButton)rowView.findViewById(R.id.option3);
        RadioButton option4=(RadioButton)rowView.findViewById(R.id.option4);

        question.setText(values.get(position).question);
        option1.setText(values.get(position).option1);
        option2.setText(values.get(position).option2);
        option3.setText(values.get(position).option3);
        option4.setText(values.get(position).option4);

        try {


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