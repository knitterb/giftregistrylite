package org.blandsite.giftreg;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.blandsite.giftreg.MainActivity.PlaceholderFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class GiftsMainFragment extends Fragment {
	
	private ArrayList<HashMap<String, String>> giftList;


	public static final String LOG_TAG="GiftsMainFragment";
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static GiftsMainFragment newInstance(int sectionNumber) {
		GiftsMainFragment fragment = new GiftsMainFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public GiftsMainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_gifts, container,
				false);
		setHasOptionsMenu(true); // call this or the menu won't show up in onCreateOptionsMenu
		
		
		
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
		Log.d(LOG_TAG, "Section attached: "+ getArguments().getInt(ARG_SECTION_NUMBER));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(LOG_TAG, "onResume()");
		
		// TODO reload from local cache
		// TODO check local cache version with server version
		// TODO retrieve remote gifts
		refresh();
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	 	//menu.clear();
	 	inflater.inflate(R.menu.main_gifts, menu);;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add_gift:
			Toast.makeText(getActivity(), "Menu: Add gift.", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_refresh_gift:
			Toast.makeText(getActivity(), "Menu: Refresh gift.", Toast.LENGTH_SHORT).show();
			refresh();
			break;
		default:
				
		}
		

		return super.onOptionsItemSelected(item);
	}
	
	private void refresh() {
		String token = ((Application)getActivity().getApplication()).token;
		giftList = new ArrayList<HashMap<String, String>>();

		String url="http://10.0.2.2:8888/_ah/api/giftendpoint/v1/gift/"+token;
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params=new RequestParams();
		
		Log.d(LOG_TAG, "refresh(): GET - "+url);
		
		
		client.get(getActivity().getApplicationContext(), url, params, new JsonHttpResponseHandler() {
			
			@Override
			public void onStart() {
				Log.d(LOG_TAG, "refresh() - onStart()");
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d(LOG_TAG, "refresh() - onSuccess()");	
				if (response.has("items")) {
					try {
						JSONArray gifts=response.getJSONArray("items");
						
						for (int i = 0; i< gifts.length(); i++) {
							JSONObject jgift=gifts.getJSONObject(i);
							String description=jgift.getString("description");
							String ranking=jgift.getString("ranking");
							String quantity=jgift.getString("quantity");
							
							HashMap<String, String> gift = new HashMap<String, String>();
							gift.put("description", description);
							gift.put("ranking", "Ranking: "+ranking);
							gift.put("quantity", "Quanity: "+quantity);
							gift.put("key", jgift.getString("key"));
							
							giftList.add(gift);
							
							ListAdapter adapter = new SimpleAdapter(getActivity(), giftList, R.layout.gift_list_item, 
									new String[] { "description", "ranking", "quantity" }, 
									new int[] { R.id.description, R.id.ranking, R.id.quantity }
									);
						    ListView list = (ListView) getActivity().findViewById(R.id.listViewGifts);
							list.setAdapter(adapter);
							
							list.setOnItemClickListener(new OnItemClickListener() {
								 
					            @Override
					            public void onItemClick(AdapterView<?> parent, View view,
					                    int position, long id) {

					            	Log.d(LOG_TAG, "onItemClick() "+position);
					            	Log.d(LOG_TAG, "onItemClick() "+giftList.get(position).get("key"));

					            }
					        });
							

							
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}

				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.d(LOG_TAG, "refresh() - onFailure(): "+responseString);
				((Application)getActivity().getApplication()).toast("Error retrieving gifts (0)");
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
				Log.d(LOG_TAG, "refresh() - onFailure(): "+response);
				((Application)getActivity().getApplication()).toast("Error retrieving gifts (1)");

			}
			
			@Override
			public void onRetry(int retryCnt) {
				Log.d(LOG_TAG, "refresh() - onRetry()");
			}
			
		});

	}

}
