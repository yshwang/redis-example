package com.aceproject.redisexample.thread;

import java.util.List;

import redis.clients.jedis.Jedis;

public class Consumer implements Runnable {

	private Jedis jedis;
	private String key;

	public Consumer(String key) {
		super();
		this.jedis = new Jedis("127.0.0.1", 6379);
		this.key = key;
	}

	public void run() {
		while (true) {
			List<String> strList = jedis.brpop(5, key);
			if (strList == null) {
				System.out.println(Thread.currentThread().getName()
						+ " isEmpty");
			} else {
				System.out.println(Thread.currentThread().getName() + " "
						+ strList.get(0) + " " + strList.get(1));
			}
		}
	}
}
