package testAppLogger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import appLogger.AppLogger;

public class TestAppLogger {

	AppLogger log = AppLogger.getInstance();

	private JFrame frame;
	private JTextPane txtLog;
	private String title = "TestAppLogger     1.0";
	private JSplitPane splitPane;
	private JPanel panel;
	private JButton btnTestTime;
	private Component verticalStrut;
	private Component verticalStrut_1;
	private JButton btnTestInfo;
	private Component verticalStrut_2;
	private JButton btnInitCounts;
	private JButton btnTestError;
	private Component verticalStrut_3;
	private JButton btnNTestWarn;
	private Component verticalStrut_4;
	private Component verticalStrut_5;
	private JButton btnTestSpecial;
	private Component verticalStrut_6;
	private JButton btnTestPost;
	private Component verticalStrut_7;
	private JButton btnDoAll;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestAppLogger window = new TestAppLogger();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}// run
		});
	}// main

	private void testInitializeCounts() {
		log.info("--------------------------------");
		log.info("testInitializeCounts");
		log.addNL();
		log.info("resetCounts()");
		log.resetCounts();
		log.addNL();
		log.info("getInfoCount() = " + log.getInfoCount());
		log.info("getErrorCount() = " + log.getErrorCount());
		log.info("getWarningCount() = " + log.getWarningCount());
		log.info("getSpecialCount() = " + log.getSpecialCount());
	}// testInitializeCounts

	private void testInfo() {
		log.info("--------------------------------");
		log.info("testInfo");
		log.addNL();
		log.info("log.info()");
		log.infof("%04X, %s%n", 17, "log.infof");
		log.addNL();
		log.info("getInfoCount() = " + log.getInfoCount());
		log.infoCount("First info for counter");
		log.infofCount("%s%n", "First infof for counter");
		log.info("getInfoCount() = " + log.getInfoCount());
	}// testInfo

	private void testError() {
		log.info("--------------------------------");
		log.info("testError");
		log.addNL();
		log.error("log.error()");
		log.errorf("%04X, %s%n", 257, "log.errorf");
		log.addNL();
		log.error("getErrorCount() = " + log.getErrorCount());
		log.errorCount("First error for counter");
		log.errorfCount("%s%n", "First errorf for counter");
		log.error("getErrorCount() = " + log.getErrorCount());
	}// testError

	private void testWarning() {
		log.info("--------------------------------");
		log.info("testWarning");
		log.addNL();
		log.warn("log.warn()");
		log.warnf("%04X, %s%n", 257, "log.warnf");
		log.addNL();
		log.warn("getErrorCount() = " + log.getWarningCount());
		log.warnCount("First warn for counter");
		log.warnfCount("%s%n", "First warnf for counter");
		log.warn("getWarningCount() = " + log.getWarningCount());
	}// testWarning

	private void testSpecial() {
		log.info("--------------------------------");
		log.info("testSpecial");
		log.addNL();
		log.special("log.special()");
		log.specialf("%04X, %s%n", 257, "log.specialf");
		log.addNL();
		log.special("getSpecialCount() = " + log.getSpecialCount());
		log.specialCount("First special for counter");
		log.specialfCount("%s%n", "First specialf for counter");
		log.special("getSpecialCount() = " + log.getSpecialCount());
	}// testSpecial

	private void testPost() {
		log.info("--------------------------------");
		log.info("testPost");
		log.addNL();
		log.post(Color.black, "%s- Log.post(Color,Format,String)%n", "Color.black");
		log.post(Color.cyan, "%s- Log.post(Color,Format,String)%n", "Color.cyan");
		log.post(Color.blue, "%s- Log.post(Color,Format,String)%n", "Color.blue");
		log.post(Color.red, "%s- Log.post(Color,Format,String)%n", "Color.red");
		log.post(Color.ORANGE, "%s- Log.post(Color,Format,String)%n", "Color.ORANGE");
		log.post(Color.GREEN, "%s- Log.post(Color,Format,String)%n", "Color.GREEN");
	}// testPost

	private void testTime() {
		log.info("--------------------------------");
		log.info("testTime");
		try {
			log.addNL();
			log.info("log.addTimeStamp()");
			Date startTime1 = log.addTimeStamp();

			TimeUnit.SECONDS.sleep(1);

			log.addNL();
			log.info("log.addTimeStamp(String)");
			log.addTimeStamp("1 Second Later");

			log.addNL();
			log.info("getElapsedTime(Date)");
			log.getElapsedTime(startTime1);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// testInitializeCounts

	/**
	 * Create the application.
	 */

	private Preferences getPreferences() {
		return Preferences.userNodeForPackage(TestAppLogger.class).node(this.getClass().getSimpleName());
	}// getPreferences

	private void appClose() {
		Preferences myPrefs = getPreferences();
		Dimension dim = frame.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frame.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane.getDividerLocation());
		myPrefs = null;
	}// appClose

	private void appInit() {
		// frame.setBounds(100, 100, 736, 488);

		Preferences myPrefs = getPreferences();
		frame.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frame.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane.setDividerLocation(myPrefs.getInt("Divider", 250));
		myPrefs = null;

		log.setTextPane(txtLog);
		log.setDoc(txtLog.getStyledDocument());
		log.addTimeStamp("Starting....");

	}// appInit

	public TestAppLogger() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}// windowClosing
		});
		frame.setBounds(100, 100, 736, 488);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		frame.getContentPane().add(splitPane, gbc_splitPane);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		JLabel lblNewLabel = new JLabel("Application Log");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.GREEN);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		scrollPane.setColumnHeaderView(lblNewLabel);

		txtLog = new JTextPane();
		scrollPane.setViewportView(txtLog);

		panel = new JPanel();
		splitPane.setLeftComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		
		verticalStrut_7 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_7 = new GridBagConstraints();
		gbc_verticalStrut_7.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_7.gridx = 0;
		gbc_verticalStrut_7.gridy = 0;
		panel.add(verticalStrut_7, gbc_verticalStrut_7);
		
		btnDoAll = new JButton("Do All Tests");
		btnDoAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testInitializeCounts();
				testInfo();
				testError();
				testWarning();
				testSpecial();
				testPost();
				testTime();
			}
		});
		GridBagConstraints gbc_btnDoAll = new GridBagConstraints();
		gbc_btnDoAll.insets = new Insets(0, 0, 5, 0);
		gbc_btnDoAll.gridx = 0;
		gbc_btnDoAll.gridy = 1;
		panel.add(btnDoAll, gbc_btnDoAll);

		verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_2.gridx = 0;
		gbc_verticalStrut_2.gridy = 2;
		panel.add(verticalStrut_2, gbc_verticalStrut_2);

		btnInitCounts = new JButton("Initialize Counts");
		btnInitCounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testInitializeCounts();
			}
		});
		GridBagConstraints gbc_btnInitCounts = new GridBagConstraints();
		gbc_btnInitCounts.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInitCounts.insets = new Insets(0, 0, 5, 0);
		gbc_btnInitCounts.gridx = 0;
		gbc_btnInitCounts.gridy = 3;
		panel.add(btnInitCounts, gbc_btnInitCounts);

		verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 4;
		panel.add(verticalStrut, gbc_verticalStrut);

		btnTestInfo = new JButton("Test Info");
		btnTestInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testInfo();
			}
		});
		GridBagConstraints gbc_btnTestInfo = new GridBagConstraints();
		gbc_btnTestInfo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTestInfo.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestInfo.gridx = 0;
		gbc_btnTestInfo.gridy = 5;
		panel.add(btnTestInfo, gbc_btnTestInfo);

		verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 0;
		gbc_verticalStrut_1.gridy = 6;
		panel.add(verticalStrut_1, gbc_verticalStrut_1);

		btnTestTime = new JButton("Test Time");
		btnTestTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				testTime();
			}
		});

		btnTestError = new JButton("Test Error");
		btnTestError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testError();
			}
		});
		GridBagConstraints gbc_btnTestError = new GridBagConstraints();
		gbc_btnTestError.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTestError.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestError.gridx = 0;
		gbc_btnTestError.gridy = 7;
		panel.add(btnTestError, gbc_btnTestError);

		verticalStrut_3 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_3 = new GridBagConstraints();
		gbc_verticalStrut_3.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_3.gridx = 0;
		gbc_verticalStrut_3.gridy = 8;
		panel.add(verticalStrut_3, gbc_verticalStrut_3);

		btnNTestWarn = new JButton("Test Warn");
		btnNTestWarn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testWarning();
			}
		});
		GridBagConstraints gbc_btnNTestWarn = new GridBagConstraints();
		gbc_btnNTestWarn.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNTestWarn.insets = new Insets(0, 0, 5, 0);
		gbc_btnNTestWarn.gridx = 0;
		gbc_btnNTestWarn.gridy = 9;
		panel.add(btnNTestWarn, gbc_btnNTestWarn);

		verticalStrut_4 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_4 = new GridBagConstraints();
		gbc_verticalStrut_4.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_4.gridx = 0;
		gbc_verticalStrut_4.gridy = 10;
		panel.add(verticalStrut_4, gbc_verticalStrut_4);

		btnTestSpecial = new JButton("Test Special");
		btnTestSpecial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testSpecial();
			}
		});
		GridBagConstraints gbc_btnTestSpecial = new GridBagConstraints();
		gbc_btnTestSpecial.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTestSpecial.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestSpecial.gridx = 0;
		gbc_btnTestSpecial.gridy = 11;
		panel.add(btnTestSpecial, gbc_btnTestSpecial);

		verticalStrut_6 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_6 = new GridBagConstraints();
		gbc_verticalStrut_6.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_6.gridx = 0;
		gbc_verticalStrut_6.gridy = 12;
		panel.add(verticalStrut_6, gbc_verticalStrut_6);

		btnTestPost = new JButton("Test Post");
		btnTestPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testPost();
			}
		});
		GridBagConstraints gbc_btnTestPost = new GridBagConstraints();
		gbc_btnTestPost.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTestPost.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestPost.gridx = 0;
		gbc_btnTestPost.gridy = 13;
		panel.add(btnTestPost, gbc_btnTestPost);

		verticalStrut_5 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_5 = new GridBagConstraints();
		gbc_verticalStrut_5.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_5.gridx = 0;
		gbc_verticalStrut_5.gridy = 14;
		panel.add(verticalStrut_5, gbc_verticalStrut_5);
		GridBagConstraints gbc_btnTestTime = new GridBagConstraints();
		gbc_btnTestTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTestTime.gridx = 0;
		gbc_btnTestTime.gridy = 15;
		panel.add(btnTestTime, gbc_btnTestTime);
		splitPane.setDividerLocation(200);
	}// initialize

}// class UniversalWindowTest
