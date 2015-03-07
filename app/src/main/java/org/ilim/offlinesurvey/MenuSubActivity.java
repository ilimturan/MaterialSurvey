package org.ilim.offlinesurvey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ilim.offlinesurvey.models.AppData;
import org.ilim.offlinesurvey.models.SubSurvey;

import java.util.Iterator;
import java.util.Map;


public class MenuSubActivity extends ActionBarActivity {

    public int selectedSurveyId = 0;
    public String selectedSurveyName = "";

    public int selectedSubSurveyId = 0;
    public String selectedSubSurveyName = "";


    private Animation animationZoomIn;
    private Animation animationZoomOut;


    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sub);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get screen width and hight
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;

        animationZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_left_in);
        animationZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_left_out);

        Intent intent = getIntent();
        selectedSurveyId = Integer.valueOf(intent.getStringExtra("selectedSurveyId"));
        selectedSurveyName = intent.getStringExtra("selectedSurveyName");
        selectedSubSurveyId = Integer.valueOf(intent.getStringExtra("selectedSubSurveyId"));
        selectedSubSurveyName = intent.getStringExtra("selectedSubSurveyName");
        setTitle(selectedSurveyName);

        GridLayout menuContainerGrid = (GridLayout) findViewById(R.id.sub_menu_container_grid);
        AppData allData = new AppData();
        Map menuData = allData.getSubSurveyMenu(selectedSurveyId);
        Iterator entries = menuData.entrySet().iterator();

        while (entries.hasNext()) {
            final Map.Entry sbSrvEnt = (Map.Entry) entries.next();
            Object key = sbSrvEnt.getKey();
            final SubSurvey subSurvey = (SubSurvey) sbSrvEnt.getValue();

            subSurvey.subSurveyProgress = getSubSurveyProgress(selectedSurveyId, subSurvey.subSurveyId);

            // Create LinearLayout
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            final ImageButton imgBtn = new ImageButton(this);
            int imageResourceId = getResources().getIdentifier((String) subSurvey.subSurveyImageIcon, "drawable", getPackageName());
            if(imageResourceId > 0){
                imgBtn.setBackground(getResources().getDrawable(imageResourceId));
            }

            BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(imageResourceId);
            int fheight = bd.getBitmap().getHeight();
            int fwidth = bd.getBitmap().getWidth();
            int xx = screenWidth / 3;
            int yy = xx * fheight / fwidth; //xx * imgBtn.getHeight() / imgBtn.getWidth();

            LinearLayout.LayoutParams layoutParamsImgBtn = new LinearLayout.LayoutParams(xx, yy);
            layoutParamsImgBtn.setMargins(5, 5, 5, 5);

            imgBtn.setLayoutParams(layoutParamsImgBtn);
            ll.addView(imgBtn);

            //Create progress bar
            LinearLayout.LayoutParams layoutParamsPrgBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ProgressBar prgBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            prgBar.setProgress(subSurvey.subSurveyProgress);
            prgBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_menu));
            prgBar.setLayoutParams(layoutParamsPrgBar);
            prgBar.setMinimumHeight(2);
            ll.addView(prgBar);

            // Create TextView
            TextView textView = new TextView(this);
            ViewGroup.LayoutParams layoutParamsText = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            textView.setText(subSurvey.subSurveyName);
            textView.setGravity(Gravity.TOP);
            textView.setGravity(Gravity.CENTER);
            textView.setMinLines(2);
            textView.setLayoutParams(layoutParamsText);
            textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
            textView.setTextColor(R.color.f_default_text_color);
            textView.setMinLines(3);
            ll.addView(textView);
            ll.setPadding(20, 20, 20, 20);

            imgBtn.startAnimation(animationZoomIn);
            // Set click listener for button
            imgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtn.startAnimation(animationZoomOut);
                    selectedSubSurveyId = (int) sbSrvEnt.getKey();
                    selectedSubSurveyName = subSurvey.subSurveyName;
                    startSurveysActivity();

                }
            });

            menuContainerGrid.addView(ll);
        }
    }


    public int getSubSurveyProgress(int surveyId, int subSurveyId) {

        String subSurveyIdName = "user_test_progress_" + surveyId + "_" + subSurveyId;
        SharedPreferences userProgress = getSharedPreferences("usrSurveyPrgs", MODE_PRIVATE);
        int currentSurveyProgress = userProgress.getInt(subSurveyIdName, 1);

        if (currentSurveyProgress > 100) {
            return 100;
        }
        return currentSurveyProgress;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void startSurveysActivity() {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putExtra("selectedSurveyId", "" + selectedSurveyId);
        intent.putExtra("selectedSurveyName", "" + selectedSurveyName);
        intent.putExtra("selectedSubSurveyId", "" + selectedSubSurveyId);
        intent.putExtra("selectedSubSurveyName", "" + selectedSubSurveyName);
        startActivity(intent);
    }


}
