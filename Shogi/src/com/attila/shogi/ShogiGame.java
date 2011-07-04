package com.attila.shogi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class ShogiGame extends Activity {
	
	private final boolean BLACK = true, WHITE = false;
	
	private ShogiPiece board[ ][ ] = new ShogiPiece[ 9 ][ 9 ];
	private int bDrop[ ] = new int[ 7 ], wDrop[ ] = new int[ 7 ], drop = -1, curMove = 1;
	private boolean turn = true;
	private BoardView bvBoard;
	private String moveList = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		initBoard( );
		
		bvBoard = new BoardView( this );
		final Context k = this;
		bvBoard.setOnLongClickListener( new OnLongClickListener( ) {
			
			public boolean onLongClick( View v )
			{
				Dialog d = new DropBoxDialog( k, (turn ? bDrop : wDrop ) );
				d.show( );
				return true;
			}
		});
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
		else
		{
			
		}
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
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setMessage( moveList + "\n" )
					.setTitle( "Move List" )
			       .setPositiveButton( "Back", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show( );
			
			return true;
		case R.id.exitg_id:	
			AlertDialog.Builder builder2 = new AlertDialog.Builder( this );
			builder2.setMessage( "Save?" )
			       .setPositiveButton( "No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   finish( );
			           }
			       })
			       .setNegativeButton( "Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	  /* Todo: save */
			        	  finish( );
			           }
			       });
			AlertDialog alert2 = builder2.create();
			alert2.show( );
			
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
		String temp = null;
		
		/*Todo: Ensure checkmate isn't given by dropping a pawn */
		if( board[ x ][ y ] != null || ( ( turn ? y == 0 : y == 8 ) && ( drop == 0 || drop == 1 || drop == 2 ) ) 
				|| ( ( turn ? y == 1 : y == 7 ) && drop == 1 ) )
		{
			Toast notify = Toast.makeText( this, "Can't drop there", Toast.LENGTH_SHORT );
		    notify.setGravity( Gravity.CENTER, 0, 0 );
		    notify.show( );
		    
		    drop = -1;
		    
			return;
		}

		switch( drop )
		{
		case 0:
			for( int i = 0; i < 9; i++ )
			{
				if( board[ x ][ i ] == null )
					continue;
				else if( board[ x ][ i ].getPiece() == "歩" && board[ x ][ i ].getSide( ) == turn )
				{
					Toast notify = Toast.makeText( this, "Can't drop there", Toast.LENGTH_SHORT );
				    notify.setGravity( Gravity.CENTER, 0, 0 );
				    notify.show( );
				    
				    drop = -1;
				    
					return;
				}
			}
			board[ x ][ y ] = new ShogiPiece( "歩", turn );
			temp = "歩";
			break;
		case 1:
			board[ x ][ y ] = new ShogiPiece( "桂", turn );
			temp = "桂";
			break;
		case 2:
			board[ x ][ y ] = new ShogiPiece( "香", turn );
			temp = "香";
			break;
		case 3:
			board[ x ][ y ] = new ShogiPiece( "銀", turn );
			temp = "銀";
			break;
		case 4:
			board[ x ][ y ] = new ShogiPiece( "金", turn );
			temp = "金";
			break;
		case 5:
			board[ x ][ y ] = new ShogiPiece( "角", turn );
			temp = "角";
			break;
		case 6:
			board[ x ][ y ] = new ShogiPiece( "飛", turn );
			temp = "飛";
		}
		
		if( turn == BLACK )
			bDrop[ drop ]--;
		else
			wDrop[ drop ]--;
		
		moveList += "\n" + (curMove++) + ". " + temp + "*" + ( 9 - x ) + ( char)( 97 + y );
		
		drop = -1;
		turn = !turn;
	}
	
	private void onPromote( ShogiPiece s )
	{
	   moveList += "+";
	   s.promote();
  	   setContentView( bvBoard );
  	   bvBoard.requestFocus();
	}
	
	public void setPiece( int ox, int oy, int x, int y )
	{
		final ShogiPiece s = this.getPiece( ox, oy );
		String temp4 = s.getPiece();
		String temp3 = "";
		
		if( s.isValidMove(ox, oy, x, y) == false || s.hasPath(board, ox, oy, x, y) == false )
		{
			Toast notify = Toast.makeText( this, "Invalid move", Toast.LENGTH_SHORT );
		    notify.setGravity( Gravity.CENTER, 0, 0 );
		    notify.show( );
		    
		    return;
		}
		
		if( this.getPiece( x, y ) == null || this.getPiece( x, y ).getSide() != s.getSide() )
		{
				board[ ox ][ oy ] = null;
				if( ( s.getSide( ) ? y <= 2 : y >= 6 ) && s.getPromote() != " " )
				{
					if( ( s.getSide( ) ? y == 0 : y == 8 ) && ( s.getPiece( ) == "歩" || s.getPiece( ) == "香" ||
							s.getPiece( ) == "桂" ) )
					{
						s.promote( );
						temp3 = "+";
					}
					else if( ( s.getSide( ) ? y == 1 : y == 7 ) && (s.getPiece( ) == "桂" ) )
					{
						s.promote( );
						temp3 = "+";
					}
					else
					{
						AlertDialog.Builder builder = new AlertDialog.Builder( this );
						builder.setMessage( "Promote?" )
						       .setPositiveButton( s.getPiece() + "(No)", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	   dialog.cancel();
						           }
						       })
						       .setNegativeButton( s.getPromote() + "(Yes)", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	  onPromote( s );
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show( );
					}
				}
				
				String temp = "-";
				
				if( this.getPiece( x, y ) != null )
				{
					temp = "x";
					if( this.getPiece( x, y ).getPiece() == "歩" || this.getPiece( x, y ).getPiece() == "と" )
					{
						if( turn == BLACK )
							bDrop[ 0 ] += 1;
						else
							wDrop[ 0 ] += 1;
					}
					else if( this.getPiece( x, y ).getPiece() == "桂" || this.getPiece( x, y ).getPiece() == "圭" )
					{
						if( turn == BLACK )
							bDrop[ 1 ] += 1;
						else
							wDrop[ 1 ] += 1;
					}
					else if( this.getPiece( x, y ).getPiece() == "香" || this.getPiece( x, y ).getPiece() == "杏" )
					{
						if( turn == BLACK )
							bDrop[ 2 ] += 1;
						else
							wDrop[ 2 ] += 1;
					}
					else if( this.getPiece( x, y ).getPiece() == "銀" || this.getPiece( x, y ).getPiece() == "全" )
					{
						if( turn == BLACK )
							bDrop[ 3 ] += 1;
						else
							wDrop[ 3 ] += 1;
					}
					else if( this.getPiece( x, y ).getPiece() == "金" )
					{
						if( turn == BLACK )
							bDrop[ 4 ] += 1;
						else
							wDrop[ 4 ] += 1;
					}
					else if( this.getPiece( x, y ).getPiece() == "角" || this.getPiece( x, y ).getPiece() == "馬" )
					{
						if( turn == BLACK )
							bDrop[ 5 ] += 1;
						else
							wDrop[ 5 ] += 1;
					}
					else if( this.getPiece( x, y ).getPiece() == "飛" || this.getPiece( x, y ).getPiece() == "龍" )
					{
						if( turn == BLACK )
							bDrop[ 6 ] += 1;
						else
							wDrop[ 6 ] += 1;
					}
				}
				
				board[ x ][ y ] = s;
				
				String temp2 = "";
				
				if( s.getPromote() == " " && temp3 == "" && ( s.getPiece() != "玉" && s.getPiece() != "王" ) )
					temp2 = "+";
				moveList += "\n" + ( curMove++ ) + ". " + temp2 + ( temp3 == "" ? s.getPiece() : temp4 ) + 
				( 9 - ox ) + ( ( char )( 97 + oy ) ) + temp + ( 9 - x ) + ( ( char)( 97 + y ) ) + temp3;
				
				turn = !turn;
			}
	}
	
	public boolean getTurn( )
	{
		return turn;
	}

}
