package gr.apostolis.github.locationawarealarmclock.activities;

import gr.apostolis.github.locationawarealarmclock.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class AlarmSettings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_settings, menu);
		return true;
	}

}
