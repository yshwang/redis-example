package com.aceproject.redisexample.leaderboard;

import java.util.List;


import redis.clients.jedis.Jedis;

public class RedisExampleMain {
	public static void main(String[] args) {
		new RedisExampleMain().strat();
	}

	private void strat() {
		Jedis jedis = new Jedis("127.0.0.1", 6379);

		leaderBoard(jedis);
	}

	private void leaderBoard(Jedis jedis) {
		Leaderboard leaderBoard = new Leaderboard(jedis);
		leaderBoard.setTeams();

		// 1
		String teamName = "team_4";
		int rank = leaderBoard.getRank(teamName);

		System.out.println("----예제1) 특정 팀 등수\n 순위 : " + rank + " (팀 : "
				+ teamName + ")");

		// 2
		List<Team> list = leaderBoard.getTopRanks(3);

		System.out.println("\n----예제2) Top 3");
		int i = 0;
		for (Team team : list) {
			System.out.println((++i) + " " + team.getRank() + "위, "
					+ team.getTeamName() + " " + team.getScore());
		}

		// 3
		list = leaderBoard.getNextRanks(5);

		System.out.println("\n----예제3) 5번째부터 아래로 3팀 리스트");
		i = 0;
		for (Team team : list) {
			System.out.println((++i) + " " + team.getRank() + "위, "
					+ team.getTeamName() + " " + team.getScore());
		}

		// 4
		list = leaderBoard.getSameRankTeams(2);
		System.out.println("\n----예제4) 해당 순위 팀리스트");
		if (list == null) {
			System.out.println("순위 팀 없음");
			return;
		}
		i = 0;
		for (Team team : list) {
			System.out.println((++i) + " " + team.getRank() + "위, "
					+ team.getTeamName() + " " + team.getScore());
		}
		System.out.println();
	}
}


/* 실행 결과 (세팅 값에 따라 결과는 달라짐)
 * 

----예제1) 특정 팀 등수
 순위 : 6 (팀 : team_4)

----예제2) Top 3
1 1위, team_8 100
2 2위, team_9 90
3 2위, team_5 90

----예제3) 5번째부터 아래로 3팀 리스트
1 2위, team_2 90
2 6위, team_4 86
3 7위, team_1 70

----예제4) 해당 순위 팀리스트
1 2위, team_2 90
2 2위, team_3 90
3 2위, team_5 90
4 2위, team_9 90

 *
 */