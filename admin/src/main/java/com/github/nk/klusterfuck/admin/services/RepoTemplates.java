package com.github.nk.klusterfuck.admin.services;

import com.github.nk.klusterfuck.admin.model.FunctionType;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by nk on 4/6/17.
 */
public class RepoTemplates {

    public static RepoInitializer getFunctionInitializer(FunctionType type) {
        switch (type) {
            case generic:
                return new FunctionRepoInitializer();
            case nodejs:
                return new NodejsFunctionRepoInitializer();
            case static_site:
                return new StaticSiteFunctionRepoInitializer();
            default:
                throw new RuntimeException("Do not know how to initialize function repository for type: " + type);
        }
    }

    public static RepoInitializer getFlowInitializer() {
        return new FlowRepoInitializer();
    }

    private static class ResourceConfig {
        private String fileName;
        private String resourcePath;

        ResourceConfig(String fileName, String resourcePath) {
            this.fileName = fileName;
            this.resourcePath = resourcePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getResourcePath() {
            return resourcePath;
        }

        public void setResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
        }
    }

    private static class Initer implements RepoInitializer {

        private ResourceConfig[] resourceConfigs;

        Initer(ResourceConfig[] resourceConfigs) {
            this.resourceConfigs = resourceConfigs;
        }

        @Override
        public void init(File repoDir) {
            ClassLoader cl = getClass().getClassLoader();
            for (ResourceConfig config : resourceConfigs) {
                try (FileOutputStream fos =
                             new FileOutputStream(new File(repoDir, config.getFileName()))) {
                    try (InputStream is = cl.getResourceAsStream(config.getResourcePath())) {
                        IOUtils.copy(is, fos);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class FunctionRepoInitializer extends Initer implements RepoInitializer {

        FunctionRepoInitializer() {
            super(new ResourceConfig[]{
                    new ResourceConfig("config.yaml", "fn_templates/config.yaml"),
                    new ResourceConfig("hello.py", "fn_templates/hello.py"),
                    new ResourceConfig("hello.js", "fn_templates/hello.js"),
                    new ResourceConfig("script.sh", "fn_templates/script.sh")
            });
        }
    }

    private static class NodejsFunctionRepoInitializer extends Initer implements RepoInitializer {

        NodejsFunctionRepoInitializer() {
            super(new ResourceConfig[]{
                    new ResourceConfig("function.js", "fn_templates/function.js")
            });
        }
    }

    private static class StaticSiteFunctionRepoInitializer extends Initer implements RepoInitializer {

        StaticSiteFunctionRepoInitializer() {
            super(new ResourceConfig[]{
                    new ResourceConfig("index.html", "fn_templates/index.html")
            });
        }
    }

    private static class FlowRepoInitializer extends Initer implements RepoInitializer {

        FlowRepoInitializer() {
            super(new ResourceConfig[]{
                    new ResourceConfig("dag.json", "fn_templates/dag.json")
            });
        }
    }
}
