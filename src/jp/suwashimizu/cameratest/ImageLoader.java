package jp.suwashimizu.cameratest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
/*
 * Uriを元にBitmapを生成するクラス&メソッド
 * StaticしたかったがContextが必要なためインスタス化する事に
 * uriLoadの引数にContextを宣言するのもあり？
 * 出力の際はアス比は維持されoutWidth,outHeightの近い数値数値が出力サイズとして適応される
 * 
 * 
 */
public class ImageLoader {
	
	private Context context;
	private Uri uri;
	private Bitmap retunrBmp;
	private Bitmap karaImg;
	
	public ImageLoader(Context context) {
		this.context = context;
	}
	
	//MAX=outSize
	public Bitmap uriCripLoad(Uri uri,int outWidth,int outHeight) throws FileNotFoundException,IOException{
		
		getOriginalImgWH();
		return null;
	}
	
	//Bmp返すだけだとインスタンス化必要なさそうなのでstatic化
	public static Bitmap createImg(Context context,String filePath,int outSize) throws FileNotFoundException,IOException{
		FileInputStream in = new FileInputStream(filePath);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap img = BitmapFactory.decodeStream(in, null, opts);
		in.close();
		
		int scale = opts.outWidth > opts.outHeight ? opts.outWidth/outSize: opts.outHeight/outSize;
		opts.inSampleSize = scale;
		opts.inPreferredConfig = Config.RGB_565;
		
		in = new FileInputStream(filePath);
		opts.inJustDecodeBounds = false;
		img = BitmapFactory.decodeStream(in,null,opts);
		
		float scaleF = img.getWidth() > img.getHeight() ? (float)outSize/img.getWidth():(float)outSize/img.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(scaleF, scaleF);
		img = Bitmap.createBitmap(img, 0,0,img.getWidth(), img.getHeight(), matrix, false);
		//Log.d("bmp",""+img.getWidth()+":"+img.getHeight());
        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
		in.close();
		return img;
	}
	
	//w,h固定のBMP作成
	public static Bitmap createWHImg(Context context,String filePath,int outWidth,int outHeight) throws FileNotFoundException,IOException{
		FileInputStream in = new FileInputStream(filePath);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap img = BitmapFactory.decodeStream(in, null, opts);
		in.close();
		
		int w = opts.outWidth;
		int h = opts.outHeight;
		
		int scaleW = opts.outWidth / outWidth;
		int scaleH = opts.outHeight / outHeight;
		
		int scale = scaleW > scaleH ? scaleW:scaleH;
		opts.inSampleSize = scale;
		opts.inPreferredConfig = Config.RGB_565;
		
		in = new FileInputStream(filePath);
		opts.inJustDecodeBounds = false;
		img = BitmapFactory.decodeStream(in,null,opts);
		
		float scaleXF =  (float)outWidth/img.getWidth();
		float scaleHF = (float)outHeight/img.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(scaleXF, scaleHF);
		img = Bitmap.createBitmap(img, 0,0,img.getWidth(), img.getHeight(), matrix, false);
		//Log.d("bmp",""+img.getWidth()+":"+img.getHeight());
        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
		in.close();
		return img;
	}
	
	public Bitmap fileLoad(String filePath,int outSize) throws FileNotFoundException,IOException{
		
		FileInputStream in = new FileInputStream(filePath);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap img = BitmapFactory.decodeStream(in, null, opts);
		in.close();
		
		int scale = opts.outWidth > opts.outHeight ? opts.outWidth/outSize: opts.outHeight/outSize;
		opts.inSampleSize = scale;
		opts.inPreferredConfig = Config.RGB_565;
		
		in = new FileInputStream(filePath);
		opts.inJustDecodeBounds = false;
		img = BitmapFactory.decodeStream(in,null,opts);
		
		float scaleF = img.getWidth() > img.getHeight() ? (float)outSize/img.getWidth():(float)outSize/img.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(scaleF, scaleF);
		img = Bitmap.createBitmap(img, 0,0,img.getWidth(), img.getHeight(), matrix, false);
		Log.d("bmp",""+img.getWidth()+":"+img.getHeight());
        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
		in.close();
		return img;
	}
	
	//outSizeに合わせてBitmap生成 ※元の画像のWH大きい方を参照
	public Bitmap uriLoad2(Uri uri,int outSize) throws FileNotFoundException,IOException{
		
		InputStream in;
		in = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap img = BitmapFactory.decodeStream(in, null, opts);
		in.close();
		
		int scale = opts.outWidth > opts.outHeight ? opts.outWidth/outSize: opts.outHeight/outSize;
		opts.inSampleSize = scale;
		opts.inPreferredConfig = Config.RGB_565;
		
		in = context.getContentResolver().openInputStream(uri);
        opts.inJustDecodeBounds = false;
		img = BitmapFactory.decodeStream(in,null,opts);
		
		float scaleF = img.getWidth() > img.getHeight() ? (float)outSize/img.getWidth():(float)outSize/img.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(scaleF, scaleF);
		img = Bitmap.createBitmap(img, 0,0,img.getWidth(), img.getHeight(), matrix, false);
		Log.d("bmp",""+img.getWidth()+":"+img.getHeight());
        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
		in.close();
		return img;
	}
	
	//WH強制出力
	public Bitmap uriLoad(Uri uri,int outWidth,int outHeight) throws FileNotFoundException,IOException{
		InputStream in;
		in = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap img = BitmapFactory.decodeStream(in, null, opts);
		in.close();
		
		//元の画像サイズの取得
		Log.d("IMGoriginal",opts.outWidth+":"+opts.outHeight);

		//大まかなスケール2^でしかスケーリング出来ない
		int scale = opts.outWidth > opts.outHeight ? opts.outWidth/outWidth:opts.outHeight/outHeight;
		opts.inSampleSize = scale;
		opts.inPreferredConfig= Config.RGB_565;
        //DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
        //opts.inDensity = dm.densityDpi;
        //opts.inSampleSize = 10;//リサイズ
        //opts.inPurgeable = true;
        //opts.inDither = false;
        // 画像ファイルオブジェクトとビットマップ作成
		
		in = context.getContentResolver().openInputStream(uri);
        opts.inJustDecodeBounds = false;
		img = BitmapFactory.decodeStream(in,null,opts);
		
		float scaleF = img.getWidth() > img.getHeight() ? (float)outWidth/img.getWidth():(float)outHeight/img.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(scaleF, scaleF);
		Bitmap returnImg = Bitmap.createBitmap(outWidth, outHeight, Config.RGB_565);
		Canvas canvas = new Canvas(returnImg);
		img = Bitmap.createBitmap(img, 0,0,img.getWidth(), img.getHeight(), matrix, false);
		canvas.drawBitmap(img, 0, 0, null);
		Log.d("bmp",""+img.getWidth()+":"+img.getHeight());
        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
		in.close();
		Log.d("aaa",""+returnImg.getWidth());
		return returnImg;
		
		/*
		BitmapFactory.Options bmpOption = new BitmapFactory.Options();
		bmpOption.inPreferredConfig= Config.ARGB_4444;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
        bmpOption.inDensity = dm.densityDpi;
        bmpOption.inSampleSize = 10;//リサイズ
        bmpOption.inPurgeable = true;
        bmpOption.inDither = false;
        // 画像ファイルオブジェクトとビットマップ作成
		Bitmap img = BitmapFactory.decodeStream(in,new Rect(0,0,5000/scale,5000/scale),bmpOption);
        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
		in.close();
		return img;*/
	}
	private void getOriginalImgWH() throws FileNotFoundException,IOException{
		
		InputStream in;
		in = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		karaImg = BitmapFactory.decodeStream(in,null,opts);
		in.close();
	}
	
	private void createBmp(int scale){
		
//		InputStream in;		
//		in = context.getContentResolver().openInputStream(uri);
//		
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inJustDecodeBounds = true;
//		Bitmap img = BitmapFactory.decodeStream(in, null, opts);
//		in.close();		
//		//元の画像サイズの取得
//		Log.d("IMGoriginal",opts.outWidth+":"+opts.outHeight);
//
//		//大まかなスケール2^でしかスケーリング出来ない
//		scale = opts.outWidth > opts.outHeight ? opts.outWidth/1:opts.outHeight/1;
//		opts.inSampleSize = scale;
//		opts.inPreferredConfig= Config.RGB_565;
//        //DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
//        //opts.inDensity = dm.densityDpi;
//        //opts.inSampleSize = 10;//リサイズ
//        //opts.inPurgeable = true;
//        //opts.inDither = false;
//        // 画像ファイルオブジェクトとビットマップ作成
//		
//		in = context.getContentResolver().openInputStream(uri);
//        opts.inJustDecodeBounds = false;
//		img = BitmapFactory.decodeStream(in,null,opts);
//		
//		
//		float scaleF = img.getWidth() > img.getHeight() ? (float)outWidth/img.getWidth():(float)outHeight/img.getHeight();
//		Matrix matrix = new Matrix();
//		matrix.setScale(scaleF, scaleF);
//		Bitmap returnImg = Bitmap.createBitmap(outWidth, outHeight, Config.RGB_565);
//		Canvas canvas = new Canvas(returnImg);
//		img = Bitmap.createBitmap(img, 0,0,img.getWidth(), img.getHeight(), matrix, false);
//		canvas.drawBitmap(img, 0, 0, null);
//		Log.d("bmp",""+img.getWidth()+":"+img.getHeight());
//        //Bitmap img = BitmapFactory.decodeFile(path, bmpOption);
//		in.close();
//		Log.d("aaa",""+returnImg.getWidth());
	}

}
