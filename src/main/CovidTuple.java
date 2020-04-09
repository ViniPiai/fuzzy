package main;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;

/**
 * Data info to calculate fuzzy to death chance.
 *
 * @author andre | vinicius
 */
public class CovidTuple {

    public static final int ID = 0;
    public static final int LOOCATION = 1;
    public static final int COUNTRY = 2;
    public static final int GENDER = 3;
    public static final int AGE = 4;
    public static final int SYMPTON_ONSET = 5;
    public static final int IF_ONSET_APPROXIMATED = 6;
    public static final int HOSP_VISIT_DATE = 7;
    public static final int EXPOSURE_START = 8;
    public static final int EXPOSURE_END = 9;
    public static final int VISITING_WUHAN = 10;
    public static final int FROM_WUHAN = 11;
    public static final int DEATH = 12;
    public static final int RECOVERED = 13;
    public static final int SYMPTOM = 14;

    private int id,
            age;

    private String location,
            country,
            sympton[];

    private char gender;

    private LocalDate symptonOnset,
            hospVisitDate,
            exposureStart,
            exposureEnd;

    private boolean isVisitingWuhan,
            isFromWuhan,
            isDeath,
            isRecovered;

    private double score;

    public CovidTuple(String[] tuple) {
        initAttribute(tuple);
    }

    /**
     * Iniate all fields in the object.
     *
     * @param tuple line of data of covid's table.
     */
    private void initAttribute(String[] tuple) {
        for (int i = 0; i < tuple.length; i++) {
            id = Integer.parseInt(tuple[ID]);
            age = Integer.parseInt(tuple[AGE]);
            location = tuple[LOOCATION];
            country = tuple[COUNTRY];
            sympton = tuple[SYMPTOM] == null ? null
                    : tuple[SYMPTOM].trim().replace("\"", "").split(",");
            gender = tuple[GENDER].charAt(0);
            symptonOnset = convertTo(tuple[SYMPTON_ONSET]);
            hospVisitDate = convertTo(tuple[HOSP_VISIT_DATE]);
            exposureStart = convertTo(tuple[EXPOSURE_START]);
            exposureEnd = convertTo(tuple[EXPOSURE_END]);
            isVisitingWuhan = tuple[VISITING_WUHAN].equals("1");
            isFromWuhan = tuple[FROM_WUHAN].equals("1");
            isDeath = tuple[DEATH].equals("1");
            isRecovered = tuple[RECOVERED].equals("1");
        }
    }

    private static final int DAY = 1;
    private static final int MONTH = 0;
    private static final int YEAR = 2;

    /**
     * Convert field date, split where separate month, day and year by "-" or
     * "/".
     *
     * @param att field where use date.
     * @return date in LocalDate.
     */
    private LocalDate convertTo(String att) {
        if (att.equals("NA")) {
            return null;
        }

        String[] date = att.split("-|/");
        int day = Integer.parseInt(date[DAY]);
        int month = Integer.parseInt(date[MONTH]);
        int year = Integer.parseInt(date[YEAR]);
        year = year < 100 ? year + 2000 : year;

        return LocalDate.of(year, month, day);
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender == 'm' ? 0 : 1;
    }

    public int getCountry() {
        return country.equals("Singapore") ? 0
                : country.equals("Italy") ? 1
                : country.equals("China") ? 2
                : 3;
    }

    public double getSympton() {
        if (sympton != null) {
            double sum = 1;
            sum += containSympton(sympton, "cough") ? 1.0 : 0.0;
            sum += containSympton(sympton, "fever") ? 1.0 : 0.0;
            sum += containSympton(sympton, "soar") ? 1.0 : 0.0;
            sum += containSympton(sympton, "breath") ? 6.0 : 0.0;
            sum += containSympton(sympton, "myalgia") ? 3.0 : 0.0;
            return sum;
        }
        return 0.0;
    }

    private boolean containSympton(String[] symptons, String sympton) {
        boolean result = false;
        for (String s : symptons) {
            if (s.contains(sympton)) {
                result = true;
            }
        }
        return result;
    }

    public long getSymptomToHospitalTime() {
        if (symptonOnset != null && hospVisitDate != null) {
            return hospVisitDate.getLong(ChronoField.EPOCH_DAY) - symptonOnset.getLong(ChronoField.EPOCH_DAY);
        } else if (symptonOnset != null && hospVisitDate == null) {
            return 5;
        } else if (symptonOnset == null && hospVisitDate != null) {
            return 2;
        }
        return 0;
    }

    public long getExposureTime() {
        if (exposureStart != null && exposureEnd != null) {
            return exposureEnd.getLong(ChronoField.EPOCH_DAY) - exposureStart.getLong(ChronoField.EPOCH_DAY);
        } else if (exposureStart != null && exposureEnd == null) {
            return 5;
        } else if (exposureStart == null && exposureEnd != null) {
            return 2;
        }
        return 0;
    }

    public int getWuhan() {
        return isVisitingWuhan ? 1
                : isFromWuhan ? 0
                        : 2;
    }

    public double getScore() {
        return score;
    }

    public CovidTuple withScore(double score) {
        this.score = score;
        return this;
    }

    private String criterioAge() {
        if (age >= 0 && age <= 12) {
            return "child";
        } else if (age >= 13 && age <= 18) {
            return "teenager";
        } else if (age >= 19 && age <= 29) {
            return "young";
        } else if (age >= 29 && age <= 59) {
            return "adult";
        }
        return "old";

    }

    private String criterioCountry() {
        if (getCountry() == 0) {
            return "singapore";
        } else if (getCountry() == 1) {
            return "italy";
        } else if (getCountry() == 2) {
            return "china";
        }
        return "other";

    }

    private String criterioSympton() {
        String temp = "";
        if (getSympton() >= 0 && getSympton() <= 3) {
            temp += "trivial, ";
        }  if (getSympton() >= 2 && getSympton() <= 6) {
            temp += "neutral, ";
        }  if (getSympton() >= 5 && getSympton() <= 15) {
            temp += "serious";
        }
        return temp;
    }

    private String criterioSymptonHosp() {
        String temp = "";
        if (getSymptomToHospitalTime() >= 0 && getSymptomToHospitalTime() <= 3) {
            temp += "short, ";
        }  if (getSymptomToHospitalTime() >= 2 && getSymptomToHospitalTime() <= 6) {
            temp += "medium, ";
        }  if (getSymptomToHospitalTime() >= 5 && getSymptomToHospitalTime() <= 15) {
            temp += "long";
        }
        return temp;
    }

    private String criterioExposureTime() {
        String temp = "";
        if (getExposureTime() >= 0 && getExposureTime() <= 3) {
            temp += "short, ";
        }  if (getExposureTime() >= 2 && getExposureTime() <= 6) {
            temp += "medium, ";
        }  if (getExposureTime() >= 5 && getExposureTime() <= 15) {
            temp += "long";
        }
        return temp;
    }

    private String criterioWuhan() {
        if (getWuhan() == 0) {
            return "from";
        } else if (getWuhan() == 1) {
            return "visiting";
        }
        return "not_visiting";

    }

    @Override
    public String toString() {
        return "id: " + id
                + "\nage: " + getAge() + " - " + criterioAge()
                + "\ngender: " + getGender() + " - " + gender
                + "\ncountry: " + getCountry() + " - " + criterioCountry()
                + "\nsympton: " + getSympton() + " - " + criterioSympton()
                + "\nsymptomHospTime: " + getSymptomToHospitalTime() + " - " + criterioSymptonHosp()
                + "\nexposureTime: " + getExposureTime() + " - " + criterioExposureTime()
                + "\nwuhan: " + getWuhan() + " - " + criterioWuhan()
                + "\nscore: " + score
                + "\n\n";
    }

}
