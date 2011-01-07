package org.eksi.android.adapter;

import java.util.ArrayList;
import java.util.regex.Matcher;

import org.eksi.android.activity.BaslikActivity;
import org.eksi.android.activity.R;
import org.eksi.lib.BaslikSayfa;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaslikPageAdapter extends BaseAdapter {
  	 private LayoutInflater mInflater;
  	 private ArrayList<String> mItems;
  	 
	 private Matcher mTarget;
  	 
  	 private OnTouchListener mOTL;

  	 public BaslikPageAdapter(Context context, ArrayList<String> items) {
  	 mInflater = LayoutInflater.from(context);
  	 mItems = items;
  	 }

  	 public Object getItem(int position) {
  	 return position;
  	 }

  	 public long getItemId(int position) {
  	 return position;
  	 }
  	 
  	 
  	private final class CallbackSpan extends ClickableSpan {
        public CallbackSpan(String url) {
        	// show.asp ... 
                m_data = "http://www.eksisozluk.com/" + url;
                m_data = m_data.replace("&iphone=1", "");
                    			
    			// "eksisozluk iPhone" tweak for links 
    			mTarget = BaslikSayfa.pTarget.matcher(m_data);
    			m_data = mTarget.replaceAll("&iphone=1");
    			mTarget.reset();
        }
        public void onClick(View view) {
        	// Toast.makeText(view.getContext(), m_data, Toast.LENGTH_SHORT).show();
        	Intent k = new Intent(view.getContext().getApplicationContext(),BaslikActivity.class);
    		k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
    		k.putExtra("baslikURL", m_data);
    		view.getContext().getApplicationContext().startActivity(k);
        }
        private String m_data;
  	}

  	 public View getView(int position, View convertView, ViewGroup parent) {
  		 ViewHolder holder;
  		 if (convertView == null) {
  		 convertView = mInflater.inflate(R.layout.baslik_view, null);
  		 holder = new ViewHolder();
  		 holder.text2 = (TextView) convertView.findViewById(R.id.EntryView);
  	
  		 convertView.setTag(holder);
  		 } else {
  		 holder = (ViewHolder) convertView.getTag();
  		 }
  		 
  		Spanned x = Html.fromHtml(mItems.get(position));
  		SpannableString message=new SpannableString(x.toString());
  		
  		Object[] spans=x.getSpans(0,x.length
  				(),Object.class);
  		for (Object span: spans) {
  			int start=x.getSpanStart(span);
  			int end=x.getSpanEnd(span);
  			int flags=x.getSpanFlags(span);
  			if (span instanceof URLSpan) {
  				URLSpan urlSpan=(URLSpan)span;
  				if (!urlSpan.getURL().startsWith("http")) {
  					span=new CallbackSpan(urlSpan.getURL());
  				}
  			}
  			message.setSpan(span,start,end,flags);
  		}
  	
  		 holder.text2.setMovementMethod(LinkMovementMethod.getInstance());
  		 holder.text2.setText(message);
  		 
  		 holder.text2.setOnTouchListener(mOTL);
  	
  		 return convertView;
  	 }
  	 
  	 public void setTouch(OnTouchListener otl){
  		 this.mOTL = otl;
  	 }

  	 static class ViewHolder {
  		 TextView text2;
  	 }

	@Override
	public int getCount() {
		return mItems.size();
	}
  	 
  }
