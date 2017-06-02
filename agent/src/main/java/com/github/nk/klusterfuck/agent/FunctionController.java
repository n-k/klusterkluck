package com.github.nk.klusterfuck.agent;

import com.github.nk.klusterfuck.common.FunctionConfig;
import com.github.nk.klusterfuck.common.Request;
import com.github.nk.klusterfuck.common.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.*;

/**
 * Created by nipunkumar on 02/06/17.
 */
@RestController
@RequestMapping({"", "/"})
public class FunctionController {

    @Value("${WORK_DIR:/Users/nipunkumar/code/github/klusterfuck/data/temp}")
    private String workDir;
    @Value("${GIT_URL:http://localhost:3000/gogsadmin/fn1.git}")
    private String gitUrl;

    private FunctionConfig config;
    int corePoolSize = 5;
    int maxPoolSize = 5;
    long keepAliveTime = 0;

    ExecutorService executor;

    @PostConstruct
    public void init() throws Exception {
        config = GitUtils.setupClone(workDir, gitUrl);
        executor =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(10)
                );
    }

    @RequestMapping(method = RequestMethod.GET)
    public FunctionConfig getConfig() {
        return config;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response run(@RequestBody Request request) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(config.getCommand());
        final Process process = processBuilder.start();
        // redirect IO
        // if exit code not known, assume non-zero error
        Future<?> procInputTask = executor.submit(new Runnable() {
            @Override
            public void run() {
                try (OutputStream procOutStream = process.getOutputStream()) {
                    procOutStream.write(request.getPayload().getBytes());
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
                                return all;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Future<?> procErrTask = executor.submit(new Runnable() {
            @Override
            public void run() {
                try (InputStream procInStream = process.getInputStream()) {
                    try (InputStreamReader isr = new InputStreamReader(procInStream)) {
                        try (BufferedReader br = new BufferedReader(isr)) {
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                System.err.println(line);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
        int status = 1;
        // give the process one second to finish
        boolean finished = process.waitFor(1000, TimeUnit.MILLISECONDS);
        if (!finished) {
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
            throw new RuntimeException("" + status);
        }
        String output = procOutputTask.get();
        Response res = new Response();
        res.setBody(output);
        return res;
    }
}
