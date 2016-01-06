package mo.workbench.switcher.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Created by Moheem Ilyas on 12/31/2015.
 *
 * Last Modified: 1/2/2016 (Moheem Ilyas)
 *
 * Description: In order to switch Niagara versions, there are three essential components that need to be done: stop
 *              the current service, install Niagara, start the service for the installed version. This class accomplishes
 *              these goals through writing batch files through a temporary directory. Additionally, if the user prefers
 *              the console, this class holds the logic to launch the console.
 *
 * Note: Originally, the logic found in this class was in SwitcherHomeController, but the line between presentation
 *       and logic became blurry. Even though essentially what is being done is file manipulation, it is easier to test if
 *       the logic has a clear line of separation.
 */

public class NiagaraModel {

    private File niagaraHome;
    private File tempDir;
    private ArrayList<String> versionNumsAsStrings = new ArrayList<>();

    /**
     * Upon instantiation, the temporary folder that will hold the batch files is created. Specifically, the folder will
     * contain the install batch files and the console execution files. The methodology is... find all of the Niagara
     * version folders in the Niagara home...create the temporary folder...traverse through the version folders and
     * get the absolute paths for the console and plat executables...write the batch files that will allow installation
     * of the desired Niagara version by using the absolute path (and if the user chooses, launch the console).
     * @param niagaraHome
     */
    public NiagaraModel(File niagaraHome){

        this.niagaraHome = niagaraHome;

        ArrayList<File> versionFolders = new ArrayList<>();

        for (File f : niagaraHome.listFiles()) {
            if (f.isDirectory() && f.getName().contains("Niagara")) versionFolders.add(f);
        }

        //tempDir = null;
        try {
            // Could THROW the IOEXCEPTION
            tempDir = Files.createTempDirectory(Paths.get(this.niagaraHome.getAbsolutePath()), "switcherTempFolder").toFile();


            // Traversing the Niagara version folder
            for (File traversingNiagaraVersionFolder : versionFolders) {
                String versionNumberAsString = new String();
                char[] folderNameCharacters = traversingNiagaraVersionFolder.getName().toCharArray();

                // Getting the full version number. Assuming the the numbers that compose the version number
                // are the only numbers in the folder name
                for (int i = 0; i < folderNameCharacters.length; i++) {
                    if (Character.isDigit(folderNameCharacters[i])) {
                        versionNumberAsString = traversingNiagaraVersionFolder.getName().substring(i);
                        versionNumsAsStrings.add(versionNumberAsString);
                        break;
                    }
                }

                // Traversing the the bin folder to get the absolute path of the plat file to write to the batch file
                for (File traversingBinFolder : traversingNiagaraVersionFolder.listFiles()) {
                    if (traversingBinFolder.getName().equals("bin")) {
                        File[] necessaryFiles = traversingBinFolder.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                if (name.equals("console.exe") || name.equals("plat.exe")) return true;
                                return false;
                            }
                        });

                        for (File f : necessaryFiles) {
                            if (f.getName().equals("console.exe")) {
                                File consoleExecutionBatch = new File(tempDir.getAbsolutePath() + "\\" + versionNumberAsString + "console.bat");

                                // Actually places the file in the tempDir.
                                // Since both creating the file and the PrintWriter constructor throw exceptions...
                                // Could THROW the FILENOTFOUND EXCEPTION or UNSUPPORTEDENCODDING EXCEPTION
                                consoleExecutionBatch.createNewFile();
                                PrintWriter printWriter = new PrintWriter(consoleExecutionBatch, "UTF-8");
                                // Try to figure out how to issue "wb" command here...
                                // ... ideally, would want to launch the console with the execution of the wb command
                                printWriter.println(f.getAbsolutePath());
                                printWriter.println("wb");
                                printWriter.close();

                            } else if (f.getName().equals("plat.exe")) {
                                File installBatch = new File(tempDir.getAbsolutePath() + "\\" + versionNumberAsString + "install.bat");
                                // Could THROW the FILENOTFOUND EXCEPTION or UNSUPPORTEDENCODDING EXCEPTION
                                installBatch.createNewFile();
                                PrintWriter printWriter = new PrintWriter(installBatch, "UTF-8");
                                printWriter.println("sc stop Niagara");
                                printWriter.println(f.getAbsolutePath() + " installdaemon");
                                printWriter.println("sc config Niagara start= demand");
                                printWriter.println("exit");
                                printWriter.close();

                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getTempDir(){
        return this.tempDir;
    }

    public ArrayList<String> getVersionNumsAsStrings(){
        return this.versionNumsAsStrings;
    }

    /**
     * Executes the batch file for installing the version that the user wants
     * @param versionNum
     */
    public void runInstall(String versionNum){
        for (File versionFile : this.tempDir.listFiles()) {
            if (versionFile.getName().equals(versionNum + "install.bat")) {
                try {
                    Runtime.getRuntime().exec("cmd /c start "  + tempDir.getAbsolutePath() + "\\" + versionFile.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Executes the batch file for running the console program for the version the user wants.
     * @param versionNum
     */
    public void launchConsole(String versionNum){
        for (File versionFile : this.tempDir.listFiles()) {
            if (versionFile.getName().equals(versionNum + "console.bat")) {
                try {
                    //System.out.println(tempDir.toString());
                    Runtime.getRuntime().exec("cmd /c start " + tempDir.getAbsolutePath() + "\\" + versionFile.getName());
                } catch (IOException e) {
                   e.printStackTrace();
                }

            }
        }
    }

    /**
     * This method is used to clear the temporary folder so it can actually be deleted. Using the File method, delete() or
     * deleteOnExit was not working. The cause for the issue is believed to be because the file was not empty.
     */
    public void deleteTempDir(){
        for (File f : this.tempDir.listFiles()){
            f.delete();
        }
        try {
            Files.delete(Paths.get(this.tempDir.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
