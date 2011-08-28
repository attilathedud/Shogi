package com.attila.shogi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {
	
	private float width, height;
	private int selX, selY, oSelX, oSelY, curRsInUse = 0;
	private boolean pieceSelected = false;
	private final ShogiGame curGame;
	private final Rect selRect = new Rect( ), rTemp = new Rect( ), rSTemp[] = new Rect[ 22 ];
	private final static Bitmap pTemp[ ] = new Bitmap[ 15 ];
	private final Paint selected = new Paint( ), squares = new Paint( ), background = new Paint( ), posMoves = new Paint(),
		foreground =  new Paint( Paint.ANTI_ALIAS_FLAG );
	private final Matrix m = new Matrix( );
	private FontMetrics fm;

	public BoardView( Context context )
	{
		super( context );
		this.curGame = ( ShogiGame ) context;
		setFocusable( true );
		setFocusableInTouchMode( true );
		selX = selY = 4;
		oSelX = oSelY = -1;
		posMoves.setColor( Color.GREEN );
		squares.setColor( Color.BLACK );
		background.setColor( getResources( ).getColor( R.color.board ) );
		foreground.setStyle( Style.FILL );
		foreground.setTextAlign( Paint.Align.CENTER );
		m.postRotate( 180 );
		pTemp[ 0 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_pawn );
		pTemp[ 1 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_p_pawn );
		pTemp[ 2 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_lance );
		pTemp[ 3 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_p_lance );
		pTemp[ 4 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_knight );
		pTemp[ 5 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_p_knight );
		pTemp[ 6 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_silver );
		pTemp[ 7 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_p_silver );
		pTemp[ 8 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_gold );
		pTemp[ 9 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_king );
		pTemp[ 10 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_o_king );
		pTemp[ 11 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_bishop );
		pTemp[ 12 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_p_bishop );
		pTemp[ 13 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_rook );
		pTemp[ 14 ] = BitmapFactory.decodeResource( getResources( ), R.drawable.s_p_rook );
		for( int i = 0; i < 22; i++ )
			rSTemp[ i ] = new Rect();
	}
	
	private void getRect( int x, int y, Rect rect )
	{
		rect.set( ( int )(x * this.width), ( int )(y * this.height), ( int )( x * width + width ), ( int )( y * height + height ));
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
				for( int i = 0; i < curRsInUse; i++)
				{
					invalidate(rSTemp[ i ]);
				}
				return;
			}
			
			this.curGame.setPiece( oSelX, oSelY, selX, selY );
			invalidate(selRect);
			for( int i = 0; i < curRsInUse; i++)
			{
				invalidate(rSTemp[ i ]);
			}
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
			
			
			curRsInUse = this.curGame.getPiece( selX, selY ).getPieceMoves( selX, selY, rSTemp, this.width, this.height );
			for( int i = 0; i < curRsInUse; i++)
			{
				invalidate( rSTemp[ i ] );
			}
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
		canvas.drawRect( 0, 0, getWidth( ), getHeight( ), background ); 
		
		//draw pieces
		Bitmap curpTemp = null;
		
		if( !Prefs.getPieces( this.curGame ) )
		{
			for( int i = 0; i < 9; i++ )
			{
				for( int j = 0; j < 9; j++ )
				{
					if( this.curGame.getPiece( i, j ) == null )
						continue;
					
					switch( this.curGame.getPiece(i , j ).getPiece( ) )
					{
					case P_PAWN:
						curpTemp = pTemp[ 0 ];
						break;
					case P_PRO_PAWN:
						curpTemp = pTemp[ 1 ];
						break;
					case P_LANCE:
						curpTemp = pTemp[ 2 ];
						break;
					case P_PRO_LANCE:
						curpTemp = pTemp[ 3 ];
						break;
					case P_KNIGHT:
						curpTemp = pTemp[ 4 ];
						break;
					case P_PRO_KNIGHT:
						curpTemp = pTemp[ 5 ];
						break;
					case P_SILVER:
						curpTemp = pTemp[ 6 ];
						break;
					case P_PRO_SILVER:
						curpTemp = pTemp[ 7 ];
						break;
					case P_GOLD:
						curpTemp = pTemp[ 8 ];
						break;
					case P_KING:
						curpTemp = pTemp[ 9 ];
						break;
					case P_OPPO_KING:
						curpTemp = pTemp[ 10 ];
						break;
					case P_BISHOP:
						curpTemp = pTemp[ 11 ];
						break;
					case P_PRO_BISHOP:
						curpTemp = pTemp[ 12 ];
						break;
					case P_ROOK:
						curpTemp = pTemp[ 13 ];
						break;
					case P_PRO_ROOK:
						curpTemp = pTemp[ 14 ];
						break;
					}
					
					getRect( i, j, rTemp );
					
					if( this.curGame.getPiece( i, j ).getSide() == true )
						canvas.drawBitmap( curpTemp, null, rTemp, null );
					else
						canvas.drawBitmap( Bitmap.createBitmap( curpTemp, 0, 0, curpTemp.getWidth(), 
								curpTemp.getHeight(), m, true ) , null, rTemp, null );
				}
			}
		}
		
		int curPos = 9;
		char curPos2 = 'a';
		
		//draw squares		
		for( int i = 0; i < 9; i++ )
		{
			canvas.drawText( Character.toString( curPos2 ), 1, (i+1) * height, squares);
			canvas.drawText( Integer.toString( curPos ), i * width + 1, 10 , squares);
			curPos--;
			curPos2++;
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
		
		
		if( Prefs.getPieces( this.curGame ) )
		{
			foreground.setTextSize( height * .75f );
			foreground.setTextScaleX( width / height );
			fm = foreground.getFontMetrics( );
			
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
			
				canvas.drawText( this.curGame.getPiece( i, j ).getPieceName(), 
					i * width + x, j * height + y, foreground );
			
				}
			}
		}
		
		if( pieceSelected == true)
		{
			for( int i = 0; i < curRsInUse; i ++)
			{
			canvas.drawCircle( (( rSTemp[ i ].right -rSTemp[ i ].left ) / 2) + rSTemp[ i ].left, 
					(( rSTemp[ i ].bottom -rSTemp[ i ].top ) / 2) + rSTemp[ i ].top, 9 , posMoves );
			}
		}
		
		//draw selection square
		if( pieceSelected == false )
			selected.setColor( getResources( ).getColor( R.color.selection_c ) );
		else
			selected.setColor( getResources( ).getColor( R.color.selection_c2 ) );
		
		getRect( selX, selY, selRect );
		
		canvas.drawRect( selRect, selected );
				
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if( event.getAction( ) != MotionEvent.ACTION_DOWN )
			return super.onTouchEvent(event);		
		
		select( ( int ) ( event.getX( ) / width ), ( int ) ( event.getY( ) / height ) );
		return super.onTouchEvent(event);
	}
	
}
