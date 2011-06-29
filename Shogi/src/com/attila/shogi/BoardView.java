package com.attila.shogi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {
	
	private float width, height;
	private int selX, selY, oSelX, oSelY;
	private boolean pieceSelected = false;
	private final ShogiGame curGame;
	private final Rect selRect = new Rect( );
	
	public BoardView( Context context )
	{
		super( context );
		this.curGame = ( ShogiGame ) context;
		setFocusable( true );
		setFocusableInTouchMode( true );
		selX = 4;
		selY = 4;
		oSelX = -1;
		oSelY = -1;
	}
	
	private void getRect( int x, int y, Rect rect )
	{
		rect.set( ( int )(x * this.width), ( int )(y * this.height ), ( int )( x * width + width ), ( int )( y * height + height ));
	}
	
	private void select( int x, int y ) 
	{	
		invalidate( selRect );
		
		selX = Math.min(Math.max(x, 0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		getRect( selX, selY, selRect );
		
		if( this.curGame.getDrop() != -1 )
		{
			pieceSelected = false;
			oSelX = -1;
			oSelY = -1;
			this.curGame.dropPiece( selX, selY );
			invalidate(selRect);
			return;
		}
		
		if( oSelX != -1 && oSelY != -1 )
		{
			if( selX == oSelX && selY == oSelY )
			{
				oSelX = -1;
				oSelY = -1;
				pieceSelected = false;
				return;
			}
			
			this.curGame.setPiece( oSelX, oSelY, selX, selY );
			invalidate(selRect);
			oSelX = -1;
			oSelY = -1;
			pieceSelected = false;
			return;
		}
		
		if( this.curGame.getPiece( selX, selY ) != null && 
				this.curGame.getPiece( selX, selY ).getSide() == this.curGame.getTurn() )
		{
			pieceSelected = true;
			oSelX = selX;
			oSelY = selY;
		}
		
		invalidate(selRect);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		this.width = w / 9f;
		this.height = h / 9f;
		getRect( selX, selY, selRect );
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) 
	{
		//draw board
		Paint background = new Paint( );
		
		background.setColor( getResources( ).getColor( R.color.board ) );
		canvas.drawRect( 0, 0, getWidth( ), getHeight( ), background ); 
		
		Paint squares = new Paint( );
		squares.setColor( Color.BLACK );
		
		for( int i = 0; i < 9; i++ )
		{
			canvas.drawLine( 0, i * height, getWidth(), i * height, squares );
			canvas.drawLine( i * width, 0, i * width, getHeight(), squares );
			if( i == 3 )
			{
				canvas.drawCircle( i * width, i * height, 5, squares );
				canvas.drawCircle( i * width, ( i + 3 ) * height, 5, squares );
			}
			else if ( i == 6 )
			{
				canvas.drawCircle( i * width, i * height, 5, squares );
				canvas.drawCircle( i * width, ( i - 3 ) * height, 5, squares );
			}
		}
		
		//draw pieces
		
		Paint foreground = new Paint( Paint.ANTI_ALIAS_FLAG );
		foreground.setStyle( Style.FILL );
		foreground.setTextSize( height * .75f );
		foreground.setTextScaleX( width / height );
		foreground.setTextAlign( Paint.Align.CENTER );
		
		FontMetrics fm = foreground.getFontMetrics( );
		
		float x = width / 2;
		float y = height / 2 - ( fm.ascent + fm.descent ) / 2;
		
		for( int i = 0; i < 9; i++ )
		{
			for( int j = 0; j < 9; j++ )
			{
				if( this.curGame.getPiece( i, j ) == null )
					continue;
				
				if( this.curGame.getPiece( i, j ).getSide() == true )
					foreground.setColor( Color.BLACK );
				else
					foreground.setColor( Color.WHITE );
				
				canvas.drawText( this.curGame.getPiece( i, j ).getPiece(), i * width + x, j * height + y, foreground );
				
			}
		}
		
		//draw selection square
		
		Paint selected = new Paint( );
		
		if( pieceSelected == false )
			selected.setColor( getResources( ).getColor( R.color.selection_c ) );
		else
			selected.setColor( getResources( ).getColor( R.color.selection_c2 ) );
		
		getRect( selX, selY, selRect );
		
		canvas.drawRect( selRect, selected );
		
		super.onDraw(canvas);
	}

	/*@Override
	Todo: ensure that user must press enter
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		switch( keyCode )
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select( selX - 1, selY );
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select( selX + 1, selY );
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			select( selX, selY - 1 );
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select( selX, selY + 1 );
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}*/

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if( event.getAction( ) != MotionEvent.ACTION_DOWN )
			return super.onTouchEvent(event);		
		
		select( ( int ) ( event.getX( ) / width ), ( int ) ( event.getY( ) / height ) );
		return super.onTouchEvent(event);
	}
	
}
