package com.aceproject.redisexample.leaderboard;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class Leaderboard {

	private Jedis jedis;
	private String key = "leaderboard";

	public Leaderboard(Jedis jedis) {
		this.jedis = jedis;
	}

	public void setTeams() {
		for (int i = 1; i <= 10; i++) {
			String teamName = "team_" + i;
			int score = (int) (Math.random() * 101);

			jedis.zadd(key, score, teamName);
		}

		// jedis.zadd(key, 70, "team_1");
		// jedis.zadd(key, 90, "team_2");
		// jedis.zadd(key, 90, "team_3");
		// jedis.zadd(key, 86, "team_4");
		// jedis.zadd(key, 90, "team_5");
		// jedis.zadd(key, 63, "team_6");
		// jedis.zadd(key, 63, "team_7");
		// jedis.zadd(key, 100, "team_8");
		// jedis.zadd(key, 90, "team_9");
		// jedis.zadd(key, 63, "team_10");

	}

	public int getRank(String teamName) {
		Double score = jedis.zscore(key, teamName);
		Long count = jedis.zcount(key, "(" + score, "+inf");

		return (int) (count + 1);
	}

	public List<Team> getTopRanks(int top) {
		return getTeamRanks(0, top - 1);
	}

	public List<Team> getNextRanks(int num) {
		int startIdx = num - 1;
		int endIdx = startIdx + 2;

		return getTeamRanks(startIdx, endIdx);
	}

	public List<Team> getTeamRanks(int startIdx, int endIdx) {
		List<Team> list = Lists.newArrayList();

		int idx = (startIdx == 0 ? 1 : -1);
		int rank = 0;
		int sameScoreCnt = 0;
		int tmp = 0;

		Set<Tuple> ranks = jedis.zrevrangeWithScores(key, startIdx, endIdx);
		Iterator<Tuple> it = ranks.iterator();
		while (it.hasNext()) {
			Tuple tuple = it.next();
			String teamName = tuple.getElement();
			int score = (int) tuple.getScore();

			if (idx == -1) {
				rank = getRank(teamName);
				sameScoreCnt = jedis.zcount(key, score, score).intValue();
				tmp = score;
				idx = startIdx + 1;
			}

			if (tmp != score) {
				if (sameScoreCnt > 1) {
					rank += sameScoreCnt;
					sameScoreCnt = 0;
				} else {
					rank = idx;
				}

				tmp = score;
			}
			idx++;

			list.add(new Team(teamName, score, rank));
		}

		return list;
	}

	public List<Team> getSameRankTeams(int rank) {
		int teamRank = 0;
		int score = 0;
		Set<Tuple> ranks = jedis.zrevrangeWithScores(key, rank - 1, rank - 1);
		Iterator<Tuple> it = ranks.iterator();
		if (it.hasNext()) {
			Tuple tuple = it.next();
			String teamName = tuple.getElement();
			teamRank = getRank(teamName);
			score = (int) tuple.getScore();
		}

		if (teamRank == rank) {
			List<String> teamNames = Lists.newArrayList(jedis.zrangeByScore(
					key, score, score));

			List<Team> teams = Lists.newArrayList();
			for (String temaName : teamNames) {
				teams.add(new Team(temaName, score, teamRank));
			}
			return teams;
		}

		return null;
	}

}
