package ru.bazis.fias;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jep.Interpreter;
import jep.SharedInterpreter;

@Service
public class RestoreServiceTask {

	@Async
	public CompletableFuture<String> restore() throws Exception {
		try (Interpreter interp = new SharedInterpreter()) {
			interp.exec("import fiases");
			interp.exec("from java.lang import System");

			interp.exec("from fiases.fias_data import createAddrSearchTemplate");
			interp.exec("createAddrSearchTemplate()");

			interp.exec("from fiases.snapshot import register,restore");
			interp.exec("from fiases.fias_update import getRestoreStatus");
			interp.exec("System.out.println('1.register fias repository')");
			interp.exec("register()");
			interp.exec("System.out.println('2.restore indices [address, houses] from snapshot...')");
			interp.exec("restore()");
			Object result = interp.invoke("System.out.println('Finish restore task. Service ready.')");
			return CompletableFuture.completedFuture((String) result);
		}
	}

	@Async
	public void getOSInfo() throws Exception {
		String cmd = "cat /etc/*-release";
		String line;

		ProcessBuilder builder = new ProcessBuilder();
		builder.command("bash", "-c", cmd);
		builder.redirectErrorStream(true);
		Process process = builder.start();

		InputStream stdout = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
		while ((line = reader.readLine()) != null) {
			System.out.println("OS info: " + line);
		}
	}


	//@Async
	public String getStatus() throws Exception {
		Thread.sleep(5000);
		BufferedReader reader = null;
		ProcessBuilder builder = new ProcessBuilder();
		String cmd = "curl -s -XGET es01:9200/_recovery | jq '.houses[\"shards\"][0] | .stage'";
		builder.redirectErrorStream(true);
		builder.command("bash", "-c", cmd);

		Process process = builder.start();
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String status = reader.readLine();
		System.out.println("........................................................................................");
		return status;
	}


}
