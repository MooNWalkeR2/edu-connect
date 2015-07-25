package in.edconnect.StudentFragments.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.edconnect.R;
import in.edconnect.TakeThisExam.CheckThisExam;
import in.edconnect.TakeThisExam.TakeThisExam;

/**
 * Created by admin on 7/19/2015.
 */
public class StudentCardAdapter1 extends RecyclerView.Adapter<StudentCardAdapter1.StudentCardHolder>  implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private ArrayList<StudentCardInfo> studentInfo;
    private Animation cardintro;
    private CardView cardView;

    public StudentCardAdapter1(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
        studentInfo = new ArrayList<>();
    }

    public void addItemToList(String examname,String assessmenttype,String classno,String startingdate,String endingdate,String status,String startorreview) {
        try {
            studentInfo.add(0, new StudentCardInfo(examname,assessmenttype,classno,startingdate,endingdate,status,startorreview));
            notifyItemInserted(0);
            notifyItemChanged(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllItems() {
        studentInfo.clear();
    }

    @Override
    public StudentCardAdapter1.StudentCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            Log.e("Here","0");
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.studentcardinfo_layout, parent, false);
            Log.e("Here","1");
            StudentCardHolder holder = new StudentCardHolder(itemView);
            Log.e("Here", "2");
            holder.itemView.setOnClickListener(StudentCardAdapter1.this);
            holder.itemView.setTag(holder);
            Log.e("Here", "3");
            cardintro = AnimationUtils.loadAnimation(context, R.anim.cardintro);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardView.startAnimation(cardintro);



            return holder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(StudentCardAdapter1.StudentCardHolder holder, int position) {

        StudentCardInfo studentCardInfo =  studentInfo.get(position);
        holder.examname.setText(studentCardInfo.examname);
        holder.assessmenttype.setText(studentCardInfo.assessmenttype);
        holder.classno.setText(studentCardInfo.classno);
        holder.startingdate.setText(studentCardInfo.startingdate);
        holder.endingdate.setText(studentCardInfo.endingdate);
        holder.status.setText(studentCardInfo.status);
        holder.startorreview.setText(studentCardInfo.startorreview);

        holder.startorreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                context.startActivity(new Intent(activity, CheckThisExam.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentInfo.size();
    }

    @Override
    public void onClick(View view) {

    }


    public class StudentCardHolder extends RecyclerView.ViewHolder{

        TextView examname,assessmenttype,classno,startingdate,endingdate,status;
        Button startorreview;

        public StudentCardHolder(View itemView) {
            super(itemView);
            examname = (TextView)itemView.findViewById(R.id.examname_studentcardinfo);
            assessmenttype = (TextView)itemView.findViewById(R.id.assessmenttype_studentcardinfo);
            classno = (TextView)itemView.findViewById(R.id.class_studentcardinfo);
            startingdate = (TextView) itemView.findViewById(R.id.startingdate_studentcardinfo);
            endingdate = (TextView) itemView.findViewById(R.id.endingdate_studentcardinfo);
            status = (TextView) itemView.findViewById(R.id.status_studentcardinfo);
            startorreview = (Button) itemView.findViewById(R.id.startorreview_studentcardinfo);
        }
    }


    private class StudentCardInfo{

        String examname,assessmenttype,classno,startingdate,endingdate,status,startorreview;

        public StudentCardInfo(String examname,String assessmenttype,String classno,String startingdate,String endingdate,String status,String startorreview){
            this.examname = examname;
            this.assessmenttype = assessmenttype;
            this.classno = classno;
            this.startingdate = startingdate;
            this.endingdate = endingdate;
            this.status = status;
            this.startorreview = startorreview;
        }
    }
}
