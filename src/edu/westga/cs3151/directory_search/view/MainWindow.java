package edu.westga.cs3151.directory_search.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import edu.westga.cs3151.directory_search.model.DirectoryWalker;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private DirectoryWalker searcher;
	
	private JPanel panel;
	
	private JRadioButton allButton;
	private JRadioButton onlyFilesButton;
	private JRadioButton onlyDirsButton;
	
	private JTextField patternMatchTextField;
	
	private JTextArea outputTextArea;

	public MainWindow() {
		this.searcher = new DirectoryWalker();
		
		this.allButton = new JRadioButton("Files and directories");
		this.onlyFilesButton = new JRadioButton("Files only");
		this.onlyDirsButton = new JRadioButton("Directories only");	
		
		this.panel = new JPanel();
	
		this.patternMatchTextField = new JTextField(20);
		
		this.outputTextArea = new JTextArea();
		this.outputTextArea.setEditable(false);
	}
	
	private void buildWindow() {
		try {
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());	
		} catch (Exception e) {}
		
		this.setTitle("Recursive Directory Walker");
		
		this.addSearchSettings();
		this.addPatternTextField();

		this.addSearchButton();
		
		this.outputTextArea.setColumns(60);
		this.outputTextArea.setRows(9);
		
		var scrollPane = new JScrollPane(this.outputTextArea);
		var outputTextPanel = new JPanel();
		outputTextPanel.add(scrollPane);
		
		this.add(this.panel);
		this.add(outputTextPanel);
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.pack();
	}
	
	public void launch() {
		this.buildWindow();
		this.setVisible(true);
	}

	private void searchFiles() {
		var chooser = this.buildFileChooser();
		var startingDir = chooser.getSelectedFile();
		
		if (startingDir != null) {
			var pattern = this.patternMatchTextField.getText();
			
			List<File> foundFiles;
			if (this.allButton.isSelected()) {
				foundFiles = searcher.searchAll(startingDir, pattern);	
			} else if (this.onlyFilesButton.isSelected()) {
				foundFiles = searcher.searchFilesOnly(startingDir, pattern);
			} else {
				foundFiles = searcher.searchDirsOnly(startingDir, pattern);
			}
			
			this.outputFoundFiles(foundFiles);
		}
	}
	
	public void outputFoundFiles(List<File> filesAndDirs) {
		var outputTextBuilder = new StringBuilder();
		
		for (var file : filesAndDirs) {
			outputTextBuilder.append(file.getAbsolutePath() + System.lineSeparator());
		}
		
		this.outputTextArea.setText(outputTextBuilder.toString());
	}
	
	private JFileChooser buildFileChooser() {
		var chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		chooser.showOpenDialog(null);
		
		return chooser;
	}

	private void addSearchButton() {
		var button = new JButton("Choose directory");		
		button.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        searchFiles();
		    }
		});
		
		this.panel.add(button);
	}

	private void addSearchSettings() {
		var searchSettingsGroup = new ButtonGroup();
		searchSettingsGroup.add(this.allButton);
		searchSettingsGroup.add(this.onlyFilesButton);
		searchSettingsGroup.add(this.onlyDirsButton);
		
		allButton.setSelected(true);
		
		var searchSettingsPanel = new JPanel();
		searchSettingsPanel.setLayout(new BoxLayout(searchSettingsPanel, BoxLayout.Y_AXIS));
		searchSettingsPanel.add(allButton);
		searchSettingsPanel.add(onlyFilesButton);
		searchSettingsPanel.add(onlyDirsButton);

		this.panel.add(searchSettingsPanel);
	}
	
	private void addPatternTextField() {
		var patternPanel = new JPanel();
		
		patternPanel.add(new JLabel("File path/name pattern:"));
		patternPanel.add(this.patternMatchTextField);
		
		this.panel.add(patternPanel);
	}
}
