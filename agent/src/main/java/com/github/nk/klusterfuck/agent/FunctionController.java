package com.github.nk.klusterfuck.agent;

import com.github.nk.klusterfuck.common.FunctionConfig;

import java.io.*;
import java.util.concurrent.*;

/**
 * Created by nipunkumar on 02/06/17.
 */
public class FunctionController {

	private String workDir;
	private String gitUrl;
	private String gogsUser;
	private String gogsPassword;
	/*
	If not specified, commit id is set to "", and latest commit is used
	 */
	private String commitId;
	private boolean disableCheckout;
	private boolean disableCache;

	private FunctionConfig functionConfig;
	int corePoolSize = 20;
	int maxPoolSize = 20;
	int queueSize = 10;
	long keepAliveTime = 1000;

	ExecutorService executor;

	public FunctionController() {
	}

	public FunctionController(Config config) {
		workDir = config.getWorkDir();
		gitUrl = config.getGitUrl();
		gogsUser = config.getGogsUser();
		gogsPassword = config.getGogsPassword();
		commitId = config.getCommitId();
		disableCheckout = config.isDisableCheckout();
		disableCache = config.isDisableCache();
	}

	public void init() throws Exception {
		if (!disableCheckout) {
			SetupUtils.setupClone(workDir, gitUrl, commitId, gogsUser, gogsPassword);
		}
		functionConfig = SetupUtils.readConfig(workDir);
		executor =
				new ThreadPoolExecutor(
						corePoolSize,
						maxPoolSize,
						keepAliveTime,
						TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(queueSize)
				);
	}

	public FunctionConfig getFunctionConfig() {
		return functionConfig;
	}

	public String run(String body) throws Exception {
		if (disableCache) {
			functionConfig = SetupUtils.readConfig(workDir);
		}
		String command = functionConfig.getCommand();
		if (command.startsWith("./")) {
			// relative from work_dir
			// first check if thee are arguments
			String[] parts = command.split("\\s+");
			File file = new File(workDir, parts[0]);
			command = file.getCanonicalFile().getAbsolutePath();
			if (parts.length > 0) {
				for (int i = 1; i < parts.length; i++) {
					command = command + " " + parts[i];
				}
			}
		}
		ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
		final Process process = processBuilder.start();
		// redirect IO
		String payload = body;
		if (payload == null) {
			payload = "";
		}
		final String fPayload = payload;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try (OutputStream procOutStream = process.getOutputStream()) {
					procOutStream.write(fPayload.getBytes());
					procOutStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Future<String> procOutputTask = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				try (InputStream procInStream = process.getInputStream()) {
					try (InputStreamReader isr = new InputStreamReader(procInStream)) {
						try (BufferedReader br = new BufferedReader(isr)) {
							String all = "";
							String line = "";
							while ((line = br.readLine()) != null) {
								all = all + line + "\n";
							}
							return all;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try (InputStream procInStream = process.getErrorStream()) {
					try (InputStreamReader isr = new InputStreamReader(procInStream)) {
						try (BufferedReader br = new BufferedReader(isr)) {
							String line = "";
							while ((line = br.readLine()) != null) {
								System.err.println("Sub process error output: " + line);
							}
						}
					}
				} catch (Exception e) {
				}
			}
		});
		int status = 1;
		// give the process one second to finish
		boolean finished = process.waitFor(5000, TimeUnit.MILLISECONDS);
		if (!finished) {
			System.out.println("Process not finished after 5 seconds!!!");
			process.destroyForcibly();
			try {
				status = process.exitValue();
			} catch (IllegalThreadStateException itse) {
				itse.printStackTrace();
			}
		} else {
			status = process.exitValue();
		}
		if (status != 0) {
			throw new RuntimeException("Process finished with non-zero status: " + status);
		}
		String output = procOutputTask.get(1000, TimeUnit.MILLISECONDS);
		return output;
	}

}
