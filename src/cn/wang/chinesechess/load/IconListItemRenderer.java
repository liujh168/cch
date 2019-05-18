
package cn.wang.chinesechess.load;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 * 装载列表项渲染器
 * 
 * @author wanghualiang
 */
public class IconListItemRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;

	/** 选中的列表项时的边框*/
	private Border selectedBorder = BorderFactory.createLineBorder(Color.blue,
			1);

	/** 没有选中的列表项的边框*/
	private Border emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

	public IconListItemRenderer() {
		setOpaque(true);

	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		IconListItem item = (IconListItem) value;
		this.setIcon(item.getIcon());
		this.setText(item.getText());

		Color background;

		if (isSelected) {
			setBorder(selectedBorder);
			background = new Color(212, 219, 238);
		} else {
			setBorder(emptyBorder);
			background = Color.WHITE;
		}

		// 设置背景色
		setBackground(background);
		return this;
	}
}
