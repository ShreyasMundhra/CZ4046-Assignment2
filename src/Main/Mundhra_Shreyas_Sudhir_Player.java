package Main;

import Main.ThreePrisonersDilemma.Player;

public class Mundhra_Shreyas_Sudhir_Player extends Player {
	static int[][][] payoff = {
			{ { 6, 3 }, // payoffs when first and second players cooperate
					{ 3, 0 } }, // payoffs when first player coops, second
								// defects
			{ { 8, 5 }, // payoffs when first player defects, second coops
					{ 5, 2 } } };// payoffs when first and second players defect
	
	private int opp1Defects = 0;
	private int opp2Defects = 0;
	
	int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
		if (n == 0)
			return 0;

		else {
			opp1Defects += oppHistory1[n - 1];
			opp2Defects += oppHistory2[n - 1];

			if (opp1Defects <= n / 2 && opp2Defects <= n / 2)
				return 0;

			if (opp1Defects > n / 2 && opp2Defects > n / 2)
				return 1;

			else {
				float[] scores = calculateScores(myHistory, oppHistory1, oppHistory2);

				if (scores[1] < scores[0] || scores[2] < scores[0]) {
					return switchToSimpleMajority(n, myHistory, oppHistory1, oppHistory2);
				} else {
					float[][] probDists = new float[2][2];

					probDists[0] = findProbabilityDist(oppHistory1);
					probDists[1] = findProbabilityDist(oppHistory2);

					float coopUtil = findExpectedUtility(0, probDists);
					float defectUtil = findExpectedUtility(1, probDists);

					if (coopUtil > defectUtil)
						return 0;

					return 1;
				}
			}
		}
	}
	
	int switchToSimpleMajority(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
		int opponentCoop1 = 0, opponentCoop2 = 0;
		int predAction1, predAction2;

		for (int i = 0; i < n; i++) {
			if (oppHistory1[i] == 0) {
				opponentCoop1 += 1;
			}
			if (oppHistory2[i] == 0) {
				opponentCoop2 += 1;
			}
		}

		if (opponentCoop1 > n / 2)
			predAction1 = 0;
		else
			predAction1 = 1;

		if (opponentCoop2 > n / 2)
			predAction2 = 0;
		else
			predAction2 = 1;

		if (payoff[0][predAction1][predAction2] > payoff[1][predAction1][predAction2])
			return 0;

		return 1;
	}

	float[] calculateScores(int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
		int rounds = myHistory.length;
		float ScoreA = 0, ScoreB = 0, ScoreC = 0;

		for (int i = 0; i < rounds; i++) {
			ScoreA = ScoreA + payoff[myHistory[i]][oppHistory1[i]][oppHistory2[i]];
			ScoreB = ScoreB + payoff[oppHistory1[i]][oppHistory2[i]][myHistory[i]];
			ScoreC = ScoreC + payoff[oppHistory2[i]][myHistory[i]][oppHistory1[i]];
		}

		float[] result = { ScoreA / rounds, ScoreB / rounds, ScoreC / rounds };
		return result;
	}
	
	float[] findProbabilityDist(int[] history) {
		float[] probDist = new float[2];

		for (int i = 0; i < history.length; i++) {
			probDist[history[i]]++;
		}

		probDist[0] = probDist[0] / history.length;
		probDist[1] = probDist[1] / history.length;

		return probDist;
	}

	float findExpectedUtility(int action, float[][] probDists) {
		float expectedUtility = 0;

		for (int j = 0; j < 2; j++) {
			for (int k = 0; k < 2; k++) {
				expectedUtility += probDists[0][j] * probDists[1][k] * payoff[action][j][k];
			}
		}

		return expectedUtility;
	}
}
