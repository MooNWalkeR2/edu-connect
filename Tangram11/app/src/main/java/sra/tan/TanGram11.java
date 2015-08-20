package sra.tan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class TanGram11 extends Activity implements OnTouchListener {
	private ImageView mMainImg, mRotateImg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setting main view for Activity
		setContentView(R.layout.main);
		mMainImg = (ImageView) findViewById(R.id.xIvimg);
		mRotateImg = (ImageView) findViewById(R.id.xIvRoller);
		// mRotateImg.setOnTouchListener(this);
		mMainImg.setOnTouchListener(this);

	}

	private int r = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eId = event.getAction();
		mRotateImg.setVisibility(ImageView.VISIBLE);
		if (v == mMainImg) {
			switch (eId) {
			case MotionEvent.ACTION_MOVE:
				drag(v, event);
				break;
			case MotionEvent.ACTION_UP:
				mRotateImg.setOnTouchListener(this);
				break;
			default:
			}

		}
		if (v == mRotateImg) {
			switch (eId) {
			case MotionEvent.ACTION_MOVE:
				r = r + 2;
				rotate(v, event);
				break;
			case MotionEvent.ACTION_UP:
				mRotateImg.setVisibility(ImageView.INVISIBLE);
				break;
			default:
			}
			if (v != mMainImg && v != mRotateImg)
				mRotateImg.setVisibility(ImageView.INVISIBLE);
		}
		return true;
	}

	private void rotate(View v, MotionEvent event) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preRotate(-r);
		Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
				true);
		BitmapDrawable bdr = new BitmapDrawable(rotaBitmap);
		mMainImg.setImageDrawable(bdr);
	}

	private void drag(View v, MotionEvent event) {
		LayoutParams mParams = (LayoutParams) mMainImg.getLayoutParams();
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		mParams.leftMargin = x - 150;
		mParams.topMargin = y - 210;
		mMainImg.setLayoutParams(mParams);
		mRotateImg.setLayoutParams(mParams);

	}

}