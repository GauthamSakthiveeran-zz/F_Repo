package com.ooyala.playback.updateSpreadSheet;

import org.testng.*;
import org.testng.internal.TestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jitendra on 4/1/17.
 */
public class TestListener implements ITestListener, IReporter {

    public static LinkedHashMap<String,String> Testdata  =
            new LinkedHashMap<String,String>();
    public String testfailed;
    public String testPassed;

    @Override
    public void generateReport(List<XmlSuite> xmlSuite, List<ISuite> iSuites, String s){
        for(ISuite suits : iSuites)  {
            Map<String ,ISuiteResult> result =  suits.getResults();
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                Set<ITestResult> testResult=  context.getFailedTests().getAllResults();
                Set<ITestResult> passTestResult= context.getPassedTests().getAllResults();
                for (ITestResult res : testResult){
                    if(testfailed==null)
                        testfailed= " ";
                    int a = res.getMethod().getTestClass().getName().split("[.]").length;
                    testfailed += " " + res.getMethod().getTestClass().getName().split("[.]")[a-1] + "\n";
                }


                for (ITestResult res : passTestResult){
                    if(testPassed==null)
                        testPassed= " ";
                    int a = res.getMethod().getTestClass().getName().split("[.]").length;
                    testPassed += " " + res.getMethod().getTestClass().getName().split("[.]")[a-1] + "\n";
                }


                setTestResult(Integer.toString(context.getPassedTests().size()), Integer.toString(context.getFailedTests().size()), Integer.toString(context.getSkippedTests().size()), Integer.toString(context.getAllTestMethods().length), context.getName(),testfailed,testPassed);
            }
        }

    }


    public void setTestResult(String pass, String fail, String skip, String total, String suiteName,String failtestname,String passedTests){
        Date date = new Date();
        int totaltest = Integer.parseInt(pass) + Integer.parseInt(fail) + Integer.parseInt(skip) ;
        String CurrntDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Testdata.put("Date",CurrntDate);
        Testdata.put("SuiteName",System.getProperty("group"));
        Testdata.put("Platform",System.getProperty("platform"));
        Testdata.put("Browser",System.getProperty("browser"));
        Testdata.put("Browser_Version",System.getProperty("version"));
        Testdata.put("Total",Integer.toString(totaltest));
        Testdata.put("Pass",pass);
        Testdata.put("Fail",fail);
        Testdata.put("Skip",skip);
        Testdata.put("Failed_Methode_Name",failtestname);
        Testdata.put("Passed_Tests",passedTests);
        System.out.println("size of map : " + Testdata.size());
        for (String key : Testdata.keySet()){
            String value = Testdata.get(key);
            System.out.println(key + " " + value);
        }

        UpdateSheet.writetosheet(Testdata);

    }


    public void onFinish(ITestContext context) {
        Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
        for (ITestResult temp : failedTests) {
            ITestNGMethod method = temp.getMethod();
            if (context.getFailedTests().getResults(method).size() > 1) {
                failedTests.remove(temp);
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    failedTests.remove(temp);
                }
            }
        }
    }

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
    }

    public void onTestFailure(ITestResult result) {

        /*System.out.println(result.getTestName());

        System.out.println(result.getMethod() + "\n");

        System.out.println("\n in on failure \n");
        XmlSuite suite = new XmlSuite();
        suite.setName("rerunFailedTestClasses");
        XmlTest test = new XmlTest(suite);
        test.setName(result.getTestName());
        List<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(result.getTestClass().getXmlClass());
        test.setXmlClasses(classes) ;
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.run();*/
    }

    public void onTestSkipped(ITestResult result) {
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {   }

    public void onStart(ITestContext context) {
    }
}
