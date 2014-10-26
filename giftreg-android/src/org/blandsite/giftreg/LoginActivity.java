package org.blandsite.giftreg;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	public static final String LOG_TAG="LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		RelativeLayout rLayout = (RelativeLayout) findViewById (R.id.relativeLayoutWelcomeScr);
		Resources res = getResources(); //resource handle
		Drawable drawable = res.getDrawable(R.drawable.gifts1); //new Image that was added to the res folder
		rLayout.setBackground(drawable);
		
		if (((Application)getApplication()).getPrefs().contains("token")) {
			findViewById(R.id.editTextUsername).setVisibility(View.GONE);
			findViewById(R.id.editTextPassword).setVisibility(View.GONE);
			findViewById(R.id.buttonLogin).setVisibility(View.GONE);

			new AsyncTask<Void, Void, Void>() {
	            @Override
	            protected Void doInBackground( final Void ... params ) {

	            	try {
						Thread.sleep(500);
						

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                return null;
	            }

	            @Override
	            protected void onPostExecute( final Void result ) {
	                // load up the main screen
	    			Intent i=new Intent(LoginActivity.this, MainActivity.class);
	    			startActivity(i);
	    			
	    			resetInputsInBackground();


	            }
	        }.execute();

			
		} else {
			
		}

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void loginButtonClicked(View v) {
		Button b=(Button) findViewById(R.id.buttonLogin);
		b.setEnabled(false);
		auth();
	}
	
	private boolean auth() {
		EditText eu = (EditText) findViewById(R.id.editTextUsername);
		EditText ep = (EditText) findViewById(R.id.editTextPassword);
		
		String u=eu.getText().toString();
		String p=ep.getText().toString();
		
		Application.username = null;
		Application.token = null;
		
		String url="http://10.0.2.2:8888/_ah/api/userendpoint/v1/authenticateUser/"+u+"/"+p;
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params=new RequestParams();
		
		Log.d(LOG_TAG, "LoginActivity.auth(): POST - "+url);
		
		client.post(this.getApplicationContext(), url, params, new JsonHttpResponseHandler() {
			
			@Override
			public void onStart() {
				Log.d(LOG_TAG, "LoginActivity.auth() - onStart()");
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d(LOG_TAG, "LoginActivity.auth() - onSuccess()");	
				
				Application app=(Application)getApplication();
				try {
					if (response.has("key") && response.has("username")) {
						app.token=response.getString("key");
						app.username=response.getString("username");
						
						SharedPreferences.Editor editor=((Application)getApplication()).getPrefs().edit();
						editor.putString("token", app.token);
						editor.commit();
						
						Intent i=new Intent(LoginActivity.this, MainActivity.class);
						startActivity(i);
						
						resetInputsInBackground();
					} else {
						Log.d(LOG_TAG, "Missing token element 'key' and/or 'username'");
						resetInputs();					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					Log.d(LOG_TAG, "Error processing JSON message", e);
					resetInputs();
				}				

				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.d(LOG_TAG, "LoginActivity.auth() - onFailure(): "+responseString);
				resetInputs();
				((Application)getApplication()).toast("Invalid username or password");
			}
			
			@Override
			public void onRetry(int retryCnt) {
				Log.d(LOG_TAG, "LoginActivity.auth() - onRetry()");
			}
			
		});
		
		return false;
	}
	
	private void resetInputs() {
    	findViewById(R.id.editTextUsername).setVisibility(View.VISIBLE);
		findViewById(R.id.editTextPassword).setVisibility(View.VISIBLE);
		findViewById(R.id.buttonLogin).setVisibility(View.VISIBLE);

		Button b=(Button) findViewById(R.id.buttonLogin);
		b.setEnabled(true);

	}
	
	private void resetInputsInBackground() {
		new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {

            	try {
					Thread.sleep(2000); // arbitrary delay to give the screen time to load
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return null;
            }

            @Override
            protected void onPostExecute( final Void result ) {
				resetInputs();

            }
        }.execute();

	}
	
	
}
