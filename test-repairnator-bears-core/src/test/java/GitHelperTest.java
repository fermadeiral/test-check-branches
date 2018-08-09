import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GitHelperTest {

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
    public void testGitClone() {
        String repoUrl = "https://github.com/fermadeiral/bears-usage.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, false);
        assertEquals(tmpDir.toPath() + "/bears-usage", gitDirPath.toString());
    }

    @Test
    public void testGitCloneWithProjectContainingSubmodule() {
        String repoUrl = "https://github.com/Spirals-Team/repairnator.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, true);
        assertEquals(tmpDir.toPath() + "/repairnator", gitDirPath.toString());

        File gitDir = new File(gitDirPath.toString());
        String commit = "2e291cb3308675762a29b82f8296d10cb4a9a9f2";
        GitHelper.gitCheckoutCommit(commit, gitDir);

        boolean wasSubmoduleFound = false;
        File[] files = gitDir.listFiles();
        for (File file: files) {
            if (file.isDirectory() && file.listFiles().length > 0 && file.getName().equals("bears-usage")) {
                wasSubmoduleFound = true;
            }
        }
        assertTrue(wasSubmoduleFound);
    }

    @Test
    public void testGitCheckoutCommit() {
        String repoUrl = "https://github.com/fermadeiral/bears-usage.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, false);
        File gitDir = new File(gitDirPath.toString());

        String commit = "64ac432f62f9b450ffb221fb8ff2caa8e81a6663";

        GitHelper.gitCheckoutCommit(commit, gitDir);

        StringBuilder gitRevParseHeadOutput = GitHelper.gitRevParseHead(gitDir);

        assertEquals(commit, gitRevParseHeadOutput.toString());
    }

    @Test
    public void testGitDiffNameStatus() {
        String repoUrl = "https://github.com/fermadeiral/bears-usage.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, false);

        String commit1 = "6565b62263c1a8209933587aa68dff5307abf32e";
        String commit2 = "e90c26bbfbdbdc9039090f4cd5108fc17273bf5d";

        StringBuilder gitOutput = GitHelper.gitDiffNameStatus(commit1, commit2, new File(gitDirPath.toString()));
        String[] lines = gitOutput.toString().split("\n");
        assertEquals(7, lines.length);
    }

    @Test
    public void testGitDiffNumStat() {
        String repoUrl = "https://github.com/fermadeiral/bears-usage.git";
        StringBuilder gitDirPath = GitHelper.gitClone(repoUrl, tmpDir, false);

        String commit1 = "6565b62263c1a8209933587aa68dff5307abf32e";
        String commit2 = "e90c26bbfbdbdc9039090f4cd5108fc17273bf5d";

        StringBuilder gitOutput = GitHelper.gitDiffNumStat(commit1, commit2, new File(gitDirPath.toString()));
        String[] lines = gitOutput.toString().split("\n");
        assertEquals(7, lines.length);
    }

}
