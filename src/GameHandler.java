import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GameHandler {
    private final int SCREEN_WIDTH = 30; //출력되는 화면 크기 범위
    private final int SCREEN_HEIGHT = 30;
    private final int FIELD_WIDTH = 15, FIELD_HEIGHT = 15; //바둑판 높이,너비
    private final int LEFT_PADDING = 3; //바둑판 왼쪽 띄우기 정도
    private final int PADDING = 2; //바둑판 위쪽 공간 띄우기

    private char stone[]; //흰돌, 검은 돌 이미지 저장
    private int num[]; //흰돌, 검은 돌 구별
    private int temp; //짝수 홀수로 돌 순서 판단
    private int posX, posY; //돌위치 중간 저장
    private final int DEFULAT_X = FIELD_WIDTH + 3;
    private final int DEFULAT_Y = FIELD_HEIGHT - 5;



    private String playerImage;
    private Player player;
    private int currentScore[], previousScore[]; //점수 저장 (이전, 현재)

    private boolean isGameOver;

    private JTextArea textArea;
    private String[][] buffer;
    private String[][] field;

    public GameHandler(JTextArea ta) {
        textArea = ta; //JFrame에 있는 textArea
        field = new String[FIELD_WIDTH][FIELD_HEIGHT];
        buffer = new String[SCREEN_WIDTH][SCREEN_HEIGHT];
        stone = new char[2];
        currentScore = new int[2];
        previousScore = new int[2];

        stone[0] = '●';
        stone[1] = '○';

        currentScore[0] = 0;
        currentScore[1] = 0;

        previousScore[0] = 0;
        previousScore[1] = 0;


        initData();
    }

    public void initData() {
        //돌 놓는 판 구역에 기호 할당

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                field[x][y] = "┼─";
            }
        }

        for(int x = 0; x < FIELD_WIDTH; x++) {
            field[x][0] = "┬─";
            field[x][FIELD_HEIGHT-1] = "┴─";
        }

        for(int y = 0; y < FIELD_HEIGHT; y++) {
            field[0][y] = "├─";
            field[FIELD_WIDTH-1][y] = "┤";
        }

        field[0][0] = "┌─";
        field[FIELD_WIDTH-1][0] = "┐";
        field[0][FIELD_HEIGHT-1] = "└─";
        field[FIELD_WIDTH-1][FIELD_HEIGHT-1] = "┘";

        temp = 1; //짝수 홀수로 돌 판단

        posX = DEFULAT_X;
        posY = DEFULAT_Y;


        //currentX = 3*2; //돌 처음 위치 값 지정 , 4번째줄 함수 상으로 3번째
        //내가 원하는 n번째 줄 -> LEFT_PADDING + n*2...
        //currentY = 1;

        isGameOver = false;
        clearBuffer();

        try { //파일이 있으면 파일을 읽어온다.
            BufferedReader in = new BufferedReader(new FileReader("previousScore.txt"));
            previousScore[0] = Integer.parseInt(in.readLine());
            previousScore[1] = Integer.parseInt(in.readLine());
            in.close();
        } catch (FileNotFoundException e) { //파일이 없을 경우 점수를 0으로 설정한다.
            previousScore[0] = 0;
            previousScore[1] = 0;
        } catch (IOException e) { //파일 오류시
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initPlayer(){

        player = new Player(posX, posY, stone[temp]);
    }


    public void gameTiming() {
        // Game tick
        try{
            Thread.sleep(50);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void clearBuffer() {
        for(int y = 0; y < SCREEN_HEIGHT; y++) {
            for(int x = 0; x < SCREEN_WIDTH; x++) {
                buffer[x][y] = " ";
            }
        }
    }

    private void drawToBuffer (int px, int py, String c) {//보드판 출력
        buffer[px + LEFT_PADDING][py + PADDING] = c;
    }

    private void drawToBuffer (int px, int py, char c) {//돌 출력 용도의 버퍼
        buffer[px+ LEFT_PADDING][py + PADDING] = String.valueOf(c); //char를 String로 형변환
    }

    private void drawPlayer(){
        drawToBuffer(posX,posY,stone[temp]);
    }

    public void drawAll() {
        //보드 판 출력
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                drawToBuffer(x, y, field[x][y]);
            }
        }

        drawPlayer();

        drawCurrentScore();
        drawPreviousScore();

        render();
    }

    public void moveRight(){

    }
    public void moveLeft(){

    }
    public void moveUp(){

    }
    public void moveDown(){

    }
    public void stonePut(){

    }

    private boolean isStoneFit(){

        return false;
    }
    private void drawCurrentScore() {

        drawToBuffer(FIELD_WIDTH + 5, 1, "┌────CURRENT────┐");
        drawToBuffer(FIELD_WIDTH + 5, 2, "│   "+ "●: " + currentScore[0] + " ○: " + currentScore[1] + "   │");
        drawToBuffer(FIELD_WIDTH + 5, 3, "└───────────────┘");

    }
    private void drawPreviousScore() {

        drawToBuffer(FIELD_WIDTH + 5, 5, "┌────PREVIOUS───┐");
        drawToBuffer(FIELD_WIDTH + 5, 6, "│   "+ "●: " + previousScore[0] + " ○: " + previousScore[1] + "   │");
        drawToBuffer(FIELD_WIDTH + 5, 7, "└───────────────┘");
    }

    private void render() { //buffer에 있는 내용을 textArea에 setText()해야한다.
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                sb.append(buffer[x][y]);
            }
            sb.append("\n");
        }
        textArea.setText(sb.toString());
    }

    boolean isGameOver( ){
        return isGameOver;
    }

}
    /*bool check(int k, string stone[19][19]);
    void location(string stone[19][19]);
    void PrintOmok(int num, string stone[][19]);

    int main()
    {
        int num = 1;
        int k = 1;
        int across, down;
        char ans;
        string stone[19][19];

        while (true)
        {
            system("cls");
            location(stone);//각각 기호에 주소 배당

            while (true)
            {
                PrintOmok(num, stone);//오목판 출력

                cout << endl;
                cout << endl;
                //좌표를 입력 받는다.
                while (true)
                {
                    if (check(k, stone) == false)
                    {
                        if (stone[down - 1][across - 1] == "●" || stone[down - 1][across - 1] == "○")
                        {
                            cout << "Can not rut there. Try again." << endl;
                            continue;
                        }
                        else
                        {
                            if (k % 2 == 0)
                            {
                                cout << "●'s X,Y : ";
                                stone[down - 1][across - 1] = "●";
                            }
                            else
                            {
                                cout << "○'s X,Y : ";
                                stone[down - 1][across - 1] = "○";
                            }
                            cin >> across >> down;
                            break;
                        }
                    }
                }

                k++;

                if (check(k, stone) == false)
                {
                    system("cls");
                    continue;
                }
                else if (check(k, stone) == true)
                {
                    if (k % 2 == 0)
                        cout << "● Win" << endl;
                    if (k % 2 == 1)
                        cout << "○ Win" << endl;
                    break;
                }
                break;
            }
            cout << "Play again ? (y / n)";
            cin >> ans;
            if (ans == 'y' || ans == 'Y')
                continue;
            if (ans == 'n' || ans == 'N')
                break;
        }
        return 0;
    }

    void location(string stone[19][19]) //각각 기호에 주소 배당
    {
        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                stone[i][j] = "╋ ";
            }
        }

        for (int i = 1; i < 18; i++)
        {
            stone[0][i] = "┳-";
            stone[i][0] = "┣-";
            stone[i][18] = "┫";
            stone[18][i] = "┻-";
        }

        stone[0][0] = "┏ ";
        stone[0][18] = "┐";
        stone[18][0] = "┗ ";
        stone[18][18] = "┘";
    }

    void PrintOmok(int num, string stone[][19]) //오목판 출력
    {
        cout << "      ====== Let's Play Omok =====      " << endl;
        cout << endl;
        cout << "  ";
        // 1...19까지 출력
        num = 1;
        for (int i = 1; i < 20; i++)
        {
            cout << setw(2) << num;
            num++;
        }

        cout << endl;
        num = 1;
        //바둑판 출력
        for (int i = 0; i < 19; i++)
        {
            cout << setw(2) << num;
            num++;

            for (int j = 0; j < 19; j++)
            {
                cout << stone[i][j];
            }
            cout << endl;
        }
    }

    bool check(int k, string stone[19][19])
    {
        string player;
        if (k % 2 == 0)
            player = "●";
        if (k % 2 == 1)
            player = "○";

        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {   // 가로(→) 방향 체크,
                if (stone[i][j] == player && stone[i][j + 1] == player &&
                        stone[i][j + 2] == player && stone[i][j + 3] == player &&
                        stone[i][j + 4] == player) return true;
                    // 세로(↓) 방향 체크,
                else if (stone[i][j] == player && stone[i + 1][j] == player &&
                        stone[i + 2][j] == player && stone[i + 3][j] == player &&
                        stone[i + 4][j] == player) return true;
                    // 대각선(↘) 방향 체크,
                else if (stone[i][j] == player && stone[i + 1][j + 1] == player &&
                        stone[i + 2][j + 2] == player && stone[i + 3][j + 3] == player &&
                        stone[i + 4][j + 4] == player) return true;
                    // 대각선(↙) 방향 체크,
                else if (stone[i][j] == player && stone[i + 1][j - 1] == player &&
                        stone[i + 2][j - 2] == player && stone[i + 3][j - 3] == player &&
                        stone[i + 4][j - 4] == player) return true;
            }
        }
        return false;
    }
}*/
