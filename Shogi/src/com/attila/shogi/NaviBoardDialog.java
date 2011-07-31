package com.attila.shogi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;

public class NaviBoardDialog extends Dialog {

	private ShogiGame curGame;
	private BoardView bvBoard;
	private ShogiPiece board[ ][ ];
	private boolean turn, apos;
	private int bDrop[ ], wDrop[ ];
	private String moveList;
	

	/*Todo: fix refresh issue by making NaviBoardDialog be a global member initialised at
	 * onCreate, then use onPromote() to pass the message to NaviBoardDialog.onPromote()
	 * ...
	 * apos really doesn't need to be passed... make getters/setters in ShogiGame
	 */
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		this.curGame.setBoard( board );
		this.curGame.setTurn( turn );
		
		this.curGame.setBDrop( bDrop );
		this.curGame.setWDrop( wDrop );
		
		this.curGame.setAPos( !this.apos );
		this.curGame.setMoveList( moveList );
		
		
		super.dismiss();
	}

	public NaviBoardDialog(Context context, boolean apos ) {
		super(context);
		this.curGame = ( ShogiGame ) context;
		board = this.curGame.getBoard();
		turn = this.curGame.getTurn();
		bDrop = this.curGame.getBDrop();
		wDrop = this.curGame.getWDrop();
		moveList = this.curGame.getMoveList();
		this.apos = apos;
	}

	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
		setTitle( "Analyse Position" );
		
		bvBoard = new BoardView( this.curGame );
		
		bvBoard.setOnLongClickListener( new OnLongClickListener( ) {
			
			public boolean onLongClick( View v )
			{
				Dialog d = new DropBoxDialog( curGame, (turn ? curGame.getBDrop() : curGame.getWDrop() ), (turn ? curGame.getWDrop() : curGame.getBDrop() ) );
				d.show( );
				return true;
			}
		});
		
		setContentView( bvBoard );
		bvBoard.requestFocus( );
	}
	
	

}
