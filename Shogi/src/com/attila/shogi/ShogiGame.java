package com.attila.shogi;

import android.app.Activity;
import android.os.Bundle;

public class ShogiGame extends Activity {
	
//	private int board[ ][ ] = new int[ 9 ][ 9 ];
	private BoardView bvBoard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bvBoard = new BoardView( this );
		setContentView( bvBoard );
		bvBoard.requestFocus( );
	}

}
