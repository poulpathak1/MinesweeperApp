package game.ui;

import game.CellState;
import game.GameStatus;
import game.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class MinesweeperFrame extends JFrame {
  public static final int SIZE = 10;
  Minesweeper minesweeper;
  public  MinesweeperCell[][] grid = new MinesweeperCell[10][10];

  public MinesweeperFrame(){
      minesweeper = new Minesweeper();
      Random random = new Random();

      minesweeper.setMines(random.nextInt());

      setLayout(new GridLayout(SIZE, SIZE));

      for (int i = 0; i < SIZE; i++) {
          for (int j = 0; j < SIZE; j++) {
              grid[i][j] = new MinesweeperCell(i, j, minesweeper);
              getContentPane().add(grid[i][j]);
              grid[i][j].addActionListener(new CellClickedHandler());
              grid[i][j].addMouseListener( new handleRightClick());
          }
      }
  }

  public static void main (String[] args){

      JFrame frame= new MinesweeperFrame();
      frame.setTitle("Pathak_Patel_Minesweeper");
      frame.setSize(500, 500);
      frame.setVisible(true);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

    private class CellClickedHandler implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent actionEvent){

        MinesweeperCell cell = (MinesweeperCell) actionEvent.getSource();

        minesweeper.exposeCell(cell.row, cell.column);
        cell.updateCellState(minesweeper);

        GameStatus gameStatus = minesweeper.getGameStatus();

        if (gameStatus == GameStatus.INPROGRESS){
            exposeHandler(cell);
        }

        else if (gameStatus == GameStatus.LOST){
            lostGameHandler();
            JOptionPane.showMessageDialog(cell, "You Lost !");
        }
        else{
            JOptionPane.showMessageDialog(cell, "You Won !");
        }
      }

        private void lostGameHandler() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                  if (minesweeper.isMineAt(i, j)){
                      MinesweeperCell cell = grid[i][j];
                      cell.setBackground(Color.black);
                      cell.setText("M");
                    }
                }
            }
        }

        public void exposeHandler(MinesweeperCell cell) {
          int row = cell.row;
          int column = cell.column;

            if (cell.cellState == CellState.EXPOSED){
                if ((cell.cellValue == 0)) {
                    cell.setText("E");
                    cell.setBackground(Color.CYAN);
                    exposeNeighborCells(cell);
                }
                else {
                    cell.setText(Integer.toString(cell.cellValue));
                    cell.setBackground(Color.CYAN);
                }
            }
        }

        public void exposeNeighborCells(MinesweeperCell cell) {
          int row = cell.row;
          int column = cell.column;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    exposeEmptyNeighbors(i, j);
                }
            }
        }

        public void exposeEmptyNeighbors(int i, int j) {
            MinesweeperCell cell;
            if (minesweeper.cellStates[i][j] == CellState.EXPOSED){
                cell = grid[i][j];
                if (cell.cellValue == 0){
                    cell.setText("E");
                    cell.setBackground(Color.CYAN);
                }
                else{
                    cell.setText(Integer.toString(cell.cellValue));
                    cell.setBackground(Color.CYAN);
                }
            }
        }
    }

    private class handleRightClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if (SwingUtilities.isRightMouseButton(mouseEvent)){
                MinesweeperCell cell = (MinesweeperCell) mouseEvent.getSource();

                if (cell.cellState == CellState.SEALED){
                    minesweeper.toggleSeal(cell.row, cell.column);
                    cell.setBackground(null);
                    cell.setText(null);
                }
                else if (cell.cellState == CellState.UNEXPOSED){
                    minesweeper.toggleSeal(cell.row, cell.column);
                    cell.setBackground(Color.red);
                    cell.setText("S");
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }
}
