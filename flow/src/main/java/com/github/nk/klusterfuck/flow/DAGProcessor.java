package com.github.nk.klusterfuck.flow;

import com.github.nk.klusterfuck.common.FunctionRef;
import com.github.nk.klusterfuck.common.StepRef;
import com.github.nk.klusterfuck.common.dag.DAG;
import com.github.nk.klusterfuck.common.dag.Node;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by nk on 6/6/17.
 */
public class DAGProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAGProcessor.class);

	private final LinkedBlockingQueue<Task> q = new LinkedBlockingQueue<>(1000);

	public void init() {
		Thread processorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Task task = q.poll(1, TimeUnit.SECONDS);
						if (task == null) {
							continue;
						}

						// if step is 'connector', pass on the payload to next steps,
						// else process and enqueue
						Node<StepRef> node = task.dag.getNode(task.id);
						StepRef ref = node.getData();
						List<Node<StepRef>> outgoingNodes = task.dag.getOutgoingNodes(task.id);
						String nextPayload = null;
						if (ref.getCategory() == StepRef.RefType.connector) {
							nextPayload = task.payload;
						} else {
							try {
								nextPayload = callFunction(((FunctionRef) ref).getUrl(), task.payload);
							} catch (Exception e) {
								LOGGER.error(e.getMessage(), e);
								continue;
							}
						}
						final String fPayload = nextPayload;
						outgoingNodes.stream()
								.map(n -> n.getId())
								.forEach(id -> {
									Task newTask = new Task();
									newTask.dag = task.dag;
									newTask.id = id;
									newTask.payload = fPayload;
									q.offer(newTask);
								});
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		});
		processorThread.setDaemon(true);
		processorThread.start();
	}

	public void enqueue(String id, DAG<StepRef> dag, String payload) {
		// if dag does not have this id, throw error
		if (dag.getNode(id) == null) {
			throw new RuntimeException("No such node: " + id);
		}
		Task task = new Task();
		task.id = id;
		task.dag = dag;
		task.payload = payload;
		q.offer(task);
	}

	private static String callFunction(String url, String payload) throws Exception {
		LOGGER.info("Calling " + url + " with payload " + payload);
		HttpClient client = HttpClientBuilder.create().build();

		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(payload));
		post.setHeader("Content-Type", "text/plain");

		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		LOGGER.info("Recieved from " + url + ": " + result);
		return result.toString();
	}

	private static class Task {
		private String id;
		private DAG<StepRef> dag;
		private String payload;
	}
}
