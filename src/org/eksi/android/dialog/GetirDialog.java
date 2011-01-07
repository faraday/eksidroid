package org.eksi.android.dialog;

import org.eksi.android.activity.BaslikActivity;
import org.eksi.android.activity.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GetirDialog extends Dialog implements OnClickListener {
	Button getirButton;

	public GetirDialog(Context context) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.getir_dialog);
		getirButton = (Button) findViewById(R.id.GetirBut);
		getirButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		/** When Getir Button is clicked, dismiss the dialog */
		if (v == getirButton){
			EditText e = (EditText) findViewById(R.id.GetirEdit);
			String query = e.getText().toString();
									
			Intent baslikActivity = new Intent(this.getOwnerActivity().getApplicationContext(),BaslikActivity.class);
			baslikActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			baslikActivity.putExtra("baslikURL", "http://www.eksisozluk.com/show.asp?t="+Uri.encode(query)+"&iphone=1");
    		this.getOwnerActivity().startActivity(baslikActivity);
    		
    		dismiss();
		}
	}

}
