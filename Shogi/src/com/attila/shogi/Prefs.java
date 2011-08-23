package com.attila.shogi;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {

	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.settings );
	}
	
	public static boolean getPieces( Context context )
	{
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pieces_k", false);
	}
}
