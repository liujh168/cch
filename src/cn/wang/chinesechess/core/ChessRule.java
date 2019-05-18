
package cn.wang.chinesechess.core;

import java.awt.Point;

import cn.wang.chinesechess.config.NAME;

/**
 * 游戏规则工具类
 * 
 * @author wanghualiang
 */
public final class ChessRule implements NAME {

	private ChessRule() {

	}
	/**
	 * 打全谱时用的棋子移动规则
	 * 
	 * @param piece
	 *            将要移动的棋子
	 * @param startX
	 *            数学意义上的 起点横坐标
	 * @param startY
	 *            数学意义上的 起点纵坐标
	 * @param endX
	 *            数学意义上的 终点横坐标
	 * @param endY
	 *            数学意义上的 终点纵坐标
	 * @param chessPoints
	 *            棋子所在棋盘的棋子点
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean allRule(ChessPiece piece, int startX, int startY,
			int endX, int endY, ChessPoint[][] chessPoints, String palyFirst) {
		return partialRule(piece, startX, startY, endX, endY, chessPoints, palyFirst);
	}

	/**
	 * 高级打谱时用的棋子移动规则
	 * 
	 * @param piece
	 *            将要移动的棋子
	 * @param startX
	 *            数学意义上的 起点横坐标
	 * @param startY
	 *            数学意义上的 起点纵坐标
	 * @param endX
	 *            数学意义上的 终点横坐标
	 * @param endY
	 *            数学意义上的 终点纵坐标
	 * @param chessPoints
	 *            棋子所在棋盘的棋子点
	 * @param palyFirst
	 * 			走棋先手方，玩家或电脑
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean partialRule(ChessPiece piece, int startX, int startY,
			int endX, int endY, ChessPoint[][] chessPoints, String palyFirst) {

		// 起点和终点为同一方棋子，不能移动
		if (isSameColor(chessPoints, startX, startY, endX, endY)) {
			return false;
		}
		// 是否可以移动
		boolean canMove = false;

		// 棋子的类别
		PieceCategory category = piece.getCategory();
		// 重用車、馬、炮的移动规则
		if (category.equals(PieceCategory.JU)
				|| category.equals(PieceCategory.MA)
				|| category.equals(PieceCategory.PAO)) {
			if(jmpRule(category, startX, startY, endX, endY, chessPoints)){
				canMove = true;
			}
			//System.out.println("移动的是車、馬、炮 335");
		}

		// 象的规则： 象飞田，横坐标和纵坐标得变化绝对值必须是2
		else if (category.equals(PieceCategory.HEIXIANG)
				|| category.equals(PieceCategory.HONGXIANG)) {
			//System.out.println("移动的是象 341");
			int centerI = (startX + endX) / 2;
			int centerJ = (startY + endY) / 2;
			int xAxle = Math.abs(startX - endX);
			int yAxle = Math.abs(startY - endY);

			if (xAxle == 2 && yAxle == 2) {
				// 卡象眼
				if (!chessPoints[centerI][centerJ].hasPiece()) {
					if((startY<=5&&endY<=5)||(startY>=6&&endY>=6)){
						canMove = true;
					}
				}
			} 
		}

		// 兵的移动规则：
		else if (category.equals(PieceCategory.BING)) {
			boolean flag;
			boolean flag2;
			if(palyFirst == null || palyFirst.equals(PALYER_FIRST)){
				flag = endY >= 6? true: false;
				flag2 = startY - endY == 1? true: false;
			}else{
				flag = endY <= 5? true: false;
				flag2 = endY - startY == 1? true: false;
			}
			int xAxle = Math.abs(startX - endX);
			// 兵没有过河 只能前进
			if (flag) {
				if (flag2 && xAxle == 0) {
					canMove = true;
				}
			}
			// 兵过河了，可以前进和平移
			else {
				if (flag2 && (xAxle == 0)) {
					canMove = true;
				} else if ((startY - endY == 0) && (xAxle == 1)) {
					canMove = true;
				} 
			}
			
//			System.out.println("移动的是兵 341");
		}
		//卒的移动规则
		else if(category.equals(PieceCategory.ZU)){
			boolean flag;
			boolean flag2;
			if(palyFirst == null || palyFirst.equals(PALYER_FIRST)){
				flag = endY <= 5? true: false;
				flag2 = endY - startY == 1? true: false;
			}else{
				flag = endY >= 6? true: false;
				flag2 = startY - endY == 1? true: false;
			}
			int xAxle = Math.abs(startX - endX);
			
			// 卒没有过河，只能前进
			if(flag){
				if(flag2 && (xAxle == 0)){
					canMove = true;
				}
			}
			// 卒过河了，可以前进和平移
			else{
				if (flag2 && (xAxle == 0)) {
					canMove = true;
				}else if ((endY - startY == 0) && (xAxle == 1)) {
					canMove = true;
				} 
			}
//			System.out.println("移动的是兵 367");
		}
		/*
		 * 士的规则：在一定范围内，坐标限制如下
		 * 
		 * 士划斜
		 */
		else if (category.equals(PieceCategory.HEISHI)
				|| category.equals(PieceCategory.HONGSHI)) {
			int xAxle = Math.abs(startX - endX);
			int yAxle = Math.abs(startY - endY);
			if(xAxle == 1 && yAxle == 1){
				if(endX>=4&&endX<=6){
					if((endY>=1&&endY<=3)||(endY>=8&&endY<=10)){
						canMove = true;
					}
				}
			}
			//System.out.println("移动的是士 394");
		}

		/*
		 * 帅和将的规则：在一定范围内，坐标限制如下
		 * 
		 * 4<=x<=6,8<=y<=10 一步一步地直走
		 */
		else if ((category.equals(PieceCategory.SHUAI))
				|| (category.equals(PieceCategory.JIANG))) {
			int xAxle = Math.abs(startX - endX);
			int yAxle = Math.abs(startY - endY);
			// 横坐标的限制条件
			if (endX <= 6 && endX >= 4) {
				if((endY>=1&&endY<=3)||(endY>=8&&endY<=10)){
					// 一次只能移动一步
					if ((xAxle == 1 && yAxle == 0) || (xAxle == 0 && yAxle == 1)){
						canMove = true;
					}
				}
			}
			//System.out.println("移动的是将帅  413");
		}
		
		//检测将帅是否对脸
		if(palyFirst == null || palyFirst.equals(PALYER_FIRST)){
			if(ChessRule.isMeeting(piece, startX, startY, endX, endY, chessPoints)){
				canMove = false; 
			}
		}else{
			if(ChessRule.isMeeting2(piece, startX, startY, endX, endY, chessPoints)){
				canMove = false; 
			}
		}
		
		return canMove;
	}
	/**
	 * 残局打谱棋盘未锁定时棋子放置的规则
	 * 1.帅（将）；红仕（黑士）；红相（黑象）；红兵（黑卒）放置的位置有要求
	 * 2.車、馬、炮可以随意放置在空闲的位置上
	 * @param piece 操作的棋子对象
	 * @param startX 初始位置横坐标
	 * @param startY 初始位置纵坐标
	 * @param endX 目标位置横坐标
	 * @param endY 目标位置纵坐标
	 * @param chessPoints 棋盘上已有的棋子集合
	 * @return
	 */
	public static boolean partialRule1(ChessPiece piece, int startX, int startY,
			int endX, int endY, ChessPoint[][] chessPoints) {
		// 起点和终点为同一方棋子，不能移动
				if (isSameColor(chessPoints, startX, startY, endX, endY)) {
					System.out.println("起点和终点为同一方棋子，不能移动 478");
					return false;
				}

				System.out.println("startX=" + startX + " startY= " + startY
						+ " endX= " + endX + " endY= " + endY);
				PieceCategory category = piece.getCategory();

				/*// 从备用棋子栏中选择棋子到棋盘中,只能将仕、相、帥放在合法的位置
				if (startX == 0 && startY == 0) {
*/
					// 仕、相、帥的终点位置有限制
					if (category.equals(PieceCategory.HONGSHI)) {
						if (endX == 4 || endX == 6) {
							if (endY == 8 || endY == 10) {
								return true;
							}
						} else if (endX == 5 && endY == 9) {
							return true;
						}
						return false;
					}

					else if (category.equals(PieceCategory.HEISHI)) {
						if (endX == 4 || endX == 6) {
							if (endY == 1 || endY == 3) {
								return true;
							}
						} else if (endX == 5 && endY == 2) {
							return true;
						}
						return false;
					}

					else if (category.equals(PieceCategory.HEIXIANG)) {
						if (endX == 1 || endX == 5 || endX == 9) {
							if (endY == 3) {
								return true;
							}
						} else if (endX == 3 || endX == 7) {
							if (endY == 1 || endY == 5) {
								return true;
							}
						}
						return false;
					}

					else if (category.equals(PieceCategory.HONGXIANG)) {
						if (endX == 1 || endX == 5 || endX == 9) {
							if (endY == 8) {
								return true;
							}
						} else if (endX == 3 || endX == 7) {
							if (endY == 6 || endY == 10) {
								return true;
							}
						}
						return false;
					}

					else if (category.equals(PieceCategory.JIANG)) {
						if (endX >= 4 && endX <= 6) {
							if (endY >= 1 && endY <= 3) {
								// 老将对脸
								if (ChessRule.isDuiLian(piece, endX, endY, chessPoints)) {
									return false;
								}
								return true;
							}
						}
						return false;
					}

					else if (category.equals(PieceCategory.SHUAI)) {
						if (endX >= 4 && endX <= 6) {
							if (endY >= 8 && endY <= 10) {
								// 老将对脸
								if (ChessRule.isDuiLian(piece, endX, endY, chessPoints)) {
									return false;
								}
								return true;
							}
						}
						return false;
					}

					// 兵的终点位置有限制
					else if (category.equals(PieceCategory.BING)) {
						if (endY == 6 || endY == 7) {
							if (endX == 1 || endX == 3 || endX == 5 || endX == 7
									|| endX == 9) {
								return true;
							}
							return false;
						} else if (endY < 6) {
							return true;
						} else {
							return false;
						}

					}
					// 卒的终点位置有限制
					else if (category.equals(PieceCategory.ZU)) {
						if (endY == 4 || endY == 5) {
							if (endX == 1 || endX == 3 || endX == 5 || endX == 7
									|| endX == 9) {
								return true;
							}
							return false;
						} else if (endY > 5) {
							return true;
						} else {
							return false;
						}

					}

					else {
						// 車、馬、炮可以放在任何位置
						return true;
					}
				
	}
	/**
	 * 車、馬、炮的移动规则，棋盘中移动时通用的规则
	 * 
	 * 重用3次
	 * 
	 * @param startX
	 *            数学意义上的 起点横坐标
	 * @param startY
	 *            数学意义上的 起点纵坐标
	 * @param endX
	 *            数学意义上的 终点横坐标
	 * @param endY
	 *            数学意义上的 终点纵坐标
	 * @param chessPoints
	 *            棋子所在棋盘的棋子点
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean jmpRule(PieceCategory category, int startX,
			int startY, int endX, int endY, ChessPoint[][] chessPoints) {
		/*
		 * System.out.println("車馬炮是否将军：(" + startX + "," + startY + "),(" + endX
		 * + "," + endY + ")");
		 */
		int minX = Math.min(startX, endX);
		int maxX = Math.max(startX, endX);
		int minY = Math.min(startY, endY);
		int maxY = Math.max(startY, endY);

		// 車的规则：車走直
		if (category.equals(PieceCategory.JU)) {
			// 竖着走
			if (startX == endX) {
				int j = 0;
				for (j = minY + 1; j <= maxY - 1; j++) {
					// 中间不能有棋子
					if (chessPoints[startX][j].hasPiece()) {
						return false;
					}
				}
				if (j == maxY) {
					return true;
				}
			}
			// 横着走
			else if (startY == endY) {
				int i = 0;
				for (i = minX + 1; i <= maxX - 1; i++) {
					if (chessPoints[i][startY].hasPiece()) {
						return false;
					}
				}
				if (i == maxX) {
					return true;
				}
			} else {
				return false;
			}
		}

		// 马的规则：马踏日,别马腿有4种情况
		else if (category.equals(PieceCategory.MA)) {
			int xAxle = Math.abs(startX - endX);
			int yAxle = Math.abs(startY - endY);

			// X方向移动2步，Y方向移动1步
			if (xAxle == 2 && yAxle == 1) {
				if (endX > startX) {
					// 别马腿
					if (chessPoints[startX + 1][startY].hasPiece()) {
						return false;
					}
					return true;
				}
				if (endX < startX) {
					// 别马腿
					if (chessPoints[startX - 1][startY].hasPiece()) {
						return false;
					}
					return true;
				}

			}

			// X方向移动1步，Y方向移动2步
			else if (xAxle == 1 && yAxle == 2) {
				if (endY > startY) {
					// 别马腿
					if (chessPoints[startX][startY + 1].hasPiece()) {
						return false;
					}
					return true;
				}
				if (endY < startY) {
					// 别马腿
					if (chessPoints[startX][startY - 1].hasPiece()) {
						return false;
					}
					return true;
				}

			} else {
				return false;
			}
		}

		// 炮的规则：
		else if (category.equals(PieceCategory.PAO)) {
			// 如果吃子，中间只能隔着一个棋子
			int number = 0;
			// 垂直方向移动
			if (startX == endX) {
				int j = 0;
				for (j = minY + 1; j <= maxY - 1; j++) {
					if (chessPoints[startX][j].hasPiece()) {
						number++;
					}
				}

				// 中间没有棋子，不能吃子
				if (number == 0 && !chessPoints[endX][endY].hasPiece()) {
					return true;
				}

				// 中间有一个棋子，必须吃一个子
				else if (number == 1) {
					if (chessPoints[endX][endY].hasPiece()) {
						return true;
					}
				}

				// 中间多于一个棋子，不能移动
				else if (number > 1) {
					return false;
				}

			}

			// 水平方向移动
			else if (startY == endY) {
				int i = 0;
				for (i = minX + 1; i <= maxX - 1; i++) {
					if (chessPoints[i][startY].hasPiece()) {
						number++;
					}
				}

				if (number > 1) {
					return false;
				} else if (number == 1) {
					if (chessPoints[endX][endY].hasPiece()) {
						return true;
					}
				}

				else if (number == 0 && !chessPoints[endX][endY].hasPiece()) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;

	}



	/**
	 * 判断起点和终点处的2个棋子是否为同一方的
	 * 
	 * @param chessPoints
	 *            棋子点集
	 * @param startX
	 *            起点横坐标
	 * @param startY
	 *            起点纵坐标
	 * @param endX
	 *            终点横坐标
	 * @param endY
	 *            起点纵坐标
	 * @return 同一方则返回true，否则返回false。
	 */
	private static boolean isSameColor(ChessPoint[][] chessPoints, int startX,
			int startY, int endX, int endY) {

		if (startX < 1 || startX > 9 || startY < 1 || startY > 10) {
			return false;
		}

		if (endX < 1 || endX > 10 || endY < 1 || endY > 10) {
			return false;
		}
		
		 /* System.out.println("是否为同一方棋子:" + startX + "," + startY + "," + endX +
		  "," + endY+"  1051");*/
		ChessPiece startPiece = chessPoints[startX][startY].getPiece();
		ChessPiece endPiece = chessPoints[endX][endY].getPiece();

		if (startPiece != null && endPiece != null) {
			String startPlayerName = startPiece.getName();
			String endPlayerName = endPiece.getName();
			if (startPlayerName.equals(endPlayerName)) {
				/*
				 * System.out.println("是否为同一方棋子：" + startPiece.getCategory() +
				 * "" + startX + "," + startY + "," + endX + "," + endY);
				 */
				//System.out.println("是同一方棋子: 1063");
				return true;
			}
		}
		//System.out.println("不是同一方棋子: 1067");
		return false;
	}


	/**
	 * 在x固定的情况下，y1---y2之间有没有棋子，用于将帅对脸检测
	 * @param x 固定的横坐标
	 * @param y1 纵坐标
	 * @param y2 纵坐标
	 * @param chessPoints 棋子点二维数组
	 * @return 有棋子返回true ，否则返回false
	 */
	public static boolean sectionHavePiece(int x,int y1,int y2,ChessPoint[][] chessPoints){
		for(int i=y1;i<=y2;i++){
			if(chessPoints[x][i].getPiece()!=null){
				return true;
			}
		}
		return false;
	}
	/**
	 * 棋子走后判断将帅是否对脸 红方棋子在下半部分
	 * @param piece 移动的棋子
	 * @param startX 起始横坐标
	 * @param startY 起始纵坐标
	 * @param endX  终点横坐标
	 * @param endY 终点纵坐标
	 * @param chessPoints 棋子点二维数组
	 * @return 如果将帅对脸，返回true；否则，返回false。
	 */
	public static boolean isMeeting(ChessPiece piece,int startX ,int startY, int endX, int endY,
			ChessPoint[][] chessPoints){
		boolean canMove = true;
		PieceCategory pc = piece.getCategory();
		if(pc.equals(PieceCategory.JIANG)){//移动的棋子是将
			for(int j=8;j<=10;j++){
				if(chessPoints[endX][j].getPiece()!=null&&
						chessPoints[endX][j].getPiece().getCategory().equals(PieceCategory.SHUAI)){
					canMove = sectionHavePiece(endX,endY+1,j-1,chessPoints);
					//System.out.println("移动的棋子是将且与帅对脸了 1143 canMove="+canMove);
					break;
				}
			}
		}else if(pc.equals(PieceCategory.SHUAI)){//移动的棋子是帅
			for(int j=1;j<=3;j++){
				if(chessPoints[endX][j].getPiece()!=null&&
						chessPoints[endX][j].getPiece().getCategory().equals(PieceCategory.JIANG)){
					canMove = sectionHavePiece(endX,j+1,endY-1,chessPoints);
					//System.out.println("移动棋子是帅且与将对脸了 1151 canMove="+canMove);
					break;
				}
			}
		}else{//移动的棋子是其他棋子
			int JIANG_Y = 0;
			int SHUAI_Y = 0;
			//在x=startX的横坐标上，区间1---(startY-1)之间有没有将
			for(int j=1;j<startY;j++){
				if(chessPoints[startX][j].getPiece()!=null
						&&chessPoints[startX][j].getPiece().getCategory().equals(PieceCategory.JIANG)){
					JIANG_Y = j;
					//System.out.println("在x=startX的横坐标上，区间1---(startY-1)之间有将 1160 canMove="+canMove);
					break;
				}
			}
			//在x=startX的横坐标上，区间(startY+1)---10之间有没有帅
			for(int j=startY+1;j<=10;j++){
				if(chessPoints[startX][j].getPiece()!=null&&
						chessPoints[startX][j].getPiece().getCategory().equals(PieceCategory.SHUAI)){
					SHUAI_Y = j;
					//System.out.println("在x=startX的横坐标上，区间(startY+1)---10之间有帅 1168");
					break;
				}
			}
			//移动的棋子在将帅之间
			if(JIANG_Y!=0&&SHUAI_Y!=0){
				if(endX == startX){//棋子纵坐标方向上移动
					canMove = sectionHavePiece(startX, JIANG_Y+1, SHUAI_Y-1, chessPoints);
					//System.out.println("棋子纵坐标方向上移动  1167 canMove="+canMove);
				}else{//棋子横坐标方向上移动
					canMove = sectionHavePiece(startX,JIANG_Y+1,startY-1,chessPoints)||
							sectionHavePiece(startX,startY+1,SHUAI_Y-1,chessPoints);
					//System.out.println("棋子横坐标方向上移动  1171 canMove="+canMove);
				}
			}
		}
		return !canMove;
	}
	/**
	 * 棋子走后判断将帅是否对脸，红方棋子在上半部分
	 * @param piece 移动的棋子
	 * @param startX 起始横坐标
	 * @param startY 起始纵坐标
	 * @param endX  终点横坐标
	 * @param endY 终点纵坐标
	 * @param chessPoints 棋子点二维数组
	 * @return 如果将帅对脸，返回true；否则，返回false。
	 */
	public static boolean isMeeting2(ChessPiece piece,int startX ,int startY, int endX, int endY,
			ChessPoint[][] chessPoints){
		boolean canMove = true;
		PieceCategory pc = piece.getCategory();
		/*PieceCategory category1 = null;
		PieceCategory category2 = null;
		if(palyFirst.equals(PALYER_FIRST) || palyFirst == null){
			category1 = PieceCategory.JIANG;
			category2 = PieceCategory.SHUAI;
		}else{
			category1 = PieceCategory.SHUAI;
			category2 = PieceCategory.JIANG;
		}*/
		if(pc.equals(PieceCategory.SHUAI)){//移动的棋子是帅
			for(int j=8;j<=10;j++){
				if(chessPoints[endX][j].getPiece()!=null&&
						chessPoints[endX][j].getPiece().getCategory().equals(PieceCategory.JIANG)){
					canMove = sectionHavePiece(endX,endY+1,j-1,chessPoints);
					//System.out.println("移动的棋子是将且与帅对脸了 1143 canMove="+canMove);
					break;
				}
			}
		}else if(pc.equals(PieceCategory.JIANG)){//移动的棋子是将
			for(int j=1;j<=3;j++){
				if(chessPoints[endX][j].getPiece()!=null&&
						chessPoints[endX][j].getPiece().getCategory().equals(PieceCategory.SHUAI)){
					canMove = sectionHavePiece(endX,j+1,endY-1,chessPoints);
					//System.out.println("移动棋子是帅且与将对脸了 1151 canMove="+canMove);
					break;
				}
			}
		}else{//移动的棋子是其他棋子
			int JIANG_Y = 0;
			int SHUAI_Y = 0;
			//在x=startX的横坐标上，区间1---(startY-1)之间有没有帅
			for(int j=1;j<startY;j++){
				if(chessPoints[startX][j].getPiece()!=null
						&&chessPoints[startX][j].getPiece().getCategory().equals(PieceCategory.SHUAI)){
					SHUAI_Y = j;
					break;
				}
			}
			//在x=startX的横坐标上，区间(startY+1)---10之间有没有将
			for(int j=startY+1;j<=10;j++){
				if(chessPoints[startX][j].getPiece()!=null&&
						chessPoints[startX][j].getPiece().getCategory().equals(PieceCategory.JIANG)){
					JIANG_Y = j;
					break;
				}
			}
			//移动的棋子在将帅之间
			if(JIANG_Y!=0&&SHUAI_Y!=0){
				if(endX == startX){//棋子纵坐标方向上移动
					canMove = sectionHavePiece(startX, SHUAI_Y+1, JIANG_Y-1, chessPoints);
				}else{//棋子横坐标方向上移动
					canMove = sectionHavePiece(startX,SHUAI_Y+1,startY-1,chessPoints)||
							sectionHavePiece(startX,startY+1,JIANG_Y-1,chessPoints);
				}
			}
		}
		return !canMove;
	}
	/**
	 * 是否对脸
	 * 
	 * @param piece
	 *            移动的棋子
	 * @param endX
	 *            终点横坐标
	 * @param endY
	 *            终点纵坐标
	 * @param chessPoints
	 *            棋子点二维数组
	 * @return 如果对脸，返回true；否则，返回false。
	 */
	public static boolean isDuiLian(ChessPiece piece, int endX, int endY,
			ChessPoint[][] chessPoints) {

		int shuaiI = 0, shuaiJ = 0;
		int jiangI = 0, jiangJ = 0;
		PieceCategory pc = piece.getCategory();

		// 棋盘中黑將的位置
		for (int i = 4; i <= 6; i++) {
			for (int j = 1; j <= 3; j++) {
				if (chessPoints[i][j].hasPiece()) {
					if (chessPoints[i][j].getPiece().getCategory()
							.equals(PieceCategory.JIANG)) {
						jiangI = i;
						jiangJ = j;
						break;
					}
				}
			}
		}

		// 棋盘中红帥的位置
		for (int i = 4; i <= 6; i++) {
			for (int j = 8; j <= 10; j++) {
				if (chessPoints[i][j].hasPiece()) {
					if (chessPoints[i][j].getPiece().getCategory()
							.equals(PieceCategory.SHUAI)) {
						shuaiI = i;
						shuaiJ = j;
						break;
					}
				}
			}
		}
		// 移动的棋子是帥或將
		if (pc == PieceCategory.SHUAI || pc == PieceCategory.JIANG) {
			int startX = 0;
			int startY = 0;
			if (pc == PieceCategory.SHUAI) {
				startX = jiangI;
				startY = jiangJ;
			} else {
				startX = shuaiI;
				startY = shuaiJ;
			}
			boolean flag = false;
			// 横坐标相同才有可能对脸
			if (startX == endX) {
				int y2 = Math.max(endY, startY);
				int y1 = Math.min(endY, startY);
				for (int j = y1 + 1; j < y2; j++) {
					if (chessPoints[startX][j].hasPiece()) {
						flag = true;
					}
				}
				if (!flag) {
					return true;
				}
			}
		}
		// 移动的棋子不是將和帥
		else {
			// 横坐标相同才有可能对脸
			if (shuaiI == jiangI) {
				boolean flag = false;
				int y2 = Math.max(shuaiJ, jiangJ);
				int y1 = Math.min(shuaiJ, jiangJ);
				for (int j = y1 + 1; j < y2; j++) {
					// 將和帥之间是否有棋子，不包含移动的棋子，因为这个棋子将要离开
					if (j != endY && chessPoints[shuaiI][j].hasPiece()) {
						flag = true;
					}
				}
				if (!flag) {
					return true;
				}
			}
		}
		// System.out.println(piece.getCategory()+"Test" + shuaiI + shuaiJ + ","
		// + endX + endY);

		return false;

	}
}
