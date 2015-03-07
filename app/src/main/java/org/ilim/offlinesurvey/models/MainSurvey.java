package org.ilim.offlinesurvey.models;

/**
 * Created by ilimturan on 27/02/15.
 */
public class MainSurvey {

    public int mainSurveyId;
    public String mainSurveyName;
    public String mainSurveyImageIcon;
    public int mainSurveyProgress = 1;
    public int mainSurveyType = 1;

    public MainSurvey(int mainSurveyId, String mainSurveyName, String mainSurveyImageIcon, int mainSurveyType) {
        this.mainSurveyId = mainSurveyId;
        this.mainSurveyName = mainSurveyName;
        this.mainSurveyImageIcon = mainSurveyImageIcon;
        this.mainSurveyType = mainSurveyType;
    }

    @Override
    public String toString() {
        return "MainSurvey{" +
                "mainSurveyId=" + mainSurveyId +
                ", mainSurveyName='" + mainSurveyName + '\'' +
                ", mainSurveyImageIcon='" + mainSurveyImageIcon + '\'' +
                ", mainSurveyProgress=" + mainSurveyProgress +
                ", mainSurveyType=" + mainSurveyType +
                '}';
    }
}
