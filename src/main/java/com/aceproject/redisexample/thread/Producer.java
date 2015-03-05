package com.aceproject.redisexample.thread;

import redis.clients.jedis.Jedis;

public class Producer implements Runnable {

	private Jedis jedis;
	private String key;

	public Producer(String key) {
		super();
		this.jedis = new Jedis("127.0.0.1", 6379);
		this.key = key;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			jedis.lpush(key, "msg_" + i);
			System.out.println(Thread.currentThread().getName() + " push_" + i);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
