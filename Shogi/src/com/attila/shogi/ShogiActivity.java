package com.attila.shogi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShogiActivity extends Activity implements OnClickListener{
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
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
			/*Todo: allow to start from scenario */
			Dialog d = new StartGameDialog( this );
			d.show();
			break;
		case R.id.cont_game_id:
			Intent lGame = new Intent( this, LoadGame.class);
			startActivity( lGame );
			break;
		case R.id.about_id:
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setMessage( "About info here" )
					.setTitle( "About Shogi" )
			       .setPositiveButton( "Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show( );
			break;
		case R.id.exit_id:
			setResult( RESULT_OK, null );
			finish( );
			break;
		}
	}
}