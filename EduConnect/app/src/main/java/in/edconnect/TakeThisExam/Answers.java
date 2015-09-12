package in.edconnect.TakeThisExam;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 8/22/2015.
 */
public class Answers  implements Parcelable{

    int position;
    int sectionId;
    String ans;
    String match1="0",match2="0",match3="0",match4="0",match5="0";

    public Answers(Parcel source){
        position=source.readInt();
        sectionId=source.readInt();
        ans=source.readString();
    }


    public Answers(String ans){
        this.ans=ans;
    }

    public Answers(String ans,int sectionId,int position){
        this.ans=ans;
        this.sectionId=sectionId;
        this.position=position;
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

    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeInt(sectionId);
        dest.writeString(ans);
    //    dest.writeString(color);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Answers createFromParcel(Parcel in) {
            return new Answers(in);
        }

        public Answers[] newArray(int size) {
            return new Answers[size];
        }
    };
}
