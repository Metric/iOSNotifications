package iosnotifications;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
import com.vantagetechnic.iosnotifications.R;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

public class MainService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String LIVE_CARD_TAG = "iosnotifications";

    private LiveCard mLiveCard;
    
    private RemoteViews mLiveRemote;
    
	private final Handler mHandler = new Handler();
    private final UpdateLiveCardRunnable mUpdateLiveCardRunnable = new UpdateLiveCardRunnable();
    
    private static final long DELAY_MILLIS = 10000;
	
    private Context overallContext;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	overallContext = this;
        if (mLiveCard == null) {
        	SharedPreferences preferences = this.getSharedPreferences("com.vantagetechnic.isonotifications", 0);
        	Editor edit = preferences.edit();
            edit.putString("Status", "Disconnected");
            edit.commit();
            
        	mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

            mLiveRemote = new RemoteViews(getPackageName(), R.layout.liveview);
           
            String statusText = preferences.getString("Status", "Disconnected");
		    mLiveRemote.setTextViewText(R.id.status, statusText);
            
            mLiveCard.setViews(mLiveRemote);
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
            mLiveCard.publish(PublishMode.REVEAL);
            
            mUpdateLiveCardRunnable.start();
        } else {
            mLiveCard.navigate();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
        	mUpdateLiveCardRunnable.stop();
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }
    
    private final class UpdateLiveCardRunnable implements Runnable {
    	
    	private boolean isStopped;
    	
    	public void stop() {
    		isStopped = true;
    	}
    	
    	public void start() {
    		isStopped = false;
    		
    		run();
    	}
    	
    	@Override
		public void run() {
    		if(!isStopped) {
    			//Do stuff to update the view!
    			SharedPreferences preferences = overallContext.getSharedPreferences("com.vantagetechnic.isonotifications", 0);

    			String statusText = preferences.getString("Status", "Disconnected");
    		    mLiveRemote.setTextViewText(R.id.status, statusText);
    			
    			mLiveCard.setViews(mLiveRemote);
    			
    			mHandler.postDelayed(mUpdateLiveCardRunnable, DELAY_MILLIS);
    		}
		}
    }
}
