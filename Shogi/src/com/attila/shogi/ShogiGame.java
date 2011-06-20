package com.attila.shogi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ShogiGame extends Activity {
	
	private ShogiPiece board[ ][ ] = new ShogiPiece[ 9 ][ 9 ];
	private boolean turn = true;
	private BoardView bvBoard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		initBoard( );
		
		bvBoard = new BoardView( this );
		setContentView( bvBoard );
		bvBoard.requestFocus( );
	}
	
	private void initBoard( )
	{
		for( int j = 0; j < 9; j++ )
		{
			board[ j ][ 2 ] = new ShogiPiece( "歩", false );
			board[ j ][ 6 ] = new ShogiPiece( "歩", true );
		}
		
		board[ 0 ][ 0 ] = new ShogiPiece( "香", false );
		board[ 1 ][ 0 ] = new ShogiPiece( "桂", false );
		board[ 8 ][ 0 ] = new ShogiPiece( "香", false );
		board[ 7 ][ 0 ] = new ShogiPiece( "桂", false );
		board[ 2 ][ 0 ] = new ShogiPiece( "銀", false );
		board[ 6 ][ 0 ] = new ShogiPiece( "銀", false );
		board[ 3 ][ 0 ] = new ShogiPiece( "金", false );
		board[ 5 ][ 0 ] = new ShogiPiece( "金", false );
		board[ 4 ][ 0 ] = new ShogiPiece( "王", false );
		board[ 1 ][ 1 ] = new ShogiPiece( "飛", false );
		board[ 7 ][ 1 ] = new ShogiPiece( "角", false );
		
		board[ 0 ][ 8 ] = new ShogiPiece( "香", true );
		board[ 8 ][ 8 ] = new ShogiPiece( "香", true );
		board[ 1 ][ 8 ] = new ShogiPiece( "桂", true );
		board[ 7 ][ 8 ] = new ShogiPiece( "桂", true );
		board[ 2 ][ 8 ] = new ShogiPiece( "銀", true );
		board[ 6 ][ 8 ] = new ShogiPiece( "銀", true );
		board[ 3 ][ 8 ] = new ShogiPiece( "金", true );
		board[ 5 ][ 8 ] = new ShogiPiece( "金", true );
		board[ 4 ][ 8 ] = new ShogiPiece( "玉", true );
		board[ 1 ][ 7 ] = new ShogiPiece( "角", true );
		board[ 7 ][ 7 ] = new ShogiPiece( "飛", true );
	}
	
	@Override
	public void onBackPressed() 
	{
		Log.d( "SHOGI" , "BACK PRESSED" );
		super.onBackPressed();
	}

	public ShogiPiece getPiece( int x, int y )
	{
		if( board[ x ][ y ] == null )
			return null;
		return board[ x ][ y ];
	}
	
	public void setPiece( int ox, int oy, int x, int y, final ShogiPiece s )
	{
		if( this.getPiece( x, y ) == null || this.getPiece( x, y ).getSide() != s.getSide() )
		{
			if( s.isValidMove( ox, oy, x, y ) == true && s.hasPath( board, ox, oy, x, y ) )
			{
				board[ ox ][ oy ] = null;
				if( ( s.getSide( ) ? y <= 2 : y >= 6 ) && s.getPromote() != " " )
				{
					/* Todo: AlertDialog causes promotions to not show up until manual refresh */
					AlertDialog.Builder builder = new AlertDialog.Builder( this );
					builder.setMessage( "Promote?" )
					       .setPositiveButton( s.getPromote(), new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   s.promote();
					           }
					       })
					       .setNegativeButton( s.getPiece(), new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   dialog.cancel();
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show( );
				}
				board[ x ][ y ] = s;
				turn = !turn;
			}
		}
	}
	
	public boolean getTurn( )
	{
		return turn;
	}

}
