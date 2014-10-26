package org.blandsite.giftreg;

import org.blandsite.giftreg.MainActivity.PlaceholderFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class GiftsMainFragment extends Fragment {

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
			break;
		default:
				
		}
		

		return super.onOptionsItemSelected(item);
	}

}
