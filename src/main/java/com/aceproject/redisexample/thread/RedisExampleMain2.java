package com.aceproject.redisexample.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisExampleMain2 {
	public static void main(String[] args) {

		String key = "testlist";

		Producer producer = new Producer(key);

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(producer);
		
		// Thread producerThread = new Thread(producer, "producer");
		// producerThread.start();
		
		for (int i = 0; i < 20; i++) {
			Consumer consumer = new Consumer(key);
			Thread consumerThread = new Thread(consumer, "consumer_" + i);
			consumerThread.start();
		}

	}
}
