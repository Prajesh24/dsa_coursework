package question3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.Timer;

public class TetrisGame extends JFrame {
private static final int BOARD_WIDTH = 10;
private static final int BOARD_HEIGHT = 20;
private static final int CELL_SIZE = 30;
private static final int PREVIEW_SIZE = 4;
private static final int GAME_SPEED = 500; // Milliseconds per tick

private JPanel gameBoard;
private JPanel previewPanel;
private JLabel scoreLabel;
private int score = 0;

private Queue<Block> blockQueue = new LinkedList<>();
private Stack<int[][]> gameStack = new Stack<>();
private int[][] currentBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
private Block currentBlock;
private Block nextBlock;

private Timer gameTimer;

public TetrisGame() {
setTitle("Tetris Game");
setSize(BOARD_WIDTH * CELL_SIZE + 200, BOARD_HEIGHT * CELL_SIZE);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLayout(new BorderLayout());

// Initialize game board
gameBoard = new JPanel() {
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
drawBoard(g);
}
};
gameBoard.setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
add(gameBoard, BorderLayout.CENTER);

// Initialize preview panel
previewPanel = new JPanel() {
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
drawPreview(g);
}
};
previewPanel.setPreferredSize(new Dimension(PREVIEW_SIZE * CELL_SIZE, PREVIEW_SIZE * CELL_SIZE));
add(previewPanel, BorderLayout.EAST);

// Initialize score label
scoreLabel = new JLabel("Score: 0");
add(scoreLabel, BorderLayout.NORTH);

// Initialize buttons
JPanel buttonPanel = new JPanel();
JButton leftButton = new JButton("Left");
JButton rightButton = new JButton("Right");
JButton rotateButton = new JButton("Rotate");

leftButton.addActionListener(e -> moveBlockLeft());
rightButton.addActionListener(e -> moveBlockRight());
rotateButton.addActionListener(e -> rotateBlock());

buttonPanel.add(leftButton);
buttonPanel.add(rightButton);
buttonPanel.add(rotateButton);
add(buttonPanel, BorderLayout.SOUTH);

// Initialize game
initializeGame();
startGame();
}

private void initializeGame() {
// Clear the board
for (int i = 0; i < BOARD_HEIGHT; i++) {
Arrays.fill(currentBoard[i], 0);
}

// Initialize queue and stack
blockQueue.clear();
gameStack.clear();

// Generate initial blocks
nextBlock = generateRandomBlock();
enqueueNextBlock();
}

private void startGame() {
gameTimer = new Timer(GAME_SPEED, e -> gameLoop());
gameTimer.start();
}

private void gameLoop() {
if (isGameOver()) {
gameTimer.stop();
JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
return;
}

if (currentBlock == null) {
currentBlock = blockQueue.poll();
enqueueNextBlock();
}

if (canMoveDown(currentBlock)) {
moveBlockDown();
} else {
placeBlock();
checkCompletedRows();
currentBlock = null;
}

gameBoard.repaint();
previewPanel.repaint();
}

private boolean isGameOver() {
for (int x = 0; x < BOARD_WIDTH; x++) {
if (currentBoard[0][x] != 0) {
return true;
}
}
return false;
}

private void enqueueNextBlock() {
blockQueue.add(nextBlock);
nextBlock = generateRandomBlock();
}

private Block generateRandomBlock() {
Random random = new Random();
int[][] shape = Block.SHAPES[random.nextInt(Block.SHAPES.length)];
return new Block(shape, BOARD_WIDTH / 2 - shape[0].length / 2, 0);
}

private void moveBlockLeft() {
if (canMoveLeft(currentBlock)) {
currentBlock.x--;
}
gameBoard.repaint();
}

private void moveBlockRight() {
if (canMoveRight(currentBlock)) {
currentBlock.x++;
}
gameBoard.repaint();
}

private void rotateBlock() {
Block rotatedBlock = currentBlock.rotate();
if (canRotate(rotatedBlock)) {
currentBlock = rotatedBlock;
}
gameBoard.repaint();
}

private void moveBlockDown() {
if (canMoveDown(currentBlock)) {
currentBlock.y++;
}
}

private void placeBlock() {
for (int i = 0; i < currentBlock.shape.length; i++) {
for (int j = 0; j < currentBlock.shape[i].length; j++) {
if (currentBlock.shape[i][j] != 0) {
currentBoard[currentBlock.y + i][currentBlock.x + j] = currentBlock.shape[i][j];
}
}
}
}

private void checkCompletedRows() {
for (int y = 0; y < BOARD_HEIGHT; y++) {
boolean isRowComplete = true;
for (int x = 0; x < BOARD_WIDTH; x++) {
if (currentBoard[y][x] == 0) {
isRowComplete = false;
break;
}
}
if (isRowComplete) {
removeRow(y);
score += 100;
scoreLabel.setText("Score: " + score);
}
}
}

private void removeRow(int row) {
for (int y = row; y > 0; y--) {
System.arraycopy(currentBoard[y - 1], 0, currentBoard[y], 0, BOARD_WIDTH);
}
Arrays.fill(currentBoard[0], 0);
}

private boolean canMoveLeft(Block block) {
for (int i = 0; i < block.shape.length; i++) {
for (int j = 0; j < block.shape[i].length; j++) {
if (block.shape[i][j] != 0) {
int newX = block.x + j - 1;
if (newX < 0 || currentBoard[block.y + i][newX] != 0) {
return false;
}
}
}
}
return true;
}

private boolean canMoveRight(Block block) {
for (int i = 0; i < block.shape.length; i++) {
for (int j = 0; j < block.shape[i].length; j++) {
if (block.shape[i][j] != 0) {
int newX = block.x + j + 1;
if (newX >= BOARD_WIDTH || currentBoard[block.y + i][newX] != 0) {
return false;
}
}
}
}
return true;
}

private boolean canMoveDown(Block block) {
for (int i = 0; i < block.shape.length; i++) {
for (int j = 0; j < block.shape[i].length; j++) {
if (block.shape[i][j] != 0) {
int newY = block.y + i + 1;
if (newY >= BOARD_HEIGHT || currentBoard[newY][block.x + j] != 0) {
return false;
}
}
}
}
return true;
}

private boolean canRotate(Block block) {
for (int i = 0; i < block.shape.length; i++) {
for (int j = 0; j < block.shape[i].length; j++) {
if (block.shape[i][j] != 0) {
int newX = block.x + j;
int newY = block.y + i;
if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT || currentBoard[newY][newX] != 0) {
return false;
}
}
}
}
return true;
}

private void drawBoard(Graphics g) {
for (int y = 0; y < BOARD_HEIGHT; y++) {
for (int x = 0; x < BOARD_WIDTH; x++) {
if (currentBoard[y][x] != 0) {
g.setColor(Color.BLUE);
g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
}
g.setColor(Color.BLACK);
g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
}
}

if (currentBlock != null) {
for (int i = 0; i < currentBlock.shape.length; i++) {
for (int j = 0; j < currentBlock.shape[i].length; j++) {
if (currentBlock.shape[i][j] != 0) {
g.setColor(Color.RED);
g.fillRect((currentBlock.x + j) * CELL_SIZE, (currentBlock.y + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
}
}
}
}
}

private void drawPreview(Graphics g) {
if (nextBlock != null) {
for (int i = 0; i < nextBlock.shape.length; i++) {
for (int j = 0; j < nextBlock.shape[i].length; j++) {
if (nextBlock.shape[i][j] != 0) {
g.setColor(Color.GREEN);
g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
}
}
}
}
}

public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
TetrisGame game = new TetrisGame();
game.setVisible(true);
});
}
}

class Block {
public static final int[][][] SHAPES = {
{{1, 1, 1, 1}}, // I-shape
{{1, 1}, {1, 1}}, // O-shape
{{0, 1, 0}, {1, 1, 1}}, // T-shape
{{1, 0, 0}, {1, 1, 1}}, // L-shape
{{0, 0, 1}, {1, 1, 1}}, // J-shape
{{0, 1, 1}, {1, 1, 0}}, // S-shape
{{1, 1, 0}, {0, 1, 1}} // Z-shape
};

public int[][] shape;
public int x, y;

public Block(int[][] shape, int x, int y) {
this.shape = shape;
this.x = x;
this.y = y;
}

public Block rotate() {
int[][] rotatedShape = new int[shape[0].length][shape.length];
for (int i = 0; i < shape.length; i++) {
for (int j = 0; j < shape[i].length; j++) {
rotatedShape[j][shape.length - 1 - i] = shape[i][j];
}
}
return new Block(rotatedShape, x, y);
}
}

