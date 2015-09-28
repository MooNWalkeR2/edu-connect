package in.edconnect.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by admin on 9/28/2015.
 */
public class ImageDATAHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="questionseduconnect1";
    public static final String TABLE_NAME="images";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_URL="url";
    public static final String COLUMN_IMAGE_BASE64="imagebase64";

    public ImageDATAHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE images (uid integer primary key autoincreament , id text , name text , url text , imagebase64 BLOB)");
    }

    public void onUpgrade(SQLiteDatabase db,int i,int j){
        db.execSQL("DROP IF EXISTS images");
        onCreate(db);
    }

    public void insertImage(String id , String name , String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID,id);
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_URL,url);
        byte[] imagebase64 = "".getBytes() ;
        try {
            URL image = new URL(url);
            URLConnection ucon = image.openConnection();

            InputStream in =  ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);

            ByteArrayBuffer bf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                bf.append((byte) current);
            }

            imagebase64 = bf.toByteArray();
        }catch(Exception en){
            Log.e("Erro","Error While Downloading Image!");
        }
        cv.put(COLUMN_IMAGE_BASE64,imagebase64);
        db.insert(TABLE_NAME,null,cv);
        Log.e("Inserted","In the images database!");
    }

}
