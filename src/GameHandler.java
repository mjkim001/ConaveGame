import javax.swing.*;
import java.io.*;

public class GameHandler {
	private final int SCREEN_WIDTH = 30; // 출력되는 화면 크기 범위
	private final int SCREEN_HEIGHT = 30;
	private final int FIELD_WIDTH = 15, FIELD_HEIGHT = 15; // 바둑판 높이,너비
	private final int LEFT_PADDING = 3; // 바둑판 왼쪽 띄우기 정도
	private final int PADDING = 2; // 바둑판 위쪽 공간 띄우기

	private String stone[]; // 흰돌, 검은 돌 이미지 저장
	private int num[]; // 흰돌, 검은 돌 구별
	private int temp; // 짝수 홀수로 돌 순서 판단
	private int posX, posY; // 돌위치 중간 저장
	private final int DEFULAT_X = FIELD_WIDTH / 2;
	private final int DEFULAT_Y = FIELD_HEIGHT / 2;
	private int win;

	// private String playerImage;
	private Player player; // 돌의 x,y좌표와 이미지를 저장하는 객체
	private int currentScore[], previousScore[]; // 점수 저장 (이전, 현재)

	private boolean isGameOver; // 게임이 종료되었는지 판단

	private JTextArea textArea; // 키보드 입력을 받은 것을 전달항기위한
	private String[][] buffer; // 출력 buffer
	private String[][] field; // 바둑판 기호를 담고 있는 공간

	public GameHandler(JTextArea ta) {
		textArea = ta; // JFrame에 있는 textArea
		field = new String[FIELD_WIDTH][FIELD_HEIGHT];
		buffer = new String[SCREEN_WIDTH][SCREEN_HEIGHT];
		stone = new String[2];
		currentScore = new int[2];
		previousScore = new int[2];

		stone[0] = "●";
		stone[1] = "○";

		currentScore[0] = 0;
		currentScore[1] = 0;

		previousScore[0] = 0;
		previousScore[1] = 0;

		initData();
	}

	public void initData() {
		// 돌 놓는 판 구역에 기호 할당

		for (int x = 0; x < FIELD_WIDTH; x++) {
			for (int y = 0; y < FIELD_HEIGHT; y++) {
				field[x][y] = "┼─";
			}
		}

		for (int x = 0; x < FIELD_WIDTH; x++) {
			field[x][0] = "┬─";
			field[x][FIELD_HEIGHT - 1] = "┴─";
		}

		for (int y = 0; y < FIELD_HEIGHT; y++) {
			field[0][y] = "├─";
			field[FIELD_WIDTH - 1][y] = "┤";
		}

		field[0][0] = "┌─";
		field[FIELD_WIDTH - 1][0] = "┐";
		field[0][FIELD_HEIGHT - 1] = "└─";
		field[FIELD_WIDTH - 1][FIELD_HEIGHT - 1] = "┘";

		temp = 1; // 짝수 홀수로 돌 판단

		posX = DEFULAT_X;
		posY = DEFULAT_Y;

		isGameOver = false;
		clearBuffer();

		try { // 파일이 있으면 파일을 읽어온다.
			BufferedReader in = new BufferedReader(new FileReader("previousScore.txt"));
			previousScore[0] = Integer.parseInt(in.readLine());
			previousScore[1] = Integer.parseInt(in.readLine());
			in.close();
		} catch (FileNotFoundException e) { // 파일이 없을 경우 점수를 0으로 설정한다.
			previousScore[0] = 0;
			previousScore[1] = 0;
		} catch (IOException e) { // 파일 오류시
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initPlayer() {

		player = new Player(posX, posY, stone[temp]);
	}

	public void gameTiming() {
		// Game tick
		try {
			Thread.sleep(50);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void clearBuffer() {
		for (int y = 0; y < SCREEN_HEIGHT; y++) {
			for (int x = 0; x < SCREEN_WIDTH; x++) {
				buffer[x][y] = " ";
			}
		}
	}

	private void drawToBuffer(int px, int py, String c) {// 보드판 출력
		buffer[px + LEFT_PADDING][py + PADDING] = c;
	}

	private void drawToBuffer(int px, int py, char c) {// 돌 출력 용도의 버퍼
		buffer[px + LEFT_PADDING][py + PADDING] = String.valueOf(c); // char를 String로 형변환
	}

	private void drawPlayer() {
		drawToBuffer(posX, posY, stone[temp]);
	}

	public void drawAll() {
		// 보드 판 출력
		for (int x = 0; x < FIELD_WIDTH; x++) {
			for (int y = 0; y < FIELD_HEIGHT; y++) {
				drawToBuffer(x, y, field[x][y]);
			}
		}

		// 바둑판에 들어갔을때 기존 돌에 "─"를 추가한다.
		// 바둑판 한칸 당 기호가 2개 들어있기 때문
		if (range(posX, posY)) {
			if (posX < FIELD_WIDTH - 1 && posY >= 0 && posY < FIELD_HEIGHT) {
				if (temp == 1)
					stone[temp] = "○─";
				else
					stone[temp] = "●─";
			} else {
				if (temp == 1)
					stone[temp] = "○";
				else
					stone[temp] = "●";
			}
		}

		drawPlayer();

		drawCurrentScore();
		drawPreviousScore();

		render();
	}

	public void moveRight() {
		if (range(posX + 1, posY)) {
			if (posX < FIELD_WIDTH - 1 && posY > 0 && posY < FIELD_HEIGHT) {
				drawToBuffer(posX, posY, "┼─");
				posX += 1;
				return;
			} else {
				drawToBuffer(posX, posY, " ");
				posX += 1;
			}
		}
	}

	public void moveLeft() {

		if (range(posX - 1, posY)) {
			if (posX < FIELD_WIDTH - 1 && posY > 0 && posY < FIELD_HEIGHT) {
				drawToBuffer(posX, posY, "┼─");
				posX -= 1;
				return;
			} else {
				drawToBuffer(posX, posY, " ");
				posX -= 1;
			}

		}

	}

	public void moveUp() {
		if (range(posX, posY - 1)) {
			if (posX < FIELD_WIDTH - 1 && posY > 0 && posY < FIELD_HEIGHT) {
				drawToBuffer(posX, posY, "┼─");
				posY -= 1;
				return;
			} else {
				drawToBuffer(posX, posY, " ");
				posY -= 1;
			}

		}
	}

	public void moveDown() {
		if (range(posX, posY + 1)) {
			if (posX < FIELD_WIDTH - 1 && posY > 0 && posY < FIELD_HEIGHT) {
				drawToBuffer(posX, posY, "┼─");
				posY += 1;
				return;
			} else {
				drawToBuffer(posX, posY, " ");
				posY += 1;
			}

		}
	}

	public void putStone() {
		if (range(posX, posY)) {
			if (doesStoneFit(posX, posY)) {
				temp = (temp == 1) ? 0 : 1;
				posX = DEFULAT_X;
				posY = DEFULAT_Y;
			}
		}
	}

	private boolean range(int x, int y) { // 돌이 움직일 수 있는 범위 제한

		if (x < FIELD_WIDTH + 10 && x >= 0 && y < FIELD_HEIGHT && y >= 0) {
			return true;
		}
		return false;
	}

	private boolean doesStoneFit(int x, int y) { // 해당 위치에 돌이 있으면 false, 돌이 없으면 true
		if (x < FIELD_WIDTH && x >= 0 && y < FIELD_HEIGHT && y >= 0) {
			if (!field[posX][posY].equals("●") && !field[posX][posY].equals("●─") && !field[posX][posY].equals("○")
					&& !field[posX][posY].equals("○─")) {
				field[posX][posY] = stone[temp];
				return true;
			}
		}
		return false;
	}

	private void drawCurrentScore() {
		drawToBuffer(FIELD_WIDTH + 5, 1, "┌────CURRENT────┐");
		drawToBuffer(FIELD_WIDTH + 5, 2, "│   " + "●: " + currentScore[0] + " ○: " + currentScore[1] + "   │");
		drawToBuffer(FIELD_WIDTH + 5, 3, "└───────────────┘");
	}

	private void drawPreviousScore() {
		drawToBuffer(FIELD_WIDTH + 5, 5, "┌────PREVIOUS───┐");
		drawToBuffer(FIELD_WIDTH + 5, 6, "│   " + "●: " + previousScore[0] + " ○: " + previousScore[1] + "   │");
		drawToBuffer(FIELD_WIDTH + 5, 7, "└───────────────┘");
	}

	public void drawGameOver() {
		currentScore[win] += 1; // 해당 턴의 돌의 점수를 +1 한다.
		if (win == 0)
			stone[win] = "●";
		else
			stone[win] = "○";
		drawToBuffer(FIELD_WIDTH + 5, 9, "╔════════════════╗");
		drawToBuffer(FIELD_WIDTH + 5, 10, "║     " + stone[win] + " WIN!     ║");
		drawToBuffer(FIELD_WIDTH + 5, 11, "║                ║");
		drawToBuffer(FIELD_WIDTH + 5, 12, "║  AGAIN? (Y/N)  ║");
		drawToBuffer(FIELD_WIDTH + 5, 13, "╚════════════════╝");

		render();
		BufferedWriter out;

		try { // 점수 저장
			out = new BufferedWriter(new FileWriter("previousScore.txt"));
			out.write(String.valueOf(currentScore[0])); // 현재 검은돌, 흰돌 점수 저장
			out.newLine();
			out.write(String.valueOf(currentScore[1]));
			out.close();
		} catch (IOException e) { // 파일 오류의 경우
			e.printStackTrace();
		}
	}

	private void render() { // buffer에 있는 내용을 textArea에 setText()해야한다.
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < SCREEN_HEIGHT; y++) {
			for (int x = 0; x < SCREEN_WIDTH; x++) {
				sb.append(buffer[x][y]);
			}
			sb.append("\n");
		}
		textArea.setText(sb.toString());
	}

	boolean isGameOver() {//승리조건에 만족했는지 판단
       //승리조건 체크 자체를 기호하나하나로 판단하고 
		//검은 돌이 이겼는지, 흰돌이 이겼는지 판단할 수 있는 변수를 하나더 만들어서
		//승리메시지 출력할 때 사용할 수 있도록 한다. 
		
		
		for (int i = 0; i < FIELD_WIDTH; i++){ // 가로(→) 방향 체크
            for (int j = 0; j < FIELD_HEIGHT-4; j++) {   
                if ((field[i][j] == "●"||field[i][j] == "●─") && field[i][j + 1] == "●─" &&
                		field[i][j + 2] == "●─" && field[i][j + 3] == "●─" &&
                				(field[i][j + 4] == "●"||field[i][j + 4] == "●─")) {
                	win = 0;
                	return true;
                }
                if ((field[i][j] == "○"||field[i][j] == "○─") && field[i][j + 1] == "○─" &&
                		field[i][j + 2] == "○─" && field[i][j + 3] == "○─" &&
                				(field[i][j + 4] == "○"||field[i][j + 4] == "○─")) {
                	win = 1;
                	return true;
                }
            }
        }
		
		for (int i = 0; i < FIELD_WIDTH-4; i++) {// 세로(↓) 방향 체크,
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if ((field[i][j] == "●" || field[i][j] == "●─") && field[i + 1][j] == "●─" &&
                		field[i + 2][j] == "●─" && field[i + 3][j] == "●─" &&
                				(field[i + 4][j] == "●"||field[i + 4][j] == "●─")) {
                	win = 0;
                	return true;
                }
                if ((field[i][j] == "○" || field[i][j] == "○─") && field[i + 1][j] == "○─" &&
                		field[i + 2][j] == "○─" && field[i + 3][j] == "○─" &&
                				(field[i + 4][j] == "○"||field[i + 4][j] == "○─")) {
                	win = 1;
                	return true;
                }
            }		
		}
		
		for (int i = 0; i < FIELD_WIDTH-4; i++) {// 대각선(↘) 방향 체크,
            for (int j = 0; j < FIELD_HEIGHT-4; j++) {
                if ((field[i][j] == "●"||field[i][j] == "●─") && field[i + 1][j + 1] == "●─" &&
                		field[i + 2][j + 2] == "●─" && field[i + 3][j + 3] == "●─" &&
                        		(field[i + 4][j + 4] == "●"||field[i + 4][j + 4] == "●─")) {
					win = 0;
					return true;
				}
                if ((field[i][j] == "○"||field[i][j] == "○─") && field[i + 1][j + 1] == "○─" &&
                		field[i + 2][j + 2] == "○─" && field[i + 3][j + 3] == "○─" &&
                        		(field[i + 4][j + 4] == "○"||field[i + 4][j + 4] == "○─")) {
					win = 1;
					return true;
				}
            }
		}
		
        for (int i = 0; i < FIELD_WIDTH-4; i++) {// 대각선(↙) 방향 체크,
            for (int j = 4; j < FIELD_HEIGHT; j++) { 
            	if ((field[i][j] == "●"||field[i][j] == "●─") && field[i + 1][j - 1] == "●─" &&
                		field[i + 2][j - 2] == "●─" && field[i + 3][j - 3] == "●─" &&
                				(field[i + 4][j - 4] == "●"||field[i + 4][j - 4] == "●─")) {
                	win = 0;
					return true;
                }
                if ((field[i][j] == "○"||field[i][j] == "○─") && field[i + 1][j - 1] == "○─" &&
                		field[i + 2][j - 2] == "○─" && field[i + 3][j - 3] == "○─" &&
                				(field[i + 4][j - 4] == "○"||field[i + 4][j - 4] == "○─")) {
                	win = 1;
					return true;
                }
            }
        }
        return false; 
	}
}
