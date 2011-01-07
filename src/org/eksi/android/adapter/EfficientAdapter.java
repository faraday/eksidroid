package org.eksi.android.adapter;

import java.util.ArrayList;

import org.eksi.android.activity.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EfficientAdapter extends BaseAdapter {
  	 private LayoutInflater mInflater;
  	 private ArrayList<String> mItems;

  	 public EfficientAdapter(Context context, ArrayList<String> items) {
  	 mInflater = LayoutInflater.from(context);
  	 mItems = items;
  	 }

  	 public Object getItem(int position) {
  	 return mItems.get(position);
  	 }

  	 public long getItemId(int position) {
  	 return position;
  	 }

  	 public View getView(int position, View convertView, ViewGroup parent) {
  		 ViewHolder holder;
  		 if (convertView == null) {
  		 convertView = mInflater.inflate(R.layout.listview, parent, false);
  		 holder = new ViewHolder();
  		 holder.text2 = (TextView) convertView.findViewById(R.id.TextView02);
  	
  		 convertView.setTag(holder);
  		 } else {
  		 holder = (ViewHolder) convertView.getTag();
  		 }
  	
  		 // holder.text2.setMovementMethod(LinkMovementMethod.getInstance());
  		 holder.text2.setText(Html.fromHtml(mItems.get(position)).toString());
  	
  		 return convertView;
  	 }

  	 static class ViewHolder {
  		 TextView text2;
  	 }

	@Override
	public int getCount() {
		return mItems.size();
	}
  	 
  }
