/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eksi.android.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eksi.android.adapter.EfficientAdapter;
import org.eksi.android.dialog.GetirDialog;
import org.eksi.lib.Baslik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A minimal "Hello, World!" application.
 */
public class MainActivity extends Activity {
	private ListView lv1;
	private Baslik b;
	
	private Pattern patternHref = Pattern.compile("href=\"(.+?)\"",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	ArrayList<String> urls;
	
	private enum MOption {GETIR, BUGUN, DUN, OPTS};
			
    public MainActivity() {
    	b = new Baslik();
		urls = new ArrayList<String>();
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.getir:
    		GetirDialog getirDialog = new GetirDialog(this);
    		getirDialog.setOwnerActivity(this);
    		getirDialog.show();
    		break;
    	case R.id.bugun:
    		refreshData(MOption.BUGUN);	  
    		break;
    	case R.id.dun:
    		refreshData(MOption.DUN);	  
    		break;
    	case R.id.ayar:
    		Intent settingsActivity = new Intent(getBaseContext(),
                    Preferences.class);
    		startActivity(settingsActivity);
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }

    public void refreshData(MOption opt){
        ArrayList<String> items = null;
		try {
			switch(opt){
			case BUGUN:
				items = b.bugun();
				setTitle(R.string.bugun_text);
				break;
			case DUN:
				items = b.dun();
				setTitle(R.string.dun_text);
				break;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Matcher mit, mTarget;
		String url;
		
		urls.clear();
		for(String it : items){
			mit = patternHref.matcher(it);
			mit.find();
			try {
				url = new String(mit.group(1).getBytes(),"UTF-8");		
				urls.add(url);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
        lv1 = (ListView) findViewById(R.id.ListView01);
        
        if(items != null){       	
        	lv1.setAdapter(new EfficientAdapter(this, items));
        }
        
        lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent k = new Intent(getApplicationContext(), BaslikActivity.class);
				k.putExtra("baslikURL", urls.get(position));
	    		startActivity(k);
				
			}
        	
		});

    }
    
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    
		setContentView(R.layout.main_activity);

		refreshData(MOption.BUGUN);	  
    }
    
}

