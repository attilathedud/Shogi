package com.attila.shogi;


public class ShogiPiece {
	
	private String piece;
	private boolean side;	//true = black, false = white

	public ShogiPiece( String piece, boolean side )
	{
		this.piece = piece;
		this.side = side;
	}
	
	public String getPiece( )
	{
		return this.piece;
	}
	
	public boolean getSide( )
	{
		return this.side;
	}
	
	public void promote( )
	{
		if( piece == "歩" )
			piece = "と";
		else if( piece == "香" )
			piece = "杏";
		else if( piece == "銀" )
			piece = "全";
		else if( piece == "桂" )
			piece = "圭";
		else if( piece == "角" )
			piece = "馬";
		else if( piece == "飛" )
			piece = "龍";
	}
	
	public String getPromote( )
	{
		if( piece == "歩" )
			return "と";
		else if( piece == "香" )
			return "杏";
		else if( piece == "銀" )
			return "全";
		else if( piece == "桂" )
			return "圭";
		else if( piece == "角" )
			return "馬";
		else if( piece == "飛" )
			return "龍";
		else
			return " ";
	}
	
	public boolean isValidMove( int ox, int oy, int x, int y )
	{
		if( piece == "歩" )
		{
			if( x != ox || (side ? y > oy : y < oy) || (side ? y < oy - 1 : y > oy + 1) )
				return false;
		}
		else if( piece == "玉" || piece == "王" )
		{
			if( ( x > ox ? x > ox + 1 : x < ox - 1 ) || ( y > oy ? y > oy + 1 : y < oy - 1 ) )
				return false;

			/*Todo check for revealing check*/
		}
		else if( piece == "金" || piece == "と" || piece == "杏" || piece == "全" || piece == "圭" )
		{
			if( ( x > ox ? x > ox + 1 : x < ox - 1 ) || ( y > oy ? y > oy + 1 : y < oy - 1 ) ||
					( x != ox && ( side ? y == oy + 1 : y == oy - 1 ) ) )
				return false;
		}
		else if( piece == "銀" )
		{
			if( ( x > ox ? x > ox + 1 : x < ox - 1 ) || ( y > oy ? y > oy + 1 : y < oy - 1 ) || 
					( y == oy && x != ox ) || ( x == ox && ( side ? y == oy + 1 : y == oy - 1 ) ) )
				return false;
		}
		else if( piece == "桂" )
		{
			if( ( x > ox ? x != ox + 1 : x != ox - 1 ) || (side ? y != oy - 2 : y != oy + 2 ) )
				return false;
		}
		else if( piece == "飛" )
		{
			if( x != ox && y != oy )
				return false;
		}
		else if( piece == "角" )
		{
			if( Math.abs( x - ox ) != Math.abs( y - oy ) )
				return false;
		}
		else if( piece == "香" )
		{
			if( x != ox || ( side ? y > oy : y < oy ) )
				return false;
		}
		else if( piece == "馬" )
		{
			if( ( x > ox ? x > ox + 1 : x < ox - 1 ) || ( y > oy ? y > oy + 1 : y < oy - 1 ) )
			{
				if( Math.abs( x - ox ) != Math.abs( y - oy ) )
					return false;
			}
		}
		else if( piece == "龍" )
		{
			if( ( x > ox ? x > ox + 1 : x < ox - 1 ) || ( y > oy ? y > oy + 1 : y < oy - 1 ) )
			{
				if( x != ox && y != oy )
					return false;
			}
		}
		
		return true;
	}
	
	public boolean hasPath( ShogiPiece board[ ][ ], int ox, int oy, int x, int y )
	{
		if( piece == "飛" || piece == "龍" )
		{
			if( x != ox )
			{
				for( int i = Math.min( x, ox) + 1; i < Math.max( x, ox ); i++ )
				{
					if( board[ i ][ y ] != null )
						return false;
				}
			}
			if( y != oy )
			{
				for( int i = Math.min( y, oy ) + 1; i < Math.max( y, oy ); i++ )
				{
					if( board[ x ][ i ] != null )
						return false;
				}
			}
		}
		else if( piece == "角" || piece == "馬" )
		{
			/*Todo: make less ugly */
			if( x > ox )
			{
				if( y > oy )
				{
					for( int i = ox + 1, j = oy + 1; i < x; i++, j++ )
					{
						if( board[ i ][ j ] != null )
							return false;
					}
				}
				else
				{
					for( int i = ox + 1, j = oy - 1; i < x; i++, j-- )
					{
						if( board[ i ][ j ] != null )
							return false;
					}
				}
			}
			else
			{
				if( y > oy )
				{
					for( int i = ox - 1, j = oy + 1; i > x; i--, j++ )
					{
						if( board[ i ][ j ] != null )
							return false;
					}
				}
				else
				{
					for( int i = ox - 1, j = oy - 1; i > x; i--, j-- )
					{
						if( board[ i ][ j ] != null )
							return false;
					}
				}
			}
		}
		else if( piece == "香" )
		{
			for( int i = Math.min( y, oy ) + 1; i < Math.max( y, oy ); i++ )
			{
				if( board[ x ][ i ] != null )
					return false;
			}
		}
		
		return true;
	}
	
}
