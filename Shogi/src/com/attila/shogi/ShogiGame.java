package com.attila.shogi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ShogiGame extends Activity {
	
	private final boolean BLACK = true, WHITE = false;
	
	private ShogiPiece board[ ][ ] = new ShogiPiece[ 9 ][ 9 ];
	private int bDrop[ ] = new int[ 7 ], wDrop[ ] = new int[ 7 ], drop = -1;
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
			board[ j ][ 2 ] = new ShogiPiece( "歩", WHITE );
			board[ j ][ 6 ] = new ShogiPiece( "歩", BLACK );
		}
		
		board[ 0 ][ 0 ] = new ShogiPiece( "香", WHITE );
		board[ 1 ][ 0 ] = new ShogiPiece( "桂", WHITE );
		board[ 8 ][ 0 ] = new ShogiPiece( "香", WHITE );
		board[ 7 ][ 0 ] = new ShogiPiece( "桂", WHITE );
		board[ 2 ][ 0 ] = new ShogiPiece( "銀", WHITE );
		board[ 6 ][ 0 ] = new ShogiPiece( "銀", WHITE );
		board[ 3 ][ 0 ] = new ShogiPiece( "金", WHITE );
		board[ 5 ][ 0 ] = new ShogiPiece( "金", WHITE );
		board[ 4 ][ 0 ] = new ShogiPiece( "王", WHITE );
		board[ 1 ][ 1 ] = new ShogiPiece( "飛", WHITE );
		board[ 7 ][ 1 ] = new ShogiPiece( "角", WHITE );
		
		board[ 0 ][ 8 ] = new ShogiPiece( "香", BLACK );
		board[ 8 ][ 8 ] = new ShogiPiece( "香", BLACK );
		board[ 1 ][ 8 ] = new ShogiPiece( "桂", BLACK );
		board[ 7 ][ 8 ] = new ShogiPiece( "桂", BLACK );
		board[ 2 ][ 8 ] = new ShogiPiece( "銀", BLACK );
		board[ 6 ][ 8 ] = new ShogiPiece( "銀", BLACK );
		board[ 3 ][ 8 ] = new ShogiPiece( "金", BLACK );
		board[ 5 ][ 8 ] = new ShogiPiece( "金", BLACK );
		board[ 4 ][ 8 ] = new ShogiPiece( "玉", BLACK );
		board[ 1 ][ 7 ] = new ShogiPiece( "角", BLACK );
		board[ 7 ][ 7 ] = new ShogiPiece( "飛", BLACK );
	}
	
	@Override
	/*Todo: Make back reverse moves */
	public void onBackPressed() 
	{
		Log.d( "SHOGI" , "BACK PRESSED" );
		if( drop != -1 )
			drop = -1;
		//super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater( ).inflate( R.menu.menu, menu);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch( item.getItemId() )
		{
		case R.id.settings_id:
			startActivity( new Intent( this, Prefs.class ) );
			return true;
		case R.id.dropboard_id:
			Dialog d = new DropBoxDialog( this, (turn ? bDrop : wDrop ) );
			d.show( );
			return true;
		case R.id.move_list_id:
			/*Todo: add in move list */
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public ShogiPiece getPiece( int x, int y )
	{
		return board[ x ][ y ];
	}
	
	public void setDrop( int k )
	{
		drop = k;
	}
	
	public int getDrop( )
	{
		return drop;
	}
	
	public void dropPiece( int x, int y )
	{
		if( board[ x ][ y ] != null )
			return;
		
		/*Todo: Check for illegal drops */

		switch( drop )
		{
		case 0:
			board[ x ][ y ] = new ShogiPiece( "歩", turn );
			break;
		case 1:
			board[ x ][ y ] = new ShogiPiece( "桂", turn );
			break;
		case 2:
			board[ x ][ y ] = new ShogiPiece( "香", turn );
			break;
		case 3:
			board[ x ][ y ] = new ShogiPiece( "銀", turn );
			break;
		case 4:
			board[ x ][ y ] = new ShogiPiece( "金", turn );
			break;
		case 5:
			board[ x ][ y ] = new ShogiPiece( "角", turn );
			break;
		case 6:
			board[ x ][ y ] = new ShogiPiece( "飛", turn );
		}
		
		if( turn == BLACK )
			bDrop[ drop ]--;
		else
			wDrop[ drop ]--;
		
		drop = -1;
		turn = !turn;
	}
	
	public void setPiece( int ox, int oy, int x, int y )
	{
		final ShogiPiece s = this.getPiece( ox, oy );
		
		if( s.isValidMove(ox, oy, x, y) == false || s.hasPath(board, ox, oy, x, y) == false )
		{
			Toast notify = Toast.makeText( this, R.string.invalid_move, Toast.LENGTH_SHORT );
		    notify.setGravity( Gravity.CENTER, 0, 0 );
		    notify.show( );
		    
		    return;
		}
		
		if( this.getPiece( x, y ) == null || this.getPiece( x, y ).getSide() != s.getSide() )
		{
				board[ ox ][ oy ] = null;
				if( ( s.getSide( ) ? y <= 2 : y >= 6 ) && s.getPromote() != " " )
				{
					/* Todo: Force promote when no other legal moves */
					AlertDialog.Builder builder = new AlertDialog.Builder( this );
					builder.setMessage( "Promote?" )
					       .setPositiveButton( s.getPiece() + "(No)", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   dialog.cancel();
					           }
					       })
					       .setNegativeButton( s.getPromote() + "(Yes)", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   s.promote();
					        	   setContentView( bvBoard );
					        	   bvBoard.requestFocus();
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show( );
				}
				
				if( this.getPiece( x, y ) != null )
				{
					/* Todo: maybe multidimensional array to cut down on code */
					if( turn == BLACK )
					{
						if( this.getPiece( x, y ).getPiece() == "歩" || this.getPiece( x, y ).getPiece() == "と" )
							bDrop[ 0 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "桂" || this.getPiece( x, y ).getPiece() == "圭" )
							bDrop[ 1 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "香" || this.getPiece( x, y ).getPiece() == "杏" )
							bDrop[ 2 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "銀" || this.getPiece( x, y ).getPiece() == "全" )
							bDrop[ 3 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "金" )
							bDrop[ 4 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "角" || this.getPiece( x, y ).getPiece() == "馬" )
							bDrop[ 5 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "飛" || this.getPiece( x, y ).getPiece() == "龍" )
							bDrop[ 6 ] += 1;
					}
					else
					{
						if( this.getPiece( x, y ).getPiece() == "歩" || this.getPiece( x, y ).getPiece() == "と" )
							wDrop[ 0 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "桂" || this.getPiece( x, y ).getPiece() == "圭" )
							wDrop[ 1 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "香" || this.getPiece( x, y ).getPiece() == "杏" )
							wDrop[ 2 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "銀" || this.getPiece( x, y ).getPiece() == "全" )
							wDrop[ 3 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "金" )
							wDrop[ 4 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "角" || this.getPiece( x, y ).getPiece() == "馬" )
							wDrop[ 5 ] += 1;
						else if( this.getPiece( x, y ).getPiece() == "飛" || this.getPiece( x, y ).getPiece() == "龍" )
							wDrop[ 6 ] += 1;
					}
				}
				
				board[ x ][ y ] = s;
				turn = !turn;
			}
	}
	
	public boolean getTurn( )
	{
		return turn;
	}

}
