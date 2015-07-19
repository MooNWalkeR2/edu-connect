package in.edconnect.StudentFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import in.edconnect.R;
import in.edconnect.StudentFragments.ViewAdapters.StudentCardAdapter;

/**
 * Created by admin on 7/18/2015.
 */
public class DoneFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static DoneFragment newInstance(){
        DoneFragment doneFragment = new DoneFragment();
        return  doneFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup viewGroup , Bundle bundle){
        View rootView = inflater.inflate(R.layout.donefragment_layout,viewGroup,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_student);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_student);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        StudentCardAdapter cardAdapter = new StudentCardAdapter(getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.addItemToList("Telugu Chapter 1 Assessment","Standard","6th class","7/7/2015 12:00:00 PM","7/8/2015 12:00:00 PM","Completed","Review");        //Arbitory Item

        return rootView;
    }
}
