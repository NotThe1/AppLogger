package appLogger;

/*
 * @version 2.1
 * 
 * 2020-03-10 - changed method  name:
 * from:  infof(Color color,String format, Object... args)
 * to  :  post(Color color,String format, Object... args)
 * 
 * 
 * 2020-02-28 - Established as a Jar 
 * 
 * 2020-02-21 - Added Color argument for method:
 *    infof(Color color,String format, Object... args)
 *    
 *    
 * Established in a stand alone project
 * This is set up as a sington class. Not to be used alone
 * Only one class that uses this should supply the StyledDocument
 *   via setDoc(StyledDocument docLog). The setText pane should also be
 *   called by the same class ( to enable popUp menu). 
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AppLogger {

	private StyledDocument docLog; // keep from failing if not set by a using Class
	private JTextPane txtLog;
	private JPopupMenu popupLog;
	private AdapterLog logAdaper = new AdapterLog();
	private String header;
	
	private int countError;
	private int countWarning;
	private int countInfo;
	private int countSpecial;

	private static AppLogger instance = new AppLogger();

	public static AppLogger getInstance() {
		return instance;
	}// getInstance

	private AppLogger() {
		setAttributes();
	}// Constructor

	public void setDoc(StyledDocument docLog) {
		this.docLog = docLog;
	}// setDoc

	public void setTextPane(JTextPane textPane) {
		setTextPane(textPane, "Application Log");
	}// setTextPane

	public void setTextPane(JTextPane textPane, String header) {
		this.txtLog = textPane;
		this.header = header;
		this.docLog = textPane.getStyledDocument();

		popupLog = new JPopupMenu();
		addPopup(txtLog, popupLog);

		JMenuItem popupLogClear = new JMenuItem("Clear Log");
		popupLogClear.setName(PUM_LOG_CLEAR);
		popupLogClear.addActionListener(logAdaper);
		popupLog.add(popupLogClear);

		JSeparator separator = new JSeparator();
		popupLog.add(separator);

		JMenuItem popupLogPrint = new JMenuItem("Print Log");
		popupLogPrint.setName(PUM_LOG_PRINT);
		popupLogPrint.addActionListener(logAdaper);
		popupLog.add(popupLogPrint);

	}// setTextPane
	/*------------------------------------*/

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.isPopupTrigger()) {
					showMenu(mouseEvent);
				} // if popup Trigger
			}// mousePressed

			public void mouseReleased(MouseEvent mouseEvent) {
				if (mouseEvent.isPopupTrigger()) {
					showMenu(mouseEvent);
				} // if
			}// mouseReleased

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}// showMenu
		});
	}// addPopup
	/*------------------------------------*/

	private void doLogClear() {
		this.clear();
	}// doLogClear

	private void doLogPrint() {
		Font originalFont = txtLog.getFont();
		try {
			txtLog.setFont(originalFont.deriveFont(8.0f));
			MessageFormat headerMessage = new MessageFormat(header);
			MessageFormat footerMessage = new MessageFormat(new Date().toString() + "           Page - {0}");
			txtLog.print(headerMessage, footerMessage);
			txtLog.setFont(originalFont);
		} catch (PrinterException e) {
			txtLog.setFont(originalFont);
			error("java.awt.print.PrinterAbortException");
			e.printStackTrace();
		} // try
	}// doLogPrint

	/*------------------------------------*/
	private static final String PUM_LOG_PRINT = "popupLogPrint";
	private static final String PUM_LOG_CLEAR = "popupLogClear";

	// ---------------------------------------------------------------------
	
	public void resetCounts() {
		countError = 0;
		countWarning = 0;
		countInfo = 0;
		countSpecial = 0;
	}//resetCounts
	
	public int getErrorCount(){
		return countError;
	}//getErrorCount

	public int getWarningCount(){
		return countWarning;
	}//getWarningCount

	public int getInfoCount(){
		return countInfo;
	}//getInfoCount

	public int getSpecialCount(){
		return countSpecial;
	}//getSpecialCount
	public void clear() {
		try {
			docLog.remove(0, docLog.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		} // try
	}// clear

	public void addNL(int linesToSkip) {
		int lines = Math.max(linesToSkip, 0);
		lines = Math.min(lines, 15);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines; i++) {
			sb.append(NL);
		} // for
		insertListing(sb.toString(), null);
	}// addNL

	public void addNL() {
		insertListing(NL, null);
	}// addNL

	private void addMeta(SimpleAttributeSet attr, String... message) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < message.length; i++) {
			sb.append(message[i]);
			sb.append(NL);
		} // for
		insertListing(sb.toString(), attr);
	}// addMeta

	public void info(String... message) {
		addMeta(attrBlack, message);
	}// info

	public void infof(String format, Object... args) {
		insertListing(doFormat(format, args), attrBlack);
	}// infof

	public void infoCount(String... message) {
		countInfo++;
		info( message);
	}// infoCount

	public void infofCount(String format, Object... args) {
		countInfo++;
		infof(format, args);
	}// infofCount

	public void post(Color color,String format, Object... args) {
		StyleConstants.setForeground(attrCustom, color);
		insertListing(doFormat(format, args), attrCustom);
	}// post
	
	public void warn(String... message) {
		addMeta(attrBlue, message);
	}// warn

	public void warnf(String format, Object... args) {
		insertListing(doFormat(format, args), attrBlue);
	}// warnf
	
	public void warnCount(String... message) {
		countWarning++;
		warn( message);
	}// warnCount

	public void warnfCount(String format, Object... args) {
		countWarning++;
		warnf(format, args);
	}// warnfCount
	
	public void error(String... message) {
		addMeta(attrRed, message);
	}// error

	public void errorf(String format, Object... args) {
		insertListing(doFormat(format, args), attrRed);
	}// error
	
	public void errorCount(String... message) {
		countError++;
		error(message);
	}// errorCount

	public void errorfCount(String format, Object... args) {
		countError++;
		errorf(format, args);
	}// errorfCount

	public void special(String... message) {
		addMeta(attrTeal, message);
	}// special

	public void specialf(String format, Object... args) {
		insertListing(doFormat(format, args), attrTeal);
	}// specialf

	public void specialCount(String... message) {
		countSpecial++;
		special( message);
	}// specialCount

	public void specialfCount(String format, Object... args) {
		countSpecial++;
		specialf(format, args);
	}// specialfCount

	private String doFormat(String format, Object... args) {
		Formatter formatter = new Formatter();
		String ans = formatter.format(format, args).toString();
		formatter.close();
		return ans;
	}// doFormat

	// ----------Time-------------------------------

	public Date addTimeStamp() {
		Date now = new Date();
		addMeta(attrMaroon, now.toString());
		return now;
	}//addTimeStamp

	public Date addTimeStamp(String message) {
		Date now = new Date();
		insertListing(message + " " + now.toString() + System.lineSeparator(), attrMaroon);
		return now;
	}// addTimeStamp

	public Date getElapsedTime(Date startTime) {
		Date endTime = new Date();
		String message = measureElapsedTimeToString(startTime, endTime);
		String display = String.format("   End     time : %s%n   Start    time : %s%nElapsed time : %s%n%n", endTime.toString(),startTime.toString(),message);
		insertListing(display, attrMaroon);
		//insertListing(message + " " + endTime.toString() + System.lineSeparator(), attrMaroon);
		return endTime;
	}// addElapsedTime

//	public Date getElapsedTime(Date startTime, String message) {
//		Date endTime = new Date();
//		insertListing(message + " " + endTime.toString() + System.lineSeparator(), attrMaroon);
//		String elapsedTime = measureElapsedTimeToString(startTime, endTime);
//		insertListing(elapsedTime + System.lineSeparator(), attrMaroon);
//		return endTime;
//	}// addElapsedTime

	private static String measureElapsedTimeToString(Date startDate, Date endDate) {
		String result = "";
		Map<TimeUnit, Long> times = measureElapsedTime(startDate, endDate);
		List<TimeUnit> timeUnits = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
		long duration;
		boolean nonZeroFlag = false;

		for (TimeUnit timeUnit : timeUnits) {
			duration = times.get(timeUnit);
			if (duration == 0) {
				if (!nonZeroFlag) {
					continue;
				} // if skip zero value
			} else {
				nonZeroFlag = true;
			} // outer if
			result = String.format("%s = %,d, %s ", timeUnit.toString(), times.get(timeUnit), result);
		} // for time Unit

		return result;
	}// getElapsedTimeToString

	private static Map<TimeUnit, Long> measureElapsedTime(Date startDate, Date endDate) {
		Map<TimeUnit, Long> result = new HashMap<TimeUnit, Long>();
		long diffInMilliseconds = endDate.getTime() - startDate.getTime();
		List<TimeUnit> timeUnits = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
		Collections.reverse(timeUnits); // Days to Nanoseconds
		long difference, milliSecondsLeftPerUnit;
		for (TimeUnit timeUnit : timeUnits) {
			difference = timeUnit.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
			result.put(timeUnit, difference);
			milliSecondsLeftPerUnit = timeUnit.toMillis(difference);
			diffInMilliseconds -= milliSecondsLeftPerUnit;
		} // for each time unit
		return result;
	}// getElapsedTime

	// ----------Time-------------------------------

	private void insertListing(String str, SimpleAttributeSet attr) {
		try {
			docLog.insertString(docLog.getLength(), str, attr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} // try
	}// insertSource
	
	// ----------------------------------------------------------------------
	private void setAttributes() {
		StyleConstants.setForeground(attrNavy, new Color(0, 0, 128));
		StyleConstants.setForeground(attrBlack, new Color(0, 0, 0));
		StyleConstants.setForeground(attrBlue, new Color(0, 0, 255));
		StyleConstants.setForeground(attrGreen, new Color(0, 128, 0));
		StyleConstants.setForeground(attrTeal, new Color(0, 128, 128));
		StyleConstants.setForeground(attrGray, new Color(128, 128, 128));
		StyleConstants.setForeground(attrRed, new Color(255, 0, 0));
		StyleConstants.setForeground(attrMaroon, new Color(128, 0, 0));
	}// setAttributes

	private SimpleAttributeSet attrCustom = new SimpleAttributeSet();
	private SimpleAttributeSet attrBlack = new SimpleAttributeSet();
	private SimpleAttributeSet attrBlue = new SimpleAttributeSet();
	private SimpleAttributeSet attrGray = new SimpleAttributeSet();
	private SimpleAttributeSet attrGreen = new SimpleAttributeSet();
	private SimpleAttributeSet attrRed = new SimpleAttributeSet();
	private SimpleAttributeSet attrNavy = new SimpleAttributeSet();
	private SimpleAttributeSet attrMaroon = new SimpleAttributeSet();
	private SimpleAttributeSet attrTeal = new SimpleAttributeSet();

	public static final Integer INFO = 0;		// Black
	public static final Integer WARNING = 1;	// Blue
	public static final Integer ERROR = 2;		// Red
	public static final Integer SPECIAL = 4;	// Teal
	public static final Integer TIME = 5;		// Maroon
	

	private static final String NL = System.lineSeparator();

	/*------------------------------------*/
	class AdapterLog implements ActionListener { // logAdapter
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String name = ((Component) actionEvent.getSource()).getName();
			switch (name) {
			case PUM_LOG_PRINT:
				doLogPrint();
				break;
			case PUM_LOG_CLEAR:
				doLogClear();
				break;
			}// switch
		}// actionPerformed
	}// class AdapterLog

	/*------------------------------------*/

}// class AppLogger
