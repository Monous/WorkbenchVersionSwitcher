package mo.workbench.switcher.controller;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Moheem Ilyas on 12/31/2015.
 */
public class SwitcherHomeControllerTest extends TestCase {
    private static SwitcherHomeController switcherHomeController;
    private static ArrayList<File> files;
    @Before
    public void setUp() throws Exception {
        switcherHomeController = new SwitcherHomeController();
        files = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testHandleSwitchVersionsHelper() throws Exception {
       // File dir = new File("C:\\Niagara");
//        for (File file : dir.listFiles())
          //  files.add(file);

       // Path tempDir = switcherHomeController.handleSwitchVersionsHelper(files);
    }
}