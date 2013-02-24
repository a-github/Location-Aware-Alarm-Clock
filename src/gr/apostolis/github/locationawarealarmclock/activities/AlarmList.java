package gr.apostolis.github.locationawarealarmclock.activities;

import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.adapters.AlarmListAdapter;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class AlarmList extends ListActivity {

	private ListView alarmList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		alarmList = getListView();
		alarmList.setAdapter(new AlarmListAdapter(getApplication(), this));
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
		if (RESULT_OK != resultCode) {
			return;
		}
		if (1 == requestCode) {
			Bundle extras = data.getExtras();
			String time = extras.getString("time");
			boolean[] repeatOn = extras.getBooleanArray("repeatOn");
			Alarm alarm = new Alarm();
			alarm.setTime(time);
			alarm.setRepeatOn(repeatOn);
			((AlarmListAdapter) alarmList.getAdapter()).addAlarm(alarm);
		}
	}
}
