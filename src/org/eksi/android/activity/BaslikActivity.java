package org.eksi.android.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eksi.android.adapter.BaslikPageAdapter;
import org.eksi.android.dialog.GetirDialog;
import org.eksi.lib.BaslikSayfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BaslikActivity extends Activity {

	private ListView lv1;
	private BaslikSayfa b;
	
	private String baslikURL;
	private String baslikT;
	private int prevPage = 0, nextPage = 0;
	private int pageSelected = 1, pageEnd = 1;
	
	private String lastEntryID;
	
	private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;
    private GestureDetector mGestureDetect;
    private OnTouchListener mGestureListener;
    
    private static final Pattern patternP = Pattern.compile("&p=\\d+",Pattern.CASE_INSENSITIVE);
    private static final Pattern patternI = Pattern.compile("&i=\\d+",Pattern.CASE_INSENSITIVE);
    private static final Pattern patternT = Pattern.compile("\\?t=[^\\&]+",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    		
	class MyGestureDetector extends SimpleOnGestureListener {
		
		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			
		}
		
	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	        try {
	            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                return false;
	            // right to left swipe
	            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	            	if(nextPage > 0){
	            		Toast.makeText(BaslikActivity.this, R.string.sonraki_sayfa_text, Toast.LENGTH_SHORT).show();
	            		
	            		Matcher m = patternP.matcher(baslikURL);
		            	String updatedURL = m.replaceFirst("");
		            	
		            	m = patternI.matcher(updatedURL);
		            	updatedURL = m.replaceFirst("");
	            		
		            	Intent k = new Intent(getApplicationContext(), BaslikActivity.class);
		            	k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            	k.putExtra("baslikURL", updatedURL+"&p="+nextPage);
			    		startActivity(k);
	            	}
	            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	            	if(prevPage > 0){
		            	Toast.makeText(BaslikActivity.this, R.string.onceki_sayfa_text, Toast.LENGTH_SHORT).show();
		            	
		            	Matcher m = patternP.matcher(baslikURL);
		            	String updatedURL = m.replaceFirst("");
		            	
		            	m = patternI.matcher(updatedURL);
		            	updatedURL = m.replaceFirst("");
		            	
		            	Intent k = new Intent(getApplicationContext(), BaslikActivity.class);
		            	k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            	k.putExtra("baslikURL", updatedURL+"&p="+prevPage);
			    		startActivity(k);
	            	}
	            }
	        } catch (Exception e) {
	            // nothing
	        	return false;
	        }
	        return true;
	    }
	    

	}
	
	
	public BaslikActivity() {
		b = new BaslikSayfa();
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu){
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.baslik_menu, menu);
	    return true;

    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(pageEnd==1){
			menu.findItem(R.id.sayfa).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent k;
    	switch(item.getItemId()){
    	case R.id.getir:
    		GetirDialog getirDialog = new GetirDialog(this);
    		getirDialog.setOwnerActivity(this);
    		getirDialog.show();
    		break;
    	case R.id.bugun:
    		k = new Intent(this,MainActivity.class);
    		k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(k);
    		break;
    	case R.id.tumunu_goster:
    		k = new Intent(getApplicationContext(), BaslikActivity.class);
    		k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		k.putExtra("baslikURL", "http://www.eksisozluk.com/show.asp"+baslikT+"&i="+lastEntryID+"&iphone=1");
    		startActivity(k);   		
    		break;
    	case R.id.sayfa:
    		
    		final CharSequence[] pItems = new CharSequence[pageEnd];
    		for(int i=0;i<pageEnd;i++){
    			pItems[i] = String.valueOf(i+1);
    		}
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Sayfa seçin");
    		builder.setSingleChoiceItems(pItems, pageSelected-1, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    		        // Toast.makeText(getApplicationContext(), pItems[item], Toast.LENGTH_SHORT).show();
    		        Toast.makeText(getApplicationContext(), "Sayfa bekleniyor", Toast.LENGTH_SHORT).show();
    		        
    		        Matcher m = patternP.matcher(baslikURL);
	            	String updatedURL = m.replaceFirst("");
	            	
	            	m = patternI.matcher(updatedURL);
	            	updatedURL = m.replaceFirst("");
	            	
	            	Intent k = new Intent(getApplicationContext(), BaslikActivity.class);
	            	k.putExtra("baslikURL", updatedURL+"&p="+pItems[item]);
		    		startActivity(k);
		    		
	            	dialog.dismiss();
    		    }
    		});
    		AlertDialog alert = builder.create();
    		alert.setOwnerActivity(this);
    		alert.show();
    		
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baslik_activity);
		
		
		baslikURL = getIntent().getStringExtra("baslikURL");
		
		// Gesture detection
		mGestureDetect = new GestureDetector(new MyGestureDetector());
		
		mGestureListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (mGestureDetect.onTouchEvent(arg1)) {
                    return true;
                }
                return false;
			}
		};
		
		setPage(baslikURL);
	}
	
	public void setPage(String URL){
        ArrayList<String> items = null;
		try {
			Log.v("ur", URL);
			items = b.entryList(URL);
			this.baslikURL = b.getRealURL();
			this.pageSelected = b.getPageSelected();
			this.pageEnd = b.getPageEnd();
			
			this.lastEntryID = b.getLastEntryID();
			
			Matcher mt = patternT.matcher(URL);
			mt.find();
			this.baslikT = mt.group();
						
			this.prevPage = b.getPrevPage();
			this.nextPage = b.getNextPage();
			
			if(pageEnd > 1){
				setTitle(pageSelected + "/" + pageEnd + " " + b.getTitle());
			}
			else {
				setTitle(b.getTitle());
			}
		} catch (IOException e1) {
    		Toast.makeText(BaslikActivity.this, "İntergalaktik hatlar kesik!", Toast.LENGTH_SHORT).show();
    		Log.e("hata", e1.toString());
    		finish();
		}
		
		
				
        lv1 = (ListView) findViewById(R.id.baslikListView);
                
        if(items != null){ 
        	BaslikPageAdapter bpa = new BaslikPageAdapter(this, items);
        	bpa.setTouch(mGestureListener);
        	lv1.setAdapter(bpa);
        	
            LinearLayout ll = (LinearLayout) findViewById(R.id.baslikLinearLayout);

        	
        	ll.setOnTouchListener(mGestureListener);        	
        	lv1.setOnTouchListener(mGestureListener);
        	
        	lv1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					// TODO Auto-generated method stub
					
				}
        		
			});
        	
        	
        }
    }



}
