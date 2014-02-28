package SearchingUI;

import indexReader.InverseIndex;
import indexReader.Doc2MD5;
import indexReader.MD52Doc;
import indexReader.PageRank;

import indexbuilder.IndexBuilder;
import indexbuilder.SplitDocuments;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by soushimei on 2/19/14.
 * 
 * Note: some of the code below are adopted and modified from:
 * http://zetcode.com/tutorials/javaswingtutorial
 */
public class SearchICS extends JFrame {
	public SearchICS() {
		init();
	}
	
	public static void setResultPath(String path) {
		resultPath = path;
	}
	
	public static String getResultPath() {
		return resultPath;
	}
	
	private void init() {
		setTitle("Search Engin for ICS Domain");
		setSize(600, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		// text area for querying words
		this.inputArea = new JTextField("Type your query...", 25);
		inputArea.setEditable(true);
		inputArea.setColumns(40);
		inputArea.setBounds(50, 100, 500, 30);
		inputArea.setHorizontalAlignment(JTextField.LEFT);
		inputArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog ad = new AboutDialog();
				ad.setVisible(true);
			}
		});
		mainPanel.add(inputArea);
		
		// button
		quitButton = new JButton("Quit");
		quitButton.setBounds(150, 150, 80, 30);
		quitButton.setToolTipText("Exit this program");
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mainPanel.add(quitButton);
		
		aboutButton = new JButton("About");
		aboutButton.setBounds(250, 150, 80, 30);
		aboutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				AboutDialog ad = new AboutDialog();
				ad.setVisible(true);
			}
		});
		mainPanel.add(aboutButton);
		
		searchButton = new JButton("Search");
		searchButton.setBounds(350, 150, 80, 30);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				ResultDialog sr = new ResultDialog();
				sr.setVisible(true);
			}
		});
		mainPanel.add(searchButton);

		// menu bar
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(exitMenuItem);
		menubar.add(file);
		
		setJMenuBar(menubar);
	}
	
	/**
	 * About panel. 
	 */
	class AboutDialog extends JDialog {
		public AboutDialog() {
			initAbout();
		}
		
		private void initAbout() {
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			Panel aboutPanel = new Panel();
			aboutPanel.setPreferredSize(getPreferredSize());
			setLayout(new BorderLayout(10, 10));
			
			queryWords = inputArea.getText();
			String about = "<html>This software is for the ICS 221 course project 3. <br>"
					+ "It is a simple GUI program that provides web page query services.<br> " 
					+ "Author: Siming Song (42682148) and Liqiang Wang (93845414)<br>"
					+ "<b>Result Page: </b>" + getResultPath() + "</html>";
			JLabel aboutText = new JLabel(about);
	        aboutText.setFont(new Font("", Font.PLAIN, 13));
	        aboutText.setAlignmentX(0.5f);
	        aboutPanel.add(aboutText, BorderLayout.BEFORE_FIRST_LINE);
	        
	        aboutPanel.add(Box.createRigidArea(new Dimension(0, 150)));
	        
	        JButton close = new JButton("Close");
	        close.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	                dispose();
	            }
	        });
	        close.setAlignmentX(0.5f);
	        aboutPanel.add(close, BorderLayout.PAGE_END);
	        
	        add(aboutPanel);
	        pack();
	        
	        setModalityType(ModalityType.APPLICATION_MODAL);
	        
	        setTitle("About");
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setSize(500, 270);
		}
	}
	
	/**
	 * Query result panel, shown after clicking "Search". 
	 */
	class ResultDialog extends JDialog {
		public ResultDialog() {
			queryWords = inputArea.getText();
			
			if (queryWords == null || queryWords == "")
				return;
			
			ResultPage result = new ResultPage(queryWords, getResultPath());
			path = result.getResultPath();
			
			initSearchResult();
			
			// Try to open the result page on default browser.
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
				
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					try {
						desktop.browse(new URI("file://" + path));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		private void initSearchResult() {
			JPanel panel = new JPanel();
	        panel.setLayout(new BorderLayout());
	        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	        JScrollPane pane = new JScrollPane();
	        JTextPane textPane = new JTextPane();

	        textPane.setContentType("text/html");
	        textPane.setEditable(false);

	        textPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

	        // load the file
	        try {
	        	File f = new File(path);
				textPane.setPage(f.toURI().toURL());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
//	        add(textPane);
	        pane.getViewport().add(textPane);
	        panel.add(pane);

	        add(panel);
	        pack();
			
			setTitle("Searching Result");
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setSize(1024, 768);
		}
		
		private Desktop desktop = null;
		
		private String path = null;
	}
	
	private String queryWords = "uci";
	
	private static String resultPath = null;
	
	JTextField inputArea = null;
	JButton quitButton = null;
	JButton aboutButton = null;
	JButton searchButton = null;
	
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
        
        if (args.length != 1) {
        	System.err.println("Usage: <path>");
        }
        
        setResultPath(args[0] + "/ResultPages/");
        
        // load index first
        IndexBuilder ib = new IndexBuilder(args[0]);
        try {
			ib.buildIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SearchICS ex = new SearchICS();
				ex.setVisible(true);
			}
		});
	 }
}
