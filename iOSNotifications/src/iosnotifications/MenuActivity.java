package iosnotifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.Runnable;

import com.vantagetechnic.iosnotifications.R;

public class MenuActivity extends Activity {

    private final Handler mHandler = new Handler();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.disconnect:
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
                post(new Runnable() {

                    @Override
                    public void run() {
                    	stopService(new Intent(MenuActivity.this, NotificationsService.class));
                    }
                });
                return true;
            case R.id.pair:
            	post(new Runnable() {

                    @Override
                    public void run() {
                    	stopService(new Intent(MenuActivity.this, NotificationsService.class));
                    	startService(new Intent(MenuActivity.this, NotificationsService.class));
                    }
                });
            	return true;
            case R.id.exitapp:
            	post(new Runnable() {
            		@Override
            		public void run() {
            			stopService(new Intent(MenuActivity.this, NotificationsService.class));
            			stopService(new Intent(MenuActivity.this, MainService.class));
            		}
            	});
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
