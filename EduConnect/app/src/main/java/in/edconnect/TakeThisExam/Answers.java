package in.edconnect.TakeThisExam;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 8/22/2015.
 */
public class Answers  implements Parcelable{

    int questionid;
    String ans;
    String match1="0",match2="0",match3="0",match4="0",match5="0";

    public Answers(String ans){
        this.ans=ans;
    }

    public void setAns(String ans){
        this.ans=ans;
    }

    public void setMatch(String match1,String match2,String match3,String match4,String match5){
        this.match1=match1;
        this.match2=match2;
        this.match3=match3;
        this.match4=match4;
        this.match5=match5;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
