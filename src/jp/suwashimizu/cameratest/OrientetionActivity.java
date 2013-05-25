package jp.suwashimizu.cameratest;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class OrientetionActivity extends Activity {

	ImageView iv;
	TextView tv;
	Matrix m;
	float angle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.orientetion);
		m = new Matrix();

		Button b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				startActivityForResult(intent, 0);
			}
		});
		
		((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		iv = (ImageView)findViewById(R.id.imageView1);
		tv = (TextView)findViewById(R.id.textView2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0 && resultCode == RESULT_OK){
			String filePath = getPath(data.getData());
			setImage(filePath);
			try {
				Bitmap b=ImageLoader.createImg(getApplicationContext(), filePath, 480);
				iv.setImageBitmap(b);
				
				m.reset();
				m.preTranslate(-b.getWidth()*0.5f, -b.getHeight()*0.5f);
				m.postRotate(angle);
				m.postTranslate(b.getWidth()*0.5f, b.getHeight()*0.5f);
				
				iv.setImageMatrix(m);
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				tv.setText(R.string.notFile);
			} catch (IOException e) {
				e.printStackTrace();
				tv.setText(R.string.ioeerror);
			}
		}
	}

	//画像情報の取得
	private void setImage(String fileName){
		ExifInterface exifInterface;
		try{
			exifInterface = new ExifInterface(fileName);
			showExif(exifInterface);
		}catch(IOException e){
			return;
		}
	}

	private void showExif(ExifInterface ei){

		String s = ei.getAttribute(ExifInterface.TAG_ORIENTATION);
		if(s.equals("8")){
			tv.setText("Orientaion:8\n時計回りに270度回転");
			angle = 270;
		}else if(s.equals("3")){
			tv.setText("Orientaion:3\n180度回転");
			angle = 180;
		}else if(s.equals("6")){
			angle = 90;
			tv.setText("Orientaion:6\n時計回りに90度回転");
		}else{
			angle = 0;
			tv.setText("Orientaion:0\n回転補正なし");
		}
		m.setRotate(angle);
	}

	private String getPath(Uri uri){
		String[] colums = {MediaStore.Images.Media.DATA};
		Cursor cursor = this.getContentResolver().query(uri, colums, null, null, null);
		cursor.moveToFirst();
		String path = cursor.getString(0);
		cursor.close();
		return path;
	}
}
