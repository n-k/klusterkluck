package com.github.nk.klusterfuck.admin.services;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by nk on 4/6/17.
 */
public class RepoTemplates {

	public static RepoInitializer getInitializer(String template) {
		if ("python".equalsIgnoreCase(template)) {
		}
		return new PythonRepoInitializer();
//		return new DefaultInitializer();
	}

	private static class DefaultInitializer implements RepoInitializer {

		@Override
		public void init(File repoDir) {
			File confFile = new File(repoDir, "config.yaml");
			try (FileOutputStream fos = new FileOutputStream(confFile)) {
				IOUtils.copy(new ByteArrayInputStream("command: \"wc\"\n".getBytes()), fos);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static class PythonRepoInitializer implements RepoInitializer {

		@Override
		public void init(File repoDir) {
			File confFile = new File(repoDir, "config.yaml");
			try (FileOutputStream fos = new FileOutputStream(confFile)) {
				IOUtils.copy(new ByteArrayInputStream("command: \"./script.sh\"\n".getBytes()), fos);

				// create hello.py, and make it executable
				ClassLoader cl = getClass().getClassLoader();
				File pyFile = new File(repoDir, "hello.py");
				try (InputStream stream = cl.getResourceAsStream("templates/hello.py");
				     FileOutputStream pyFileOs = new FileOutputStream(pyFile)) {
					IOUtils.copy(stream, pyFileOs);
				}
				pyFile.setExecutable(true, true);
				File jsFile = new File(repoDir, "hello.js");
				try (InputStream stream = cl.getResourceAsStream("templates/hello.js");
				     FileOutputStream pyFileOs = new FileOutputStream(jsFile)) {
					IOUtils.copy(stream, pyFileOs);
				}
				jsFile.setExecutable(true, true);

				File shFile = new File(repoDir, "script.sh");
				try (InputStream stream = cl.getResourceAsStream("templates/script.sh");
				     FileOutputStream pyFileOs = new FileOutputStream(shFile)) {
					IOUtils.copy(stream, pyFileOs);
				}
				shFile.setExecutable(true, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
