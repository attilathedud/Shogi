package com.attila.shogi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShogiGame extends Activity {
	
	private final boolean BLACK = true, WHITE = false;
	
	private ShogiPiece board[ ][ ] = new ShogiPiece[ 9 ][ 9 ];
	private int bDrop[ ] = new int[ 7 ], wDrop[ ] = new int[ 7 ], drop = -1, curMove = 1;
	private boolean turn = true, apos = false;
	private String moveList = "", saveName, phoneNumber = "";
	private ViewGroup l;
	private Button send;
	private NaviBoardDialog navi;
	private SmsManager s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
		
		s = SmsManager.getDefault();
		
		Bundle extras = getIntent( ).getExtras();
		
		saveName = extras.getString( "SaveName" );
		phoneNumber = saveName.substring( saveName.indexOf( "( ") + 1, saveName.lastIndexOf( " )" ) ).trim();
		 
		if( extras.getString( "MoveList" ) == null )
			initBoard( );
		else
		{	
			String dBox = extras.getString("DropBox").trim();
			
			for( int i = 0; i < 7; i++ )
			{
				bDrop[ i ] = dBox.getBytes()[ i ] - 48;
			}
			
			for( int i = 0; i < 7; i++ )
			{
				wDrop[ i ] = dBox.getBytes()[ i + 7 ] - 48;
			}
			
			moveList = extras.getString( "MoveList" ).trim();
			
			if( moveList != null && moveList.length() == 0)
			{
				curMove = 1;
			}
			else if( moveList != null && moveList.lastIndexOf( "\n") == -1 && moveList.length() != 0)
			{
				curMove = 2;
				turn = false;
			}
			else
			{
				String lastLine = moveList.substring(moveList.lastIndexOf( "\n" ) );
				lastLine = lastLine.substring(0, lastLine.indexOf( "." ) );
				curMove = Integer.parseInt( lastLine.trim() ) + 1;
				
				if( curMove % 2 == 0)
					turn = false;
			}
			
			String tempBoard = extras.getString( "CurBoard" ).trim();
			char [ ] buffer = new char[ 200 ];
			int x = 0, y = 0;

			tempBoard.getChars(0, tempBoard.length(), buffer, 0);
			
			for( int i = 0; i < buffer.length - 1; i ++ )
			{
				if( buffer[ i ] != '?' )
				{
					/* Ugh... this needs cleaning up */
					if( buffer[ i ] == 'A' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PAWN, BLACK );
					else if( buffer[ i ] == 'B' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PAWN, WHITE );
					else if( buffer[ i ] == 'C' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_PAWN, BLACK );
					else if( buffer[ i ] == 'D' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_PAWN, WHITE );
					else if( buffer[ i ] == 'E' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_LANCE, BLACK );
					else if( buffer[ i ] == 'F' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_LANCE, WHITE );
					else if( buffer[ i ] == 'G' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_LANCE, BLACK );
					else if( buffer[ i ] == 'H' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_LANCE, WHITE );
					else if( buffer[ i ] == 'I' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_KNIGHT, BLACK );
					else if( buffer[ i ] == 'J' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_KNIGHT, WHITE );
					else if( buffer[ i ] == 'K' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_KNIGHT, BLACK );
					else if( buffer[ i ] == 'L' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_KNIGHT, WHITE );
					else if( buffer[ i ] == 'M' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_SILVER, BLACK );
					else if( buffer[ i ] == 'N' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_SILVER, WHITE );
					else if( buffer[ i ] == 'O' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_SILVER, BLACK );
					else if( buffer[ i ] == 'P' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_SILVER, WHITE );
					else if( buffer[ i ] == 'Q' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_GOLD, BLACK );
					else if( buffer[ i ] == 'R' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_GOLD, WHITE );
					else if( buffer[ i ] == 'S' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_KING, WHITE );
					else if( buffer[ i ] == 'T' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_OPPO_KING, BLACK );
					else if( buffer[ i ] == 'U' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_BISHOP, BLACK );
					else if( buffer[ i ] == 'V' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_BISHOP, WHITE );
					else if( buffer[ i ] == 'W' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_BISHOP, BLACK );
					else if( buffer[ i ] == 'X' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_BISHOP, WHITE );
					else if( buffer[ i ] == 'Y' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_ROOK, BLACK );
					else if( buffer[ i ] == 'Z' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_ROOK, WHITE );
					else if( buffer[ i ] == '$' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_ROOK, BLACK );
					else if( buffer[ i ] == '&' )
						board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PRO_ROOK, WHITE );
				}
				
				x++;
				if( x == 10 )
				{
					x = 0;
					y++;
				}
			}
		}
		
		navi = new NaviBoardDialog( this );
		
		BoardView bvBoard = new BoardView( this );
		final Context k = this;
		bvBoard.setOnLongClickListener( new OnLongClickListener( ) {
			
			public boolean onLongClick( View v )
			{
				Dialog d = new DropBoxDialog( k, (turn ? bDrop : wDrop ), (turn ? wDrop : bDrop ) );
				d.show( );
				return true;
			}
		});
		
		l = new RelativeLayout( this );
		send = new Button(this);
		send.setText( "Send Move" );
		send.setVisibility( View.INVISIBLE );
		send.setOnClickListener( new View.OnClickListener( ) {			
			public void onClick( View v )
			{
				send.setVisibility( View.INVISIBLE );
				
				String lastMove = moveList.substring( moveList.lastIndexOf( "\n" ) + 1 ).trim();
				
				s.sendTextMessage( phoneNumber, null, lastMove, null, null );
			}
		});
	
		l.addView( bvBoard );
		l.addView( send );

		setContentView( l );
		l.requestFocus();
	}
     	
	@Override
	public void onBackPressed() 
	{
		if( drop != -1 )
			drop = -1;
		else
		{
			AlertDialog.Builder builder2 = new AlertDialog.Builder( this );
			builder2.setMessage( "Save?" )
			       .setPositiveButton( "No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   finish( );
			           }
			       })
			       .setNegativeButton( "Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	  saveGame( );
			        	  finish( );
			           }
			       });
			AlertDialog alert2 = builder2.create();
			alert2.show( );
			
			if( !alert2.isShowing() )
			{
				super.onBackPressed();
			}	
		}
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
		case R.id.apos_id:
			apos = true;
			navi.setGame( this );
			navi.show();
			return true;
		case R.id.dropboard_id:
			Dialog d = new DropBoxDialog( this, (turn ? bDrop : wDrop ), (turn ? wDrop : bDrop ) );
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
			        	  saveGame( );
			        	  finish( );
			           }
			       });
			AlertDialog alert2 = builder2.create();
			alert2.show( );
			
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void initBoard( )
	{
		for( int j = 0; j < 9; j++ )
		{
			board[ j ][ 2 ] = new ShogiPiece( PieceEnum.P_PAWN, WHITE );
			board[ j ][ 6 ] = new ShogiPiece( PieceEnum.P_PAWN, BLACK );
		}
		
		board[ 0 ][ 0 ] = new ShogiPiece( PieceEnum.P_LANCE, WHITE );
		board[ 1 ][ 0 ] = new ShogiPiece( PieceEnum.P_KNIGHT, WHITE );
		board[ 8 ][ 0 ] = new ShogiPiece( PieceEnum.P_LANCE, WHITE );
		board[ 7 ][ 0 ] = new ShogiPiece( PieceEnum.P_KNIGHT, WHITE );
		board[ 2 ][ 0 ] = new ShogiPiece( PieceEnum.P_SILVER, WHITE );
		board[ 6 ][ 0 ] = new ShogiPiece( PieceEnum.P_SILVER, WHITE );
		board[ 3 ][ 0 ] = new ShogiPiece( PieceEnum.P_GOLD, WHITE );
		board[ 5 ][ 0 ] = new ShogiPiece( PieceEnum.P_GOLD, WHITE );
		board[ 4 ][ 0 ] = new ShogiPiece( PieceEnum.P_KING, WHITE );
		board[ 1 ][ 1 ] = new ShogiPiece( PieceEnum.P_ROOK, WHITE );
		board[ 7 ][ 1 ] = new ShogiPiece( PieceEnum.P_BISHOP, WHITE );
		
		board[ 0 ][ 8 ] = new ShogiPiece( PieceEnum.P_LANCE, BLACK );
		board[ 8 ][ 8 ] = new ShogiPiece( PieceEnum.P_LANCE, BLACK );
		board[ 1 ][ 8 ] = new ShogiPiece( PieceEnum.P_KNIGHT, BLACK );
		board[ 7 ][ 8 ] = new ShogiPiece( PieceEnum.P_KNIGHT, BLACK );
		board[ 2 ][ 8 ] = new ShogiPiece( PieceEnum.P_SILVER, BLACK );
		board[ 6 ][ 8 ] = new ShogiPiece( PieceEnum.P_SILVER, BLACK );
		board[ 3 ][ 8 ] = new ShogiPiece( PieceEnum.P_GOLD, BLACK );
		board[ 5 ][ 8 ] = new ShogiPiece( PieceEnum.P_GOLD, BLACK );
		board[ 4 ][ 8 ] = new ShogiPiece( PieceEnum.P_OPPO_KING, BLACK );
		board[ 1 ][ 7 ] = new ShogiPiece( PieceEnum.P_BISHOP, BLACK );
		board[ 7 ][ 7 ] = new ShogiPiece( PieceEnum.P_ROOK, BLACK );
	}

	public void saveGame( )
	{
		String gameBoard = "";
		
		for( int i = 0; i < 9; i++ )
		{
			for( int j = 0; j < 9; j++ )
			{
				gameBoard += ( board[ j ][ i ] == null ? "?" : board[ j ][ i ].getSaveName() );
			}
			
			gameBoard += "\n";
		}
		
		gameBoard += "END_OF_GAMEBOARD";
		
		String dropBoard = "\nEND_OF_MOVELIST";
		
		for( int i = 0; i < 7; i++ )
		{
			dropBoard += bDrop[ i ];
		}
		
		for( int i = 0; i < 7; i++ )
		{
			dropBoard += wDrop[ i ];
		}
		
		try {
			FileOutputStream fos = openFileOutput(saveName, Context.MODE_PRIVATE);
			fos.write( gameBoard.getBytes() );
			fos.write( (moveList + " ").getBytes() );
			fos.write( dropBoard.getBytes() );
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*
	public void navigate( )
	{
		String k = moveList.substring( moveList.lastIndexOf( "." ) + 1 ).trim();
		char [ ] buffer = new char[ 10 ];
		
		k.getChars(0, k.length(), buffer, 0);
		
		ShogiPiece temp = board[ (9 - ( buffer[ 4 ] - 48 ) ) ][ ( buffer[ 5 ] - 97 ) ];
		board[ (9 - ( buffer[ 4 ] - 48 ) ) ][ ( buffer[ 5 ] - 97 ) ] = null;
		board[ (9 - ( buffer[ 1 ] - 48 ) ) ][ ( buffer[ 2 ] - 97 ) ] = temp;
		
		turn = !turn;
		
	 	setContentView( bvBoard );
	  	bvBoard.requestFocus();
	}*/
	
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
				else if( board[ x ][ i ].getPiece() == PieceEnum.P_PAWN && board[ x ][ i ].getSide( ) == turn )
				{
					Toast notify = Toast.makeText( this, "Can't drop there", Toast.LENGTH_SHORT );
				    notify.setGravity( Gravity.CENTER, 0, 0 );
				    notify.show( );
				    
				    drop = -1;
				    
					return;
				}
			}
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_PAWN, turn );
			temp = "P";
			break;
		case 1:
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_KNIGHT, turn );
			temp = "Kn";
			break;
		case 2:
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_LANCE, turn );
			temp = "L";
			break;
		case 3:
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_SILVER, turn );
			temp = "S";
			break;
		case 4:
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_GOLD, turn );
			temp = "G";
			break;
		case 5:
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_BISHOP, turn );
			temp = "B";
			break;
		case 6:
			board[ x ][ y ] = new ShogiPiece( PieceEnum.P_ROOK, turn );
			temp = "R";
		}
		
		if( turn == BLACK )
			bDrop[ drop ]--;
		else
			wDrop[ drop ]--;
		
		moveList += "\n" + (curMove++) + ". " + temp + "*" + ( 9 - x ) + ( char)( 97 + y ) + " ";
		
		drop = -1;
		turn = !turn;
	}
	
	private void onPromote( ShogiPiece s )
	{
	   moveList += "+";
	   s.promote();
	   if( apos == false )
	   {
		   setContentView( l );
		   l.requestFocus();
	   }
	   else
		   navi.onPromote( s );
	}
	
	public void setPiece( int ox, int oy, int x, int y )
	{
		final ShogiPiece s = this.getPiece( ox, oy );
		String temp4 = s.getPieceHistoryName();
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
					if( ( s.getSide( ) ? y == 0 : y == 8 ) && ( s.getPiece( ) == PieceEnum.P_PAWN || 
							s.getPiece( ) == PieceEnum.P_LANCE || s.getPiece( ) == PieceEnum.P_KNIGHT ) )
					{
						s.promote( );
						temp3 = "+";
					}
					else if( ( s.getSide( ) ? y == 1 : y == 7 ) && (s.getPiece( ) == PieceEnum.P_KNIGHT ) )
					{
						s.promote( );
						temp3 = "+";
					}
					else
					{
						AlertDialog.Builder builder = new AlertDialog.Builder( this );
						builder.setMessage( "Promote?" )
							   .setCancelable( false )
						       .setPositiveButton( s.getPieceHistoryName() + "(No)", new DialogInterface.OnClickListener() {
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
					
					int tempDrop = 0;
					
					switch( this.getPiece( x, y ).getPiece() )
					{
					case P_PAWN:
					case P_PRO_PAWN:
						tempDrop = 0;
						break;
					case P_KNIGHT:
					case P_PRO_KNIGHT:
						tempDrop = 1;
						break;
					case P_LANCE:
					case P_PRO_LANCE:
						tempDrop = 2;
						break;
					case P_SILVER:
					case P_PRO_SILVER:
						tempDrop = 3;
						break;
					case P_GOLD:
						tempDrop = 4;
						break;
					case P_BISHOP:
					case P_PRO_BISHOP:
						tempDrop = 5;
						break;
					case P_ROOK:
					case P_PRO_ROOK:
						tempDrop = 6;
						break;
					}
					
					if( turn == BLACK )
						bDrop[ tempDrop ] += 1;
					else
						wDrop[ tempDrop ] += 1;
					
				}
				
				board[ x ][ y ] = s;
				
				send.setVisibility( View.VISIBLE );
				
				String temp2 = "";
				
				if( s.getPromote() == " " && temp3 == "" && ( s.getPiece() != PieceEnum.P_KING && 
						s.getPiece() != PieceEnum.P_OPPO_KING && s.getPiece() != PieceEnum.P_GOLD) )
					temp2 = "+";
				moveList += "\n" + ( curMove++ ) + ". " + temp2 + ( temp3 == "" ? s.getPieceHistoryName( ) : temp4 ) + 
				( 9 - ox ) + ( ( char )( 97 + oy ) ) + temp + ( 9 - x ) + ( ( char)( 97 + y ) ) + temp3 + " ";
				
				turn = !turn;
			}
	}
	
	public ShogiPiece[][] getBoard( )
	{
		ShogiPiece tboard[ ][ ] = new ShogiPiece[ 9 ][ 9 ];
		
		for( int i = 0; i < 9; i++ )
		{
			for( int j = 0; j < 9; j++ )
			{
				if( board[ i ][ j ] == null )
					continue;
				else
					tboard[ i ][ j ] = new ShogiPiece( board[ i ][ j ].getPiece(), board[ i ][ j ].getSide() );
			}
		}
		
		return tboard;
	}
	
	public void setBoard( ShogiPiece[][] t )
	{
		board = t;
	}
	 
	public int[] getBDrop( )
	{
		int td[] = new int[ 7 ];
		
		for( int i = 0; i < 7; i++ )
			td[ i ] = bDrop[ i ];
		
		return td;
	}
	
	public int[] getWDrop( )
	{
		int td[] = new int[ 7 ];
		
		for( int i = 0; i < 7; i++ )
			td[ i ] = wDrop[ i ];
		
		return td;
	}
	
	public void setBDrop( int[] t )
	{
		bDrop = t;
	}
	
	public void setWDrop( int[] t )
	{
		wDrop = t;
	}
	
	public void setAPos( boolean c )
	{
		apos = c;
	}
	
	public String getMoveList()
	{
		String c = new String( moveList );
		
		return c;
	}
	
	public void setMoveList(String c)
	{
		moveList = c;
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
	
	public boolean getTurn( )
	{
		return turn;
	}
	
	public void setTurn( boolean t )
	{
		turn = t;
	}

}
