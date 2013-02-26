package gr.apostolis.github.locationawarealarmclock.activities;

import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.adapters.AlarmListAdapter;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import gr.apostolis.github.locationawarealarmclock.layouts.AlarmListItem;
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
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class AlarmList extends ListActivity implements UndoListener,
		OnGestureListener, AnimationListener {

	private ListView alarmList;
	private UndoBarController undoBarController;
	private int lastDeletedPosition;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private static final int REQUEST_ALARM_NEW = 1;
	private static final int REQUEST_ALARM_UPDATE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		alarmList = getListView();
		alarmList.setAdapter(new AlarmListAdapter(getApplication(), this));
		final GestureDetector gestureDetector = new GestureDetector(
				alarmList.getContext(), this);
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		undoBarController.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		undoBarController.onRestoreInstanceState(state);
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		if (AdapterView.INVALID_POSITION == lastDeletedPosition) {
			return;
		}
		((AlarmListAdapter) alarmList.getAdapter())
				.removeAlarm(lastDeletedPosition);
		lastDeletedPosition = AdapterView.INVALID_POSITION;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1 == null || e2 == null) {
			return false;
		}

		lastDeletedPosition = alarmList.pointToPosition((int) e1.getX(),
				(int) e1.getY());
		if (AdapterView.INVALID_POSITION == lastDeletedPosition) {
			return false;
		}

		AlarmListItem item = (AlarmListItem) alarmList
				.getChildAt(lastDeletedPosition);

		if (null == item) {
			return false;
		}

		float dX = e2.getX() - e1.getX();
		float dY = e1.getY() - e2.getY();

		if (Math.abs(dY) < SWIPE_MAX_OFF_PATH
				&& Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY
				&& Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
			Animation anim;
			if (dX > 0) {
				anim = AnimationUtils.makeOutAnimation(item.getContext(), true);
			} else {
				anim = AnimationUtils
						.makeOutAnimation(item.getContext(), false);
			}
			anim.setAnimationListener(this);
			item.startAnimation(anim);

			return true;
		}
		return false;

	}

	@Override
	public void onLongPress(MotionEvent e) {
		int position = alarmList
				.pointToPosition((int) e.getX(), (int) e.getY());
		if (AdapterView.INVALID_POSITION == position) {
			return;
		}

		AlarmListItem item = (AlarmListItem) alarmList.getChildAt(position);

		if (null == item) {
			return;
		}

		Intent intent = new Intent(alarmList.getContext(), AddNewAlarm.class);
		intent.putExtra("ALARM", item.getAlarm());
		startActivityForResult(intent, REQUEST_ALARM_UPDATE);

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void onUndo(Parcelable token) {
		((AlarmListAdapter) alarmList.getAdapter()).undoDeleteAlarm();
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
			startActivityForResult(intent, REQUEST_ALARM_NEW);
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

		Bundle extras = data.getExtras();
		Alarm alarm = (Alarm) extras.getSerializable("ALARM");
		switch (requestCode) {
		case REQUEST_ALARM_NEW:
			((AlarmListAdapter) alarmList.getAdapter()).addAlarm(alarm);
			break;
		case REQUEST_ALARM_UPDATE:
			((AlarmListAdapter) alarmList.getAdapter()).saveAlarm(alarm);
			break;
		default:
		}
	}
}
