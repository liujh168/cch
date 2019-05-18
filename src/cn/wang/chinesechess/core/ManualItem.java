
package cn.wang.chinesechess.core;

import java.io.Serializable;

import cn.wang.chinesechess.config.NAME;


/**
 * 棋子走法 记录一条棋谱
 * 
 * @author wanghualiang
 */
public class ManualItem implements Serializable,NAME{

	private static final long serialVersionUID = 259L;

	private PieceId eatedPieceId;// 被吃棋子的ID，悔棋时使用

	private MoveStep moveStep;// 从哪移动到哪

	private PieceId movePieceId;// 移动棋子的ID，暂时不使用

	public ManualItem() {
		moveStep = null;
		eatedPieceId = null;
	}

	public ManualItem(PieceId movePieceId, PieceId eatedPieceId,
			MoveStep moveStep) {
		this.movePieceId = movePieceId;
		this.eatedPieceId = eatedPieceId;
		this.moveStep = moveStep;
	}

	public MoveStep getMoveStep() {
		return moveStep;
	}

	public PieceId getEatedPieceId() {
		return eatedPieceId;
	}

	public void setEatedPieceId(PieceId eatedPieceId) {
		this.eatedPieceId = eatedPieceId;
	}

	public PieceId getMovePieceId() {
		return movePieceId;
	}

	public void setMovePieceId(PieceId movePieceId) {
		this.movePieceId = movePieceId;
	}

	public void setMoveStep(MoveStep moveStep) {
		this.moveStep = moveStep;
	}
}
