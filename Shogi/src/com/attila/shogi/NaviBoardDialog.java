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
	private boolean turn;
	private int bDrop[ ], wDrop[ ], curMove;
	private String moveList;
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		this.curGame.setBoard( board );
		this.curGame.setTurn( turn );
		
		this.curGame.setBDrop( bDrop );
		this.curGame.setWDrop( wDrop );
		
		this.curGame.setAPos( false );
		this.curGame.setMoveList( moveList );
		this.curGame.setCurMove( curMove );
		
		
		super.dismiss();
	}

	public NaviBoardDialog(Context context ) {
		super(context);
	}
	
	public void setGame( Context context )
	{
		this.curGame = ( ShogiGame ) context;
		board = this.curGame.getBoard();
		turn = this.curGame.getTurn();
		bDrop = this.curGame.getBDrop();
		wDrop = this.curGame.getWDrop();
		moveList = this.curGame.getMoveList();
		curMove = this.curGame.getCurMove();
	}
	
	public void onPromote( ShogiPiece s )
	{
	   setContentView( bvBoard );
	   bvBoard.requestFocus();
	}

	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
		setTitle( "Analyse Position" );
		
		bvBoard = new BoardView( this.curGame );
		
		bvBoard.setOnLongClickListener( new OnLongClickListener( ) {
			
			public boolean onLongClick( View v )
			{
				Dialog d = new DropBoxDialog( curGame, (curGame.getTurn() ? curGame.getBDrop() : curGame.getWDrop() ), (curGame.getTurn() ? curGame.getWDrop() : curGame.getBDrop() ) );
				d.show( );
				return true;
			}
		});
		
		setContentView( bvBoard );
		bvBoard.requestFocus( );
	}
	
	

}
