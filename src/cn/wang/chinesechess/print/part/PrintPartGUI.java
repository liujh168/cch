
package cn.wang.chinesechess.print.part;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import cn.wang.chinesechess.ChessGUI;
import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;
import cn.wang.chinesechess.core.ChessManual;
import cn.wang.chinesechess.core.ChessPiece;
import cn.wang.chinesechess.core.ChessPoint;
import cn.wang.chinesechess.core.ChessRule;
import cn.wang.chinesechess.core.ManualItem;
import cn.wang.chinesechess.core.MoveStep;
import cn.wang.chinesechess.core.PieceUtil;
import cn.wang.chinesechess.save.GameRecord;
import cn.wang.chinesechess.save.ISaveManual;
import cn.wang.chinesechess.save.SaveAsDialog;
import cn.wang.chinesechess.save.SaveDialog;


/**
 * 打谱类 --自定义棋子位置，主要用来打残局棋谱
 * 
 * @author wanghualiang
 */
public class PrintPartGUI extends JFrame implements ActionListener, NAME,
		ISaveManual {

	private static final long serialVersionUID = 266L;

	private JMenu fileMenu, manualMenu, settingMenu, helpMenu;

	private JMenuItem newManual, saveManual, saveManualAs, exitGame;
	
	private JMenuItem reprintManual, inputManual, undoManual;

	private JMenuItem setting, helpContent, aboutGame, welcome;

	private JCheckBoxMenuItem bgSound,gmSound;

	private JButton newButton, reprint, save, saveAs, undo, lock, set,
			inputManualButton;
	
	private JButton prev, next, auto, first, last;

	private JPanel toolBar = new JPanel();

	private JPanel manualTools;

	private ArrayList<ManualItem> records;

	private ArrayList<Position> initPositions;

	private PrintPartBoard board;

	public ChessManual chessManual;

	private JPanel gameStatusPanel;

	public ChessBoxPanel piecesPanel;

	public int curIndex = -1;

	// 游戏状态
	JLabel gameStatusContent, gameStatusIcon;

	public JScrollPane manualScroll;

	private Vector descs;

	// 按钮共用的手形图标
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	/**boolean 默认没有锁定棋局*/
	public boolean isLock = false;
	
	/**boolean 棋盘是否可以下棋，默认可以*/
	protected boolean canPaly = true;

	private int time = 1000;

	/*// 锁定棋局之后，哪方先走
	public boolean redFirst = true;*/

	/**boolean[][] temp[i][j]=true意为棋盘[i][j]位置上的棋子是临时的*/
	public boolean[][] temp = new boolean[11][11];
	public PrintPartGUI() {

		board = new PrintPartBoard(this);

		chessManual = board.chessManual;
		manualScroll = chessManual.manualScroll;
		// manual = chessManual.manual;
		descs = chessManual.descs;
		records = chessManual.getManualItems();
		initMenuBar();
		initButtons();
		initToolBar();
		initPanels();
		handleKeyEvent();
		ChessUtil.setLoop(true);
		ChessUtil.setStart(true);

		setSize(670, 760);
		setTitle("中国象棋---残局打谱");
		setResizable(false);
		setIconImage(ChessUtil.getAppIcon());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//需要点击确认退出才退出
		addWindowListener(new WindowAdapter() {
			// 响应默认的退出事件
			public void windowClosing(WindowEvent e) {
				handleExitGame();
			}

		});
	}

	/**
	 * 响应键盘事件
	 * 
	 */
	private void handleKeyEvent() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			public void eventDispatched(AWTEvent event) {
				if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
					int code = ((KeyEvent) event).getKeyCode();
					if (code == KeyEvent.VK_F1) {
						ChessUtil.showHelpDialog();
					} else if (code == KeyEvent.VK_DOWN) {
						next();
						System.out.println("next");
					} else if (code == KeyEvent.VK_UP) {
						prev();
						System.out.println("prev");
					} else if (code == KeyEvent.VK_HOME) {
						first();
						System.out.println("first");
					} else if (code == KeyEvent.VK_END) {
						last();
						System.out.println("last");
					}

				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

	}

	/**
	 * 初始化面板
	 * 
	 */
	private void initPanels() {
		// 构造右边的面板
		JPanel rightPanel = new JPanel(new BorderLayout());

		// 棋谱描述面板
		JPanel recordsPanel = new JPanel(new BorderLayout());

		TitledBorder recordsBorder = new TitledBorder(
				PropertyReader.get("CHESS_MESSAGE_TOOLTIP"));
		recordsPanel.setBorder(recordsBorder);
		recordsPanel.setPreferredSize(new Dimension(240, 350));

		recordsPanel.add(BorderLayout.CENTER, chessManual);
		// 工具栏
		manualTools = new JPanel(new FlowLayout(FlowLayout.CENTER));
		// manualTools.setPreferredSize(new Dimension(220, 20));
		manualTools.add(first);
		manualTools.add(prev);
		manualTools.add(auto);
		manualTools.add(next);
		manualTools.add(last);

		recordsPanel.add(BorderLayout.SOUTH, manualTools);

		rightPanel.add(BorderLayout.CENTER, recordsPanel);

		// 水平分割栏
		JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, board,
				rightPanel);
		splitH.setDividerSize(5);
		splitH.setDividerLocation(450);

		initPiecesPanel();

		// 游戏状态面板
		gameStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// gameStatusPanel.setBackground(new Color(122, 146, 170));
		gameStatusIcon = new JLabel(ChessUtil.getImageIcon("hongshuai.gif"));
		// 游戏状态栏
		gameStatusContent = new JLabel("红方先走棋");
		gameStatusContent.setFont(new Font("宋体", Font.PLAIN, 16));

		TitledBorder gameStatusBorder = new TitledBorder("游戏状态");
		gameStatusBorder.setTitleColor(Color.RED);
		gameStatusBorder.setTitleFont(new Font("宋体", Font.PLAIN, 16));
		gameStatusPanel.setToolTipText("游戏状态");
		gameStatusPanel.setBorder(gameStatusBorder);

		gameStatusPanel.add(gameStatusIcon);
		gameStatusPanel.add(gameStatusContent);

		// add(BorderLayout.SOUTH, gameStatusPanel);
		add(BorderLayout.CENTER, splitH);
		add(BorderLayout.NORTH, toolBar);
	}

	/**
	 * 初始化棋子面板
	 * 
	 */
	private void initPiecesPanel() {

		GridLayout gridLayout = new GridLayout(4, 8);
		gridLayout.setHgap(5);
		gridLayout.setVgap(2);
		piecesPanel = new ChessBoxPanel("chessBoxBack.jpg");
		piecesPanel.setLayout(gridLayout);
		piecesPanel.add(board.红車1);
		piecesPanel.add(board.红馬1);
		piecesPanel.add(board.红相1);
		piecesPanel.add(board.红仕1);

		piecesPanel.add(board.红仕2);
		piecesPanel.add(board.红相2);
		piecesPanel.add(board.红馬2);
		piecesPanel.add(board.红車2);

		piecesPanel.add(board.红帥);
		piecesPanel.add(board.红炮1);
		piecesPanel.add(board.红炮2);
		piecesPanel.add(board.红兵1);
		piecesPanel.add(board.红兵2);
		piecesPanel.add(board.红兵3);
		piecesPanel.add(board.红兵4);
		piecesPanel.add(board.红兵5);

		piecesPanel.add(board.黑車1);
		piecesPanel.add(board.黑馬1);
		piecesPanel.add(board.黑象1);
		piecesPanel.add(board.黑士1);
		piecesPanel.add(board.黑士2);
		piecesPanel.add(board.黑象2);
		piecesPanel.add(board.黑馬2);
		piecesPanel.add(board.黑車2);

		piecesPanel.add(board.黑將);
		piecesPanel.add(board.黑炮1);
		piecesPanel.add(board.黑炮2);
		piecesPanel.add(board.黑卒1);
		piecesPanel.add(board.黑卒2);
		piecesPanel.add(board.黑卒3);
		piecesPanel.add(board.黑卒4);
		piecesPanel.add(board.黑卒5);

		add(BorderLayout.SOUTH, piecesPanel);

		TitledBorder piecesPanelBorder = new TitledBorder("备用棋子");
		piecesPanelBorder.setTitleColor(Color.RED);
		piecesPanelBorder.setTitleFont(new Font("宋体", Font.PLAIN, 14));
		piecesPanel.setBorder(piecesPanelBorder);
	}

	/**
	 * 初始化按钮
	 * 
	 */
	private void initButtons() {
		Dimension iconSize = new Dimension(16, 16);
		prev = new JButton(ChessUtil.getImageIcon("prev.gif"));
		prev.addActionListener(this);
		prev.setToolTipText("上一步");
		prev.setCursor(new Cursor(Cursor.HAND_CURSOR));
		prev.setPreferredSize(iconSize);

		next = new JButton(ChessUtil.getImageIcon("next.gif"));
		next.addActionListener(this);
		next.setToolTipText("下一步");
		next.setCursor(new Cursor(Cursor.HAND_CURSOR));
		next.setPreferredSize(iconSize);

		first = new JButton(ChessUtil.getImageIcon("first.gif"));
		first.addActionListener(this);
		first.setToolTipText("第一步");
		first.setCursor(new Cursor(Cursor.HAND_CURSOR));
		first.setPreferredSize(iconSize);

		last = new JButton(ChessUtil.getImageIcon("last.gif"));
		last.addActionListener(this);
		last.setToolTipText("最后一步");
		last.setCursor(new Cursor(Cursor.HAND_CURSOR));
		last.setPreferredSize(iconSize);

		auto = new JButton(ChessUtil.getImageIcon("auto.gif"));
		auto.addActionListener(this);
		auto.setToolTipText("自动演示");
		auto.setPreferredSize(iconSize);
		auto.setCursor(new Cursor(Cursor.HAND_CURSOR));

		Insets insets = new Insets(1, 1, 1, 1);

		reprint = new JButton(ChessUtil.getImageIcon("reprint.gif"));
		reprint.setToolTipText("重新打谱");
		reprint.addActionListener(this);
		// reprint.setPreferredSize(dimension);
		reprint.setCursor(handCursor);
		reprint.setMargin(insets);

		save = new JButton(ChessUtil.getImageIcon("save.gif"));
		save.addActionListener(this);
		save.setToolTipText("保存棋谱");
		// save.setPreferredSize(dimension);
		save.setCursor(handCursor);
		save.setEnabled(false);
		save.setMargin(insets);

		saveAs = new JButton(ChessUtil.getImageIcon("saveas.gif"));
		saveAs.addActionListener(this);
		saveAs.setToolTipText("另存棋谱");
		// saveAs.setPreferredSize(dimension);
		saveAs.setCursor(handCursor);
		saveAs.setEnabled(false);
		saveAs.setMargin(insets);

		lock = new JButton(ChessUtil.getImageIcon("unLock.gif"));
		lock.addActionListener(this);
		lock.setToolTipText("锁定");
		// saveAs.setPreferredSize(dimension);
		lock.setCursor(handCursor);
		lock.setMargin(insets);

		undo = new JButton(ChessUtil.getImageIcon("undo.gif"));
		undo.addActionListener(this);
		undo.setToolTipText("悔棋");
		undo.setCursor(handCursor);
		undo.setEnabled(false);
		undo.setMargin(insets);

		set = new JButton(ChessUtil.getImageIcon("welcome.gif"));
		set.setToolTipText("设置");
		set.addActionListener(this);
		set.setCursor(handCursor);
		set.setMargin(insets);

		newButton = new JButton(ChessUtil.getImageIcon("newManual.gif"));
		newButton.setToolTipText("新建棋谱");
		newButton.addActionListener(this);
		newButton.setCursor(handCursor);
		newButton.setMargin(insets);

		inputManualButton = new JButton(
				ChessUtil.getImageIcon("inputManual.gif"));
		inputManualButton.setToolTipText("输入棋谱");
		inputManualButton.addActionListener(this);
		inputManualButton.setCursor(handCursor);
		inputManualButton.setEnabled(false);
		inputManualButton.setMargin(insets);

	}

	/**
	 * 初始化菜单
	 * 
	 */
	private void initMenuBar() {
		// 构造菜单
		JMenuBar bar = new JMenuBar();

		// 构造文件菜单
		fileMenu = new JMenu("文件(G)");

		newManual = new JMenuItem("新建", ChessUtil.getImageIcon("newManual.gif"));
		saveManual = new JMenuItem("保存", ChessUtil.getImageIcon("save.gif"));
		saveManual.setEnabled(false);
		saveManualAs = new JMenuItem("另存", ChessUtil.getImageIcon("saveas.gif"));
		saveManualAs.setEnabled(false);
		exitGame = new JMenuItem("退出", ChessUtil.getImageIcon("exit.gif"));
		// 向文件菜单中增加菜单项
		fileMenu.add(newManual);
		fileMenu.add(saveManual);
		fileMenu.add(saveManualAs);
		fileMenu.add(exitGame);

		// 构造棋谱菜单
		manualMenu = new JMenu("棋谱(M)");
		reprintManual = new JMenuItem("重新打谱",ChessUtil.getImageIcon("reprint.gif"));
		inputManual = new JMenuItem("输入棋谱",ChessUtil.getImageIcon("inputManual.gif"));
		inputManual.setEnabled(false);
		undoManual = new JMenuItem("悔棋", ChessUtil.getImageIcon("undo.gif"));
		undoManual.setEnabled(false);
		// 向棋谱菜单中添加菜单项
		manualMenu.add(reprintManual);
		manualMenu.add(inputManual);
		manualMenu.add(undoManual);

		// 构造帮助菜单
		helpMenu = new JMenu("帮助(H)");
		welcome = new JMenuItem("欢迎", ChessUtil.getImageIcon("welcome.gif"));
		helpContent = new JMenuItem("帮助内容", ChessUtil.getImageIcon("help.gif"));
		aboutGame = new JMenuItem("关于游戏", ChessUtil.getImageIcon("info.gif"));

		helpMenu.add(welcome);
		helpMenu.add(helpContent);
		helpMenu.add(aboutGame);

		// 构造设置菜单
		settingMenu = new JMenu("设置(S)");
		bgSound = new JCheckBoxMenuItem("背景音乐");
		gmSound = new JCheckBoxMenuItem("游戏音效");
		bgSound.setSelected(true);
		gmSound.setSelected(true);

		settingMenu.add(bgSound);
		settingMenu.add(gmSound);

		// 向菜单条中添加菜单
		bar.add(fileMenu);
		bar.add(manualMenu);
		bar.add(settingMenu);
		bar.add(helpMenu);

		setJMenuBar(bar);

		// 设置快捷键

		saveManual.setAccelerator(KeyStroke.getKeyStroke('S',
				InputEvent.CTRL_DOWN_MASK));
		saveManualAs.setAccelerator(KeyStroke.getKeyStroke('U',
				InputEvent.CTRL_DOWN_MASK));
		reprintManual.setAccelerator(KeyStroke.getKeyStroke('P',
				InputEvent.CTRL_DOWN_MASK));
		exitGame.setAccelerator(KeyStroke.getKeyStroke('E',
				InputEvent.CTRL_DOWN_MASK));

		newManual.setAccelerator(KeyStroke.getKeyStroke('N',
				InputEvent.CTRL_DOWN_MASK));
		inputManual.setAccelerator(KeyStroke.getKeyStroke('I',
				InputEvent.CTRL_DOWN_MASK));
		undoManual.setAccelerator(KeyStroke.getKeyStroke('U',
				InputEvent.CTRL_DOWN_MASK));

		welcome.setAccelerator(KeyStroke.getKeyStroke('W',
				InputEvent.CTRL_DOWN_MASK));
		helpContent.setAccelerator(KeyStroke.getKeyStroke('H',
				InputEvent.CTRL_DOWN_MASK));
		aboutGame.setAccelerator(KeyStroke.getKeyStroke('A',
				InputEvent.CTRL_DOWN_MASK));

		bgSound.setAccelerator(KeyStroke.getKeyStroke('B',
				InputEvent.CTRL_DOWN_MASK));
		gmSound.setAccelerator(KeyStroke.getKeyStroke('T',
				InputEvent.CTRL_DOWN_MASK));

		// 设置助记符
		fileMenu.setMnemonic(KeyEvent.VK_G);
		manualMenu.setMnemonic(KeyEvent.VK_M);
		settingMenu.setMnemonic(KeyEvent.VK_S);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// 注册监听器
		newManual.addActionListener(this);
		saveManual.addActionListener(this);
		saveManualAs.addActionListener(this);
		exitGame.addActionListener(this);

		inputManual.addActionListener(this);
		reprintManual.addActionListener(this);
		undoManual.addActionListener(this);

		gmSound.addActionListener(this);
		bgSound.addActionListener(this);

		welcome.addActionListener(this);
		helpContent.addActionListener(this);
		aboutGame.addActionListener(this);

		setJMenuBar(bar);
	}

	/**
	 * 初始化工具条
	 * 
	 */
	private void initToolBar() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		toolBar.setLayout(flowLayout);

		toolBar.add(newButton);
		toolBar.add(save);
		toolBar.add(saveAs);

		toolBar.add(reprint);
		toolBar.add(undo);
		toolBar.add(inputManualButton);
		toolBar.add(lock);
		toolBar.add(set);

	}

	/**
	 * 响应事件
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// 下一步
		if (source == next) {
			next();
		} else if (source == prev) {
			prev();
		} else if (source == first) {
			first();
		} else if (source == last) {
			last();
		} else if (source == auto) {
			DemoThread auto = new DemoThread();
			auto.start();
			System.out.println("刚刚写的新线程");
		} else if (source == helpContent) {
			ChessUtil.showHelpDialog();
		} 

		// 重新打谱
		else if (source == reprint) {
			int result = JOptionPane.showConfirmDialog(this, "您确定需要重新打谱么？",
					"您确定需要重新打谱么？", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				// 销毁旧的打谱窗口，然后新建一个打谱窗口
				dispose();
				PrintPartGUI newPrint = new PrintPartGUI();
				newPrint.setVisible(true);
			}

		} else if (source == lock) {
			if(!isLock){
				int result = JOptionPane
						.showConfirmDialog(
								this,
								"  锁定棋局之后，不能再将备用棋子放到棋盘中,\n也不能将棋盘中的棋子放回备用棋子栏\n之后，可以保存棋谱，方便下次演示",
								"您确定要锁定棋局么？", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					int lockResult = canLock();
					if (lockResult == 2) {
						lockManual();
					} else if (lockResult == 1) {
						JOptionPane.showMessageDialog(this, "帥没有位于九宫内", "锁定棋局失败",
								JOptionPane.ERROR_MESSAGE);
					} else if (lockResult == -1) {
						JOptionPane.showMessageDialog(this, "將没有位于九宫内", "锁定棋局失败",
								JOptionPane.ERROR_MESSAGE);
					} else if (lockResult == 0) {
						JOptionPane.showMessageDialog(this, "將和帥没有位于九宫内", "锁定棋局失败",
								JOptionPane.ERROR_MESSAGE);
					} else if (lockResult == 11) {
						JOptionPane.showMessageDialog(this, "將和帥不能对脸", "锁定棋局失败",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}else{
				int result = JOptionPane
						.showConfirmDialog(
								this,
								"解锁定棋局之后，可以修改新放入的临时棋子",
								"您确定要解锁棋局么？", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					unlockManual();
				}
			}
			
		}

		else if (source == save) {
			if (!isLock) {
				JOptionPane.showMessageDialog(this, "锁定之后才能保存");
				return;
			}
			SaveDialog dialog = new SaveDialog(this);
			dialog.setVisible(true);

		} else if (source == saveAs) {
			if (!isLock) {
				JOptionPane.showMessageDialog(this, "锁定之后才能另存");
				return;
			}
			SaveAsDialog dialog = new SaveAsDialog(this);
			dialog.setVisible(true);
		} else if (source == exitGame) {
			handleExitGame();
		} else if (source == helpContent) {
			ChessUtil.showHelpDialog();
		} else if (source == welcome) {
			ChessUtil.showWelcomeDialog();
		} else if (source == aboutGame) {
			ChessUtil.showAboutDialog();
		} 

		// 重新打谱
		else if (source == reprint || source == reprintManual) {
			int result = JOptionPane.showConfirmDialog(this, "您需要保存当前的棋谱吗？",
					"您需要保存当前的棋谱吗？", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				SaveDialog dialog = new SaveDialog(this);
				dialog.setVisible(true);
			}
			int size = chessManual.getManualItems().size();
			for (int index = 0; index < size; index++) {
				undo();
			}
		}

		// 悔棋
		else if (source == undo || source == undoManual) {
			last();
			undo();

		} else if (source == save || source == saveManual) {
			SaveDialog dialog = new SaveDialog(this);
			dialog.setVisible(true);

		} else if (source == saveAs || source == saveManualAs) {
			SaveAsDialog dialog = new SaveAsDialog(this);
			dialog.setVisible(true);
		} else if (source == inputManualButton || source == inputManual) {
			Icon icon = null;
			if(board.getPlayerName().equals(RED_NAME)){
				 icon = ChessUtil.getImageIcon("hongshuai.gif");
			}else{
				icon = ChessUtil.getImageIcon("heijiang.gif");
			}
			
			String manual = (String) JOptionPane.showInputDialog(this, "请输入棋谱", "请输入棋谱",
					JOptionPane.PLAIN_MESSAGE,icon,null,null);
			if(!canPaly){
				JOptionPane.showConfirmDialog(this, "游戏已经结束", "提示",
						JOptionPane.YES_OPTION);
				return;
			}
			if (manual != null) {
				manual = manual.trim();// 去掉前导空白和尾部空白
			}
			movePieceByManual(manual);

		} else if (source == newButton || source == newManual) {
			PrintPartGUI newPrint = new PrintPartGUI();
			newPrint.setVisible(true);
		}  else if (source == bgSound){
			ChessUtil.setLoop(bgSound.isSelected());
		} else if (source == gmSound){
			ChessUtil.setStart(gmSound.isSelected());
		}
	}
	
	/**
	 * 根据棋谱，如馬八进七，移动棋子
	 * 
	 * @param manual
	 */
	private void movePieceByManual(String manual) {
		board.setNeedWink(false);
		if(!canPaly){
			JOptionPane.showConfirmDialog(this, "游戏已经结束", "提示",
					JOptionPane.YES_OPTION);
			return;
		}
		if (manual != null && manual.length() == 4) {
			ChessPiece movePiece = board.getMovePiece(manual);
			if (movePiece == null || !movePiece.getName().equals(board.getPlayerName())) {
				return;
			}
			//ChessPiece removedPiece = board.getRemovedPiece(manual);
			Point pStart = board.getStartPosition(manual);
			Point pEnd = board.getEndPosition(manual);

			int startX = (int) pStart.getX();
			int startY = (int) pStart.getY();
			int endX = (int) pEnd.getX();
			int endY = (int) pEnd.getY();
			System.out.println(movePiece.getCategory() + " :" + startX + startY
					+ endX + endY);
			boolean rule = ChessRule.allRule(movePiece, startX, startY, endX,
					endY, board.chessPoints, null);
			System.out.println("能否移动棋子：" + rule);
			if (rule) {
				//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
				board.addChessRecord(movePiece, startX, startY, endX, endY);
				//移动棋子
				board.movePiece(movePiece, startX, startY, endX, endY);
				/*// 一定要先删除棋子，再设置新的棋子的位置
				board.movePiece(movePiece, startX, startY, endX, endY);
				ManualItem record = new ManualItem();
				MoveStep moveStep = new MoveStep(new Point(startX, startY),
						new Point(endX, endY));
				record.setMoveStep(moveStep);
				if (removedPiece != null) {
					record.setEatedPieceId(removedPiece.getId());
				} else {
					record.setEatedPieceId(null);
				}
				record.setMovePieceId(movePiece.getId());
				board.chessManual.addManualItem(record);*/
				// 打谱类当前索引+1
				curIndex++;

				// 播放棋子被吃声音
				ChessUtil.playSound("eat.wav");

				validate();
				repaint();

				// 更换己方名字，实现一人轮流走红黑2方的棋子
				board.changeSide();

			}
		}
	}

	/**
	 * 
	 */
	private void undo() {
		canPaly = true;
		boolean flag = chessManual.removeManualItem();
		if (flag) {
			if (board.getPlayerName().equals(RED_NAME)) {
				board.setPlayerName(BLACK_NAME);
			} else {
				board.setPlayerName(RED_NAME);
			}
			curIndex--;
			
		}
	}
	
	/**
	 * 解锁棋局
	 */
	private void unlockManual(){
		if(!canPaly){
			JOptionPane.showConfirmDialog(this, "游戏已经结束", "提示",
					JOptionPane.YES_OPTION);
			return;
		}
		isLock = false;
		
		saveManual.setEnabled(false);
		saveManualAs.setEnabled(false);
		inputManual.setEnabled(false);
		undoManual.setEnabled(false);
		
		board.setSelected(true);
		board.setNeedWink(true);
		//lock.setEnabled(true);
		save.setEnabled(false);
		saveAs.setEnabled(false);
		undo.setEnabled(false);
		inputManualButton.setEnabled(false);
		//更改图标显示
		lock.setIcon(ChessUtil.getImageIcon("unLock.gif"));
		lock.setToolTipText("锁定");
		
	}
	/**
	 * 锁定棋局
	 * 
	 */
	private void lockManual() {
		if(!canPaly){
			JOptionPane.showConfirmDialog(this, "游戏已经结束", "确认",
					JOptionPane.YES_OPTION);
			return;
		}
		//将PrintPartBoard中要用到的temp赋为false,表示棋盘上的棋子全部不是临时棋子
		for(int i=1;i<=9;i++)
			for(int j=1;j<=10;j++){
				temp[i][j]=false;
			}
		//如果是刚开始下棋，就选择谁先下
		if(descs.size()==0){
			int result = JOptionPane.showConfirmDialog(this,
					"红方先走选是，黑方先走选否，取消则不再锁定", "哪方先走呢？",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (result == JOptionPane.YES_OPTION) {
				board.setPlayerName(RED_NAME);
				board.setPalyerFirst(RED_NAME);
			} else if (result == JOptionPane.NO_OPTION) {
				board.setPlayerName(BLACK_NAME);
				board.setPalyerFirst(BLACK_NAME);
			}
		}

		// 锁定了棋局
		isLock = true;
		//更改图标显示
		lock.setIcon(ChessUtil.getImageIcon("lock.gif"));
		lock.setToolTipText("解锁");
		saveManual.setEnabled(true);
		saveManualAs.setEnabled(true);
		inputManual.setEnabled(true);
		undoManual.setEnabled(true);
		
		board.setSelected(false);
		board.setNeedWink(false);

		//lock.setEnabled(false);
		save.setEnabled(true);
		saveAs.setEnabled(true);
		undo.setEnabled(true);
		inputManualButton.setEnabled(true);

		// 保存棋子的初始位置
		initPositions = new ArrayList<Position>();
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 10; j++) {
				ChessPiece piece = board.chessPoints[i][j].getPiece();
				Position position = new Position();
				if (piece != null) {
					position.setId(piece.getId());
					position.setPoint(piece.getPosition());
					initPositions.add(position);
				}
			}
		}
	}

	public void scrollToView() {
		if (curIndex >= 0 && curIndex < descs.size()) {
			int lastIndex = curIndex;
			Rectangle rect = chessManual.manualList.getCellBounds(lastIndex,
					lastIndex);
			if (rect != null) {
				manualScroll.getViewport().scrollRectToVisible(rect);
			}
			// 选中当前行，提示玩家
			System.out.println("应该选中第N行：" + curIndex);
			chessManual.manualList.setSelectedIndex(curIndex);
		}

	}

	/**
	 * 处理退出事件 首先关闭闪烁线程
	 * 
	 */
	private void handleExitGame() {
		int result = JOptionPane.showConfirmDialog(this, "您确定要退出么？", "确认退出",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			if (board.getWinkThread() != null) {
				board.getWinkThread().interrupt();
				System.out.println("关闭中...");
			}
			dispose();
		}
	}

	/**
	 * 上一步
	 * 
	 */

	private void prev() {
		System.out.println("当前索引为：" + curIndex);
		if (curIndex == -1) {
			return;
		}

		int size = records.size();

		// 更新棋盘界面
		ManualItem record = new ManualItem();
		MoveStep moveStep;

		ChessPiece eatedPiece;
		int startI = 0;
		int startJ = 0;
		int endI = 0;
		int endJ = 0;

		if (size > 0 && curIndex < size && curIndex >= 0) {
			// 获得指定的元素
			record = (ManualItem) records.get(curIndex);
			eatedPiece = PieceUtil.createPiece(record.getEatedPieceId());

			moveStep = record.getMoveStep();
			startI = moveStep.start.x;
			startJ = moveStep.start.y;
			endI = moveStep.end.x;
			endJ = moveStep.end.y;

			// 上一步没有吃棋子
			if (eatedPiece == null) {
				System.out.println("没吃棋子");

				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();

				board.chessPoints[startI][startJ].setPiece(piece, board);
				(board.chessPoints[endI][endJ]).setHasPiece(false);

			}
			// 上一步吃了棋子
			else {
				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();
				board.chessPoints[startI][startJ].setPiece(piece, board);

				board.chessPoints[endI][endJ].setPiece(eatedPiece, board);
				(board.chessPoints[endI][endJ]).setHasPiece(true);

			}
		}

//		// 后退时，画标记与正常移动时不同
//		if (curIndex >= 1) {
//			record = (ManualItem) records.get(curIndex - 1);
//			moveStep = record.getMoveStep();
//			startI = moveStep.start.x;
//			startJ = moveStep.start.y;
//			endI = moveStep.end.x;
//			endJ = moveStep.end.y;
//		}
//
//		// 如果移动，应该画2条提示框
//		board.setMoveFlag(true);
//		board.movePoints[0] = new Point(
//				(int) (board.chessPoints[endI][endJ].getX()),
//				(int) (board.chessPoints[endI][endJ].getY()));
//		board.movePoints[1] = new Point(
//				(int) (board.chessPoints[startI][startJ].getX()),
//				(int) (board.chessPoints[startI][startJ].getY()));

		curIndex--;
		scrollToView();

		if (curIndex == -1) {
			// 退回到没有任何棋子移动的状态时，不用画移动标记
			board.setMoveFlag(false);
			/*
			 * 当curIndex == -1时，应该不选中任何一样,
			 * 而manual.setSelectedIndex(-1)不能时列表框不选中任何一行
			 * manual.setListData(descs);可以恢复到默认状态不选中任何一行
			 */
			chessManual.manualList.setListData(descs);
		}

		repaint();
		validate();
	}

	/**
	 * 演示index指定的一步
	 * 
	 * @param index
	 */
	public void step(int index) {
		ManualItem moveRecord = records.get(index);
		MoveStep step = moveRecord.getMoveStep();
		Point pStart = step.start;
		Point pEnd = step.end;
		int startI = pStart.x;
		int startJ = pStart.y;
		int endI = pEnd.x;
		int endJ = pEnd.y;

		ChessPiece piece = (board.chessPoints)[startI][startJ].getPiece();
		board.movePiece(piece, startI, startJ, endI, endJ);

		/*
		 * // 如果移动，应该画2条提示框 board.setMoveFlag(true); board.movePoints[0] = new
		 * Point((int) (board.chessPoints[endI][endJ] .getX()), (int)
		 * (board.chessPoints[endI][endJ].getY())); board.movePoints[1] = new
		 * Point( (int) (board.chessPoints[startI][startJ].getX()), (int)
		 * (board.chessPoints[startI][startJ].getY()));
		 * 
		 * 
		 * if ((board.chessPoints)[endI][endJ].hasPiece()) { ChessPiece
		 * pieceRemoved = (board.chessPoints)[endI][endJ] .getPiece();
		 * board.remove(pieceRemoved);
		 * (board.chessPoints)[endI][endJ].setPiece(piece, board);
		 * (board.chessPoints)[startI][startJ].setHasPiece(false); } else {
		 * (board.chessPoints)[endI][endJ].setPiece(piece, board);
		 * (board.chessPoints)[startI][startJ].setHasPiece(false); }
		 */
		// 演示时不能移动棋子的
		repaint();
		validate();
	}

	/**
	 * 演示当前步的下一步
	 * 
	 */
	public void next() {
		System.out.println("当前索引为：" + curIndex);

		if (curIndex == descs.size() - 1) {
			return;
		}

		curIndex++;
		if (curIndex < records.size()) {
			step(curIndex);
		}
		scrollToView();
	}

	/**
	 * 移动到最后一步
	 * 
	 */
	public void last() {
		int size = descs.size();
		System.out.println(size + "aa" + curIndex);
		while (curIndex < size - 1) {
			next();
		}
	}

	/**
	 * 移动到第一步
	 * 
	 */
	private void first() {
		while (curIndex != -1) {
			prev();
		}
	}

	/**
	 * 判断是否可以锁定棋局
	 * 
	 * 棋盘中有红帥和黑將时才能锁定
	 * 
	 * @return
	 */
	private int canLock() {

		boolean hasShuai = false;
		boolean hasJiang = false;

		ChessPoint[][] chessPoints = board.chessPoints;

		int shuaiX = 0, shuaiY = 0, jiangX = 0, jiangY = 0;

		// 棋盘中是否有黑將
		for (int i = 4; i <= 6; i++) {
			for (int j = 1; j <= 3; j++) {
				if (chessPoints[i][j].hasPiece()) {
					if (chessPoints[i][j].getPiece().getCategory()
							.equals(PieceCategory.JIANG)) {
						hasJiang = true;
						jiangX = i;
						jiangY = j;
						break;
					}
				}
			}
		}
		// 棋盘中是否有红帥
		for (int i = 4; i <= 6; i++) {
			for (int j = 8; j <= 10; j++) {
				if (chessPoints[i][j].hasPiece()) {
					if (chessPoints[i][j].getPiece().getCategory()
							.equals(PieceCategory.SHUAI)) {
						hasShuai = true;
						shuaiX = i;
						shuaiY = j;
						break;
					}
				}
			}
		}

		if (hasShuai) {
			if (hasJiang) {
				boolean flag = false;
				// 將帥是否对脸
				if (shuaiX == jiangX) {

					for (int j = jiangY + 1; j <= shuaiY - 1; j++) {
						if (chessPoints[shuaiX][j].hasPiece()) {
							flag = true;
						}
					}
				} else {
					flag = true;
				}

				if (flag) {
					return 2;
				}
				return 11;

			}
			return -1;
		}
		if (hasJiang) {
			return 1;
		}
		return 0;

	}
	
	/**
	 * 提示游戏已经结束
	 * @param canPaly 标记游戏是否结束 true未结束 ；false结束
	 * @param category 被将军的棋子种类，帅或将
	 */
	protected void gameOver(boolean canPaly,PieceCategory category){
		if(!canPaly){
			this.canPaly = canPaly;
			if(category.equals(PieceCategory.JIANG)){
				JOptionPane.showConfirmDialog(this, "游戏结束,红方获胜", "提示",
						JOptionPane.YES_OPTION);
			}else{
				JOptionPane.showConfirmDialog(this, "游戏结束,黑方获胜", "提示",
						JOptionPane.YES_OPTION);
			}			
		}
	}
	private class DemoThread extends Thread {

		public DemoThread() {
			System.out.println("刚刚写的新线程构造完成了");
		}

		public void run() {
			System.out.println("刚刚写的新线程运行了");
			int size = records.size();

			while (curIndex < size - 1) {
				try {
					Thread.sleep(time);

					next();
					if (curIndex >= size - 1) {
						this.join(100);
						break;
					}
				} catch (InterruptedException ie) {
					break;
				}
			}
		}

	}

	public static void main(String[] args) {
		/* 首先设置GUI外观样式：本地系统样式 */
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintPartGUI printEndManual = new PrintPartGUI();
		printEndManual.setVisible(true);

	}

	@Override
	public ArrayList<String> getSavePaths() {
		ArrayList<String> paths = new ArrayList<String>();
		String path = "src/manuals/partial/";
		String path2 = "src/manuals/partial/";

		paths.add(path);
		paths.add(path2);
		return paths;
	}

	@Override
	public GameRecord getGameRecord() {
		GameRecord gameRecord = new GameRecord(ManualType.PRINT_PARTIAL,
				ChessUtil.getDateAndTime(), "",
				board.chessManual.getManualItems(), board.chessManual.descs,
				initPositions, PALYER_FIRST);
		return gameRecord;
	}

}
