package gr.apostolis.github.locationawarealarmclock.layouts;

import gr.apostolis.github.locationawarealarmclock.adapters.AlarmListAdapter;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

public class AlarmListItemOnGestureListener extends SimpleOnGestureListener
		implements AnimationListener {

	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private ListView lv;
	private int lastDeletedPosition;

	public AlarmListItemOnGestureListener(ListView lv) {
		this.lv = lv;
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

		lastDeletedPosition = lv.pointToPosition((int) e1.getX(),
				(int) e1.getY());
		if (AdapterView.INVALID_POSITION == lastDeletedPosition) {
			return false;
		}

		AlarmListItem item = (AlarmListItem) lv.getChildAt(lastDeletedPosition);

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
	public void onAnimationEnd(Animation arg0) {
		if (AdapterView.INVALID_POSITION == lastDeletedPosition) {
			return;
		}
		((AlarmListAdapter) lv.getAdapter()).removeAlarm(lastDeletedPosition);
		lastDeletedPosition = AdapterView.INVALID_POSITION;
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
	}

	@Override
	public void onAnimationStart(Animation arg0) {
	}

}
