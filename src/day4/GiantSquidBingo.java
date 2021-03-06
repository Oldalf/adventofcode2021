package day4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import adventofcode2021.AdventDay;

public class GiantSquidBingo implements AdventDay {
	final static private String path = "inputs/4.txt";

	private LinkedList<Integer> draws = new LinkedList<Integer>();
	private LinkedList<Integer[][]> boards = new LinkedList<Integer[][]>();

	public GiantSquidBingo() {
		try {
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));

			Arrays.stream(inputStream.readLine().split(",")).forEach(value -> draws.add(Integer.parseInt(value)));

			LinkedList<Integer> boardParts = new LinkedList<Integer>();
			String line;
			while ((line = inputStream.readLine()) != null) {
				if (line.trim().length() == 0) {
					if (boardParts.size() == 25) {
						// make/add board and clear parts.
						Integer[][] b = getBoard(boardParts);
						boards.add(b);
						boardParts.clear();
					}
				} else {
					String[] segments = Arrays.stream(line.trim().split(" ")).filter(value -> value.trim().length() > 0)
							.toArray(size -> new String[size]);

					for (String s : segments) {
						boardParts.add(Integer.parseInt(s));
					}
				}
			}

			if (boardParts.size() == 25) {
				boards.add(getBoard(boardParts));
				boardParts.clear();
			}

			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void runA() {
		LinkedList<Integer> drawn = new LinkedList<Integer>();

		Integer[][] winnerBoard = null;

		int counter = 0;
		while (drawn.size() < draws.size() && winnerBoard == null) {
			drawn.add(draws.get(counter++));

			if (drawn.size() > 5) {
				// row
				for (Integer[][] board : boards) {
					for (int row = 0; row < 5 && winnerBoard == null; row++) {
						for (int col = 0; col < 5; col++) {
							if (drawn.size() == 12) {
							}
							if (!drawn.contains(board[row][col])) {
								break;
							}
							if (col == 4) {
								winnerBoard = board;
								break;
							}
						}
					}
				}

				// col
				for (Integer[][] board : boards) {
					for (int col = 0; col < 5 && winnerBoard == null; col++) {
						for (int row = 0; row < 5; row++) {
							if (!drawn.contains(board[row][col])) {
								break;
							}
							if (row == 4) {
								winnerBoard = board;
								break;
							}
						}
					}
				}
			}
		}

		LinkedList<Integer> flatBoard = Arrays.stream(winnerBoard).flatMap(o -> Arrays.stream(o))
				.collect(Collectors.toCollection(LinkedList::new));

		Integer unmarkedSum = flatBoard.stream().filter(value -> !drawn.contains(value)).reduce(0, Integer::sum);

		Integer score = unmarkedSum * drawn.get(drawn.size() - 1);

		System.out.println("Day4 A: " + score);
	}

	public void runB() {
		LinkedList<Integer> drawn = new LinkedList<Integer>();

		LinkedList<Integer[][]> winningBoards = new LinkedList<Integer[][]>();

		int counter = 0;
		while (drawn.size() < draws.size() && boards.size() > 0) {
			drawn.add(draws.get(counter++));
			
			LinkedList<Integer[][]> boardsToRemove = new LinkedList<Integer[][]>();

			if (drawn.size() > 5) {
				// row
				for (Integer[][] board : boards) {
					for (int row = 0; row < 5; row++) {
						for (int col = 0; col < 5; col++) {
							if (!drawn.contains(board[row][col])) {
								break;
							}
							if (col == 4) {
								winningBoards.add(board);
								boardsToRemove.add(board);
								break;
							}
						}
					}
				}
				
				boardsToRemove.stream().forEach(v -> boards.remove(v));
				boardsToRemove.clear();

				// col
				for (Integer[][] board : boards) {
					for (int col = 0; col < 5; col++) {
						for (int row = 0; row < 5; row++) {
							if (!drawn.contains(board[row][col])) {
								break;
							}
							if (row == 4) {
								winningBoards.add(board);
								boardsToRemove.add(board);
								break;
							}
						}
					}
				}

				boardsToRemove.stream().forEach(v -> boards.remove(v));
			}
		}	

		LinkedList<Integer> flatBoard = Arrays.stream(winningBoards.get(winningBoards.size() - 1))
				.flatMap(o -> Arrays.stream(o)).collect(Collectors.toCollection(LinkedList::new));

		Integer unmarkedSum = flatBoard.stream().filter(value -> !drawn.contains(value)).reduce(0, Integer::sum);

		Integer score = unmarkedSum * drawn.get(drawn.size() - 1);

		System.out.println("Day4 B: " + score);
	}

	public Integer[][] getBoard(LinkedList<Integer> boardParts) {
		return new Integer[][] { boardParts.subList(0, 5).stream().toArray(size -> new Integer[size]),
				boardParts.subList(5, 10).stream().toArray(size -> new Integer[size]),
				boardParts.subList(10, 15).stream().toArray(size -> new Integer[size]),
				boardParts.subList(15, 20).stream().toArray(size -> new Integer[size]),
				boardParts.subList(20, 25).stream().toArray(size -> new Integer[size]), };
	}

}
