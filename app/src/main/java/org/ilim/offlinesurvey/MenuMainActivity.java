package org.ilim.offlinesurvey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ilim.offlinesurvey.models.AppData;
import org.ilim.offlinesurvey.models.MainSurvey;

import java.util.Iterator;
import java.util.Map;


public class MenuMainActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_menu_main);

        //Get screen width and hight
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;

        animationZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_left_in);
        animationZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_left_out);


        GridLayout menuContainerGrid = (GridLayout) findViewById(R.id.main_menu_container_grid);
        AppData allData = new AppData();
        Map menuData = allData.getUnits();
        Iterator entries = menuData.entrySet().iterator();

        while (entries.hasNext()) {
            final Map.Entry menu = (Map.Entry) entries.next();
            Object key = menu.getKey();
            final MainSurvey mainSrv = (MainSurvey) menu.getValue();

            mainSrv.mainSurveyProgress = getMainSurveyProgress(mainSrv);

            // Create LinearLayout
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            ViewGroup.LayoutParams layoutParamsText = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            //Create IMG button
            final ImageButton imgBtn = new ImageButton(this);

            int imageResourceId = getResources().getIdentifier((String) mainSrv.mainSurveyImageIcon, "drawable", getPackageName());

            if(imageResourceId > 0){
                imgBtn.setBackground(getResources().getDrawable(imageResourceId));
            }else{
                Log.e("Menu icon not found ", mainSrv.mainSurveyImageIcon+"");
            }

            BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(imageResourceId);
            int fheight = bd.getBitmap().getHeight();
            int fwidth = bd.getBitmap().getWidth();
            int xx = screenWidth / 3;
            int yy = xx * fheight / fwidth;

            //img button layout
            LinearLayout.LayoutParams layoutParamsImgBtn = new LinearLayout.LayoutParams(xx, yy);
            layoutParamsImgBtn.setMargins(5, 5, 5, 5);
            imgBtn.setLayoutParams(layoutParamsImgBtn);
            ll.addView(imgBtn);

            //Create progress bar
            LinearLayout.LayoutParams layoutParamsPrgBar = new LinearLayout.LayoutParams(xx, LayoutParams.WRAP_CONTENT);
            ProgressBar prgBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            prgBar.setProgress(mainSrv.mainSurveyProgress);
            prgBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_menu));
            prgBar.setLayoutParams(layoutParamsPrgBar);
            prgBar.setMinimumHeight(2);
            prgBar.setMinimumWidth(2);
            ll.addView(prgBar);

            // Create TextView
            TextView textView = new TextView(this);
            textView.setText(mainSrv.mainSurveyName);
            textView.setGravity(Gravity.TOP);
            textView.setGravity(Gravity.CENTER);
            textView.setMinLines(1);
            textView.setLayoutParams(layoutParamsText);
            textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
            textView.setTextColor(R.color.f_default_text_color);
            textView.setMinLines(1);
            ll.addView(textView);
            ll.setPadding(20, 20, 20, 20);
            imgBtn.startAnimation(animationZoomIn);
            // Set click listener for button
            imgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtn.startAnimation(animationZoomOut);
                    selectedSurveyId = (int) menu.getKey();
                    selectedSurveyName = mainSrv.mainSurveyName;
                    startSubMenuActivity();
                }
            });

            menuContainerGrid.addView(ll);

        }

    }

    public void startSubMenuActivity() {
        Intent intent = new Intent(this, MenuSubActivity.class);
        intent.putExtra("selectedSurveyId", "" + selectedSurveyId);
        intent.putExtra("selectedSurveyName", "" + selectedSurveyName);
        intent.putExtra("selectedSubSurveyId", "" + selectedSubSurveyId);
        intent.putExtra("selectedSubSurveyName", "" + selectedSubSurveyName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //Nothing bro
    }


    public int getMainSurveyProgress(MainSurvey mainSrv) {

        SharedPreferences userProgress = getSharedPreferences("usrSurveyPrgs", MODE_PRIVATE);
        int mainSurveyProgress = 0;

        if (mainSrv.mainSurveyType == 1) {
            int p1 = userProgress.getInt("user_test_progress_" + mainSrv.mainSurveyId + "_" + mainSrv.mainSurveyId + "1", 1);
            int p2 = userProgress.getInt("user_test_progress_" + mainSrv.mainSurveyId + "_" + mainSrv.mainSurveyId + "2", 1);
            int p3 = userProgress.getInt("user_test_progress_" + mainSrv.mainSurveyId + "_" + mainSrv.mainSurveyId + "3", 1);
            mainSurveyProgress = (p1 + p2 + p3) / 3;
        } else if (mainSrv.mainSurveyType == 2) {
            mainSurveyProgress = userProgress.getInt("user_test_progress_" + mainSrv.mainSurveyId + "_" + mainSrv.mainSurveyId + "1", 1);
        }

        if (mainSurveyProgress > 100) {
            return 100;
        }
        return mainSurveyProgress;
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