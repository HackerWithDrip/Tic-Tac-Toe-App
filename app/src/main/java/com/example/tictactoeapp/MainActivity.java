package com.example.tictactoeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button[] buttons = new Button[9];
    private Button resetGame;
    private  int playerOneScoreCount, playerTwoScoreCount, roundCount;
    boolean activePlayer;

    // p1=>0
    //p2 =>1
    // empty =>2
    int[] gameState = {2,2,2,2,2,2,2,2,2};

    //identify all winning positions

    int[][] winningPositions = {
            {0,1,2},{3,4,5},{6,7,8}, // rows
            {0,3,6},{1,4,7},{2,5,8}, // columns
            {0,4,8},{2,4,6}// cross/diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore =(TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);
        resetGame =  findViewById(R.id.btnResetGame);

        for(int i = 0; i < buttons.length; i++){
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID,"id",getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        roundCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
    }

    @Override
    public void onClick(View view) {
        //ensure player two does not press player one button
        if(!((Button)view).getText().toString().equals("")){
            return;
        }

        //which button has been pressed and identify the position in the array

        String buttonID = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer =Integer.parseInt( buttonID.substring(buttonID.length()-1,buttonID.length())); // 2

        if(activePlayer){
            ((Button)view).setText("X");
            ((Button)view).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        }else {
            ((Button)view).setText("O");
            ((Button)view).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        roundCount++;

        if(checkWinner()){
            if(activePlayer){
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this,"Player One Won!",Toast.LENGTH_SHORT).show();
                playAgain();
            }else {
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this,"Player Two Won!",Toast.LENGTH_SHORT).show();
                playAgain();
            }

        }else if(roundCount==9){
            playAgain();
            Toast.makeText(this,"No Winner!",Toast.LENGTH_SHORT).show();
        }else {
            activePlayer = !activePlayer;
        }

        if(playerOneScoreCount>playerTwoScoreCount){
            playerStatus.setText("Player One is winning!");
        }
        else if(playerOneScoreCount<playerTwoScoreCount){
            playerStatus.setText("Player Two is winning!");
        }else {
            playerStatus.setText("");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }
    });

    }

    public boolean checkWinner(){
        boolean winnerResult = false;

        for(int [] winningPos:winningPositions){
            if(gameState[winningPos[0]]==gameState[winningPos[1]] &&
                    gameState[winningPos[1]] == gameState[winningPos[2]] &&
                    gameState[winningPos[0]] !=2)
            {
                winnerResult = true;
            }
        }
        return  winnerResult;
    }

    public  void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public  void playAgain(){
        roundCount = 0;
        activePlayer = true;

        for(int i = 0;i< buttons.length;i++){
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }
}