package com.attila.shogi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShogiActivity extends Activity implements OnClickListener{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button newButton = ( Button ) findViewById( R.id.new_game_id );
		newButton.setOnClickListener( this );
		Button contButton = ( Button ) findViewById( R.id.cont_game_id );
		contButton.setOnClickListener( this );
		Button aboutButton = ( Button ) findViewById( R.id.about_id );
		aboutButton.setOnClickListener( this );
		Button exitButton = ( Button ) findViewById( R.id.exit_id );
		exitButton.setOnClickListener( this );
	}

	public void onClick( View v )
	{
		switch( v.getId( ) )
		{
		case R.id.new_game_id:
			startNewGame( );
			break;
		case R.id.cont_game_id:
			Log.d( "Shogi", "Continue game selected" );
			break;
		case R.id.about_id:
			AlertDialog aboutDialog = new AlertDialog.Builder( this ).create( );
			aboutDialog.setTitle( "About Shogi" );
			aboutDialog.setMessage( "SHOGI IS A GAME BRAH" );
			aboutDialog.setButton( "Back", new DialogInterface.OnClickListener( ) {
				public void onClick(DialogInterface dialog, int sButton)
				{
					//Empty
				}
			});
			aboutDialog.show();
			break;
		case R.id.exit_id:
			setResult( RESULT_OK, null );
			finish( );
			break;
		}
	}

	private void startNewGame( )
	{
		Intent iGame = new Intent( this, ShogiGame.class );
		startActivity( iGame );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater( ).inflate( R.menu.menu, menu);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() )
		{
		case R.id.settings_id:
			startActivity( new Intent( this, Prefs.class ) );
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}