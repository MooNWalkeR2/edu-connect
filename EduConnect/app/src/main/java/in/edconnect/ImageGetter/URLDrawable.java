package in.edconnect.ImageGetter;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 9/10/2015.
 */
public class URLDrawable extends BitmapDrawable {


    protected Drawable drawable;

    @Override
    public void draw(Canvas canvas){
        if(drawable!=null){
            drawable.draw(canvas);
        }
    }
}
