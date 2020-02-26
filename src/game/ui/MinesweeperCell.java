package game.ui;

import game.CellState;
import game.Minesweeper;
import game.ui.MinesweeperFrame;
import javax.swing.*;
import java.awt.*;

public class MinesweeperCell extends JButton {
    private static final int BOUNDS = 10 ;
    public final int  row;
    public final int  column;
    public Minesweeper minesweeper;
    public CellState cellState;
    public int cellValue;
    public boolean isCellMine;

    public MinesweeperCell(int row, int column, Minesweeper theMinesweeper){
      this.row = row;
      this.column = column;
      minesweeper = theMinesweeper;
      setSize(50, 50);
      cellValue = minesweeper.adjacentMinesCountAt(row, column);
      isCellMine= minesweeper.isMineAt(row, column);
      updateCellState(minesweeper);
    }

    public void updateCellState(Minesweeper minesweeper){
        cellState = minesweeper.getCellState(row, column);
    }

}
