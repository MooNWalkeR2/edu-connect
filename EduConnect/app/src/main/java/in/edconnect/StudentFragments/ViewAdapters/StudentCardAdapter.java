package in.edconnect.StudentFragments.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.ArrayList;

import in.edconnect.R;

/**
 * Created by admin on 7/19/2015.
 */
public class StudentCardAdapter extends RecyclerView.Adapter<StudentCardAdapter.StudentCardHolder>  implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private ArrayList<StudentCardInfo> studentInfo;
    private Animation cardintro;
    private CardView cardView;

    public StudentCardAdapter(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
        studentInfo = new ArrayList<>();
    }

    public void addItemToList() {
        try {
            studentInfo.add(0, new StudentCardInfo());
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
    public StudentCardAdapter.StudentCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            Log.e("Here","0");
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.studentcardinfo_layout, parent, false);
            Log.e("Here","1");
            StudentCardHolder holder = new StudentCardHolder(itemView);
            Log.e("Here", "2");
            holder.itemView.setOnClickListener(StudentCardAdapter.this);
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
    public void onBindViewHolder(StudentCardAdapter.StudentCardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return studentInfo.size();
    }

    @Override
    public void onClick(View view) {

    }


    public class StudentCardHolder extends RecyclerView.ViewHolder{

        public StudentCardHolder(View itemView) {
            super(itemView);
        }
    }


    private class StudentCardInfo{

        public StudentCardInfo(){

        }
    }
}
