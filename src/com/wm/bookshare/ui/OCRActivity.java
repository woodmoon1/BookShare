package com.wm.bookshare.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.wm.bookshare.R;
import com.wm.bookshare.util.DrawableUtil;
import com.wm.bookshare.util.GeneratedClassUtils;

public class OCRActivity extends BaseActivity {
    private static final String TAG = "OCRActivity";
   
    private static final String TESSBASE_PATH = "/mnt/sdcard/tesseract/";
    private File sdcardTempFile;
    private static final String DEFAULT_LANGUAGE = "chi_sim";
    private static final String IMAGE_PATH = "/mnt/sdcard/test1.jpg";
    private static final String EXPECTED_FILE = TESSBASE_PATH + "tessdata/" + DEFAULT_LANGUAGE
            + ".traineddata";
    private Menu mMenu;
    private int crop = 250;
    public TextView content;

    private ImageView img ;
    private Bitmap bmp ;
    
    private int defaultDrawable=R.drawable.test;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        content=(TextView)findViewById(R.id.ocr_content);
        img = (ImageView) findViewById(R.id.ocr_image);
        bmp = BitmapFactory.decodeResource(getResources(),defaultDrawable);
        img.setImageBitmap(bmp);
   
        WindowManager wm = this.getWindowManager();
        
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LayoutParams para = img.getLayoutParams();
        para.height = height/4;
        para.width =  width-20;
        img.setLayoutParams(para);
        sdcardTempFile = new File("/mnt/sdcard/tmp/", "tmp_pic_bookshare.jpg");
    }
    @Override  
    protected void onStart() {  
        super.onStart();  
      //  ProgressDialog dialog = ProgressDialog.show(OCRActivity.this, "提示", "正在扫描中....");  
        //testOcr();
        // The activity is about to become visible.   
    }  
   

	public void testOcr(){
        mHandler.post(new Runnable() {
           
            @Override
            public void run() {
                Log.d(TAG, "OCR begin>>>>>>>");
                //ocr();
                //test();
               
            }
        });

    }
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
	    	getSupportMenuInflater().inflate(R.menu.ocr, menu);
	         mMenu = menu;
	         return true;
		}

	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
		
	         switch (item.getItemId()) {
	             case android.R.id.home:
	            	 toHome();
	             case R.id.action_select_photo:
	            	 selectPhoto();
	                 return true;
	             case R.id.action_camera:
	            	 takePhoto();
	                 return true;
	             default:
	                 return super.onOptionsItemSelected(item);
	         }
		}
	    
	    /**
	     * 拍照
	     */
	   private void toHome(){
	    Intent intent = new Intent();  
	    intent.setClass(this, GeneratedClassUtils.get(MainActivity.class));  
	    startActivity(intent);  
	   }
	   /**
	     * 选择图片
	     */
	   private void selectPhoto(){
		   Intent innerIntent = new Intent("android.intent.action.PICK");  
	        innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片  
	        innerIntent.putExtra("aspectX", 1); // 出现放大和缩小  
	      
	        innerIntent.putExtra("scale",true);	// 是否保留比例
	        //innerIntent.putExtra("outputX", crop);// 裁剪区的宽
	        //innerIntent.putExtra("outputY", crop);//裁剪区的高
	        innerIntent.setType("image/*"); // 查看类型 详细的类型在 com.google.android.mms.ContentType   
	        innerIntent.putExtra("return-data", false);    
	        innerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
	        innerIntent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
	        innerIntent.putExtra("noFaceDetection", true); 
	        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择图片"); //开始 并设置标题  
	        startActivityForResult(wrapperIntent, 1); // 设返回 码为 1  onActivityResult 中的 requestCode 对应  
	   
	   	   }  
	    @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        super.onActivityResult(requestCode, resultCode, data);   
	       if (resultCode == RESULT_OK) {  
	           //System.out.println("requestCode"+requestCode);  
	        if (requestCode == 1) {  
		              try {  
	            	  //bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
		            if(bmp.isRecycled()==false) bmp.recycle();
	            	  bmp =decodeUriAsBitmap(Uri.fromFile(sdcardTempFile));
	            	  img.setImageBitmap(bmp);  
	            	  img.invalidate();
	            	  content.setText("");
	              } catch (Exception e) {  
	       // TODO Auto-generated catch block  
	       e.printStackTrace();  
	   }  
	     
	       }  
	    }  
	     
	    }  
	    private Bitmap decodeUriAsBitmap(Uri uri){
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			return bitmap;
		}
	    /**
	     * 拍照
	     */
	   private void takePhoto(){
	    Intent intent = new Intent();  
	    intent.setClass(this, CameraActivity.class);  
	    startActivity(intent);  
	   }
	   /**
	    * 扫描照片
	    * @param v
	    */
	   public void scanPhoto(View v){
		 
		   ScanTask  task=new ScanTask(content);
		   task.execute("Test");
	   }
	   /**
	    * 显示照片
	    * @param v
	    */
	 public void showPhoto(View v){
		    Intent intent = new Intent(this, GeneratedClassUtils.get(ImageViewActivity.class));
		    intent.putExtra(ImageViewActivity.IMAGE_BYTE, DrawableUtil.bitmap2Bytes(bmp));
		    startActivity(intent);
		    }
	   
	   
    public String test(){
        final TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
       if(bmp==null){
    	   bmp = BitmapFactory.decodeResource(getResources(),defaultDrawable);
       }
        baseApi.setImage(bmp);
        Log.v("Kishore","Kishore:Working");
        String outputText = baseApi.getUTF8Text();
        Log.v("Kishore","Kishore:"+outputText);
        //content.setText(outputText.trim());
        baseApi.end();
        bmp.recycle();
   	   // dialog.cancel();
        return outputText.trim(); 
    } 
   
    protected void ocr() {
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        //Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_PATH, options);
        Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.number);
        try {
            ExifInterface exif = new ExifInterface(IMAGE_PATH);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
 
            Log.v(TAG, "Orient: " + exifOrientation);
 
            int rotate = 0;
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }
 
            Log.v(TAG, "Rotation: " + rotate);
 
            if (rotate != 0) {
 
                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
 
                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);
 
                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                // tesseract req. ARGB_8888
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
 
        } catch (IOException e) {
            Log.e(TAG, "Rotate or coversion failed: " + e.toString());
        }
 
        ImageView iv = (ImageView) findViewById(R.id.ocr_image);
        iv.setImageBitmap(bitmap);
        iv.setVisibility(View.VISIBLE);
 
        Log.v(TAG, "Before baseApi");
 
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();
 
        Log.v(TAG, "OCR Result: " + recognizedText);
 
        // clean up and show
        if (DEFAULT_LANGUAGE.equalsIgnoreCase("eng")) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }
        if (recognizedText.length() != 0) {
            ((TextView) findViewById(R.id.ocr_content)).setText(recognizedText.trim());
        }
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
        
        };
    };
    
    class ScanTask extends AsyncTask<String,Integer,String> {//继承AsyncTask  
    	public ScanTask(TextView textView) {
			super();
			this.textView = textView;
		}

		TextView textView;
        @Override  
        protected String doInBackground(String... param) {//处理后台执行的任务，在后台线程执行  
            String result="";
        	try {  
            	
      		     // testOcr(); 
        		result=test();
      		    
            } catch (Exception e) {  
            	 dialog.cancel();
            	 Toast.makeText(OCRActivity.this, "扫描图片错误："+e.getMessage(), Toast.LENGTH_LONG).show();  	
            	 Log.e( TAG, e.getMessage());
                return "error";  
            }  
          
            //mImageView.setImageBitmap(result); 不能在后台线程操作ui  
            return result;  
        }  
       
        protected void onProgressUpdate(Integer... progress) {//在调用publishProgress之后被调用，在ui线程执行  
         //
         }  
  
         protected void onPostExecute(String result) {//后台任务执行完之后被调用，在ui线程执行  
        	
        	 if(!result.equals("error")) {  
        		 textView.setText(result);  
        		 dialog.cancel();
        		 Toast.makeText(OCRActivity.this, "扫描图片成功", Toast.LENGTH_LONG).show();  	
            	
             }else {  
                 Toast.makeText(OCRActivity.this, "扫描图片失败", Toast.LENGTH_LONG).show(); 
                 textView.setText("扫描图片失败");  
             }  
             
         }  
           
         protected void onPreExecute () {//在 doInBackground(Params...)之前被调用，在ui线程执行  
        	 textView.setText("扫描中....");  
        	 dialog = new ProgressDialog(OCRActivity.this);
        	 dialog.setCancelable(false);
        	 // 设置进度条风格，风格为圆形，旋转的
        	 dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             // 设置ProgressDialog 标题
        	 dialog.setTitle("提示");
        	// 设置ProgressDialog 提示信息
        	 dialog.setMessage("正在扫描中....");
        	//设置可点击的按钮，最多有三个(默认情况下)  
             dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",  
                     new DialogInterface.OnClickListener() {
                         @Override  
                         public void onClick(DialogInterface dialog, int which) {  
                        	 taskCansel();
                        	 dialog.cancel();
                        	 textView.setText("用户已中断扫描");  
                         }  
                     });
        	 dialog.show();
         }  
         /**
          * 任务退出
          */
          private void taskCansel(){
        	  this.cancel(true);
          }
         protected void onCancelled () {//在ui线程执行  
        	 dialog.cancel();
         }  
          
    }  
  
}  



