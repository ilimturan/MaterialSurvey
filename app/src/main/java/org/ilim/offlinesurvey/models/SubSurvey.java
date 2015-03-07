package org.ilim.offlinesurvey.models;

/**
 * Created by ilimturan on 27/02/15.
 */
public class SubSurvey {

    public int subSurveyId;
    public String subSurveyName;
    public String subSurveyImageIcon;
    public int subSurveyProgress = 1;
    public String questionImagePrfx;
    public Test testler;

    public SubSurvey(int subSurveyId, String subSurveyName, String subSurveyImageIcon, String questionImagePrfx, Test testler) {
        this.subSurveyId = subSurveyId;
        this.subSurveyName = subSurveyName;
        this.subSurveyImageIcon = AppSetting.subMenuNamePrefix + subSurveyImageIcon;
        this.questionImagePrfx = AppSetting.surveyImagePrefix + questionImagePrfx+ "_";
        this.testler = testler;
    }

    @Override
    public String toString() {
        return "SubSurvey{" +
                "subSurveyId=" + subSurveyId +
                ", subSurveyName='" + subSurveyName + '\'' +
                ", subSurveyImageIcon='" + subSurveyImageIcon + '\'' +
                ", subSurveyProgress=" + subSurveyProgress +
                ", questionImagePrfx='" + questionImagePrfx + '\'' +
                ", testler=" + testler +
                '}';
    }
}