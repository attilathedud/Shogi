package com.attila.shogi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class DropBoxDialog extends Dialog {
	
	private int drop[ ] = new int[ 7 ];
	private final View pieces[ ] = new View[ 7 ];
	private final ShogiGame curGame;
	
	public DropBoxDialog( Context context, int drop[ ] )
	{
		super( context );
		this.curGame = ( ShogiGame )context;
		this.drop = drop;
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.dropbox );
		
		setTitle( drop[ 0 ] + "P, " + drop[ 1 ] + "Kn, " + drop[ 2 ] + "L, " + drop[ 3 ] + "S, " +
				drop[ 4 ] + "G, " + drop[ 5 ] + "B, " + drop[ 6 ] + "R" );
		
		pieces[ 0 ] = findViewById( R.id.pawn_id );
		pieces[ 1 ] = findViewById( R.id.knight_id );
		pieces[ 2 ] = findViewById( R.id.lance_id );
		pieces[ 3 ] = findViewById( R.id.silver_id );
		pieces[ 4 ] = findViewById( R.id.gold_id );
		pieces[ 5 ] = findViewById( R.id.bishop_id );
		pieces[ 6 ] = findViewById( R.id.rook_id );
		
		for( int i = 0; i < pieces.length; i++ )
		{
			final int k = i;
			
			if( drop[ i ] == 0 )
				pieces[ i ].setVisibility( View.INVISIBLE ); 
			
			pieces[ i ].setOnClickListener( new View.OnClickListener( ){
				public void onClick(View v) {
					curGame.setDrop( k );
					dismiss( );
				}
			});
		}
		
	}
	
}
