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

public class MainActivity extends Activity {

	ImageView iv;
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
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
				Intent intent = new Intent(MainActivity.this,OrientetionActivity.class);
				startActivity(intent);
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
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				tv.setText(R.string.notFile);
			} catch (IOException e) {
				e.printStackTrace();
				tv.setText(R.string.ioeerror);
			}
			//saveImgFile();
			//imageCrop(data.getData());
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
		String exifString = getExifString(ei, ExifInterface.TAG_DATETIME)+"<br>" + "\n";  
	    exifString += getExifString(ei,ExifInterface.TAG_FLASH) +"<br>" + "\n";  
	    exifString +=getExifString(ei, ExifInterface.TAG_GPS_LATITUDE) +"<br>" + "\n";  
	    exifString +=getExifString(ei, ExifInterface.TAG_GPS_LONGITUDE) +"<br>" + "\n";  
	    exifString +=getExifString(ei, ExifInterface.TAG_IMAGE_LENGTH) +"<br>" + "\n";  
	    exifString +=getExifString(ei, ExifInterface.TAG_IMAGE_WIDTH) +"<br>" + "\n";  
	    exifString +=getExifString(ei, ExifInterface.TAG_MODEL) +"<br>" + "\n";  
	    exifString += "<font color=\"red\">"+getExifString(ei, ExifInterface.TAG_ORIENTATION) +"<br></font>" + "\n";
	    exifString +=getExifString(ei, ExifInterface.TAG_WHITE_BALANCE) +"<br>" + "\n"; 
	    
	    Log.d("TAGS",exifString);
	    
	    tv.setText(toHtmlFormat(exifString));
	}
	
	private CharSequence toHtmlFormat(String str){
		CharSequence source = Html.fromHtml(str);
		return source;
		
	}
	
	private String getExifString(ExifInterface ei,String tag){
		return tag + ":" + ei.getAttribute(tag);
	}
	
	private String getPath(Uri uri){
		String[] colums = {MediaStore.Images.Media.DATA};
		Cursor cursor = this.getContentResolver().query(uri, colums, null, null, null);
		cursor.moveToFirst();
		String path = cursor.getString(0);
		cursor.close();
		return path;
	}
	
	private class view extends View{

		Paint p;
		
		public view(Context context) {
			super(context);
			inislized();
		}
		
		

		public view(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			inislized();
		}



		public view(Context context, AttributeSet attrs) {
			super(context, attrs);
			inislized();
		}
		
		private void inislized(){
			p = new Paint();
			p.setTextSize(30);
			p.setColor(Color.BLACK);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
		}		
	}

}
