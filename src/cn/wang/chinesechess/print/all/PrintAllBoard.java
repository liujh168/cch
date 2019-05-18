
package cn.wang.chinesechess.print.all;

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


/**
 * 全局打谱类专用的棋盘
 * @author wanghualiang
 *
 */
public class PrintAllBoard extends ChessBoard {

	private static final long serialVersionUID = 1L;

	/**private PrintAllGUI 打谱时，棋盘的拥有者*/
	private PrintAllGUI boardOwner;

	public PrintAllBoard(PrintAllGUI boardOwner) {
		this.boardOwner = boardOwner;
		// manualList = chessManual.manualList;

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
	 * 全局打谱用的鼠标适配器
	 * 
	 * @author 雷文
	 * 
	 */
	private class PrintAllMouseAdapter extends MouseAdapter {
		PrintAllBoard board;

		public PrintAllMouseAdapter(PrintAllBoard board) {
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
			if(!boardOwner.canPaly){//游戏结束
				return;
			}
			// 玩家点击棋子时，当然是为了移动棋子
			boardOwner.last();

			// 第一次选中棋子
			if (!isSelected) {
				ChessPiece piece = null;
				Rectangle rect = null;
				
				// 第一次点击了棋盘，忽略不计
				if (e.getSource() == this) {
					isSelected = false;
					return;
				}

				// 第一次点击了棋子
				if (e.getSource() instanceof ChessPiece) {
					piece = (ChessPiece) e.getSource();
					// 第一次点击下棋方的棋子
					if (piece.getName().equals(playerName)) {
						System.out.println("第一次选中下棋方的棋子 284");
						isSelected = true;
						ChessUtil.playSound("select.wav");
						winkPiece = piece;
						needWink = true;
						Point p = getPiecePoint(piece);
						//得到棋子坐标
						startI = p.x;
						startJ = p.y;
						//清除之前可能留下的提示
						board.clearTipPoints();
						//可走提示
						board.initTipPoints(piece, startI, startJ);
					}else{
						return;
					}
				}else{
					return;
				}
			}

			// 第二次点击
			else {
				boolean canMove = false;
//				// 修改图标为默认图标
//				setCursor(defaultCursor);
				int endI = 0, endJ = 0;
				// 第二次点击了棋盘
				if (e.getSource() == board) {
					// 绝对坐标转化为gamePoints[][]的坐标
					double x1 = e.getX();
					double y1 = e.getY();
					for (int i = 1; i <= X; i++) {
						for (int j = 1; j <= Y; j++) {
							double x0 = chessPoints[i][j].getX();
							double y0 = chessPoints[i][j].getY();

							if ((Math.abs(x0 - x1) <= PIECE_WIDTH / 2)
									&& (Math.abs(y0 - y1) <= PIECE_HEIGHT / 2)) {
								// 终点坐标
								endI = i;
								endJ = j;
								break;
							}
						}
					}
					System.out.println("第2次点击棋盘时,终点坐标为：(" + endI + "," + endJ
							+ ") 330");

					if (endI == 0 || endJ == 0) {
						// 第2次点击时,没有点击到棋盘范围内
						return;
					}else{//第2次点击时,点击到棋盘范围内
						canMove = ChessRule.allRule(winkPiece, startI, startJ, endI,
								endJ, chessPoints, null);
						if(canMove){
							System.out.println("移动棋子 339");
							//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
							board.addChessRecord(winkPiece, startI,
									startJ, endI, endJ);
							// 棋子在棋盘中移动
							movePiece(winkPiece, startI, startJ, endI,
									endJ);
							/*//在棋盘上，初始位置去除棋子
							chessPoints[startI][startJ].setHasPiece(false);
							//目标位置加载棋子
							chessPoints[endI][endJ].setPiece(winkPiece, board);*/
							needWink = false;
							winkPiece.setVisible(true);
							isSelected = false;
							ChessUtil.playSound("eat.wav");
							// 修改当前索引
							boardOwner.curIndex++;
							changeSide();
						}else{
							System.out.println("第2次点击时,不符合棋子移动规则！358");
						}
					}
				}

				// 第二次点击了棋子
				else if (e.getSource() instanceof ChessPiece) {
					System.out.println("第二次点击了棋子 332");
					ChessPiece piece2 = (ChessPiece) e.getSource();
					Point point = getPiecePoint(piece2);
					endI = point.x;
					endJ = point.y;
//					Rectangle rect2 = piece2.getBounds();
//					for (int i = 1; i <= X; i++) {
//						for (int j = 1; j <= Y; j++) {
//							int x = chessPoints[i][j].getX();
//							int y = chessPoints[i][j].getY();
//							if (rect2.contains(x, y)) {
//								// 保存棋子的起始坐标
//								endI = i;
//								endJ = j;
//								break;
//							}
//						}
//					}

					// 第二次点击了本方棋子
					if (piece2.getName().equals(playerName)) {
						needWink = true;
						winkPiece.setVisible(true);
						winkPiece = piece2;
						startI = endI;
						startJ = endJ;
						isSelected = true;
						ChessUtil.playSound("select.wav");
						// 清除上次的提示标记
						board.clearTipPoints();
						board.initTipPoints(piece2, startI, startJ);
					}else{//第二次点击了不同方的棋子
						canMove = ChessRule.allRule(winkPiece, startI, startJ, endI,
								endJ, chessPoints, null);
						if (canMove) {
							ChessPiece pieceRemoved = chessPoints[endI][endJ].getPiece();
							//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
							board.addChessRecord(winkPiece, startI,
									startJ, endI, endJ);
							// 棋子在棋盘中移动
							movePiece(winkPiece, startI, startJ, endI,
									endJ);
							needWink = false;
							winkPiece.setVisible(true);
							isSelected = false;
							// 播放棋子被吃声音
							ChessUtil.playSound("eat.wav");
							// 打谱类当前索引+1
							boardOwner.curIndex++;
							changeSide();
							if(pieceRemoved.getCategory().equals(PieceCategory.JIANG)){
								canPaly = false;
								boardOwner.gameOver(canPaly,PieceCategory.JIANG);
							}else if(pieceRemoved.getCategory().equals(PieceCategory.SHUAI)){
								canPaly = false;
								boardOwner.gameOver(canPaly,PieceCategory.SHUAI);
							}
							
						} else {
							return;
						}
					}
					
				}
			}
			//setCursor(defaultCursor);
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
	protected Color getBackgroundColor() {
		return ColorUtil.getPrintWholeBgcolor();
	}

	@Override
	protected MouseAdapter getMouseAdapter() {
		return new PrintAllMouseAdapter(this);
	}

	@Override
	protected BoardType getBoardType() {
		return BoardType.printWhole;
	}

}
