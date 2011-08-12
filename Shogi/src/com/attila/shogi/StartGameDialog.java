package com.attila.shogi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StartGameDialog extends Dialog {
	
	public StartGameDialog( Context context )
	{
		super( context );
	}
	
	private void startNewGame( )
	{
		Intent iGame = new Intent( getContext(), ShogiGame.class );
		iGame.putExtra("SaveName", ((EditText) findViewById(R.id.savename_id)).getText().toString() + 
				" ( " + ((EditText) findViewById(R.id.oPhone_id)).getText().toString() + " )" );
		getContext().startActivity( iGame );
	}
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.configstartgame );
		
		setTitle( "Game Options" );
		
		((EditText) findViewById(R.id.oPhone_id)).setOnFocusChangeListener( new View.OnFocusChangeListener( ) {
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				//if( ((EditText) findViewById(R.id.oPhone_id)).getText().toString().isEmpty() )
				if( ((EditText) findViewById(R.id.oPhone_id)).getText().toString().length() == 0 )
					findViewById( R.id.startb_id ).setEnabled( false );
				else
					findViewById( R.id.startb_id ).setEnabled( true );
			}
		});
		
		findViewById( R.id.startb_id ).setOnClickListener( new View.OnClickListener( ){
				public void onClick(View v) {
					startNewGame();
					dismiss( );
				}
		});
		
	}
	
}
