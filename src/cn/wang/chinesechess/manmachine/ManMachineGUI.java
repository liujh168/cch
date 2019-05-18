
package cn.wang.chinesechess.manmachine;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import cn.wang.chinesechess.ChessUtil;
import cn.wang.chinesechess.config.NAME;
import cn.wang.chinesechess.config.PropertyReader;
import cn.wang.chinesechess.config.NAME.PieceCategory;
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
 * 人机对弈图形用户界面
 * 
 * @author wanghualiang
 */
public class ManMachineGUI extends JFrame implements ActionListener,
		NAME, ISaveManual {

private static final long serialVersionUID = 11L;
	
	/**private JMenu 菜单栏*/
	private JMenu fileMenu, manualMenu, settingMenu, helpMenu;
	
	/**private JMenuItem 文件菜单选项*/
	private JMenuItem newGame, saveManual, saveManualAs, exitGame;
	
	/**private JMenuItem 棋谱菜单选项*/
	private JMenuItem repalyGame, inputManual, undoManual;
	
	/**private JMenuItem 帮助菜单选项*/
	private JMenuItem setting, helpContent, aboutGame, welcome;
	
	/**private JMenuItem 设置菜单选项*/
	private JCheckBoxMenuItem bgSound,gmSound;
	
	/**private JButton 棋谱演示按钮*/
	private JButton prev, next, auto, first, last;
	
	/**private JButton 功能按钮*/
	private JButton newButton, save, saveAs, repaly, inputManualButton, undo;
	
	/**private ArrayList<ManualItem> 棋谱记录*/
	private ArrayList<ManualItem> records;
	
	/**private TwoManBoard  棋盘*/
	private ManMachineBoard board;
	
	/**private ChessManual  棋谱*/
	private ChessManual chessManual;
	
	/**private JPanel  游戏状态面板*/
	private JPanel gameStatusPanel;
	
	/**private JPanel 存放棋谱演示按钮的工具栏*/
	private JPanel manualTools;
	
	/**boolean 棋盘是否可以下棋，默认可以*/
	protected boolean canPaly = true;
	
	/**private JLabel 游戏状态*/ 
	private JLabel gameStatusContent, gameStatusIcon;
	
	/**private JPanel 存放功能按钮的工具栏*/
	private JPanel toolBar = new JPanel();
	
	/**public int 当前的索引 */
	public int curIndex = -1;
	
	/**private JScrollPane 棋谱显示滚动面板 */
	private JScrollPane manualScroll;
	
	/**public int 棋谱自动演示的时间间隔 */
	private int time = 1000;
	
	/**private JList 棋谱列表 */
	private JList manual;
	
	/**private Vector 棋谱对应的文字描述 */
	private Vector descs;
	
	/**Cursor handCursor 按钮共用的手形图标*/
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	/**boolean 默认没有锁定棋局*/
	public boolean isLock = false;
	// 走法栈
	private Stack<ManualItem> moveStack = new Stack<ManualItem>();
	/**private static final 最大值*/
	private static final int MAX_VALUE = 3000;
	/**private int 搜索深度*/
	private int maxDepth;
	/**private ManualItem 对于电脑最好的走法*/
	private ManualItem bestChessMove;
	/**每次局面的棋子数组*/
	private ChessPoint[][] chessPoints;
	/**private ChessPoint[][] 搜索时临时的棋子数组 */
	private ChessPoint[][] tempChessPoints = new ChessPoint[10][11];
	/**private ManMachineBoard 搜索时临时的棋盘 */
	private ManMachineBoard tempBoard;

	/**
	 * 构造函数
	 */
	public ManMachineGUI() {
		
		int result = JOptionPane.showConfirmDialog(this,"YES玩家先手，NO电脑先手", "选择先手方",
				JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION){
			board = new ManMachineBoard(this);
			board.initChess(PALYER_FIRST);
		}else if(result == JOptionPane.NO_OPTION){
			board = new ManMachineBoard(this){
				@Override
				protected void drawShuXianFlag(Graphics2D g2){
					drawShuXianFlag2(g2);
				}
			};
			board.initChess(COMPUTER_FIRST);
		}else{
			dispose();
			return;
		}
		
		chessManual = board.chessManual;
		manual = chessManual.manualList;
		manualScroll = chessManual.manualScroll;
		descs = chessManual.descs;
		chessPoints = board.chessPoints;
		records = chessManual.getManualItems();
		initMenuBar();
		initButtons();
		initPanels();
		handleKeyEvent();
		ChessUtil.setLoop(true);
		ChessUtil.setStart(true);
		if(result == JOptionPane.NO_OPTION){
			computerThink();
		}
		setSize(670, 660);
		setTitle("中国象棋---人机对战");
		setResizable(false);
		setIconImage(ChessUtil.getAppIcon());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			// 响应默认的退出事件
			public void windowClosing(WindowEvent e) {
				handleExitGame();
			}

		});
	}

	/**
	 * private void
	 * 初始化菜单
	 */
	private void initMenuBar(){
		// 构造菜单
		JMenuBar bar = new JMenuBar();
		// 构造文件菜单
		fileMenu = new JMenu("文件(G)");
		newGame = new JMenuItem("新建", ChessUtil.getImageIcon("newManual.gif"));
		saveManual = new JMenuItem("保存", ChessUtil.getImageIcon("save.gif"));
		saveManualAs = new JMenuItem("另存", ChessUtil.getImageIcon("saveas.gif"));
		exitGame = new JMenuItem("退出", ChessUtil.getImageIcon("exit.gif"));
		// 向文件菜单中增加菜单项
		fileMenu.add(newGame);
		fileMenu.add(saveManual);
		fileMenu.add(saveManualAs);
		fileMenu.add(exitGame);
		
		// 构造棋谱菜单
		manualMenu = new JMenu("棋谱(M)");
		repalyGame = new JMenuItem("重新开始",ChessUtil.getImageIcon("reprint.gif"));
		inputManual = new JMenuItem("输入棋谱",ChessUtil.getImageIcon("inputManual.gif"));
		undoManual = new JMenuItem("悔棋", ChessUtil.getImageIcon("undo.gif"));
		// 向棋谱菜单中添加菜单项
		manualMenu.add(repalyGame);
		manualMenu.add(inputManual);
		manualMenu.add(undoManual);
		
		// 构造设置菜单
		settingMenu = new JMenu("设置(S)");
		bgSound = new JCheckBoxMenuItem("背景音乐");
		gmSound = new JCheckBoxMenuItem("游戏音效");
		bgSound.setSelected(true);
		gmSound.setSelected(true);
		// 向设置菜单中添加菜单项
		settingMenu.add(bgSound);
		settingMenu.add(gmSound);
		
		// 构造帮助菜单
		helpMenu = new JMenu("帮助(H)");
		welcome = new JMenuItem("欢迎", ChessUtil.getImageIcon("welcome.gif"));
		helpContent = new JMenuItem("帮助内容", ChessUtil.getImageIcon("help.gif"));
		aboutGame = new JMenuItem("关于游戏", ChessUtil.getImageIcon("info.gif"));
		// 向帮助菜单中添加菜单项
		helpMenu.add(welcome);
		helpMenu.add(helpContent);
		helpMenu.add(aboutGame);
		
		// 向菜单条中添加菜单
		bar.add(fileMenu);
		bar.add(manualMenu);
		bar.add(settingMenu);
		bar.add(helpMenu);
		
		setJMenuBar(bar);
		// 设置快捷键
		newGame.setAccelerator(KeyStroke.getKeyStroke('N',InputEvent.CTRL_DOWN_MASK));
		saveManual.setAccelerator(KeyStroke.getKeyStroke('S',InputEvent.CTRL_DOWN_MASK));
		saveManualAs.setAccelerator(KeyStroke.getKeyStroke('U',InputEvent.CTRL_DOWN_MASK));
		exitGame.setAccelerator(KeyStroke.getKeyStroke('E',InputEvent.CTRL_DOWN_MASK));

		repalyGame.setAccelerator(KeyStroke.getKeyStroke('P',InputEvent.CTRL_DOWN_MASK));
		inputManual.setAccelerator(KeyStroke.getKeyStroke('I',InputEvent.CTRL_DOWN_MASK));
		undoManual.setAccelerator(KeyStroke.getKeyStroke('U',InputEvent.CTRL_DOWN_MASK));
				
		welcome.setAccelerator(KeyStroke.getKeyStroke('W',InputEvent.CTRL_DOWN_MASK));
		helpContent.setAccelerator(KeyStroke.getKeyStroke('H',InputEvent.CTRL_DOWN_MASK));
		aboutGame.setAccelerator(KeyStroke.getKeyStroke('A',InputEvent.CTRL_DOWN_MASK));

		bgSound.setAccelerator(KeyStroke.getKeyStroke('B',InputEvent.CTRL_DOWN_MASK));
		gmSound.setAccelerator(KeyStroke.getKeyStroke('T',InputEvent.CTRL_DOWN_MASK));
		
		// 设置助记符
		fileMenu.setMnemonic(KeyEvent.VK_G);
		manualMenu.setMnemonic(KeyEvent.VK_M);
		settingMenu.setMnemonic(KeyEvent.VK_S);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		// 注册监听器
		newGame.addActionListener(this);
		saveManual.addActionListener(this);
		saveManualAs.addActionListener(this);
		exitGame.addActionListener(this);

		inputManual.addActionListener(this);
		repalyGame.addActionListener(this);
		undoManual.addActionListener(this);

		gmSound.addActionListener(this);
		bgSound.addActionListener(this);

		welcome.addActionListener(this);
		helpContent.addActionListener(this);
		aboutGame.addActionListener(this);
	}
	
	/**
	 * 初始化按钮
	 * 
	 */
	private void initButtons(){
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
		
		newButton = new JButton(ChessUtil.getImageIcon("newManual.gif"));
		newButton.setToolTipText("新建棋局");
		newButton.addActionListener(this);
		newButton.setCursor(handCursor);
		newButton.setMargin(insets);
		
		repaly = new JButton(ChessUtil.getImageIcon("reprint.gif"));
		repaly.setToolTipText("重新开始");
		repaly.addActionListener(this);
		repaly.setCursor(handCursor);
		repaly.setMargin(insets);
		
		save = new JButton(ChessUtil.getImageIcon("save.gif"));
		save.addActionListener(this);
		save.setToolTipText("保存棋局");
		save.setCursor(handCursor);
		save.setMargin(insets);
		save.setOpaque(true);
		
		saveAs = new JButton(ChessUtil.getImageIcon("saveas.gif"));
		saveAs.addActionListener(this);
		saveAs.setToolTipText("另存棋局");
		saveAs.setCursor(handCursor);
		saveAs.setMargin(insets);
		
		undo = new JButton(ChessUtil.getImageIcon("undo.gif"));
		undo.addActionListener(this);
		undo.setToolTipText("悔棋");
		undo.setCursor(handCursor);
		undo.setMargin(insets);
		
		inputManualButton = new JButton(ChessUtil.getImageIcon("inputManual.gif"));
		inputManualButton.setToolTipText("输入棋谱");
		inputManualButton.addActionListener(this);
		inputManualButton.setCursor(handCursor);
		inputManualButton.setMargin(insets);
	}
	/**
	 * 初始化面板
	 * 
	 */
	private void initPanels(){
		// 构造右边的面板
		JPanel rightPanel = new JPanel(new BorderLayout());
		// 棋谱描述面板
		JPanel recordsPanel = new JPanel(new BorderLayout());
		//存放棋谱描述面板的边框
		TitledBorder recordsBorder = new TitledBorder(PropertyReader.get("CHESS_MESSAGE_TOOLTIP"));
		recordsPanel.setBorder(recordsBorder);
		recordsPanel.setPreferredSize(new Dimension(240, 330));
		recordsPanel.add(BorderLayout.CENTER, chessManual);
		//工具栏
		manualTools = new JPanel(new FlowLayout(FlowLayout.CENTER));
		manualTools.add(first);
		manualTools.add(prev);
		manualTools.add(auto);
		manualTools.add(next);
		manualTools.add(last);
		
		recordsPanel.add(BorderLayout.SOUTH, manualTools);
		rightPanel.add(BorderLayout.CENTER, recordsPanel);

		JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, board,rightPanel);
		splitH.setDividerSize(5);
		splitH.setDividerLocation(450);
		
		// 游戏状态面板
		gameStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		gameStatusPanel.setPreferredSize(new Dimension(660, 80));
		gameStatusIcon = new JLabel(ChessUtil.getImageIcon("hongshuai.gif"));
		// 游戏状态栏
		gameStatusContent = new JLabel("红方先走棋");
		gameStatusContent.setFont(new Font("宋体", Font.PLAIN, 16));
		// 游戏状态面板边框
		TitledBorder gameStatusBorder = new TitledBorder("游戏状态");
		gameStatusBorder.setTitleColor(Color.RED);
		gameStatusBorder.setTitleFont(new Font("宋体", Font.PLAIN, 16));
		gameStatusPanel.setToolTipText("游戏状态");
		gameStatusPanel.setBorder(gameStatusBorder);
		gameStatusPanel.add(gameStatusIcon);
		gameStatusPanel.add(gameStatusContent);
		
		initToolBar();
		
		add(BorderLayout.NORTH, toolBar);
		add(BorderLayout.CENTER, splitH);
		add(BorderLayout.SOUTH, gameStatusPanel);
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
		toolBar.add(repaly);
		toolBar.add(undo);
		toolBar.add(inputManualButton);
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
					} else if (code == KeyEvent.VK_ENTER) {
						auto();
					}

				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

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
	 * 上一步
	 * 
	 */

	private void prev() {
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
			record = records.get(curIndex);
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
				board.chessPoints[endI][endJ].setHasPiece(false);

			}
			// 上一步吃了棋子
			else {
				ChessPiece piece = board.chessPoints[endI][endJ].getPiece();
				board.chessPoints[startI][startJ].setPiece(piece, board);
				board.chessPoints[endI][endJ].setPiece(eatedPiece, board);

			}
		}

		// 后退时，画标记与正常移动时不同
		if (curIndex >= 1) {
			record = (ManualItem) records.get(curIndex - 1);
			moveStep = record.getMoveStep();
			startI = moveStep.start.x;
			startJ = moveStep.start.y;
			endI = moveStep.end.x;
			endJ = moveStep.end.y;
		}

		// 如果移动，应该画2条提示框
		board.setMoveFlag(true);
		board.movePoints[0] = new Point(endI, endJ);
		board.movePoints[1] = new Point(startI, startJ);

		curIndex--;
		scrollToView();

		repaint();
		validate();
	}
	/**
	 * 演示当前步的下一步
	 * 
	 */
	public void next() {
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
		while (curIndex != descs.size() - 1) {
			next();
		}
	}
	
	/**
	 * 演示index指定的一步
	 * 
	 * @param index
	 */
	private void step(int index) {
		if (index < 0) {
			return;
		}

		ManualItem moveRecord = records.get(index);
		MoveStep step = moveRecord.getMoveStep();
		Point pStart = step.start;
		Point pEnd = step.end;
		int startI = pStart.x;
		int startJ = pStart.y;
		int endI = pEnd.x;
		int endJ = pEnd.y;

		ChessPiece piece = board.chessPoints[startI][startJ].getPiece();
		board.movePiece(piece, startI, startJ, endI, endJ);

		// 演示时不能移动棋子的
		repaint();
		validate();
	}
	/**
	 * 自动演示
	 */
	private void auto() {
		first();
		DemoThread auto = new DemoThread();
		auto.start();
	}

	/**
	 * 自动演示线程
	 * @author wanghualiang
	 *
	 */
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

	/**
	 * 把棋谱列表滚动到当前行
	 */
	public void scrollToView() {
		if (curIndex >= 0 && curIndex < descs.size()) {
			// 选中当前行，提示玩家
			System.out.println("应该选中第N行：" + curIndex);
			manual.setSelectedIndex(curIndex);

			int lastIndex = curIndex;
			Rectangle rect = manual.getCellBounds(lastIndex, lastIndex);
			manualScroll.getViewport().scrollRectToVisible(rect);

		}
		if (curIndex == -1) {
			// 退回到没有任何棋子移动的状态时，不用画移动标记
			board.setMoveFlag(false);
			/*
			 * 当curIndex == -1时，应该不选中任何一样,
			 * 而manual.setSelectedIndex(-1)不能时列表框不选中任何一行
			 * manual.setListData(descs);可以恢复到默认状态不选中任何一行
			 */
			System.out.println("没有选中任何一行：" + curIndex);
			manual.setListData(descs);
		}
	}

	/**
	 * 事件响应
	 */
	@Override
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
			auto();
		} else if (source == helpContent) {
			ChessUtil.showHelpDialog();
		} else if (source == welcome) {
			ChessUtil.showWelcomeDialog();
		} else if (source == aboutGame) {
			ChessUtil.showAboutDialog();
		} 
		// 重新开始棋局
		else if (source == repaly || source == repalyGame) {
			int result = JOptionPane.showConfirmDialog(this, "您需要保存当前的棋谱吗？",
					"您需要保存当前的棋谱吗？", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				SaveDialog dialog = new SaveDialog(this);
				dialog.setVisible(true);
			}
			/*int size = chessManual.getManualItems().size();
			for (int index = 0; index < size; index++) {
				undo();
			}*/
			dispose();
			ManMachineGUI newgame = new ManMachineGUI();
			newgame.setVisible(true);
		}

		// 悔棋
		else if (source == undo || source == undoManual) {
			last();
			undo();
			undo();

		} else if (source == save || source == saveManual) {
			SaveDialog dialog = new SaveDialog(this);
			dialog.setVisible(true);

		} else if (source == saveAs || source == saveManualAs) {
			SaveAsDialog dialog = new SaveAsDialog(this);
			dialog.setVisible(true);
		} else if (source == exitGame) {
			handleExitGame();

		} else if (source == inputManualButton || source == inputManual) {
			Icon icon = null;
			if(board.getPlayerName().equals(RED_NAME)){
				 icon = ChessUtil.getImageIcon("hongshuai.gif");
			}else{
				icon = ChessUtil.getImageIcon("heijiang.gif");
			}
			
			String manual = (String) JOptionPane.showInputDialog(this, "请输入棋谱", "请输入棋谱",
					JOptionPane.PLAIN_MESSAGE,icon,null,null);
			if (manual != null) {
				manual = manual.trim();// 去掉前导空白和尾部空白
			}
			movePieceByManual(manual);

		} else if (source == newButton || source == newGame) {
			ManMachineGUI newgame = new ManMachineGUI();
			newgame.setVisible(true);
		} else if (source == bgSound){
			ChessUtil.setLoop(bgSound.isSelected());
		} else if (source == gmSound){
			ChessUtil.setStart(gmSound.isSelected());
		}
		
	}
	/**
	 * 悔棋，后退一步
	 */
	private void undo() {
		canPaly = true;
		boolean flag = chessManual.removeManualItem();
		if (flag) {
			if (board.getPlayerName().equals(RED_NAME)) {
				board.changeSide();
				updateGameStatus(2, "轮到黑方走喽！");
			} else {
				board.changeSide();
				updateGameStatus(1, "轮到红方走喽！");
			}
			curIndex--;
		}
	}

	/**
	 * 处理关闭事件
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
	 * 更新游戏状态
	 * 
	 * @param state
	 *            图标标识
	 * @param content
	 *            游戏状态文字描述
	 */
	public void updateGameStatus(int state, String content) {

		switch (state) {
		case 1:
			gameStatusIcon.setIcon(ChessUtil.getImageIcon("hongshuai.gif"));
			gameStatusIcon.setToolTipText("轮到红方走喽！");
			break;
		case 2:
			gameStatusIcon.setIcon(ChessUtil.getImageIcon("heijiang.gif"));
			gameStatusIcon.setToolTipText("轮到黑方走喽！");
			break;
		default:
			break;
		}

		if (content != null && !content.equals("")) {
			gameStatusContent.setText(content);
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
			if (movePiece == null|| !movePiece.getName().equals(board.getPlayerName())) {
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
					endY, board.chessPoints, board.getPalyerFirst());
			System.out.println("能否移动棋子：" + rule);
			if (rule) {
				//添加一条棋谱信息，要在移动之前添加，否则被吃掉的棋子无法记录
				board.addChessRecord(movePiece, startX, startY, endX, endY);
				//移动棋子
				board.movePiece(movePiece, startX, startY, endX, endY);
				// 打谱类当前索引+1
				curIndex++;

				// 播放棋子被吃声音
				ChessUtil.playSound("eat.wav");

				validate();
				repaint();

				// 更换己方名字，实现一人轮流走红黑2方的棋子
				board.changeSide();
				computerThink();

			}
		}
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


	private void makeMove(ManualItem chessMove) {
		//moveStack.add(chessMove);
		MoveStep moveStep = chessMove.getMoveStep();
		int startX = (int) moveStep.getStart().getX();
		int startY = (int) moveStep.getStart().getY();
		int endX = (int) moveStep.getEnd().getX();
		int endY = (int) moveStep.getEnd().getY();

		ChessPiece piece = tempChessPoints[startX][startY].getPiece();
		ChessPiece eatedPiece = tempChessPoints[endX][endY].getPiece();
		//tempChessPoints[endX][endY].removePiece(eatedPiece, tempBoard);
		//tempChessPoints[endX][endY].setPiece(piece, tempBoard);
		tempChessPoints[endX][endY].setPiece(piece);
		tempChessPoints[startX][startY].setHasPiece(false);

		if(tempBoard.getPlayerName().equals(RED_NAME)){
			tempBoard.setPlayerName(BLACK_NAME);
		}else{
			tempBoard.setPlayerName(RED_NAME);
		}
	}

	private void unMakeMove(ManualItem chessMove) {
		//ManualItem chessMove = moveStack.pop();
		MoveStep moveStep = chessMove.getMoveStep();
		PieceId eatedPieceId = chessMove.getEatedPieceId();

		int startX = (int) moveStep.getStart().getX();
		int startY = (int) moveStep.getStart().getY();
		int endX = (int) moveStep.getEnd().getX();
		int endY = (int) moveStep.getEnd().getY();

		ChessPiece piece = tempChessPoints[endX][endY].getPiece();
		ChessPiece eatedPiece = PieceUtil.createPiece(eatedPieceId);
		tempChessPoints[startX][startY].setPiece(piece);
		//tempChessPoints[startX][startY].setPiece(piece, tempBoard);
		if (eatedPiece == null) { // 上一步没有吃棋子
			tempChessPoints[endX][endY].setHasPiece(false);
		} else {// 上一步吃了棋子
			tempChessPoints[endX][endY].setPiece(eatedPiece);
			//tempChessPoints[endX][endY].setPiece(eatedPiece, tempBoard);
			//eatedPiece.addMouseListener(tempBoard.getMouseAdapter());
		}

		if(tempBoard.getPlayerName().equals(RED_NAME)){
			tempBoard.setPlayerName(BLACK_NAME);
		}else{
			tempBoard.setPlayerName(RED_NAME);
		}
	}

	/**
	 * 人机对战测试程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		ManMachineGUI ai = new ManMachineGUI();
		ai.setVisible(true);

	}


	


	/**
	 * （非 Javadoc）
	 * 
	 * @see com.fans.chess.common.ISaveManual#getSavePaths()
	 */
	public ArrayList<String> getSavePaths() {
		ArrayList<String> paths = new ArrayList<String>();
		String path = "src/manuals/ai/";
		String path2 = "src/manuals/ai/";

		paths.add(path);
		paths.add(path2);
		return paths;
	}

	public GameRecord getGameRecord() {
		GameRecord gameRecord = new GameRecord(ManualType.MAN_MACHINE, ChessUtil
				.getDateAndTime(), "", board.chessManual.getManualItems(),
				board.chessManual.descs, null ,board.getPalyerFirst());
		return gameRecord;
	}

	public void computerThink() {
		isLock = true;//锁定棋盘，玩家无法再点击棋子
		new Thread(){
			@Override
			public void run(){
				 tempBoard = new ManMachineBoard();
				 for(int i = 1; i <= 9; i++ ){
					 for(int j = 1; j <= 10; j++ ){
						 if(chessPoints[i][j]!=null){
							 try {
								tempChessPoints[i][j] = (ChessPoint) chessPoints[i][j].clone();//深复制
								} catch (CloneNotSupportedException e) {e.printStackTrace();}
						 }
					 }
				 }
				 tempBoard.setPlayerName(board.getPlayerName());
				bestChessMove = null;maxDepth = 4;
				alphaBetaSearch(maxDepth,-MAX_VALUE, MAX_VALUE);
				if (bestChessMove == null) {
					return;
				}
				MoveStep step = bestChessMove.getMoveStep();
				int startX = (int) step.getStart().getX();int startY = (int) step.getStart().getY();
				int endX = (int) step.getEnd().getX();int endY = (int) step.getEnd().getY();
				ChessPiece movePiece = chessPoints[startX][startY].getPiece();
				board.addChessRecord(movePiece, startX, startY, endX, endY);
				board.movePiece(movePiece, startX, startY, endX, endY);
				isLock = false;//解锁棋盘
				curIndex++;board.changeSide();
				if(bestChessMove.getEatedPieceId() != null && 
				bestChessMove.getEatedPieceId().equals(PieceCategory.JIANG)){
					JOptionPane.showConfirmDialog(null, "游戏结束,红方获胜", "提示",
							JOptionPane.YES_OPTION);
					ChessUtil.playSound("gameover.wav");
					canPaly = false;
				}else if(bestChessMove.getEatedPieceId() != null && 
						bestChessMove.getEatedPieceId().equals(PieceCategory.SHUAI)){
					JOptionPane.showConfirmDialog(null, "游戏结束,黑方获胜", "提示",
							JOptionPane.YES_OPTION);
					ChessUtil.playSound("gameover.wav");
					canPaly = false;
				}	
			}
		}.start();
	}

	/**
	 * 
	 * 
	 * @param depth
	 * @return
	 */
	private int alphaBetaSearch(int depth, int alpha, int beta) {
		// int best = 0;
		int value = 0;

		// best = -MAX_VALUE;

		if (depth == 0) {
			return AIUtil.evaluate(tempBoard.getPlayerName(), tempChessPoints, board.getPalyerFirst());
		}

		ArrayList<ManualItem> chessMoves = AIUtil.generateAllChessMove(tempBoard
				.getPlayerName(), tempChessPoints , board.getPalyerFirst());
		//printChessMove(chessMoves);
		int size = chessMoves.size();

		for (int index = 0; index < size; index++) {
			ManualItem chessMove = chessMoves.get(index);
			makeMove(chessMove);
			value = -alphaBetaSearch(depth - 1, -beta, -alpha);
			unMakeMove(chessMove);

			if (value >= beta) {
				return beta;
			}
			if (value > alpha) {
				alpha = value;
				if (depth == maxDepth) {
					bestChessMove = chessMove;
					System.out.println("maxSearch找到了最佳走法！");
				}
			}

		}
		return alpha;

	}
	/**
	 * 
	 * @param depth 深度
	 * @param palyer 为true代表电脑为max方；false代表玩家为min方
	 * @param alpha 下界
	 * @param beta 上界
	 * @return
	 */
	private int alphaBetaSearch2(int depth,Boolean palyer, int alpha, int beta) {
		if (depth == 0) {
			return AIUtil.evaluate(tempBoard.getPlayerName(),
					tempChessPoints, board.getPalyerFirst());
		}
		ArrayList<ManualItem> chessMoves = AIUtil.generateAllChessMove(board
				.getPlayerName(), chessPoints, board.getPalyerFirst());
		int size = chessMoves.size();
		if (palyer) {//max
			for (int index = 0; index < size; index++) {
				ManualItem chessMove = chessMoves.get(index);
				makeMove(chessMove);
				int value = alphaBetaSearch2(depth - 1,!palyer,alpha, beta);
				unMakeMove(chessMove);
				if(value > alpha){//通过向上传递的子节点beta值修正alpha值 
					alpha = value;
					bestChessMove = chessMove;//记录最好走法
					if(alpha >= beta) {   //发生 beta剪枝 
	                    return alpha;
	                }
				}
			}
			return alpha;	
		}
		else{//min
			for (int index = 0; index < size; index++) {
				ManualItem chessMove = chessMoves.get(index);
				makeMove(chessMove);
				int value = alphaBetaSearch2(depth - 1,!palyer, alpha, beta);
				unMakeMove(chessMove);
				if(value < beta){//通过向上传递的子节点alpha值修正beta值  
					beta = value;
					bestChessMove = chessMove;//记录最好走法
					System.out.println("minSearch找到了最佳走法！787");
					if(alpha >= beta) {   //发生 alpha剪枝 
	                    return beta;
	                }
				}
			}
			return beta;
		}
	}
	

	

}
