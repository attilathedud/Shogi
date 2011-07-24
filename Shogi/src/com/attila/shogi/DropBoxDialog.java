package com.attila.shogi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DropBoxDialog extends Dialog {
	
	private int drop[ ] = new int[ 7 ], odrop[ ] = new int[ 7 ];
	private final Button pieces[ ] = new Button[ 7 ], opieces[ ] = new Button[ 7 ];
	private final ShogiGame curGame;
	
	public DropBoxDialog( Context context, int drop[ ], int odrop[ ] )
	{
		super( context );
		this.curGame = ( ShogiGame )context;
		this.drop = drop;
		this.odrop = odrop;
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.dropbox );
		
		setTitle( "Drop-box" );
		
		pieces[ 0 ] = ( Button )findViewById( R.id.pawn_id );
		pieces[ 1 ] = ( Button )findViewById( R.id.knight_id );
		pieces[ 2 ] = ( Button )findViewById( R.id.lance_id );
		pieces[ 3 ] = ( Button )findViewById( R.id.silver_id );
		pieces[ 4 ] = ( Button )findViewById( R.id.gold_id );
		pieces[ 5 ] = ( Button )findViewById( R.id.bishop_id );
		pieces[ 6 ] = ( Button )findViewById( R.id.rook_id );
		
		for( int i = 0; i < pieces.length; i++ )
		{
			final int k = i;
			
			pieces[ i ].setText( "" + pieces[ i ].getText() + " x" + drop[ i ] );
			
			if( drop[ i ] == 0 )
				pieces[ i ].setVisibility( View.INVISIBLE ); 
			
			pieces[ i ].setOnClickListener( new View.OnClickListener( ){
				public void onClick(View v) {
					curGame.setDrop( k );
					dismiss( );
				}
			});
		}
		
		opieces[ 0 ] = ( Button )findViewById( R.id.pawn_id_2 );
		opieces[ 1 ] = ( Button )findViewById( R.id.knight_id_2 );
		opieces[ 2 ] = ( Button )findViewById( R.id.lance_id_2 );
		opieces[ 3 ] = ( Button )findViewById( R.id.silver_id_2 );
		opieces[ 4 ] = ( Button )findViewById( R.id.gold_id_2 );
		opieces[ 5 ] = ( Button )findViewById( R.id.bishop_id_2 );
		opieces[ 6 ] = ( Button )findViewById( R.id.rook_id_2 );
		
		for( int i = 0; i < opieces.length; i++ )
		{
			opieces[ i ].setText( "" + opieces[ i ].getText() + " x" + odrop[ i ] );
			
			if( odrop[ i ] == 0 )
				opieces[ i ].setVisibility( View.INVISIBLE );
		}
		
	}
	
}
