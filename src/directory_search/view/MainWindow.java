package directory_search.view;

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

import directory_search.model.DirectoryWalker;

public class MainWindow {

	private DirectoryWalker directoryWalker;
	
	private JFrame frame;
	private JPanel panel;
	
	private JRadioButton allButton;
	private JRadioButton onlyFilesButton;
	private JRadioButton onlyDirsButton;
	
	private JRadioButton fullPathNamesButton;
	private JRadioButton fileNamesOnlyButton;
	
	private JTextField patternMatchTextField;
	
	private JTextArea outputTextArea;

	public MainWindow() {
		this.directoryWalker = new DirectoryWalker();
		
		this.frame = new JFrame();
		this.panel = new JPanel();
		
		this.allButton = new JRadioButton("Files and directories");
		this.onlyFilesButton = new JRadioButton("Files only");
		this.onlyDirsButton = new JRadioButton("Directories only");
		
		this.fullPathNamesButton = new JRadioButton("Full path names");
		this.fileNamesOnlyButton = new JRadioButton("File names only");
	
		this.patternMatchTextField = new JTextField(20);
		
		this.outputTextArea = new JTextArea();
	}
		
	public void launch() {
		this.buildWindow();
		this.frame.setVisible(true);
	}

	
	private void buildWindow() {
		try {
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());	
		} catch (Exception e) {}
		
		this.frame.setResizable(false);
		this.frame.setTitle("Recursive Directory Walker");
		this.frame.setLayout(new BoxLayout(this.frame.getContentPane(), BoxLayout.Y_AXIS));
		
		this.addSearchSettings();
		this.addOutputFormatSettings();
		
		this.addPatternTextField();

		this.addSearchButton();
		
		this.addOutputTextArea();

		this.frame.pack();
	}

	private void searchFiles() {
		var chooser = this.buildFileChooser();
		var value = chooser.showOpenDialog(null);
		if (value != JFileChooser.APPROVE_OPTION) {
		    return;
		}
		
		var startingDir = chooser.getSelectedFile();
		
		if (startingDir != null) {
			this.directoryWalker.setSearchFileNamesOnly(this.fileNamesOnlyButton.isSelected());
			var pattern = this.patternMatchTextField.getText();
			
			List<File> foundFiles;
			if (this.allButton.isSelected()) {
				foundFiles = directoryWalker.searchAll(startingDir, pattern);	
			} else if (this.onlyFilesButton.isSelected()) {
				foundFiles = directoryWalker.searchFilesOnly(startingDir, pattern);
			} else {
				foundFiles = directoryWalker.searchDirsOnly(startingDir, pattern);
			}
			
			this.outputFoundFiles(foundFiles);
		}
	}
	
	private void outputFoundFiles(List<File> filesAndDirs) {
		var outputTextBuilder = new StringBuilder();
		
		for (var file : filesAndDirs) {
			if (this.fullPathNamesButton.isSelected()) {
				outputTextBuilder.append(file.getAbsolutePath());
			} else {
				outputTextBuilder.append(file.getName());
			}
			
			outputTextBuilder.append(System.lineSeparator());
		}
		
		this.outputTextArea.setText(outputTextBuilder.toString());
	}
	
	private JFileChooser buildFileChooser() {
		var chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
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
		
		this.allButton.setSelected(true);
		
		var searchSettingsPanel = new JPanel();
		searchSettingsPanel.setLayout(new BoxLayout(searchSettingsPanel, BoxLayout.Y_AXIS));
		
		searchSettingsPanel.add(this.allButton);
		searchSettingsPanel.add(this.onlyFilesButton);
		searchSettingsPanel.add(this.onlyDirsButton);

		this.panel.add(searchSettingsPanel);
	}
	
	private void addOutputFormatSettings() {
		var outputSettingsGroup = new ButtonGroup();
		outputSettingsGroup.add(this.fullPathNamesButton);
		outputSettingsGroup.add(this.fileNamesOnlyButton);
		
		this.fullPathNamesButton.setSelected(true);
		
		var outputSettingsPanel = new JPanel();
		outputSettingsPanel.setLayout(new BoxLayout(outputSettingsPanel, BoxLayout.Y_AXIS));
		outputSettingsPanel.add(this.fullPathNamesButton);
		outputSettingsPanel.add(this.fileNamesOnlyButton);
		
		this.panel.add(outputSettingsPanel);
	}
	
	private void addPatternTextField() {
		var patternPanel = new JPanel();
		
		patternPanel.add(new JLabel("File path/name pattern:"));
		patternPanel.add(this.patternMatchTextField);
		
		this.panel.add(patternPanel);
	}
	
	private void addOutputTextArea() {
		this.outputTextArea.setEditable(false);
		this.outputTextArea.setColumns(69);
		this.outputTextArea.setRows(9);
		
		var scrollPane = new JScrollPane(this.outputTextArea);
		var outputTextPanel = new JPanel();
		outputTextPanel.add(scrollPane);
		
		this.frame.add(this.panel);
		this.frame.add(outputTextPanel);
	}
}
