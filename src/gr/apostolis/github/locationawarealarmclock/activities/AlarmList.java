package gr.apostolis.github.locationawarealarmclock.activities;

import java.util.ArrayList;
import java.util.List;

import gr.apostolis.github.locationawarealarmclock.Alarm;
import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.adapters.AlarmListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class AlarmList extends Activity {

	private ListView alarmList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		alarmList = (ListView)findViewById(R.id.AlarmList);
		List<Alarm> alarms = new ArrayList<Alarm>();
		alarmList.setAdapter(new AlarmListAdapter(this, alarms));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_alarm_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_alarm:
			Intent intent = new Intent(this, AddNewAlarm.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.menu_settings:
			return false;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK !=  resultCode) {
			return;
		}
		if (1 == requestCode) {
			Bundle extras =  data.getExtras();
			String time = extras.getString("time");
			boolean[] repeatOn = extras.getBooleanArray("repeatOn");
			Alarm alarm = new Alarm();
			alarm.setTime(time);
			alarm.setRepeatOn(repeatOn);
			Log.d(getClass().toString(), alarm.toString());
			((AlarmListAdapter)alarmList.getAdapter()).add(alarm);
		}
	}
}
