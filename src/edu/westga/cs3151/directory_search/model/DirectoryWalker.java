package edu.westga.cs3151.directory_search.model;

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
		addAll(startingDir, text);
		
		return new ArrayList<File>(this.files);
	}
	
	private void addAll(File filePath, String text) {
		if (filePath.isDirectory()) {
			for (var file : filePath.listFiles()) {
				addAll(file, text);
			}
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
			for (var file : filePath.listFiles()) {
				addFilesOnly(file, text);
			}
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
			this.files.add(filePath);
			for (var file : filePath.listFiles()) {
				this.addDirsOnly(file, text);
			}
		}
	}
	
	private boolean fileMatches(File filePath, String text) {
		return text.isEmpty() || filePath.getAbsolutePath().toLowerCase().contains(text.toLowerCase());
	}
	
}
