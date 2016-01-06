package mo.workbench.switcher.controller;

import mo.workbench.switcher.model.NiagaraModel;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Moheem Ilyas on 1/5/2016.
 */
public class NiagaraModelTest {
    private static File controlNiagaraDir;
    private static NiagaraModel testNiagaraModel;


    @BeforeClass
    public static void setUp() throws Exception {
        // The directory should be there (cause I put it there...jajajajjaja >:)
        // In the application, a new directory for the Niagara home can be set if it's
        // not in the default location, but here we just want to make sure the constructor is working.
        controlNiagaraDir = new File("C:\\Niagara");
        testNiagaraModel = new NiagaraModel(controlNiagaraDir);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Although the deleteTempDir() method is not explicitly tested with an @Test method, the use here will suffice
        testNiagaraModel.deleteTempDir();

        assertTrue(testNiagaraModel.getTempDir().exists() == false);
    }

    @Test
    public void testConstructor() throws Exception {
        assertTrue(testNiagaraModel.getTempDir().isDirectory());

        int knownNumOfFiles = 8;
        int numOfFilesDetected = 0;

        for (File f : testNiagaraModel.getTempDir().listFiles()){
            numOfFilesDetected += 1;
        }

        assertTrue(knownNumOfFiles == numOfFilesDetected);
    }

    @Test
    public void testGetVersionNumsAsStrings() throws Exception {
        ArrayList<String> versionNums = testNiagaraModel.getVersionNumsAsStrings();

        ArrayList<String> knownVersionNums = new ArrayList<>();
        knownVersionNums.addAll(Arrays.asList("3.5.406", "3.6.406", "3.7.106", "3.8.38"));

        assertTrue(versionNums.containsAll(knownVersionNums));
    }

    @Test
    public void testRunInstall() throws Exception {
        /*
        UNABLE TO TEST ON THIS COMPUTER (COMPLETE NIAGARA FILE STRUCTURE DOES NOT EXIST ON THIS MACHINE)
         */
    }

    @Test
    public void testLaunchConsole() throws Exception {
        // Just trust me. (The tempDir folder could be deleted by that time this point is reached).
    }

}