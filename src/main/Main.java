package main;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Main {

    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String COUNTRY = "country";
    public static final String SYMPTOM = "sympton";
    public static final String SYMPTOM_HOSP_TIME = "sympton_hosp_time";
    public static final String EXPOSURE_TIME = "expousure_time";
    public static final String WUHAN = "wuhan";
    public static final String SCORE = "score";

    public static final String REPOSITORY_PATH = "./repository/";
    public static final String FILE_PATH = REPOSITORY_PATH + "file/";
    public static final String FUNCTION_BLOCK = "covid";
    public static final String CSV_FILE = FILE_PATH + FUNCTION_BLOCK + ".csv";
    public static final String FCL_FILE = FILE_PATH + FUNCTION_BLOCK + ".fcl";

    public static double score = 0.0;

    public static void main(String[] args) {
        CsvFile csv = new CsvFile(CSV_FILE);
        CovidData data = new CovidData(csv.getData());
        FIS fis = FIS.load(FCL_FILE, true);

        for (CovidTuple tuple : data.getCovidTuples()) {
            tuple.withScore(getScore(tuple, fis));
        }


//        CovidTuple tuple = data.getTupleById(427);
//        getScore(tuple, fis);

        Variable tip = fis.getFunctionBlock(FUNCTION_BLOCK).getVariable(SCORE);
        JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
        
        data.print();
        Feature.execute(score / data.getCovidTuples().size());
    }

    public static double getScore(CovidTuple tuple, FIS fis) {
        fis.setVariable(GENDER, tuple.getGender());
        fis.setVariable(AGE, tuple.getAge());
        fis.setVariable(COUNTRY, tuple.getCountry());
        fis.setVariable(SYMPTOM, tuple.getSympton());
        fis.setVariable(SYMPTOM_HOSP_TIME, tuple.getSymptomToHospitalTime());
        fis.setVariable(EXPOSURE_TIME, tuple.getExposureTime());
        fis.setVariable(WUHAN, tuple.getWuhan());
        fis.evaluate();

        double aux = fis.getFunctionBlock(FUNCTION_BLOCK).getVariable(SCORE).defuzzify();
        score += aux;
        return aux;
    }
}
