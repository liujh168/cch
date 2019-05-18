
package cn.wang.chinesechess.save;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.core.ManualUtil;


/**
 * 另存棋谱时用的对话框
 * 
 * @author wanghualiang
 */
public class SaveAsDialog extends JFrame implements ActionListener, NAME {

	private static final long serialVersionUID = 11L;

	private JButton ok, cancel, chooser;

	JLabel name = new JLabel("保存路径:");

	JLabel desc = new JLabel("棋局描述:");

	JTextField pathField = new JTextField(20);

	JTextArea area = new JTextArea(2,26);

	String path;

	private ISaveManual owner;

	/**
	 * 带有参数的构造方法
	 * 
	 * @param owner
	 *            实现了ISaveManual接口的对象
	 */
	public SaveAsDialog(ISaveManual owner) {
		this.owner = owner;
		initButtons();
		GridLayout gridlayout = new GridLayout(3, 1);
		FlowLayout flowLlayout = new FlowLayout();
		JPanel panel = new JPanel(gridlayout);
		JPanel panel1 = new JPanel(flowLlayout);
		JPanel panel2 = new JPanel(flowLlayout);
		JPanel panel3 = new JPanel(flowLlayout);
		area.setTabSize(10);
		area.setLineWrap(true);//激活自动换行功能
		area.setWrapStyleWord(true);//激活断行不断字功能
		JScrollPane jspanel = new JScrollPane(area);
		panel1.add(name);
		panel1.add(pathField);
		pathField.setEditable(false);
		panel1.add(chooser);
		panel2.add(desc);
		panel2.add(jspanel);
		panel3.add(ok);
		panel3.add(cancel);
		panel.add(panel1);
		panel.add(panel2);
		panel.add(panel3);
		add(panel);
		setSize(300, 230);
		setTitle("中国象棋---另存棋谱");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(ChessUtil.getAppIcon());

		// 响应默认的退出事件
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	/**
	 * 初始化按钮 添加事件响应，设置提示信息，设置鼠标形状，设置首选大小
	 * 
	 */
	private void initButtons() {
		int width = ChessUtil.getImageIcon("ok.png").getIconWidth();
		int height = ChessUtil.getImageIcon("ok.png").getIconHeight();
		ok = new JButton(ChessUtil.getImageIcon("ok.png"));
		ok.addActionListener(this);
		ok.setToolTipText("确定");
		ok.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ok.setPreferredSize(new Dimension(width, height));

		cancel = new JButton(ChessUtil.getImageIcon("cancel.png"));
		cancel.addActionListener(this);
		cancel.setToolTipText("取消");
		cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cancel.setPreferredSize(new Dimension(width, height));

		chooser = new JButton("路径");
		chooser.addActionListener(this);
		chooser.setToolTipText("路径");
		chooser.setCursor(new Cursor(Cursor.HAND_CURSOR));

	}

	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == ok) {
			String name = pathField.getText();
			String desc = area.getText();
			if (name == null || name.equals("")) {
				JOptionPane.showMessageDialog(this, "请输入棋谱名称", "请输入棋谱名称",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			} else if (desc == null || desc.equals("")) {
				JOptionPane.showMessageDialog(this, "请输入棋局描述", "请输入棋局描述",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			GameRecord gameRecord = owner.getGameRecord();
			gameRecord.setDesc(desc);

			boolean flag = ManualUtil.saveManual(path + EXTENSION_NAME, path
					+ ".txt", gameRecord);
			;

			if (flag) {
				JOptionPane.showMessageDialog(this, "棋谱保存成功喽！", "棋谱保存成功喽！",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "棋谱保存失败！", "棋谱保存失败！",
						JOptionPane.ERROR_MESSAGE);
			}
			dispose();
		} else if (source == cancel) {
			dispose();
		} else if (source == chooser) {
			JFileChooser fileChooser = new JFileChooser();
			int state = fileChooser.showSaveDialog(null);
			File saveFile = fileChooser.getSelectedFile();
			if (saveFile != null && state == JFileChooser.APPROVE_OPTION) {
				path = saveFile.getPath();
				pathField.setText(path);

			}
		}

	}

}