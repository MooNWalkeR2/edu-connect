package in.edconnect.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 9/12/2015.
 */
public class PassageDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="questionseduconnect1";
    public static final String TABLE_NAME="passages";
    public static final String PASSAGE_ID="passageid";
    public static final String UID="uid";
    public static final String PASSAGE_LANGUAGE="passagelanguage";

    public static final String PASSAGE="passage";

    public PassageDBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE_NAME + " ( uid integer primary key , passageid text , passagelanguage text, passage text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertPassage(String passageid,String language  , String passage){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        try{
            cv.put("passageid",passageid);
            cv.put("passagelanguage",language);
            cv.put("passage",passage);

            db.insert(TABLE_NAME,null,cv);
        }catch(Exception en){
            Log.e("ErrorIn","insertPassage()");
        }
    }

    public String getPassage(String passageid,String language){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PASSAGE + " FROM " + TABLE_NAME + " WHERE " + PASSAGE_ID + "=" + passageid + " AND " + PASSAGE_LANGUAGE + "='" + language + "'", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(PASSAGE));
    }
}
