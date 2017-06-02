package com.github.nk.klusterfuck.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.nk.klusterfuck.common.FunctionConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

/**
 * Created by nipunkumar on 02/06/17.
 */
public class GitUtils {

    public static FunctionConfig setupClone(String dir, String gitRepo) throws Exception {
        File cloneDir = new File(dir);
        CredentialsProvider provider = new UsernamePasswordCredentialsProvider("gogsadmin", "admin");
        try (Git cloned = Git.cloneRepository()
                .setURI(gitRepo)
                .setCredentialsProvider(provider)
                .setDirectory(cloneDir.getCanonicalFile())
                .call()) {
            File confFile = new File(cloneDir, "config.yaml");
            YAMLFactory yf = new YAMLFactory();
            ObjectMapper mapper = new ObjectMapper(yf);
            FunctionConfig functionConfig = mapper.readValue(confFile, FunctionConfig.class);
            return functionConfig;
        }
    }
}
