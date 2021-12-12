import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class GameBoard implements GameBoardMove {
    private boolean notValid;
    private boolean move;

    public static final int ROWS = 4;
    public static final int COLS = 4;

    private static final int startingTiles = 2;
    private Tile[][] board;
    private boolean dead;
    private boolean won;
    private BufferedImage gameBoard;
    private BufferedImage finalBoard;
    private int x;
    private int y;
    private int score = 0;
    private int highscore = 0;
    private Font scoreFont;

    private static int SPACING = 10;
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.HEIGHT;

    private boolean hasStarted;

    private String saveDataPath;
    private String fileName = "SaveData";
    public GameBoard(int x, int y) {
        try{
            saveDataPath = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        scoreFont = Game.main.deriveFont(24f);
        this.x = x;
        this.y = y;
        board = new Tile[ROWS][COLS];
        gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        loadHighScore();
        createBoardImage();
        start();
    }

    private void createSaveData(){
        try{
            File file = new File(saveDataPath,fileName);

            FileWriter output  = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + 0);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void loadHighScore(){

        try {
            File f = new File(saveDataPath, fileName);
            if (!f.isFile()){
                createSaveData();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            highscore = Integer.parseInt(reader.readLine());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setHighscore(){
        FileWriter output = null;

        try{
          File f = new File(saveDataPath, fileName);
          output = new FileWriter(f);
          BufferedWriter writer = new BufferedWriter(output);
          writer.write(""+highscore);
          writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void createBoardImage() {
        Graphics2D g = (Graphics2D) gameBoard.getGraphics();
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(Color.lightGray);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = SPACING + SPACING * col + Tile.WIDTH * col;
                int y = SPACING + SPACING * row + Tile.HEIGHT * row;
                g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
            }
        }
    }

    private void start(){
        for(int i= 0; i < startingTiles; i++){
            spawnRandom();
        }
    }

    private void spawn(int row, int col, int value) {
        board[row][col] = new Tile(value, getTileX(col), getTileY(row));
    }

    public void spawnRandom(){
    	setNotValid(true);
        Random random = new Random();
        while(isNotValid()){
            int location = random.nextInt(ROWS*COLS);
            int row = location / ROWS;
            int col = location % COLS;
            Tile current = board[row][col];
            if(current == null){
                int value = random.nextInt(10) < 9 ? 2 : 4;
                Tile tile = new Tile(value, getTileX(col),getTileY(row));
                board[row][col] = tile;
                setNotValid(false);
            }
        }
    }

    public int getTileX(int col){
        return SPACING+ col*Tile.WIDTH + col*SPACING;
    }

    public int getTileY(int row){
        return SPACING+ row*Tile.WIDTH + row*SPACING;
    }

    @Override
    public void render(Graphics2D g){
        BufferedImage finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)finalBoard.getGraphics();
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g2d.drawImage(gameBoard,0,0,null);

        for(int row=0;row<ROWS;row++){
            for (int col =  0;col<COLS;col++){
                Tile current = board[row][col];
                if (current == null) continue;
                current.render(g2d);
            }
        }

        g.drawImage(finalBoard, x, y,null);
        g2d.dispose();

        g.setColor(Color.lightGray);
        g.setFont(scoreFont);
        g.drawString("" + score,30,40);
        g.setColor(Color.red);
        g.drawString("Best: " + highscore, Game.WIDTH - DrawUtils.getMessageWidth("Best: " + highscore,scoreFont,g) - 20,40);
    }

    @Override
    public  void  update(){
        checkKeys();

        if(score>highscore){
            highscore = score;
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null) continue;
                current.update();
                resetPosition(current, row, col);
                if (current.getValue() == 2048) {
                }
            }
        }
    }

    private void resetPosition(Tile tile, int row, int col) {
        if (tile == null) return;

        int x = getTileX(col);
        int y = getTileY(row);

        int distX = tile.getX() - x;
        int distY = tile.getY() - y;

        if (Math.abs(distX) < Tile.SLIDE_SPEED) {
            tile.setX(tile.getX() - distX);
        }

        if (Math.abs(distY) < Tile.SLIDE_SPEED) {
            tile.setY(tile.getY() - distY);
        }

        if (distX < 0) {
            tile.setX(tile.getX() + Tile.SLIDE_SPEED);
        }
        if (distY < 0) {
            tile.setY(tile.getY() + Tile.SLIDE_SPEED);
        }
        if (distX > 0) {
            tile.setX(tile.getX() - Tile.SLIDE_SPEED);
        }
        if (distY > 0) {
            tile.setY(tile.getY() - Tile.SLIDE_SPEED);
        }
    }

    public boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction dir) {
        boolean canMove = false;
        Tile current = board[row][col];
        if (current == null) return false;
        setMove(true);
        int newCol = col;
        int newRow = row;
        while (isMove()) {
            newCol += horizontalDirection;
            newRow += verticalDirection;
            if (checkOutOfBounds(dir, newRow, newCol)) break;
            if (board[newRow][newCol] == null) {
                board[newRow][newCol] = current;
                canMove = true;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
            }
            else if (board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].canCombine()) {
                board[newRow][newCol].setCanCombine(false);
                board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
                canMove = true;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
                score += board[newRow][newCol].getValue();
            }
            else {
                setMove(false);
            }
        }
        return canMove;
    }

    public void keyPressed(KeyEvent e) {

    }

    private boolean checkOutOfBounds(Direction dir, int row, int col) {
        if(dir == Direction.LEFT){
            return col < 0;
        }
        else if (dir == Direction.RIGHT){
            return col > COLS -1;
        }
        else if (dir == Direction.UP){
            return row<0;
        }
        else if (dir == Direction.DOWN){
            return row > ROWS-1;
        }
        return false;
    }

    private void moveTiles(Direction dir){
        boolean canMove = false;
        int horizontalDirection = 0;
        int verticalDirection =0;

        if(dir == Direction.LEFT){
            horizontalDirection = -1;
            for (int row = 0; row <ROWS; row++){
                for (int col=0; col < COLS;col++){
                    if(!canMove)
                        canMove = move(row,col,horizontalDirection,verticalDirection,dir);
                    else move(row,col,horizontalDirection,verticalDirection,dir);
                }
            }
        }

        else if  (dir ==Direction.RIGHT){
           horizontalDirection = 1;
           for (int row = 0; row <ROWS; row++){
               for (int col = COLS -1; col >=0; col--){
                   if(!canMove)
                       canMove = move(row,col,horizontalDirection,verticalDirection,dir);
                   else move(row,col,horizontalDirection,verticalDirection,dir);
               }
           }
       }

        else if(dir ==Direction.UP){
           verticalDirection = -1;
           for (int row = 0; row <ROWS; row++){
               for (int col=0; col < COLS;col++){
                   if(!canMove)
                       canMove = move(row,col,horizontalDirection,verticalDirection,dir);
                   else move(row,col,horizontalDirection,verticalDirection,dir);
               }
           }
       }

       else if(dir ==Direction.DOWN){
           verticalDirection = 1;
           for (int row = ROWS-1; row >=0; row--){
               for (int col=0; col < COLS;col++){
                   if(!canMove)
                       canMove = move(row,col,horizontalDirection,verticalDirection,dir);
                   else move(row,col,horizontalDirection,verticalDirection,dir);
               }
           }
       }

       for(int row = 0; row<ROWS; row++){
           for(int col = 0; col<COLS; col ++){
              Tile current = board[row][col];
              if (current == null) continue;
              current.setCanCombine(true);
           }
       }

       if (canMove){
           spawnRandom();
           checkDead();
       }
   }

   private void checkDead(){
        for (int row =0; row <ROWS;row++){
            for (int col = 0; col <COLS;col++){
                if (board[row][col] == null) return;
                boolean canCombine = CST(row, col, board[row][col]);
                if (canCombine){
                    return;
                }
            }
        }

        dead = true;
        setHighscore();

   }

    private boolean CST(int row, int col, Tile current) {
        if (row > 0) {
            Tile check = board[row - 1][col];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        }
        if (row < ROWS - 1) {
            Tile check = board[row + 1][col];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        }
        if (col > 0) {
            Tile check = board[row][col - 1];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        }
        if (col < COLS - 1) {
            Tile check = board[row][col + 1];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        }
        return false;
    }

    private void checkKeys() {
        if (!Keys.pressed[KeyEvent.VK_LEFT] && Keys.prev[KeyEvent.VK_LEFT]) {
            moveTiles(Direction.LEFT);
            if (!hasStarted) hasStarted = !dead;
        }
        if (!Keys.pressed[KeyEvent.VK_RIGHT] && Keys.prev[KeyEvent.VK_RIGHT]) {
            moveTiles(Direction.RIGHT);
            if (!hasStarted) hasStarted = !dead;
        }
        if (!Keys.pressed[KeyEvent.VK_UP] && Keys.prev[KeyEvent.VK_UP]) {
            moveTiles(Direction.UP);
            if (!hasStarted) hasStarted = !dead;
        }
        if (!Keys.pressed[KeyEvent.VK_DOWN] && Keys.prev[KeyEvent.VK_DOWN]) {
            moveTiles(Direction.DOWN);
            if (!hasStarted) hasStarted = !dead;
        }
    }

	public boolean isNotValid() {
		return notValid;
	}

	public void setNotValid(boolean notValid) {
		this.notValid = notValid;
	}

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

}