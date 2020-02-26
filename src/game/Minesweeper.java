package game;

import java.util.Random;
import static java.lang.Math.*;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class Minesweeper {
  private static final int BOUNDS = 10;
  public CellState[][] cellStates = new CellState[BOUNDS][BOUNDS];
  private boolean[][] mines = new boolean[BOUNDS][BOUNDS];

  public Minesweeper() {
    for(int i = 0; i < BOUNDS; i++) {
      for(int j = 0; j < BOUNDS; j++) {
        cellStates[i][j] = CellState.UNEXPOSED;
        mines[i][j] = false;
      }
    }
  }

  public void exposeCell(int row, int column) {

    if(cellStates[row][column] == CellState.UNEXPOSED) {
      cellStates[row][column] = CellState.EXPOSED;


      if (adjacentMinesCountAt(row, column) == 0){
        exposeNeighbors(row, column);
      }
    }
  }

  public CellState getCellState(int row, int column) {
    return cellStates[row][column];
  }
  
  public void toggleSeal(int row, int column) {
    if(cellStates[row][column] != CellState.EXPOSED) {
      cellStates[row][column] =
        cellStates[row][column] == CellState.SEALED ?
          CellState.UNEXPOSED : CellState.SEALED;
    }
  }

  public void exposeNeighbors(int row, int column) {
    for(int i = max(0, row - 1); i < min(row + 2, BOUNDS); i++) {
      for(int j = max(0, column - 1); j < min(column + 2, BOUNDS); j++) {
        exposeCell(i, j);
      }
    }
  }

  public boolean isMineAt(int row, int column) {
    return row >=0 && row < BOUNDS && column >= 0 && column < BOUNDS && 
      mines[row][column];
  }

  public void setMine(int row, int column) {
    mines[row][column] = true;
  }

  public int adjacentMinesCountAt(int row, int column) {
    int count = 0;
    int[][] neighbors = {
            {-1, -1}, {-1, 0}, {-1, +1},
            {0, -1},          {0, +1},
            {+1, -1}, {+1, 0}, {+1, +1}
    };

    for (int[] neighborLocation :neighbors) {
      if(isMineAt(row + neighborLocation[0], column + neighborLocation[1])){
        count++;
      }
    }

    return count;
  }

  public GameStatus getGameStatus() {
    if(IntStream.range(0, BOUNDS)
      .filter(i ->
        IntStream.range(0, BOUNDS)
          .filter(j ->
            isMineAt(i, j) && cellStates[i][j] == CellState.EXPOSED)
          .count() > 0)
      .count() > 0) {
        return GameStatus.LOST;
    }    

    BiPredicate<Integer, Integer> mineSealOrUnminedExposed =
      (i, j) -> isMineAt(i, j) && cellStates[i][j] == CellState.SEALED ||
        !isMineAt(i, j) && cellStates[i][j] == CellState.EXPOSED;

    if(IntStream.range(0, BOUNDS)
      .filter(i -> IntStream.range(0, BOUNDS)
        .filter(j -> !mineSealOrUnminedExposed.test(i, j))
        .count() > 0)
      .count() > 0) {
      return GameStatus.INPROGRESS;    
    }
    
    return GameStatus.WON;
  }

  public void setMines(int i) {
    Random random = new Random(i);

    int countMines = 0;

    while(countMines<10){
      int x = (int) (random.nextDouble() * 10);
      int y = (int) (random.nextDouble() * 10);

      if (!isMineAt(x, y)){
        setMine(x, y);
        countMines++;
      }
    }
  }
}
