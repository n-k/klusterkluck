package com.github.nk.klusterfuck.admin;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static class Config {

        @Value("${app.kube.master:}")
        private String kubeMaster;
        @Value("${app.kube.configType:env}")
        private KubeConfigType configType;

        @Bean
        public DefaultKubernetesClient kubernetesClient() throws IOException {
            switch (configType) {
                case url:
                    return new DefaultKubernetesClient(kubeMaster);
                case env:
                    return new DefaultKubernetesClient();
                case kubeconf:
                    String userHome = System.getProperty("user.home");
                    File confFile = new File(new File(userHome, ".kube"), "config");
                    try (InputStream is = new FileInputStream(confFile)) {
                        return DefaultKubernetesClient.fromConfig(is);
                    }
            }
            throw new RuntimeException("Unsupported config type " + configType);
        }
    }
}
