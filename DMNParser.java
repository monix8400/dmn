package gruppe4;

import org.camunda.bpm.dmn.engine.*;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;

import java.io.File;

class DMNParser {

    public String getDecision() {

        // Load the DMN file
        DmnModelInstance dmnModel = Dmn.readModelFromFile(new File("D:\\Faculta\\master\\semestrul2\\modelierungsTools\\consumer-journey\\trying_stuff\\src\\main\\resources\\diagram.dmn"));

        // Create the DMN engine
        DmnEngineConfiguration dmnEngineConfiguration = new DefaultDmnEngineConfiguration();
        DmnEngine dmnEngine = dmnEngineConfiguration.buildEngine();

        // Retrieve the decision from the DMN model
        DmnDecision decisionPT = dmnEngine.parseDecision("produkt_typ", dmnModel);
        DmnDecision decisionH = dmnEngine.parseDecision("honig", dmnModel);
        DmnDecision decisionP = dmnEngine.parseDecision("produkt", dmnModel);
        System.out.println(decisionPT);
        System.out.println(decisionH);
        System.out.println(decisionP);



        DmnDecisionTableResult result1, result2, result = null;
        // Evaluate the decision with input data
        try {
            result1 = dmnEngine.evaluateDecisionTable(decisionPT, createVariablesPT());
            System.out.println(result1.getSingleResult().getEntry("produkt_typ").toString());

            result2 = dmnEngine.evaluateDecisionTable(decisionH, createVariablesH());
            System.out.println(result2.getSingleResult().getEntry("honig").toString());

            result = dmnEngine.evaluateDecisionTable(decisionP,
                    createVariablesP(result1.getSingleResult().getEntry("produkt_typ").toString(),
                            result2.getSingleResult().getEntry("honig")));
            // Process the decision table result as needed
        } catch (
                DmnEngineException e) {
            // Handle evaluation exception
        }
        return String.valueOf(result);
    }

    private VariableMap createVariablesPT() {
        return Variables
                .putValue("Verwendung", "SkinCare") //verwendung
                .putValue("Korperteil", "Mund"); //korperteil
    }

    private VariableMap createVariablesH() {
        return Variables
                .putValue("Honig_Typ", "BlutenHonig") //honigTyp
                .putValue("Quantitat", 400);  //quantitat
    }

    private VariableMap createVariablesP(String produktTyp,
                                         String honig) {
        return Variables
                .putValue("Produkt_Typ", produktTyp) //"Lippenstift"
                .putValue("Honig", honig);
    }
}