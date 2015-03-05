package com.aceproject.redisexample.leaderboard;

import lombok.Data;

@Data
public class Team {
	private String teamName;
	private int score;
	private int rank;

	public Team() {
		super();
	}

	public Team(String teamName, int rank) {
		super();
		this.teamName = teamName;
		this.rank = rank;
	}

	public Team(String teamName, int score, int rank) {
		super();
		this.teamName = teamName;
		this.score = score;
		this.rank = rank;
	}
}
