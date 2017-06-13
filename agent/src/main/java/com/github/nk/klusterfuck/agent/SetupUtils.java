package com.github.nk.klusterfuck.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.nk.klusterfuck.common.FunctionConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Arrays;

/**
 * Created by nipunkumar on 02/06/17.
 */
public class SetupUtils {

	public static void setupClone(
			String dir,
			String gitRepo,
			String commitId,
			String gogsUser,
			String gogsPassword) throws Exception {
		File cloneDir = new File(dir);
		CredentialsProvider provider = new UsernamePasswordCredentialsProvider(gogsUser, gogsPassword);
		try (Git cloned = Git.cloneRepository().setURI(gitRepo).setCredentialsProvider(provider)
				.setDirectory(cloneDir.getCanonicalFile()).call()) {
			if (commitId != null && !commitId.isEmpty()) {
				cloned.checkout().setName(commitId).call();
			}
			FunctionConfig functionConfig = readConfig(dir);
			// set files to be executable, to get around bug in jgit :(
			Arrays.stream(functionConfig.getExecutables())
					.forEach(exexFileName -> {
						File execfile = new File(cloneDir, exexFileName);
						if (execfile.exists() && execfile.isFile()) {
							execfile.setExecutable(true, true);
						}
					});
		}
	}

	synchronized public static FunctionConfig readConfig(String dir) throws Exception {
		File confFile = new File(dir, "config.yaml");
		YAMLFactory yf = new YAMLFactory();
		ObjectMapper mapper = new ObjectMapper(yf);
		FunctionConfig functionConfig = mapper.readValue(confFile, FunctionConfig.class);
		return functionConfig;
	}
}
