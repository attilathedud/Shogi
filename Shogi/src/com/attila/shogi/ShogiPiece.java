package com.attila.shogi;

public class ShogiPiece {

	private PieceEnum piece;
	private boolean side; // true = black, false = white

	public ShogiPiece(PieceEnum piece, boolean side) {
		this.piece = piece;
		this.side = side;
	}

	public PieceEnum getPiece() {
		return this.piece;
	}

	public String getSaveName()
	{
		switch( piece )
		{
		case P_PAWN:
			if( side == true )
				return "A";
			else
				return "B";
		case P_PRO_PAWN:
			if( side == true )
				return "C";
			else
				return "D";
		case P_LANCE:
			if( side == true )
				return "E";
			else
				return "F";
		case P_PRO_LANCE:
			if( side == true )
				return "G";
			else
				return "H";
		case P_KNIGHT:
			if( side == true )
				return "I";
			else
				return "J";
		case P_PRO_KNIGHT:
			if( side == true )
				return "K";
			else
				return "L";
		case P_SILVER:
			if( side == true )
				return "M";
			else
				return "N";
		case P_PRO_SILVER:
			if( side == true )
				return "O";
			else
				return "P";
		case P_GOLD:
			if( side == true )
				return "Q";
			else
				return "R";
		case P_KING:
			return "S";
		case P_OPPO_KING:
			return "T";
		case P_BISHOP:
			if( side == true )
				return "U";
			else
				return "V";
		case P_PRO_BISHOP:
			if( side == true )
				return "W";
			else
				return "X";
		case P_ROOK:
			if( side == true )
				return "Y";
			else
				return "Z";
		case P_PRO_ROOK:
			if( side == true )
				return "$";
			else
				return "&";
		}
		
		return "";
	}
	
	public String getPieceHistoryName() {
		switch (piece) 
		{
		case P_PAWN:
		case P_PRO_PAWN:
			return "P";
		case P_LANCE:
		case P_PRO_LANCE:
			return "L";
		case P_KNIGHT:
		case P_PRO_KNIGHT:
			return "Kn";
		case P_SILVER:
		case P_PRO_SILVER:
			return "S";
		case P_GOLD:
			return "G";
		case P_KING:
		case P_OPPO_KING:
			return "K";
		case P_BISHOP:
		case P_PRO_BISHOP:
			return "B";
		case P_ROOK:
		case P_PRO_ROOK:
			return "R";
		default:
			return "?";
		}
	}

	public boolean getSide() {
		return this.side;
	}

	public void promote() {
		switch (piece) {
		case P_PAWN:
			piece = PieceEnum.P_PRO_PAWN;
			break;
		case P_LANCE:
			piece = PieceEnum.P_PRO_LANCE;
			break;
		case P_SILVER:
			piece = PieceEnum.P_PRO_SILVER;
			break;
		case P_KNIGHT:
			piece = PieceEnum.P_PRO_KNIGHT;
			break;
		case P_BISHOP:
			piece = PieceEnum.P_PRO_BISHOP;
			break;
		case P_ROOK:
			piece = PieceEnum.P_PRO_ROOK;
			break;
		}
	}

	public String getPromote() {
		switch (piece) {
		case P_PAWN:
			return "P*";
		case P_LANCE:
			return "L*";
		case P_SILVER:
			return "S*";
		case P_KNIGHT:
			return "Kn*";
		case P_BISHOP:
			return "B*";
		case P_ROOK:
			return "R*";
		default:
			return " ";
		}
	}

	/* Todo check for revealing check */
	public boolean isValidMove(int ox, int oy, int x, int y) {
		switch (piece) {
		case P_PAWN:
			if (x != ox || (side ? y > oy : y < oy)
					|| (side ? y < oy - 1 : y > oy + 1))
				return false;
			break;
		case P_KING:
		case P_OPPO_KING:
			if ((x > ox ? x > ox + 1 : x < ox - 1)
					|| (y > oy ? y > oy + 1 : y < oy - 1))
				return false;
			break;
		case P_GOLD:
		case P_PRO_PAWN:
		case P_PRO_LANCE:
		case P_PRO_SILVER:
		case P_PRO_KNIGHT:
			if ((x > ox ? x > ox + 1 : x < ox - 1)
					|| (y > oy ? y > oy + 1 : y < oy - 1)
					|| (x != ox && (side ? y == oy + 1 : y == oy - 1)))
				return false;
			break;
		case P_SILVER:
			if ((x > ox ? x > ox + 1 : x < ox - 1)
					|| (y > oy ? y > oy + 1 : y < oy - 1)
					|| (y == oy && x != ox)
					|| (x == ox && (side ? y == oy + 1 : y == oy - 1)))
				return false;
			break;
		case P_KNIGHT:
			if ((x > ox ? x != ox + 1 : x != ox - 1)
					|| (side ? y != oy - 2 : y != oy + 2))
				return false;
			break;
		case P_ROOK:
			if (x != ox && y != oy)
				return false;
			break;
		case P_BISHOP:
			if (Math.abs(x - ox) != Math.abs(y - oy))
				return false;
			break;
		case P_LANCE:
			if (x != ox || (side ? y > oy : y < oy))
				return false;
			break;
		case P_PRO_BISHOP:
			if ((x > ox ? x > ox + 1 : x < ox - 1)
					|| (y > oy ? y > oy + 1 : y < oy - 1)) {
				if (Math.abs(x - ox) != Math.abs(y - oy))
					return false;
			}
			break;
		case P_PRO_ROOK:
			if ((x > ox ? x > ox + 1 : x < ox - 1)
					|| (y > oy ? y > oy + 1 : y < oy - 1)) {
				if (x != ox && y != oy)
					return false;
			}
			break;
		}

		return true;
	}

	public boolean hasPath(ShogiPiece board[][], int ox, int oy, int x, int y) {
		switch (piece) {
		case P_ROOK:
		case P_PRO_ROOK:
			if (x != ox) {
				for (int i = Math.min(x, ox) + 1; i < Math.max(x, ox); i++) {
					if (board[i][y] != null)
						return false;
				}
			}
			if (y != oy) {
				for (int i = Math.min(y, oy) + 1; i < Math.max(y, oy); i++) {
					if (board[x][i] != null)
						return false;
				}
			}
			break;
		case P_BISHOP:
		case P_PRO_BISHOP:
			/* Todo: make less ugly */
			if (x > ox) {
				if (y > oy) {
					for (int i = ox + 1, j = oy + 1; i < x; i++, j++) {
						if (board[i][j] != null)
							return false;
					}
				} else {
					for (int i = ox + 1, j = oy - 1; i < x; i++, j--) {
						if (board[i][j] != null)
							return false;
					}
				}
			} else {
				if (y > oy) {
					for (int i = ox - 1, j = oy + 1; i > x; i--, j++) {
						if (board[i][j] != null)
							return false;
					}
				} else {
					for (int i = ox - 1, j = oy - 1; i > x; i--, j--) {
						if (board[i][j] != null)
							return false;
					}
				}
			}
			break;
		case P_LANCE:
			for (int i = Math.min(y, oy) + 1; i < Math.max(y, oy); i++) {
				if (board[x][i] != null)
					return false;
			}
			break;
		}

		return true;
	}
}
