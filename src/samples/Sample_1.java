package samples;



import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Sample_1 {

    public static void main(String[] args) {
        // Load from 'FCL' file
        String fileName = "./repository/file/tipper.fcl";

        FIS fis = FIS.load(fileName, true);

        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        // Show 
        JFuzzyChart.get().chart(fis.getFunctionBlock("tipper"));

        // Set inputs
        fis.setVariable("service", 5);
        fis.setVariable("food", 5);

        // Evaluate
        fis.evaluate();

        System.out.println(fis.getFunctionBlock("tipper").getVariable("tip").defuzzify());
        // Show output variable's chart
        Variable tip = fis.getFunctionBlock("tipper").getVariable("tip");
        System.out.println(tip.getLatestDefuzzifiedValue());
        JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

        // Print ruleSet
        System.out.println(fis);
    }
}
