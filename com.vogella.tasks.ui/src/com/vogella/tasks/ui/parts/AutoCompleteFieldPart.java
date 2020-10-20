package com.vogella.tasks.ui.parts;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.widgets.TextFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.vogella.tasks.model.TaskService;

public class AutoCompleteFieldPart {
	
	private Path lastDir;

	@PostConstruct
	public void createControls(Composite parent, TaskService todoService) {
		GridLayoutFactory.fillDefaults().applyTo(parent);

//		Combo combo = new Combo(parent, SWT.NONE);
//		AutoCompleteField autoCompleteField = new AutoCompleteField(combo, new ComboContentAdapter());
//
//		combo.addModifyListener(e -> {
//			Path dir = getPathWithoutFileName(combo.getText());
//			if (dir == null || dir.equals(lastDir) || !isDirectory(dir)) {
//				return;
//			}
//			lastDir = dir;
//			try (Stream<Path> paths = Files.list(dir)) {
//				List<String> directories = filterPaths(paths);
//				autoCompleteField.setProposals(directories.toArray(new String[directories.size()]));
//			} catch (IOException ex) {
//				// ignore
//			}
//		});
		
		Text text = TextFactory.newText(SWT.BORDER).create(parent);
	}

	private Path getPathWithoutFileName(String inputPath) {
		int lastIndex = inputPath.lastIndexOf(File.separatorChar);
		if (separatorNotFound(lastIndex)) {
			return null;
		} else if (endsWithSeparator(inputPath, lastIndex)) {
			return getPath(inputPath);
		} else {
			return getPath(removeFileName(inputPath, lastIndex));
		}
	}

	private boolean separatorNotFound(int lastIndex) {
		return lastIndex < 0;
	}

	private boolean endsWithSeparator(String inputPath, int lastIndex) {
		return lastIndex == inputPath.length();
	}

	private String removeFileName(String text, int lastIndex) {
		if (lastIndex == 0) {
			return File.separator;
		} else {
			return text.substring(0, lastIndex);
		}
	}

	private Path getPath(String text) {
		try {
			return Paths.get(text);
		} catch (InvalidPathException ex) {
			return null;
		}
	}

	private boolean isDirectory(Path dir) {
		try {
			return Files.isDirectory(dir);
		} catch (SecurityException ex) {
			return false;
		}
	}

	private List<String> filterPaths(Stream<Path> paths) {
		return paths.filter(path -> {
			String[] directoriesInPath = path.toString().split(File.separator);
			String fileName = directoriesInPath[directoriesInPath.length - 1];
			String lastDirectory = directoriesInPath[directoriesInPath.length - 2];
			return !lastDirectory.equals(".") && !fileName.startsWith(".") && Files.isDirectory(path);
		}).map(Path::toString).collect(Collectors.toList());
	}
}
