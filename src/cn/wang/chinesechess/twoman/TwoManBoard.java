package cn.wang.chinesechess.twoman;


import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.ColorUtil;
import cn.wang.chinesechess.core.ChessBoard;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ChessRule;
import cn.wang.chinesechess.print.all.PrintAllBoard;
import cn.wang.chinesechess.print.all.PrintAllGUI;

/**
 * 双人对战棋盘
 * @author wanghualiang
 *
 */
public class TwoManBoard extends ChessBoard{
	private static final long serialVersionUID = 1L;
	/**private TwoManGUI 棋盘拥有者*/
	private TwoManGUI boardOwner;
	public TwoManBoard(TwoManGUI boardOwner) {
		this.boardOwner = boardOwner;
	}
	/**
	 * 初始化红方、蓝方棋子的宽度，高度和前景色
	 * 默认红方先走棋
	 * 
	 */
	public void initChess() {
		playerName = RED_NAME;
		chessPoints[1][10].setPiece(红車1, this);
		红車1.setPosition(new Point(1, 10));
		chessPoints[2][10].setPiece(红馬1, this);
		红馬1.setPosition(new Point(2, 10));
		chessPoints[3][10].setPiece(红相1, this);
		红相1.setPosition(new Point(3, 10));
		chessPoints[4][10].setPiece(红仕1, this);
		红仕1.setPosition(new Point(4, 10));
		chessPoints[5][10].setPiece(红帥, this);
		红帥.setPosition(new Point(5, 10));
		chessPoints[6][10].setPiece(红仕2, this);
		红仕2.setPosition(new Point(6, 10));
		chessPoints[7][10].setPiece(红相2, this);
		红相2.setPosition(new Point(7, 10));
		chessPoints[8][10].setPiece(红馬2, this);
		红馬2.setPosition(new Point(8, 10));
		chessPoints[9][10].setPiece(红車2, this);
		红車2.setPosition(new Point(9, 10));
		chessPoints[2][8].setPiece(红炮1, this);
		红炮1.setPosition(new Point(2, 8));
		chessPoints[8][8].setPiece(红炮2, this);
		红炮2.setPosition(new Point(8, 8));
		chessPoints[1][7].setPiece(红兵1, this);
		红兵1.setPosition(new Point(1, 7));
		chessPoints[3][7].setPiece(红兵2, this);
		红兵2.setPosition(new Point(3, 7));
		chessPoints[5][7].setPiece(红兵3, this);
		红兵3.setPosition(new Point(5, 7));
		chessPoints[7][7].setPiece(红兵4, this);
		红兵4.setPosition(new Point(7, 7));
		chessPoints[9][7].setPiece(红兵5, this);
		红兵5.setPosition(new Point(9, 7));

		chessPoints[1][1].setPiece(黑車1, this);
		黑車1.setPosition(new Point(1, 1));
		chessPoints[2][1].setPiece(黑馬1, this);
		黑馬1.setPosition(new Point(2, 1));
		chessPoints[3][1].setPiece(黑象1, this);
		黑象1.setPosition(new Point(3, 1));
		chessPoints[4][1].setPiece(黑士1, this);
		黑士1.setPosition(new Point(4, 1));
		chessPoints[5][1].setPiece(黑將, this);
		黑將.setPosition(new Point(5, 1));
		chessPoints[6][1].setPiece(黑士2, this);
		黑士2.setPosition(new Point(6, 1));
		chessPoints[7][1].setPiece(黑象2, this);
		黑象2.setPosition(new Point(7, 1));
		chessPoints[8][1].setPiece(黑馬2, this);
		黑馬2.setPosition(new Point(8, 1));
		chessPoints[9][1].setPiece(黑車2, this);
		黑車2.setPosition(new Point(9, 1));
		chessPoints[2][3].setPiece(黑炮1, this);
		黑炮1.setPosition(new Point(2, 3));
		chessPoints[8][3].setPiece(黑炮2, this);
		黑炮2.setPosition(new Point(8, 3));
		chessPoints[1][4].setPiece(黑卒1, this);
		黑卒1.setPosition(new Point(1, 4));
		chessPoints[3][4].setPiece(黑卒2, this);
		黑卒2.setPosition(new Point(3, 4));
		chessPoints[5][4].setPiece(黑卒3, this);
		黑卒3.setPosition(new Point(5, 4));
		chessPoints[7][4].setPiece(黑卒4, this);
		黑卒4.setPosition(new Point(7, 4));
		chessPoints[9][4].setPiece(黑卒5, this);
		黑卒5.setPosition(new Point(9, 4));
	}
	/**
	 * 红黑方交替
	 * 
	 */
	public void changeSide() {
		if (playerName.equals(RED_NAME)) {
			playerName = BLACK_NAME;
			boardOwner.updateGameStatus(2, "轮到黑方走喽！");

		} else {
			playerName = RED_NAME;
			boardOwner.updateGameStatus(1, "轮到红方走喽！");
		}
	}
	/**
	 * 双人对战的鼠标适配器
	 * 
	 * @author 雷文
	 * 
	 */
	private class TwoManBoardMouseAdapter extends MouseAdapter {
		TwoManBoard board;

		public TwoManBoardMouseAdapter(TwoManBoard board) {
			this.board = board;
		}

		/**
		 * 鼠标移动到棋盘或棋子上面
		 */
		public void mouseEntered(MouseEvent e) {
			ChessPiece piece = null;
			// 如果鼠标移动到己方棋子上面,就设置成手形光标；
			// 否则，设置成默认光标
			if (e.getSource() instanceof ChessPiece) {
				piece = (ChessPiece) e.getSource();
				if (piece.getName().equals(playerName)) {
					piece.setCursor(handCursor);
				} else {
					piece.setCursor(defaultCursor);
				}
			}
		}
		
		/**
		 * 按下鼠标
		 */
		public void mousePressed(MouseEvent e) {
			if(!boardOwner.canPaly){return;}//游戏结束
			boardOwner.last();// 将棋谱列表框移动到最后一条棋谱项
			if (!isSelected) {//目前选中了棋子，即为第一次点击
				ChessPiece piece = null;
				if (e.getSource() == this) {// 第一次点击了棋盘，忽略不计
					isSelected = false;return;
				}
				if (e.getSource() instanceof ChessPiece) {// 第一次点击了棋子
					piece = (ChessPiece) e.getSource();
					if (piece.getName().equals(playerName)) {// 第一次点击下棋方的棋子
						isSelected = true;ChessUtil.playSound("select.wav");
						winkPiece = piece;needWink = true;
						Point p = getPiecePoint(piece);
						startI = p.x;startJ = p.y;
						board.initTipPoints(piece, startI, startJ);
					}
				}
			}
			else {// 目前选中棋子，此次即为第二次点击
				boolean canMove = false;int endI = 0, endJ = 0;
				if (e.getSource() == board) {// 第二次点击了棋盘
					double x1 = e.getX();double y1 = e.getY();
					for (int i = 1; i <= X; i++) {
						for (int j = 1; j <= Y; j++) {
							double x0 = chessPoints[i][j].getX();
							double y0 = chessPoints[i][j].getY();
							if ((Math.abs(x0 - x1) <= PIECE_WIDTH / 2)
									&& (Math.abs(y0 - y1) <= PIECE_HEIGHT / 2)) {
								endI = i;endJ = j;// 终点坐标
								break;
							}
						}
					}
					if (endI == 0 || endJ == 0) {// 第2次点击时,没有点击到棋盘范围内
						return;
					}else{//第2次点击时,点击到棋盘范围内
						canMove = ChessRule.allRule(winkPiece, startI, startJ, endI,
								endJ, chessPoints, null);
						if(canMove){
							board.addChessRecord(winkPiece, startI,
									startJ, endI, endJ);//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
							movePiece(winkPiece, startI, startJ, endI,
									endJ);// 棋子在棋盘中移动
							chessPoints[startI][startJ].setHasPiece(false);//在棋盘上，初始位置去除棋子
							chessPoints[endI][endJ].setPiece(winkPiece, board);//目标位置加载棋子
							needWink = false;winkPiece.setVisible(true);isSelected = false;
							boardOwner.curIndex++;// 修改当前索引
							ChessUtil.playSound("eat.wav");changeSide();
						}
					}
				}
				else if (e.getSource() instanceof ChessPiece) {// 第二次点击了棋子
					ChessPiece piece2 = (ChessPiece) e.getSource();
					Point point = getPiecePoint(piece2);
					endI = point.x;endJ = point.y;
					if (piece2.getName().equals(playerName)) {// 第二次点击了本方棋子
						needWink = true;winkPiece.setVisible(true);winkPiece = piece2;
						startI = endI;startJ = endJ;isSelected = true;
						ChessUtil.playSound("select.wav");
						board.clearTipPoints();board.initTipPoints(piece2, startI, startJ);
					}else{//第二次点击了不同方的棋子
						canMove = ChessRule.allRule(winkPiece, startI, startJ, endI,
								endJ, chessPoints, null);
						if (canMove) {
							ChessPiece pieceRemoved = chessPoints[endI][endJ].getPiece();
							board.addChessRecord(winkPiece, startI,
									startJ, endI, endJ);
							movePiece(winkPiece, startI, startJ, endI,
									endJ);
							needWink = false;winkPiece.setVisible(true);isSelected = false;
							ChessUtil.playSound("eat.wav");boardOwner.curIndex++;changeSide();
							if(pieceRemoved.getCategory().equals(PieceCategory.JIANG)){
								canPaly = false;boardOwner.gameOver(canPaly,PieceCategory.JIANG);
							}else if(pieceRemoved.getCategory().equals(PieceCategory.SHUAI)){
								ChessUtil.playSound("jiangjun2.wav");canPaly = false;
								boardOwner.gameOver(canPaly,PieceCategory.SHUAI);
							}else{
								ChessUtil.playSound("eat.wav");
							}
						} 
					}	
				}
			}
			validateAll();
		}
	}
	public void validateAll() {
		System.out.println("刷新！");
		validate();
		repaint();
		boardOwner.validate();
	}
	

	@Override
	protected BoardType getBoardType() {
		return BoardType.twoman;
	}
	@Override
	protected MouseAdapter getMouseAdapter() {
		return new TwoManBoardMouseAdapter(this);
	}
	
}
