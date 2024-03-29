package iosnotifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.Runnable;

import com.vantagetechnic.iosnotifications.R;

public class NotificationMenuActivity extends Activity {

    private final Handler mHandler = new Handler();

    private String mCard;
    
    @Override
    public void onCreate(Bundle bundle) {
    	super.onCreate(bundle);
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notificationmenu, menu);
        return true;
    }
    
    @Override
    public void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	
    	Log.d("IOSNOTIFICATIONS", "New Intent Received");
    	
    	setIntent(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
    	Intent intent = getIntent();
    	mCard = intent.getStringExtra("LiveCardId");
        switch (item.getItemId()) {
            case R.id.delete:
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
             
            	Intent sintent = new Intent("COM.VANTAGETECHNIC.IOSNOTIFICATIONS.DELETECARD");
            	sintent.putExtra("LiveCardId", mCard);
            	sendBroadcast(sintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // Nothing else to do, closing the Activity.
        finish();
    }

    /**
     * Posts a {@link Runnable} at the end of the message loop, overridable for testing.
     */
    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }

}

