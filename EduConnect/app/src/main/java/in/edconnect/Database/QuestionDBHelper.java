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
        db.execSQL("create table questions (uid integer primary key , id text , sectionid text , sectionname text , correctanswer text , questionmarks text ,type text , languagename text , languagetext text , option1 text , option2 text ,option3 text , option4 text) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertQuestion(String id ,String sectionid , String sectionname,String correctAnswer , String questionMarks ,String type , String languageName , String languageText,String option1,String option2,String option3,String option4){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Log.e("SECIDS::", sectionid);
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
            Log.e("INSERTED:","INTO DATABASE");
        }catch(Exception en){
            Log.e("ERROR", en.toString());
            return false;
        }

        return true;
    }


    public Question getThisQuestion(String secid,String id , String languageName){
        Question question = new Question();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;

        Log.e("VALUES",secid + "  "+id+"   "+languageName);
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + SECTION_ID + "=" + secid + " AND "+ID + "=" + id + " AND languagename='"+languageName+"'", null);
        }catch (Exception en){
            Log.e("ERROR",en.toString());
        }
        cursor.moveToFirst();
        question.question=cursor.getString(cursor.getColumnIndex(LANGUAGE_TEXT));
        question.option1=cursor.getString(cursor.getColumnIndex(OPTION_ONE));
        question.option2=cursor.getString(cursor.getColumnIndex(OPTION_TWO));
        question.option3=cursor.getString(cursor.getColumnIndex(OPTION_THREE));
        question.option4=cursor.getString(cursor.getColumnIndex(OPTION_FOUR));
        question.correctAnswer=cursor.getString(cursor.getColumnIndex(CORRECT_ANSWER));
        question.questionMarks=cursor.getString(cursor.getColumnIndex(QUESTION_MARKS));
        question.sectionid=cursor.getString(cursor.getColumnIndex(SECTION_ID));
        Log.e("LANGUAGE :",cursor.getString(cursor.getColumnIndex(LANGUAGE_NAME))+ "N'??????? ??'");

        /////////////////////////// SET THE QUESTION HERE //////////////////////////////////////

        return question;
    }



    public Question getThisQuestion( int sectionid , int position){
        Question question = new Question();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor= db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + SECTION_ID + "="+sectionid +"AND "+ ID +"="+position,null);
        }catch(Exception en){
            Log.e("THis","getThisQuestion(sec,pos) not working="+en.toString());
        }

        cursor.moveToFirst();
        question.question=cursor.getString(cursor.getColumnIndex(LANGUAGE_TEXT));
        question.option1=cursor.getString(cursor.getColumnIndex(OPTION_ONE));
        question.option2=cursor.getString(cursor.getColumnIndex(OPTION_TWO));
        question.option3=cursor.getString(cursor.getColumnIndex(OPTION_THREE));
        question.option4=cursor.getString(cursor.getColumnIndex(OPTION_FOUR));
        question.correctAnswer=cursor.getString(cursor.getColumnIndex(CORRECT_ANSWER));
        question.questionMarks=cursor.getString(cursor.getColumnIndex(QUESTION_MARKS));

        return question;

    }


    public ArrayList<String> getThisLanguages(int secid,int questionid){
        ArrayList<String> langNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT DISTINCT "+LANGUAGE_NAME+" FROM "+TABLE_NAME+" WHERE "+SECTION_ID+"="+secid+" AND "+ID+"="+questionid,new String[]{});
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                langNames.add(cursor.getString(cursor.getColumnIndex(LANGUAGE_NAME)));
                cursor.moveToNext();
            }
        }catch (Exception en){
            Log.e("ERROR",en.toString());
        }

        //////////////////////////  SET THE LANGUAGES HERE ////////////////////////////////

        return langNames;
    }

    public ArrayList<Sections> getThisSections(){
        ArrayList<Sections> section = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e("DATABASE:",db.toString());
        try{
            Cursor cursor = db.rawQuery("SELECT DISTINCT "+SECTION_ID+","+SECTION_NAME+" FROM "+TABLE_NAME,null);

            Log.e("CURSOR:",cursor.getColumnCount()+" "+cursor.getCount());
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String id=cursor.getString(0);
                String name = cursor.getString(1);
                Log.e("IDSEC",id);
                section.add(new Sections(Integer.parseInt(id),name));
                cursor.moveToNext();
            }

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

    public int getNoQuestionSectionId(int id){
        int number=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT uid FROM "+TABLE_NAME+" WHERE "+SECTION_ID+"="+id,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            number++;
            cursor.moveToNext();
        }
        return  number;

    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }



}
