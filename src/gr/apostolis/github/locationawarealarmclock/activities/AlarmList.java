package gr.apostolis.github.locationawarealarmclock.activities;

import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.adapters.AlarmListAdapter;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import gr.apostolis.github.locationawarealarmclock.layouts.AlarmListItemOnGestureListener;
import gr.apostolis.github.locationawarealarmclock.layouts.UndoBarController;
import gr.apostolis.github.locationawarealarmclock.layouts.UndoBarController.UndoListener;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class AlarmList extends ListActivity implements UndoListener {

	private ListView alarmList;
	private UndoBarController undoBarController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		alarmList = (ListView) findViewById(android.R.id.list);
		alarmList.setAdapter(new AlarmListAdapter(getApplication(), this));
		final GestureDetector gestureDetector = new GestureDetector(
				alarmList.getContext(), new AlarmListItemOnGestureListener(
						alarmList));
		View.OnTouchListener gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				boolean handled = gestureDetector.onTouchEvent(event);
				if (handled) {
					undoBarController.showUndoBar(false,
							getString(R.string.undo_delete_alarm), null);
				}
				return handled;
			}
		};
		alarmList.setOnTouchListener(gestureListener);

		undoBarController = new UndoBarController(findViewById(R.id.undobar),
				this);
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

	@Override
	public void onUndo(Parcelable token) {
		((AlarmListAdapter) alarmList.getAdapter()).undoDeleteAlarm();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		undoBarController.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		undoBarController.onRestoreInstanceState(state);
	}
}
