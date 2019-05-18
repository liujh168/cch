package cn.wang.chinesechess.load;

import java.awt.Point;
import java.awt.event.MouseAdapter;

import cn.wang.chinesechess.core.ChessBoard;

public class LoadBoard extends ChessBoard{
	/**boolean ÊÇ·ñ¿ÉÒÔ×ßÆå */
	public boolean canPaly = false;
	/**String Õ½¶··½µÄÃû×Ö£¬ºì·½orºÚ·½*/
	protected String playerName;
	/**
	 * ÔÚÆåÅÌ³õÊ¼»¯Æå×ÓµÄÎ»ÖÃ
	 */
	public void initChess(String palyerFirst){
		if(palyerFirst == null || palyerFirst.equals(RED_NAME)){
			chessPoints[1][10].setPiece(ºìÜ‡1, this);
			ºìÜ‡1.setPosition(new Point(1, 10));
			chessPoints[2][10].setPiece(ºìñR1, this);
			ºìñR1.setPosition(new Point(2, 10));
			chessPoints[3][10].setPiece(ºìÏà1, this);
			ºìÏà1.setPosition(new Point(3, 10));
			chessPoints[4][10].setPiece(ºìÊË1, this);
			ºìÊË1.setPosition(new Point(4, 10));
			chessPoints[5][10].setPiece(ºì›, this);
			ºì›.setPosition(new Point(5, 10));
			chessPoints[6][10].setPiece(ºìÊË2, this);
			ºìÊË2.setPosition(new Point(6, 10));
			chessPoints[7][10].setPiece(ºìÏà2, this);
			ºìÏà2.setPosition(new Point(7, 10));
			chessPoints[8][10].setPiece(ºìñR2, this);
			ºìñR2.setPosition(new Point(8, 10));
			chessPoints[9][10].setPiece(ºìÜ‡2, this);
			ºìÜ‡2.setPosition(new Point(9, 10));
			chessPoints[2][8].setPiece(ºìÅÚ1, this);
			ºìÅÚ1.setPosition(new Point(2, 8));
			chessPoints[8][8].setPiece(ºìÅÚ2, this);
			ºìÅÚ2.setPosition(new Point(8, 8));
			chessPoints[1][7].setPiece(ºì±ø1, this);
			ºì±ø1.setPosition(new Point(1, 7));
			chessPoints[3][7].setPiece(ºì±ø2, this);
			ºì±ø2.setPosition(new Point(3, 7));
			chessPoints[5][7].setPiece(ºì±ø3, this);
			ºì±ø3.setPosition(new Point(5, 7));
			chessPoints[7][7].setPiece(ºì±ø4, this);
			ºì±ø4.setPosition(new Point(7, 7));
			chessPoints[9][7].setPiece(ºì±ø5, this);
			ºì±ø5.setPosition(new Point(9, 7));

			chessPoints[1][1].setPiece(ºÚÜ‡1, this);
			ºÚÜ‡1.setPosition(new Point(1, 1));
			chessPoints[2][1].setPiece(ºÚñR1, this);
			ºÚñR1.setPosition(new Point(2, 1));
			chessPoints[3][1].setPiece(ºÚÏó1, this);
			ºÚÏó1.setPosition(new Point(3, 1));
			chessPoints[4][1].setPiece(ºÚÊ¿1, this);
			ºÚÊ¿1.setPosition(new Point(4, 1));
			chessPoints[5][1].setPiece(ºÚŒ¢, this);
			ºÚŒ¢.setPosition(new Point(5, 1));
			chessPoints[6][1].setPiece(ºÚÊ¿2, this);
			ºÚÊ¿2.setPosition(new Point(6, 1));
			chessPoints[7][1].setPiece(ºÚÏó2, this);
			ºÚÏó2.setPosition(new Point(7, 1));
			chessPoints[8][1].setPiece(ºÚñR2, this);
			ºÚñR2.setPosition(new Point(8, 1));
			chessPoints[9][1].setPiece(ºÚÜ‡2, this);
			ºÚÜ‡2.setPosition(new Point(9, 1));
			chessPoints[2][3].setPiece(ºÚÅÚ1, this);
			ºÚÅÚ1.setPosition(new Point(2, 3));
			chessPoints[8][3].setPiece(ºÚÅÚ2, this);
			ºÚÅÚ2.setPosition(new Point(8, 3));
			chessPoints[1][4].setPiece(ºÚ×ä1, this);
			ºÚ×ä1.setPosition(new Point(1, 4));
			chessPoints[3][4].setPiece(ºÚ×ä2, this);
			ºÚ×ä2.setPosition(new Point(3, 4));
			chessPoints[5][4].setPiece(ºÚ×ä3, this);
			ºÚ×ä3.setPosition(new Point(5, 4));
			chessPoints[7][4].setPiece(ºÚ×ä4, this);
			ºÚ×ä4.setPosition(new Point(7, 4));
			chessPoints[9][4].setPiece(ºÚ×ä5, this);
			ºÚ×ä5.setPosition(new Point(9, 4));

		}else{
			
			chessPoints[1][10].setPiece(ºÚÜ‡1, this);
			ºÚÜ‡1.setPosition(new Point(1, 10));
			chessPoints[2][10].setPiece(ºÚñR1, this);
			ºÚñR1.setPosition(new Point(2, 10));
			chessPoints[3][10].setPiece(ºÚÏó1, this);
			ºÚÏó1.setPosition(new Point(3, 10));
			chessPoints[4][10].setPiece(ºÚÊ¿1, this);
			ºÚÊ¿1.setPosition(new Point(4, 10));
			chessPoints[5][10].setPiece(ºÚŒ¢, this);
			ºÚŒ¢.setPosition(new Point(5, 10));
			chessPoints[6][10].setPiece(ºÚÊ¿2, this);
			ºÚÊ¿2.setPosition(new Point(6, 10));
			chessPoints[7][10].setPiece(ºÚÏó2, this);
			ºÚÏó2.setPosition(new Point(7, 10));
			chessPoints[8][10].setPiece(ºÚñR2, this);
			ºÚñR2.setPosition(new Point(8, 10));
			chessPoints[9][10].setPiece(ºÚÜ‡2, this);
			ºÚÜ‡2.setPosition(new Point(9, 10));
			chessPoints[2][8].setPiece(ºÚÅÚ1, this);
			ºÚÅÚ1.setPosition(new Point(2, 8));
			chessPoints[8][8].setPiece(ºÚÅÚ2, this);
			ºÚÅÚ2.setPosition(new Point(8, 8));
			chessPoints[1][7].setPiece(ºÚ×ä1, this);
			ºÚ×ä1.setPosition(new Point(1, 7));
			chessPoints[3][7].setPiece(ºÚ×ä2, this);
			ºÚ×ä2.setPosition(new Point(3, 7));
			chessPoints[5][7].setPiece(ºÚ×ä3, this);
			ºÚ×ä3.setPosition(new Point(5, 7));
			chessPoints[7][7].setPiece(ºÚ×ä4, this);
			ºÚ×ä4.setPosition(new Point(7, 7));
			chessPoints[9][7].setPiece(ºÚ×ä5, this);
			ºÚ×ä5.setPosition(new Point(9, 7));

			chessPoints[1][1].setPiece(ºìÜ‡1, this);
			ºìÜ‡1.setPosition(new Point(1, 1));
			chessPoints[2][1].setPiece(ºìñR1, this);
			ºìñR1.setPosition(new Point(2, 1));
			chessPoints[3][1].setPiece(ºìÏà1, this);
			ºìÏà1.setPosition(new Point(3, 1));
			chessPoints[4][1].setPiece(ºìÊË1, this);
			ºìÊË1.setPosition(new Point(4, 1));
			chessPoints[5][1].setPiece(ºì›, this);
			ºì›.setPosition(new Point(5, 1));
			chessPoints[6][1].setPiece(ºìÊË2, this);
			ºìÊË2.setPosition(new Point(6, 1));
			chessPoints[7][1].setPiece(ºìÏà2, this);
			ºìÏà2.setPosition(new Point(7, 1));
			chessPoints[8][1].setPiece(ºìñR2, this);
			ºìñR2.setPosition(new Point(8, 1));
			chessPoints[9][1].setPiece(ºìÜ‡2, this);
			ºìÜ‡2.setPosition(new Point(9, 1));
			chessPoints[2][3].setPiece(ºìÅÚ1, this);
			ºìÅÚ1.setPosition(new Point(2, 3));
			chessPoints[8][3].setPiece(ºìÅÚ2, this);
			ºìÅÚ2.setPosition(new Point(8, 3));
			chessPoints[1][4].setPiece(ºì±ø1, this);
			ºì±ø1.setPosition(new Point(1, 4));
			chessPoints[3][4].setPiece(ºì±ø2, this);
			ºì±ø2.setPosition(new Point(3, 4));
			chessPoints[5][4].setPiece(ºì±ø3, this);
			ºì±ø3.setPosition(new Point(5, 4));
			chessPoints[7][4].setPiece(ºì±ø4, this);
			ºì±ø4.setPosition(new Point(7, 4));
			chessPoints[9][4].setPiece(ºì±ø5, this);
			ºì±ø5.setPosition(new Point(9, 4));
		}
		
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	protected BoardType getBoardType() {
		// TODO Auto-generated method stub
		return BoardType.printWhole;
	}

	@Override
	protected MouseAdapter getMouseAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

}
