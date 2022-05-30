package com.niceplaces.niceplaces.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.dao.DaoQuiz
import com.niceplaces.niceplaces.models.Quiz
import com.niceplaces.niceplaces.utils.MyRunnable

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvPlaceName: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var tvScore: TextView
    private var btnsAnswers: MutableList<Button> = ArrayList()
    private var currentQuestion = -1
    private var questionList: MutableList<Quiz> = ArrayList()
    private var answerList: MutableList<String> = ArrayList()
    private var correctCount = 0
    private var totalCount = 0
    private var remainedQuestions: MutableList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        supportActionBar!!.hide()
        val layout = findViewById<LinearLayout>(R.id.layout_quiz)
        layout.visibility = GONE
        tvPlaceName = findViewById(R.id.tv_quiz_place_name)
        tvQuestion = findViewById(R.id.tv_quiz_question)
        btnsAnswers.add(findViewById(R.id.btn_quiz_answer1))
        btnsAnswers.add(findViewById(R.id.btn_quiz_answer2))
        btnsAnswers.add(findViewById(R.id.btn_quiz_answer3))
        tvScore = findViewById(R.id.tv_quiz_score)
        for (btn in btnsAnswers){
            btn.setOnClickListener(this)
        }
        val daoQuiz = DaoQuiz(this)
        val successCallback: MyRunnable = object : MyRunnable() {
            override fun run() {
                questionList = this.quiz!!
                for (i in 0 until questionList.size){
                    remainedQuestions.add(i)
                }
                loadQuestion()
                layout.visibility = VISIBLE
                //alertController.loadingSuccess()
            }
        }
        daoQuiz.getAll(successCallback, Runnable {
            //alertController.loadingError()
        }
        )
    }

    private fun loadQuestion() {
        for (btn in btnsAnswers){
            btn.setBackgroundResource(R.drawable.bg_quiz_answer)
        }
        val choiceIndex = (0 until remainedQuestions.size).random()
        val choice = remainedQuestions[choiceIndex]
        remainedQuestions.remove(choice)
        currentQuestion = choice
        tvPlaceName.setText(questionList[currentQuestion].placeName + "\n" +
                questionList[currentQuestion].areaName)
        tvQuestion.setText(questionList[currentQuestion].question)
        answerList = ArrayList()
        answerList.add(questionList[currentQuestion].correctAnswer)
        answerList.add(questionList[currentQuestion].wrongAnswer1)
        answerList.add(questionList[currentQuestion].wrongAnswer2)
        answerList.shuffle()
        for (i in 0 until answerList.size){
            btnsAnswers[i].setText(answerList[i])
        }
        try {
            tvScore.setText("Domanda " + (totalCount+1) + " di " + questionList.size + "\n" +
                    "Punteggio: " + correctCount*100/totalCount + "%")
        } catch (e: ArithmeticException){
            tvScore.setText("Domanda " + (totalCount+1) + " di " + questionList.size + "\n" +
                    "Punteggio: -")
        }
    }

    override fun onClick(view: View?) {
        revealSolution(view)
        totalCount++
        Handler().postDelayed({
            if (remainedQuestions.size > 0){
                loadQuestion()
            } else {
                val result = correctCount*100/totalCount
                tvScore.setText("Domanda $totalCount di ${questionList.size} \n" +
                        "Punteggio: $result%")
                val alertDialogBuilder = AlertDialog.Builder(this)
                    .setTitle("Risultato")
                    .setMessage("$result%")
                alertDialogBuilder.show()
            }
        }, 3000)
    }

    private fun revealSolution(view: View?) {
        if ((view as Button).text == questionList[currentQuestion].correctAnswer){
            view.setBackgroundResource(R.drawable.bg_quiz_answer_correct)
            correctCount++
        } else {
            view.setBackgroundResource(R.drawable.bg_quiz_answer_wrong)
            for (btn in btnsAnswers){
                if (btn.text.toString() == questionList[currentQuestion].correctAnswer){
                    btn.setBackgroundResource(R.drawable.bg_quiz_answer_correct)
                }
            }
        }
    }
}