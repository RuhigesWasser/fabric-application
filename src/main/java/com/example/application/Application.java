package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ComponentScan(value = "com.example.application")
@SpringBootApplication
@RestController
public class Application {

	private static String[] args;
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		Application.args = args;
		Application.context = SpringApplication.run(Application.class, args);
	}

	@GetMapping("/admin/restart")
	public String restart() {

		ExecutorService threadPool = new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>( 1 ),new ThreadPoolExecutor.DiscardOldestPolicy ());
		threadPool.execute (()->{
			context.close ();
			context = SpringApplication.run ( Application.class,args );
		} );
		threadPool.shutdown ();

		return "配置刷新成功";
	}
}
