package directory_search.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryWalker {
	
	private List<File> files;

	public DirectoryWalker() {
		this.files = new ArrayList<File>();
	}
	
	public List<File> searchAll(File startingDir, String text) {
		this.files.clear();
		this.addAll(startingDir, text);
		
		return new ArrayList<File>(this.files);
	}
	
	private void addAll(File filePath, String text) {
		if (filePath.isDirectory()) {
			try {
				for (var file : filePath.listFiles()) {
					this.addAll(file, text);
				}
			} catch (NullPointerException e) {}
		}
		
		if (this.fileMatches(filePath, text)) {
			this.files.add(filePath);	
		}
	}
	
	public List<File> searchFilesOnly(File startingDir, String text) {
		this.files.clear();
		this.addFilesOnly(startingDir, text);
		
		return new ArrayList<File>(this.files);
	}
	
	private void addFilesOnly(File filePath, String text) {
		if (filePath.isDirectory()) {
			try {
				for (var file : filePath.listFiles()) {
					addFilesOnly(file, text);
				}	
			} catch (NullPointerException e) {}
		} else if (this.fileMatches(filePath, text)) {
			this.files.add(filePath);
		}
	}
	
	public List<File> searchDirsOnly(File startingDir, String text) {
		this.files.clear();
		this.addDirsOnly(startingDir, text);
		
		return new ArrayList<File>(this.files);
	}
	
	private void addDirsOnly(File filePath, String text) {
		if (filePath.isDirectory()) {
			if (this.fileMatches(filePath, text)) {
				this.files.add(filePath);	
			}
			
			try {
				for (var file : filePath.listFiles()) {
					this.addDirsOnly(file, text);
				}				
			} catch (NullPointerException e) {}
		}
	}
	
	private boolean fileMatches(File filePath, String text) {
		return text.isEmpty() || filePath.getAbsolutePath().toLowerCase().contains(text.toLowerCase());
	}
	
}
