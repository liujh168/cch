
package cn.wang.chinesechess.core;

import java.awt.Point;

import cn.wang.chinesechess.config.NAME;

/**
 * 棋子点，将棋子和棋盘关联起来
 * 
 * @author wanghualiang
 */
public class ChessPoint implements Cloneable,NAME{

	public static final long serialVersionUID = 261L;

	private int x, y;// 棋子点的坐标

	private ChessPiece piece = null;// 棋子的引用

	public ChessPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public ChessPoint(int x, int y, ChessPiece piece, ChessBoard board) {
		this(x, y);
		setPiece(piece, board);
	}

	public ChessPoint() {
	}

	public boolean hasPiece() {
		return piece != null;
	}
	/**
	 *设置该棋子点的棋子对象是否有效。hasPiece 为false时，将棋子对象指向null
	 * @param hasPiece
	 */
	public void setHasPiece(boolean hasPiece) {
		// 很重要
		if (!hasPiece) {
			piece = null;
		}
	}

	public Point getPoint() {
		return new Point(x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * 
	 * @param piece
	 *            被删除的棋子
	 * @param board
	 *            棋盘接口
	 */
	public void setPiece(ChessPiece piece, ChessBoard board) {
		if (piece == null) {
			return;
		}
		if (board == null) {
			return;
		}
		this.piece = piece;
		board.addPiece(piece);
		//指定棋子在棋盘上的位置和大小
		piece.setBounds(x - UNIT_WIDTH / 2, y
				- UNIT_HEIGHT / 2, UNIT_WIDTH,
				UNIT_HEIGHT);
	}

	// 不更新棋盘界面
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}

	/**
	 * 删除棋子
	 * 
	 * @param piece
	 *            被删除的棋子
	 * @param board
	 *            棋盘接口
	 */
	public void removePiece(ChessPiece piece, ChessBoard board) {
		if (piece == null) {
			return;
		}
		if (board == null) {
			return;
		}
		board.removePiece(piece);
		piece = null;
	}

	public ChessPiece getPiece() {
		return piece;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object obj = super.clone();
		ChessPoint cp = (ChessPoint)obj;
		if(this.piece!=null){
			cp.piece = (ChessPiece) this.piece.clone();
		}
		return cp;
	}
}
