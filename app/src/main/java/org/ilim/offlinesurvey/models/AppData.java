package org.ilim.offlinesurvey.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ilimturan on 07/03/15.
 */
public class AppData {


    public AppData() {

        createUnits();

        /**
         * CREATE DUMP DATA AND TROTS
         */

        trots.put(11, new SubSurvey(11, "Sub Survey Title 1", "sb4", /* questiom img prefix */ "agustos_y", new Test(new String[]{"D", "B", "C", "B", "D", "A", "C", "D", "C", "A", "B", "D", "C", "B", "A",})));

        trots.put(21, new SubSurvey(21, "Sub Survey Title 2", "sb1", "agustos_i",  new Test(new String[]{"D", "B", "D", "A", "B", "A", "B", "C", "A", "C", "D", "C", "A", "D", "B", "A", "C", "D", "A", "D", "C", "B", "A", "D", "D", "A", "A", "C", "D", "B", "A", "B", "C", "C", "B", "A", "A", "C", "B", "C", "D", "C", "D", "A", "D", "B", "C", "B", "D", "B",})));

        trots.put(22, new SubSurvey(22, "Sub Survey Title 3", "sb2", "agustos_m", new Test(new String[]{"D", "C", "A", "B", "C", "B", "A", "B", "D", "D", "A", "C", "A", "D", "C",})));

        trots.put(23, new SubSurvey(23, "Sub Survey Title 4", "sb3", "agustos_t",  new Test(new String[]{"A", "B", "D", "C", "B", "A", "D", "A", "D", "C", "D", "B", "C", "A", "C",})));

        /**
         * END OF DUMB DATA
         */
    }

    Map units = new HashMap<Integer, MainSurvey>();
    Map trots = new HashMap<Integer, SubSurvey>();

    private void createUnits() {

        for (int i = 1; i <= AppSetting.surveyCount; i++) {

            int sType = 1;
            if ((i % 2 == 0)) {
                sType = 2;
            } else {
                sType = 1;
            }

            units.put(i, new MainSurvey(i, AppSetting.mainMenuNamePrefix + " " + i, AppSetting.mainMenuImagePrefix + i, sType));


        }
    }



    public Map getUnits() {
        return units;
    }

    public SubSurvey getSurveyAllData(Integer selectedSubSurveyId) {
        //for survey
        if (trots.containsKey(selectedSubSurveyId)) {
            return (SubSurvey) trots.get(selectedSubSurveyId);
        } else {
            new Exception("Menu error");
        }
        return null;
    }

    public Map getSubSurveyMenu(Integer selectedSurveyId) {
        //for Sub Menu
        Map selectedTopics = new HashMap();
        Iterator entries = trots.entrySet().iterator();

        Integer kk = 0;

        while (entries.hasNext()) {
            final Map.Entry tp = (Map.Entry) entries.next();

            if (selectedSurveyId > 9) {
                kk = Integer.valueOf(tp.getKey().toString().substring(0, 2));
                if (tp.getKey().toString().length() > 2 && kk == selectedSurveyId) {
                    selectedTopics.put(tp.getKey(), (SubSurvey) tp.getValue());
                }
            } else {
                kk = Integer.valueOf(tp.getKey().toString().substring(0, 1));
                if (tp.getKey().toString().length() < 3 && kk == selectedSurveyId) {
                    selectedTopics.put(tp.getKey(), (SubSurvey) tp.getValue());
                }
            }

            kk = 0; //bug fix
        }

        return selectedTopics;
    }


}
