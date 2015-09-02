package in.edconnect.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.edconnect.TakeThisExam.Question;
import in.edconnect.TakeThisExam.Sections;

/**
 * Created by admin on 8/27/2015.
 */
public class QuestionDBHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "questionseduconnect";
    public static final String TABLE_NAME ="questions";
    public static final String UN_ID="uid";
    public static final String ID = "id";
    public static final String SECTION_ID="sectionid";
    public static final String SECTION_NAME="sectionname";
    public static final String CORRECT_ANSWER ="correctanswer";
    public static final String QUESTION_MARKS="questionmarks";
    public static final String TYPE = "type";
    public static final String LANGUAGE_NAME ="languagename";
    public static final String LANGUAGE_TEXT="languagetext";
    public static final String OPTION_ONE="option1";
    public static final String OPTION_TWO="option2";
    public static final String OPTION_THREE="option3";
    public static final String OPTION_FOUR="option4";


    public QuestionDBHelper(Context context){
        super(context, DATABASE_NAME , null , 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table questions uid integer primery key , id integer , sectionid integer , sectionname text , correctanswer integer , questionmarks integer ,type text , languagename text , languagetext text , option1 text , option2 text ,option3 text , option4 text ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertQuestion(int id ,int sectionid , String sectionname, int correctAnswer , int questionMarks ,String type , String languageName , String languageText,String option1,String option2,String option3,String option4){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try{
            cv.put(ID,id);
            cv.put(SECTION_ID,sectionid);
            cv.put(SECTION_NAME,sectionname);
            cv.put(CORRECT_ANSWER,correctAnswer);
            cv.put(QUESTION_MARKS,questionMarks);
            cv.put(TYPE,type);
            cv.put(LANGUAGE_NAME,languageName);
            cv.put(LANGUAGE_TEXT,languageText);
            cv.put(OPTION_ONE,option1);
            cv.put(OPTION_TWO,option2);
            cv.put(OPTION_THREE,option3);
            cv.put(OPTION_FOUR,option4);
            db.insert(TABLE_NAME,null,cv);
        }catch(Exception en){
            Log.e("ERROR", en.toString());
            return false;
        }

        return true;
    }


    public Question getThisQuestion(int id, String languageName){
        Question question = new Question();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id + " AND " + LANGUAGE_NAME + "=" + languageName, new String[]{});
        }catch (Exception en){
            Log.e("ERROR",en.toString());
        }

        /////////////////////////// SET THE QUESTION HERE //////////////////////////////////////

        return question;
    }


    public ArrayList<String> getThisLanguages(int id){
        ArrayList<String> langNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT "+LANGUAGE_NAME+" FROM "+TABLE_NAME+" WHERE "+ID+"="+id,new String[]{});
        }catch (Exception en){
            Log.e("ERROR",en.toString());
        }

        //////////////////////////  SET THE LANGUAGES HERE ////////////////////////////////

        return langNames;
    }

    public ArrayList<Sections> getThisSections(){
        ArrayList<Sections> section = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT "+SECTION_NAME+" FROM "+TABLE_NAME,new String[]{});

        }catch (Exception en){
            Log.e("Error",en.toString());
        }

        return section;
    }


    public ArrayList<Question> getThisSectionQuestions(int sectionid){
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+SECTION_ID+"="+sectionid,new String[]{});

            ///////////////////// Set the arraylist of perticular section ////////////////////////
        }catch (Exception en){
            Log.e("ERROR",en.toString());
        }



        return questions;
    }



}
