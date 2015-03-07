package org.ilim.offlinesurvey;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import org.ilim.offlinesurvey.models.AppData;
import org.ilim.offlinesurvey.models.AppSetting;
import org.ilim.offlinesurvey.models.SubSurvey;

import java.util.ArrayList;


public class SurveyActivity extends ActionBarActivity {

    public int selectedSurveyId = 0;
    public String selectedSurveyName = "";

    public int selectedSubSurveyId = 0;
    public String selectedSubSurveyName = "";

    private SubSurvey testAllData;
    private ProgressBar surveyActProgressBar;
    private ScrollView surveyActParentScrollView;
    private ImageView surveyActImgView;
    private ImageButton buttonSurveyAnswerNext;
    private ImageButton buttonSurveyAnswerConfirm;
    private ImageButton buttonSurveyAnswerDetermine;

    private FrameLayout showQuestionResultFrame;
    private ImageView showQuestionResultImage;

    ImageButton btnOptionA;
    ImageButton btnOptionB;
    ImageButton btnOptionC;
    ImageButton btnOptionD;
    ImageButton btnOptionEmpty;

    private Animation imageAnimation;
    private Animation btnAnimation;
    private Animation questionAnswerAnimationIn;
    private Animation questionAnswerAnimationOut;

    private int testQuestionIndex;
    private int testQuestionCount;
    private String[] testAnswersAll;
    private String selectedOption;

    private int userAnswerTrueCount;
    private int userAnswerFalseCount;
    private int userAnswerEmptyCount;
    private ArrayList<String> userAnswerAll;

    private Boolean isFinished = false;

    private int screenWidth;
    private int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);
        //getSupportActionBar().hide();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get screen width and hight
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;

        Intent intent = getIntent();
        selectedSurveyId = Integer.valueOf(intent.getStringExtra("selectedSurveyId"));
        selectedSurveyName = intent.getStringExtra("selectedSurveyName");
        selectedSubSurveyId = Integer.valueOf(intent.getStringExtra("selectedSubSurveyId"));
        selectedSubSurveyName = intent.getStringExtra("selectedSubSurveyName");
        setTitle(selectedSurveyName + " : " + selectedSubSurveyName);

        myLoadScreenDefault();


    }

    public void myLoadScreenDefault() {

        isFinished = false;
        testQuestionIndex = 0;
        testQuestionCount = 0;
        testAnswersAll = null;
        selectedOption = "";

        userAnswerTrueCount = 0;
        userAnswerFalseCount = 0;
        userAnswerEmptyCount = 0;
        userAnswerAll = new ArrayList<>();

        //GET DATA
        AppData allData = new AppData();
        testAllData = allData.getSurveyAllData(selectedSubSurveyId);

        surveyActProgressBar = (ProgressBar) findViewById(R.id.testler_progress_bar);
        surveyActParentScrollView = (ScrollView) findViewById(R.id.testler_parent_scrollview);
        surveyActImgView = (ImageView) findViewById(R.id.testler_img_view);
        buttonSurveyAnswerNext = (ImageButton) findViewById(R.id.button_survey_question_next);
        buttonSurveyAnswerConfirm = (ImageButton) findViewById(R.id.button_survey_question_confirm);
        buttonSurveyAnswerDetermine = (ImageButton) findViewById(R.id.button_survey_question_determine);

        buttonSurveyAnswerNext.setVisibility(View.INVISIBLE);
        buttonSurveyAnswerConfirm.setVisibility(View.INVISIBLE);
        buttonSurveyAnswerDetermine.setVisibility(View.VISIBLE);

        testAnswersAll = testAllData.testler.trotKeys;
        testQuestionCount = testAnswersAll.length;

        btnOptionA = (ImageButton) findViewById(R.id.option_a);
        btnOptionB = (ImageButton) findViewById(R.id.option_b);
        btnOptionC = (ImageButton) findViewById(R.id.option_c);
        btnOptionD = (ImageButton) findViewById(R.id.option_d);
        btnOptionEmpty = (ImageButton) findViewById(R.id.option_empty);

        showQuestionResultFrame = (FrameLayout) findViewById(R.id.show_question_result_frame);
        showQuestionResultImage = (ImageView) findViewById(R.id.show_question_result_image);

        questionAnswerAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.survey_slide_left_in);
        questionAnswerAnimationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.survey_slide_left_out);
        showQuestionResultFrame.setVisibility(View.INVISIBLE);

        setNextButtonsUnLock();
        setOptionButtonsUnLock();

        buttonSurveyAnswerDetermine.setEnabled(false);
        loadNextSurveyQuestion();
        String msg = getResources().getString(R.string.f_survey_start_info_text_1) + testQuestionCount + getResources().getString(R.string.f_survey_start_info_text_2);
        showInfoDialog(getResources().getString(R.string.f_survey_start_info_title), msg);
    }


    public void showFinishDialog() {

        isFinished = true;
        saveUserSurveyAnswers();
        setUserProgress(100);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_finish_surveys);

        ImageButton buttonStartTestAgain = (ImageButton) dialog.findViewById(R.id.btn_start_test_again);
        ImageButton buttonStartTestNew = (ImageButton) dialog.findViewById(R.id.btn_start_test_new);

        buttonStartTestAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                myLoadScreenDefault();
            }
        });

        buttonStartTestNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startMenuMainActivity();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(0, 200, 200, 200)));
        dialog.setCanceledOnTouchOutside(false);

        showSurveyResultDialog();


    }

    public void showSurveyResultDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getResources().getString(R.string.f_survey_end_button_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //
            }
        });
        AlertDialog alertDialog = builder.create();
        String title = getResources().getString(R.string.f_survey_end_alert_title);
        String message = getResources().getString(R.string.f_survey_end_alert_true_count) + userAnswerTrueCount +
                getResources().getString(R.string.f_survey_end_alert_false_count) + userAnswerFalseCount +
                getResources().getString(R.string.f_survey_end_alert_empty_count) + userAnswerEmptyCount;
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    public String getDirectoryPath() {
        return "drawable";
    }

    public String getQuestionImageFilePath() {

        return testAllData.questionImagePrfx + testQuestionIndex;
    }

    public void showInfoDialog(final String title, final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getResources().getString(R.string.f_survey_info_start_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //DO TASK
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.show();
    }
  public void showErrorDialog(final String title, final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getResources().getString(R.string.f_survey_end_button_error), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startMenuMainActivity();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.show();
    }


    public void resetLayoutView() {
        setOptionButtonsNoHover();
        selectedOption = "";
        buttonSurveyAnswerNext.setVisibility(View.INVISIBLE);
        buttonSurveyAnswerConfirm.setVisibility(View.INVISIBLE);
        buttonSurveyAnswerDetermine.setVisibility(View.VISIBLE);
        showQuestionResultFrame.setVisibility(View.INVISIBLE);

    }


    public void loadNextSurveyQuestion() {
        testQuestionIndex++;
        resetLayoutView();
        if (testQuestionIndex <= testQuestionCount) {
            int imageResourceId = getResources().getIdentifier(getQuestionImageFilePath(), getDirectoryPath(), getPackageName());

            if (imageResourceId > 0) {
                imageAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.survey_anim_3);
                surveyActImgView.setImageResource(imageResourceId);
                surveyActParentScrollView.scrollTo(0, 0);
                surveyActImgView.startAnimation(imageAnimation);
                int prc = (100 * testQuestionIndex) / testQuestionCount;
                surveyActProgressBar.setProgress(prc);
                setUserProgress(prc);
                setOptionButtonsUnLock();

            } else {
                showErrorDialog(getApplication().getString(R.string.f_error_title), getApplication().getString(R.string.f_error_not_found));
            }
        } else {

            setOptionButtonsLock();
            setNextButtonsLock();
            showFinishDialog();
        }
    }


    public void checkUserAnswer(View v) {
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.survey_btn_anim_1);

        switch (v.getId()) {
            case R.id.button_survey_question_determine:
                break;
            case R.id.button_survey_question_confirm:
                buttonSurveyAnswerConfirm.setVisibility(View.INVISIBLE);
                buttonSurveyAnswerDetermine.setVisibility(View.INVISIBLE);
                buttonSurveyAnswerNext.setVisibility(View.VISIBLE);
                buttonSurveyAnswerNext.startAnimation(btnAnimation);
                setOptionButtonsLock();
                userAnswerSave();
                break;
            case R.id.button_survey_question_next:
                buttonSurveyAnswerConfirm.setVisibility(View.INVISIBLE);
                buttonSurveyAnswerDetermine.setVisibility(View.VISIBLE);
                buttonSurveyAnswerNext.setVisibility(View.INVISIBLE);
                buttonSurveyAnswerDetermine.startAnimation(btnAnimation);
                loadNextSurveyQuestion();
                break;
            default:
                break;
        }
    }


    public void optionHover(View v) {

        switch (v.getId()) {
            case R.id.option_a:
                changeOptionButtonsHover("a");
                selectedOption = "a";
                break;
            case R.id.option_b:
                changeOptionButtonsHover("b");
                selectedOption = "b";
                break;
            case R.id.option_c:
                changeOptionButtonsHover("c");
                selectedOption = "c";
                break;
            case R.id.option_d:
                changeOptionButtonsHover("d");
                selectedOption = "d";
                break;
            case R.id.option_empty:
                changeOptionButtonsHover("empty");
                selectedOption = "empty";
                break;
        }

        buttonSurveyAnswerConfirm.setVisibility(View.VISIBLE);
        buttonSurveyAnswerDetermine.setVisibility(View.INVISIBLE);
        buttonSurveyAnswerNext.setVisibility(View.INVISIBLE);

    }


    public void changeOptionButtonsHover(String sOption) {
        setOptionButtonsNoHover();
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.survey_btn_anim_1);
        if (sOption == "a") {
            btnOptionA.setBackgroundResource(R.drawable.option_a_hover);
            btnOptionA.startAnimation(btnAnimation);
        } else if (sOption == "b") {
            btnOptionB.setBackgroundResource(R.drawable.option_b_hover);
            btnOptionB.startAnimation(btnAnimation);
        } else if (sOption == "c") {
            btnOptionC.setBackgroundResource(R.drawable.option_c_hover);
            btnOptionC.startAnimation(btnAnimation);
        } else if (sOption == "d") {
            btnOptionD.setBackgroundResource(R.drawable.option_d_hover);
            btnOptionD.startAnimation(btnAnimation);
        } else if (sOption == "empty") {
            btnOptionEmpty.setBackgroundResource(R.drawable.option_empty_hover);
            btnOptionEmpty.startAnimation(btnAnimation);
        }

    }


    public void setOptionButtonsNoHover() {
        btnOptionA.setBackgroundResource(R.drawable.option_a);
        btnOptionB.setBackgroundResource(R.drawable.option_b);
        btnOptionC.setBackgroundResource(R.drawable.option_c);
        btnOptionD.setBackgroundResource(R.drawable.option_d);
        btnOptionEmpty.setBackgroundResource(R.drawable.option_empty);
    }

    public void setOptionButtonsLock() {
        btnOptionA.setEnabled(false);
        btnOptionB.setEnabled(false);
        btnOptionC.setEnabled(false);
        btnOptionD.setEnabled(false);
        btnOptionEmpty.setEnabled(false);
    }

    public void setOptionButtonsUnLock() {
        btnOptionA.setEnabled(true);
        btnOptionB.setEnabled(true);
        btnOptionC.setEnabled(true);
        btnOptionD.setEnabled(true);
        btnOptionEmpty.setEnabled(true);
    }

    public void setNextButtonsLock() {
        buttonSurveyAnswerConfirm.setEnabled(false);
        buttonSurveyAnswerDetermine.setEnabled(false);
        buttonSurveyAnswerNext.setEnabled(false);
    }

    public void setNextButtonsUnLock() {
        buttonSurveyAnswerConfirm.setEnabled(true);
        buttonSurveyAnswerDetermine.setEnabled(true);
        buttonSurveyAnswerNext.setEnabled(true);
    }

    public void userAnswerSave() {
        String userAnswer = selectedOption.toString().toLowerCase();
        String trueAnswer = testAnswersAll[testQuestionIndex - 1].toString().toLowerCase();
        if (userAnswer == null || trueAnswer == null) {

            showInfoDialog(getApplication().getString(R.string.f_info_error_title), getApplication().getString(R.string.f_info_error_answer_not_found));
        } else {
            if (userAnswer.equals("empty")) {
                userAnswerEmptyCount++;
                userAnswerAll.add(userAnswer);
                loadNextSurveyQuestion();
            } else {
                if (userAnswer.equals(trueAnswer)) {
                    userAnswerTrueCount++;
                    int imageResourceId = getResources().getIdentifier(AppSetting.surveyOptionIconPrefix + "true", "drawable", getPackageName());
                    showQuestionResultImage.setImageResource(imageResourceId);
                } else {
                    userAnswerFalseCount++;
                    int imageResourceId = getResources().getIdentifier(AppSetting.surveyOptionIconPrefix + trueAnswer, "drawable", getPackageName());
                    showQuestionResultImage.setImageResource(imageResourceId);
                }
                showQuestionResultFrame.setVisibility(View.VISIBLE);
                showQuestionResultFrame.startAnimation(questionAnswerAnimationIn);
                userAnswerAll.add(userAnswer);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (isFinished) {
            showFinishDialog();
        }
    }

    public void setUserProgress(int currentSurveyNewProgress) {

        String subSurveyIdName = "user_test_progress_" + selectedSurveyId + "_" + selectedSubSurveyId;
        SharedPreferences userProgress = getSharedPreferences("usrSurveyPrgs", MODE_PRIVATE);
        int currentSurveyProgress = userProgress.getInt(subSurveyIdName, 1);

        if (currentSurveyNewProgress > currentSurveyProgress) {
            currentSurveyProgress = currentSurveyNewProgress;
            SharedPreferences.Editor userProgressEditor = userProgress.edit();
            userProgressEditor.putInt(subSurveyIdName, currentSurveyProgress);
            userProgressEditor.commit();
        }

    }

    public void saveUserSurveyAnswers() {

        String us1 = "user_test_result_answer_all_" + selectedSurveyId + "_" + selectedSubSurveyId;
        String us2 = "user_test_result_true_count_" + selectedSurveyId + "_" + selectedSubSurveyId;
        String us3 = "user_test_result_false_count_" + selectedSurveyId + "_" + selectedSubSurveyId;
        String us4 = "user_test_result_empty_count_" + selectedSurveyId + "_" + selectedSubSurveyId;

        SharedPreferences userAnswersPro = getSharedPreferences("usrSurveyPrgs", MODE_PRIVATE);
        SharedPreferences.Editor userAnswersEditor = userAnswersPro.edit();

        String ansStr = "";

        for (String a : userAnswerAll) {
            ansStr = ansStr + a.toString().toLowerCase() + "|";
        }

        userAnswersEditor.putString(us1, ansStr);
        userAnswersEditor.putInt(us2, userAnswerTrueCount);
        userAnswersEditor.putInt(us3, userAnswerFalseCount);
        userAnswersEditor.putInt(us4, userAnswerEmptyCount);
        userAnswersEditor.commit();

    }

    public void startMenuMainActivity() {
        Intent intent = new Intent(this, MenuMainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
