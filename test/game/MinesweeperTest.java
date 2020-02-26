package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MinesweeperTest {

  Minesweeper minesweeper;

  @BeforeEach
  public void setUp() {
    minesweeper = new Minesweeper();
  }

  @Test
  public void Canary() {
    assertTrue(true);
  }

  @Test
  public void userExposesUnexposedCell() {
    minesweeper.exposeCell(0, 0);

    assertEquals(CellState.EXPOSED, minesweeper.getCellState(0, 0));
  }

  @Test
  public void userExposesExposedCell() {
    minesweeper.exposeCell(0, 0);
    minesweeper.exposeCell(0, 0);

    assertEquals(CellState.EXPOSED, minesweeper.getCellState(0, 0));
  }

  @Test
  public void userExposesACellOutsideOfRange() {
    assertAll(
      () -> assertThrows(IndexOutOfBoundsException.class,
        () -> minesweeper.exposeCell(-1, 3)),
          () -> assertThrows(IndexOutOfBoundsException.class,
            () -> minesweeper.exposeCell(10, 3)),
          () -> assertThrows(IndexOutOfBoundsException.class,
            () -> minesweeper.exposeCell(1, -3)),
          () -> assertThrows(IndexOutOfBoundsException.class,
            () -> minesweeper.exposeCell(1, 13))
    );
  }

  @Test
  public void checkInitialCellState() {
    assertEquals(CellState.UNEXPOSED, minesweeper.getCellState(4, 5));
  }

  @Test
  public void sealAnUnexposedCell() {
    minesweeper.toggleSeal(4, 5);

    assertEquals(CellState.SEALED, minesweeper.getCellState(4, 5));
  }

  @Test
  public void unSealASealedCell() {
    minesweeper.toggleSeal(3, 6);
    minesweeper.toggleSeal(3, 6);

    assertEquals(CellState.UNEXPOSED, minesweeper.getCellState(3, 6));
  }

  @Test
  public void sealAnExposedCell() {
    minesweeper.exposeCell(3, 6);
    minesweeper.toggleSeal(3, 6);

    assertEquals(CellState.EXPOSED, minesweeper.getCellState(3, 6));
  }

  @Test
  public void exposeASealedCell() {
    minesweeper.toggleSeal(3, 6);
    minesweeper.exposeCell(3, 6);

    assertEquals(CellState.SEALED, minesweeper.getCellState(3, 6));
  }

  @Test
  void exposeCellCallsExposeNeighbors() {
    boolean[] exposeNeighborsCalled = new boolean[1];

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeNeighbors(int row, int column) {
        exposeNeighborsCalled[0] = true;
      }
    };

    minesweeper.exposeCell(4, 7);

    assertTrue(exposeNeighborsCalled[0]);
  }

  @Test
  public void onAlreadyExposedCellExposeCellDoesNotCallExposeNeighbors() {
    boolean[] exposeNeighborsCalled = new boolean[1];

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeNeighbors(int row, int col) {
        exposeNeighborsCalled[0] = true;
      }
    };

    minesweeper.cellStates[2][3] = CellState.EXPOSED;

    minesweeper.exposeCell(2, 3);

    assertFalse(exposeNeighborsCalled[0]);
  }

  @Test
  public void exposeCellOnSealedCellDoesNotCallExposeNeighbors() {
    boolean[] exposeNeighborsCalled = new boolean[1];

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeNeighbors(int row, int col) {
        exposeNeighborsCalled[0] = true;
      }
    };

    minesweeper.cellStates[4][5] = CellState.SEALED;

    minesweeper.exposeCell(4, 5);

    assertFalse(exposeNeighborsCalled[0]);
  }

  @Test
  void exposeNeighborsCallsExposeCellForEightNeighbors() {
    var neighbors = new ArrayList<String>();

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeCell(int row, int column) {
        neighbors.add(row + "-" + column);
      }
    };

    minesweeper.exposeNeighbors(3, 2);

    assertEquals(List.of("2-1", "2-2", "2-3", "3-1", "3-2", "3-3",
      "4-1", "4-2", "4-3"), neighbors);
  }

  @Test
  public void exposeNeighborsOnTopLeftCellProperlyCallsExposeCell() {
    var neighbors = new ArrayList<String>();

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeCell(int row, int column) {
        neighbors.add(row + "-" + column);
      }
    };

    minesweeper.exposeNeighbors(0, 0);

    assertEquals(List.of("0-0", "0-1", "1-0", "1-1"), neighbors);
  }

  @Test
  public void exposeNeighborsOnBottomRightCellProperlyCallsExposeCell() {
    var neighbors = new ArrayList<String>();

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeCell(int row, int column) {
        neighbors.add(row + "-" + column);
      }
    };

    minesweeper.exposeNeighbors(9, 9);

    assertEquals(List.of("8-8", "8-9", "9-8", "9-9"), neighbors);
  }

  @Test
  public void exposeNeighborsOnBorderCellProperlyCallsExposeCell() {
    var neighbors = new ArrayList<String>();

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeCell(int row, int column) {
        neighbors.add(row + "-" + column);
      }
    };

    minesweeper.exposeNeighbors(0, 5);

    assertEquals(List.of("0-4", "0-5", "0-6", "1-4", "1-5", "1-6"),
      neighbors);
  }

  @Test
  public void checkMineAt3_2() {
    assertFalse(minesweeper.isMineAt(3, 2));
  }

  @Test
  public void setMineAndCheckIfPresent() {
    minesweeper.setMine(3, 2);

    assertTrue(minesweeper.isMineAt(3, 2));
  }


  @Test
  public void isMineAtOutOfBoundsReturnsFalse() {
    //Feedback: assertAll
    assertFalse(minesweeper.isMineAt(-1, 4));
    assertFalse(minesweeper.isMineAt(10, 5));
    assertFalse(minesweeper.isMineAt(5, -1));
    assertFalse(minesweeper.isMineAt(7, 10));
  }

  @Test
  public void exposingAnAdjacentCellDoesNotCallExposeNeighbors() {
    boolean[] exposeNeighborsCalled = new boolean[1];

    Minesweeper minesweeper = new Minesweeper() {
      public void exposeNeighbors(int row, int col) {
        exposeNeighborsCalled[0] = true;
      }
    };

    minesweeper.setMine(4, 6);
    minesweeper.exposeCell(5, 6);

    assertFalse(exposeNeighborsCalled[0]);
  }

  @Test
  public void adjacentMinesCountAtMethodAtFirst() {
    assertEquals(0, minesweeper.adjacentMinesCountAt(4, 6));
  }

  @Test
  public void VerifyAdjacentMinesCountAtMethod() {
    minesweeper.setMine(3, 4);
    assertEquals(0, minesweeper.adjacentMinesCountAt(3, 4));
    assertEquals(1, minesweeper.adjacentMinesCountAt(3, 5));

    minesweeper.setMine(2, 6);
    assertEquals(2, minesweeper.adjacentMinesCountAt(3, 5));

    minesweeper.setMine(0, 1);
    assertEquals(1, minesweeper.adjacentMinesCountAt(0, 0));

    assertEquals(0, minesweeper.adjacentMinesCountAt(0, 9));

    minesweeper.setMine(9, 8);
    assertEquals(1, minesweeper.adjacentMinesCountAt(9, 9));

    assertEquals(0, minesweeper.adjacentMinesCountAt(9, 0));
  }


  @Test
  public void checkIfGameStatusReturnsInProgress() {
    assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
  }

  @Test
  public void exposeAMinedCell() {
    minesweeper.setMine(3, 5);

    minesweeper.exposeCell(3, 5);

    assertEquals(GameStatus.LOST, minesweeper.getGameStatus());
  }

  @Test
  public void gameInProgressAfterAllMinesSealedButCellsRemainUnexposed() {
    minesweeper.setMine(6, 3);
    minesweeper.toggleSeal(6, 3);

    assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
  }

  @Test
  public void gameInProgressAfterAllMinesAreSealedButAnEmptyCellIsSealed() {
    minesweeper.setMine(4, 8);

    minesweeper.toggleSeal(4, 8);

    minesweeper.toggleSeal(1, 2);

    assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
  }

  @Test
  public void gameInProgressAfterAllMinesAreSealedButAnAdjacentCellIsUnexposed() {
    minesweeper.setMine(0, 1);
    minesweeper.setMine(1, 1);
    minesweeper.setMine(1, 0);

    minesweeper.exposeCell(5, 5);

    assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
  }

  @Test
  public void gameWONAfterAllMinesAreSealedAndAllOtherCellsExposed() {
    minesweeper.setMine(5, 5);
    minesweeper.setMine(7, 8);

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (i == 5 && j == 5) {
          minesweeper.toggleSeal(5, 5);
        }
        else if (i == 7 && j == 8) {
          minesweeper.toggleSeal(7, 8);
        }
        else {
          minesweeper.exposeCell(i, j);
        }
      }
    }

    assertEquals(GameStatus.WON, minesweeper.getGameStatus());
  }

  @Test
  public void setMinesCreates10Mines(){
    minesweeper.setMines(0);
    int count=0;

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (minesweeper.isMineAt(i, j)){
          count++;
        }
      }
    }

    assertEquals(10, count);

  }

  @Test
  public void verifySetMinesRandomizesMinesPlacement(){
    Minesweeper m1 = minesweeper;
    Minesweeper m2 = new Minesweeper();

    m1.setMines(0);
    m2.setMines(1);
    boolean isSame = true;

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (m1.isMineAt(i, j)){
          isSame = m2.isMineAt(i, j);

          if (!isSame){
            break;
          }
        }
      }
    }

    assertFalse(isSame);
  }
}

