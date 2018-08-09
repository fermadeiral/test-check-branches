import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class PatchStatsTest {

    private File tmpDir;

    @Before
    public void setUp() throws IOException {
        tmpDir = Files.createTempDirectory("workspace").toFile();
    }

    @After
    public void tearDown() throws IOException {
        TestUtils.deleteFile(tmpDir);
    }

    @Test
    public void testPatchStatsInit() {
        PatchStats patchStats =  new PatchStats();
        assertEquals(0, patchStats.getChangedFiles().size());
        assertEquals(0, patchStats.getAddedFiles().size());
        assertEquals(0, patchStats.getDeletedFiles().size());
        assertEquals(0, patchStats.getNbAddedLines());
        assertEquals(0, patchStats.getNbDeletedLines());
    }

    @Test
    public void testComputeFiles() {
        String repoUrl = "https://github.com/fermadeiral/bears-usage.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, false);

        String commit1 = "6565b62263c1a8209933587aa68dff5307abf32e";
        String commit2 = "e90c26bbfbdbdc9039090f4cd5108fc17273bf5d";

        PatchStats patchStats =  new PatchStats();
        patchStats.computeFiles(commit1, commit2, new File(gitDirPath.toString()));

        assertEquals(5, patchStats.getChangedFiles().size());
        assertEquals(1, patchStats.getAddedFiles().size());
        assertEquals(1, patchStats.getDeletedFiles().size());
    }

    @Test
    public void testComputeFilesWithCommitThatRenameFile() {
        String repoUrl = "https://github.com/Spirals-Team/repairnator.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, true);

        String commit1 = "811ce1d03c9f9e63a421231ea7d8e4a9a7f82ece";
        String commit2 = "82c199366f552e3e102e4e702ddc70ac767334dd";

        PatchStats patchStats =  new PatchStats();
        patchStats.computeFiles(commit1, commit2, new File(gitDirPath.toString()));

        assertEquals(10, patchStats.getChangedFiles().size());
        assertEquals(0, patchStats.getAddedFiles().size());
        assertEquals(0, patchStats.getDeletedFiles().size());
    }

    @Test
    public void testComputeLines() {
        String repoUrl = "https://github.com/fermadeiral/bears-usage.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, false);

        String commit1 = "6565b62263c1a8209933587aa68dff5307abf32e";
        String commit2 = "e90c26bbfbdbdc9039090f4cd5108fc17273bf5d";

        PatchStats patchStats =  new PatchStats();
        patchStats.computeLines(commit1, commit2, new File(gitDirPath.toString()));

        assertEquals(84, patchStats.getNbAddedLines());
        assertEquals(71, patchStats.getNbDeletedLines());
    }

}
