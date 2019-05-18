
package cn.wang.chinesechess.print.part;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.ColorUtil;
import cn.wang.chinesechess.core.ChessBoard;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ChessRule;
import cn.wang.chinesechess.core.ManualItem;
import cn.wang.chinesechess.core.MoveStep;

/**
 * 棋盘类
 * 
 * @author wanghualiang
 */
public class PrintPartBoard extends ChessBoard {

	private static final long serialVersionUID = 1L;
	
	public JList manualList;

	public PrintPartGUI boardOwner;// 打谱时，棋盘的拥有者
	/**boolean[][] temp[i][j]=true意为棋盘[i][j]位置上的棋子是临时的*/
	private boolean[][] temp=new boolean[11][11];
	/** 闪烁的棋子所在的位置*/
	private boolean winkPieceAtBoard = false;

	public PrintPartBoard(PrintPartGUI owner) {
		super();
		boardOwner = owner;
		temp = owner.temp;
		playerName = RED_NAME;// 初始化时，红方
		manualList = chessManual.manualList;

	}

	/**
	 * 
	 * 鼠标适配器
	 */
	private class PartialMouseAdapter extends MouseAdapter {
		PrintPartBoard board;

		public PartialMouseAdapter(PrintPartBoard board) {
			this.board = board;
		}

		/**
		 * 鼠标移动到棋子上面
		 */

		public void mouseEntered(MouseEvent e) {
			if (e.getSource() instanceof ChessPiece) {
				ChessPiece piece = (ChessPiece) e.getSource();
				Point point = isPieceAtBoard(piece);

				if (boardOwner.isLock) {
					// 棋局已经锁定,棋盘中的本方棋子手形鼠标
					if (point != null && piece.getName().equals(playerName)) {
						piece.setCursor(handCursor);
					} else {
						piece.setCursor(defaultCursor);
					}

				} else {
					// 棋局没有锁定,所有棋子都可以选中,手形鼠标
					piece.setCursor(handCursor);
				}
			}
		}

		public void mousePressed(MouseEvent e) {
			if(!boardOwner.canPaly){
				return;
			}
			boardOwner.last();
			if (!isSelected) {
				if(!canPaly){
					return;
				}
				ChessPiece piece = null;
				if (e.getSource() == board) {// 第一次点击了棋盘，忽略不计
					isSelected = false;
				}
				if (e.getSource() instanceof ChessPiece) {// 第一次点击了棋子
					piece = (ChessPiece) e.getSource();
					winkPieceAtBoard = false;// 判断棋子是在棋盘中还是备用棋子面板中
					Point point = isPieceAtBoard(piece);
					if (point != null) {
						winkPieceAtBoard = true;
						startI = (int) point.getX();
						startJ = (int) point.getY();
					}
				
					if (winkPieceAtBoard) {//棋子在棋盘上
						if (!boardOwner.isLock) {//棋盘还未锁定
							if(temp[startI][startJ]){//该棋子为临时棋子
								isSelected = true;
								ChessUtil.playSound("select.wav");
								winkPiece = piece;needWink = true;
							}
						} else {//棋盘已锁定
							if (piece.getName().equals(playerName)) {
								isSelected = true;
								ChessUtil.playSound("select.wav");
								winkPiece = piece;needWink = true;
								board.initTipPoints(piece, startI, startJ);
							} 
						}
					} 
					else {//棋子在备用棋盒中
						if (!boardOwner.isLock) {//棋盘还未锁定
							startI = 0;startJ = 0;
							isSelected = true;ChessUtil.playSound("select.wav");
							winkPiece = piece;needWink = true;	
							winkPieceAtBoard = false;
						} 
					}
				}
			}
			else {// 第二次点击
				boolean canMove = true;
				ChessPiece pieceRemoved = null;
				int endI = 0, endJ = 0;
				if (e.getSource() == board) {// 第二次点击了棋盘
					double x1 = e.getX();
					double y1 = e.getY();
					for (int i = 1; i <= X; i++) {
						for (int j = 1; j <= Y; j++) {//点击的点坐标转化为棋子点坐标
							double x0 = chessPoints[i][j].getX();
							double y0 = chessPoints[i][j].getY();
							if ((Math.abs(x0 - x1) <= PIECE_WIDTH / 2)
									&& (Math.abs(y0 - y1) <= PIECE_HEIGHT / 2)) {
								endI = i;endJ = j;// 终点坐标
								break;
							}
						}
					}
					if (endI == 0 || endJ == 0) {// 没有点击在棋盘有效范围内，删除棋子
						if (!boardOwner.isLock) {
							if(temp[startI][startJ]){
								board.remove(winkPiece);
								boardOwner.piecesPanel.add(winkPiece);
								needWink = false;
								winkPiece.setVisible(true);
								isSelected = false;
								temp[startI][startJ] = false;//将临时棋子标识去除
								if (winkPieceAtBoard) {
									chessPoints[startI][startJ].setHasPiece(false);
								}
							}
						}
					}
					
					else {// 第二次点击在棋盘有效范围内
						if(!boardOwner.isLock){//棋盘未锁定
							if(temp[startI][startJ]){//之前点击的棋子为临时棋子
								canMove = ChessRule.partialRule1(winkPiece, startI,
										startJ, endI, endJ, chessPoints);
								if (canMove) {//如果满足放置规则，将棋盒中的棋子放置在棋盘中
									temp[startI][startJ] = false;//将临时棋子的位置改变
									temp[endI][endJ] = true;
									winkPiece.setPosition(new Point(endI, endJ));
									movePiece(winkPiece, startI, startJ, endI, endJ);
									needWink = false;winkPiece.setVisible(true);
									isSelected = false;ChessUtil.playSound("eat.wav");
								}
							}else{//之前点击的棋子不为临时棋子
								if(!winkPieceAtBoard){//之前点击的棋子不在在棋盘上
									canMove = ChessRule.partialRule1(winkPiece, startI,
											startJ, endI, endJ, chessPoints);
									if (canMove) {//如果满足放置规则，将棋盒中的棋子放置在棋盘中
										winkPiece.setPosition(new Point(endI, endJ));
										chessPoints[endI][endJ].setPiece(winkPiece, board);
										needWink = false;isSelected = false;
										ChessUtil.playSound("eat.wav");
										boardOwner.piecesPanel.remove(winkPiece);
										boardOwner.piecesPanel.validate();
										winkPiece.setVisible(true);
										temp[endI][endJ]=true;//将之前点击的棋子标记为临时棋子
									}
								}
							}
							
						}else{//棋盘已经上锁
							if(winkPieceAtBoard){//之前点击的棋子在棋盘上
								canMove = ChessRule.partialRule(winkPiece, startI,
										startJ, endI, endJ, chessPoints, null);
								if(canMove){
									board.addChessRecord(winkPiece, startI,startJ, endI, endJ);
									movePiece(winkPiece, startI, startJ, endI,endJ);
									chessPoints[startI][startJ].setHasPiece(false);
									chessPoints[endI][endJ].setPiece(winkPiece, board);
									needWink = false;winkPiece.setVisible(true);
									isSelected = false;ChessUtil.playSound("eat.wav");
									boardOwner.curIndex++;changeSide();
								}
							}	
						}
					}
				}

				
				else if (e.getSource() instanceof ChessPiece) {// 第二次点击了棋子
					ChessPiece piece = (ChessPiece) e.getSource();
					boolean flag = false;// 第二次点击的对方棋子是否在棋盘中
					for (int i = 1; i <= X; i++) {
						for (int j = 1; j <= Y; j++) {
							ChessPiece tempPiece = chessPoints[i][j].getPiece();
							if (chessPoints[i][j].hasPiece()&& tempPiece.equals(piece)) {
								endI = i;endJ = j;flag = true;break;
							}
						}
					}
					if (!flag) {// 第2次点击了棋盒中的棋子
						if (!boardOwner.isLock) {
							startI = 0;startJ = 0;
							winkPiece.setVisible(true);
							ChessUtil.playSound("select.wav");
							winkPiece = piece;needWink = true;
							isSelected = true;winkPieceAtBoard = false;
						} 
					} else {//棋子在棋盘上
						if(boardOwner.isLock){
							// 第二次点击了棋盘中同一方的棋子，更换选中的棋子
							if (piece.getName().equals(winkPiece.getName())) {
								winkPiece.setVisible(true);winkPiece = piece;
								needWink = true;startI = endI;startJ = endJ;
								isSelected = true;winkPieceAtBoard = true;
								ChessUtil.playSound("select.wav");tipPoints.clear();
								board.initTipPoints(piece, startI, startJ);
							}else{//第二次点击了棋盘中不同方的棋子，吃掉选中的棋子
								canMove = ChessRule.partialRule(winkPiece, startI, startJ,
												endI, endJ, chessPoints, null);
								if(canMove){
									pieceRemoved = chessPoints[endI][endJ].getPiece();
									board.addChessRecord(winkPiece, startI,startJ, endI, endJ);
									movePiece(winkPiece, startI, startJ, endI,endJ);
									boardOwner.piecesPanel.add(pieceRemoved);//将被吃掉的棋子重新放回棋盒中
									needWink = false;winkPiece.setVisible(true);
									isSelected = false;ChessUtil.playSound("eat.wav");
									tipPoints.clear();boardOwner.curIndex++;changeSide();
									if(pieceRemoved.getCategory().equals(PieceCategory.JIANG)){
										canPaly = false;boardOwner.gameOver(canPaly,PieceCategory.JIANG);
									}else if(pieceRemoved.getCategory().equals(PieceCategory.SHUAI)){
										canPaly = false;boardOwner.gameOver(canPaly,PieceCategory.SHUAI);
									}	
								}	
							}
						}
						else{//棋盘未上锁
							if(temp[endI][endJ]){//点击的棋子为临时棋子
								//如果先前点击的棋子在棋盒中，防止因闪烁使得之前的棋子不可见
								winkPiece.setVisible(true);
								ChessUtil.playSound("select.wav");
								winkPiece = piece;
								needWink = true;
								isSelected = true;
								winkPieceAtBoard = true;
							}
						}
					}
				}
			}
			validateAll();
		}
	}

	/**
	 * 红黑方交替
	 * 
	 */
	public void changeSide() {
		if (playerName.equals(RED_NAME)) {
			playerName = BLACK_NAME;
		} else if (playerName.equals(BLACK_NAME)) {
			playerName = RED_NAME;
		}
	}

	public void validateAll() {
		System.out.println("刷新！");
		validate();
		repaint();
		boardOwner.validate();
	}

	/**
	 * 判断一个棋子是否在棋盘中,
	 * 
	 * @param piece
	 *            棋子
	 * @return 如果在返回棋子所在的坐标，否则返回null
	 */
	private Point isPieceAtBoard(ChessPiece piece) {
		return getPiecePoint(piece);
	}

	@Override
	protected Color getBackgroundColor() {
		return ColorUtil.getDefaultBgcolor();
	}

	@Override
	protected MouseAdapter getMouseAdapter() {
		return new PartialMouseAdapter(this);
	}

	@Override
	protected BoardType getBoardType() {
		return BoardType.printPartial;
	}

}
