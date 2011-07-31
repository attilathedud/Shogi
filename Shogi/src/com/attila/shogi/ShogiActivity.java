package com.attila.shogi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShogiActivity extends Activity implements OnClickListener{

	byte [] buffer = new byte[ 1000 ];
	
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
			startNewGame( );
			break;
		case R.id.cont_game_id:
			try {
				FileInputStream fos = openFileInput( "Game 1" );
				fos.read( buffer );
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/* Todo: create activity so we can hold more than one save */
			AlertDialog.Builder builder2 = new AlertDialog.Builder( this );
			builder2.setMessage( new String( buffer ).trim() + "\n" )
			       .setPositiveButton( "Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       })
			       .setNegativeButton( "Load", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   loadGame( new String( buffer ) );
			        	   dialog.cancel( );
			           }
			       });
			AlertDialog alert2 = builder2.create();
			alert2.show( );
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

	/* Load drop board */
	private void loadGame( String buffer )
	{
	   Intent iGame = new Intent( this, ShogiGame.class );
	   iGame.putExtra( "CurBoard", buffer.trim().split( "END_OF_GAMEBOARD")[ 0 ] );
 	   iGame.putExtra( "MoveList", buffer.split( "END_OF_GAMEBOARD")[ 1 ] );
 	   startActivity( iGame );
	}
	
	private void startNewGame( )
	{
		Intent iGame = new Intent( this, ShogiGame.class );
		startActivity( iGame );
	}

}