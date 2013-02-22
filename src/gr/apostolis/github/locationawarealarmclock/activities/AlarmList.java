package gr.apostolis.github.locationawarealarmclock.activities;

import java.util.ArrayList;
import java.util.List;

import gr.apostolis.github.locationawarealarmclock.Alarm;
import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.adapters.AlarmListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class AlarmList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		ListView alarmList = (ListView)findViewById(R.id.AlarmList);
		List<Alarm> alarms = new ArrayList<Alarm>();
		for (int i = 0; i < 20 ; i++) {
			alarms.add(new Alarm());
		}

		alarmList.setAdapter(new AlarmListAdapter(this, alarms));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_alarm_list, menu);
		return true;
	}

}
