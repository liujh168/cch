
package cn.wang.chinesechess.print.part;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import cn.wang.chinesechess.ChessUtil;


/**
 * 高级打谱容纳备用棋子的面板
 * 
 * @author wanghualiang
 */
public class ChessBoxPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private String imgName = "";

	public ChessBoxPanel(String imgName) {
		this.imgName = imgName;
	}

	public ChessBoxPanel(GridLayout layout) {
		super(layout);
	}
	
	public ChessBoxPanel(String imgName,GridLayout layout) {
		super(layout);
		this.imgName = imgName;
	}
	@Override
	protected void paintComponent(Graphics g) { // 重写此方法，可加入自己的图片
		super.paintComponent(g);
		Dimension size = new Dimension(super.getWidth(), super.getHeight());
		g.drawImage(ChessUtil.getImage(imgName), 0, 0, size.width,
				size.height, null);
	}

}
