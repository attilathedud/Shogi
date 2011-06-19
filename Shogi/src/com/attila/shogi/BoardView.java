package com.attila.shogi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {
	
	private float width, height;
	private int selX, selY;
	private final ShogiGame curGame;
	private final Rect selRect = new Rect( );
	
	public BoardView( Context context )
	{
		super( context );
		this.curGame = ( ShogiGame ) context;
		setFocusable( true );
		setFocusableInTouchMode( true );
	}
	
	private void getRect( int x, int y, Rect rect )
	{
		rect.set( ( int )(x * this.width), ( int )(y * this.height ), ( int )( x * width + width ), ( int )( y * height + height ));
	}
	
	private void select( int x, int y ) {
		invalidate( selRect );
		
		selX = Math.min(Math.max(x, 0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		getRect( selX, selY, selRect );
		
		invalidate(selRect);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.width = w / 9f;
		this.height = h / 9f;
		getRect( selX, selY, selRect );
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
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
		
		//draw selection square
		Paint selected = new Paint( );
		selected.setColor( getResources( ).getColor( R.color.selection_c ) );
		canvas.drawRect( selRect, selected );
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if( event.getAction( ) != MotionEvent.ACTION_DOWN )
			return super.onTouchEvent(event);
		
		select( ( int ) ( event.getX( ) / width ), ( int ) ( event.getY( ) / height ) );
		return super.onTouchEvent(event);
	}
	
}
