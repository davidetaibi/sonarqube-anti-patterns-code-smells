/*
* Sonarqube anti-pattern and code smell detection plugin
* Copyright (C) 2017  Giuseppe Aina, Alexandru Stan, Davide Taibi, Gustavs Venters
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.locator.FileLocation;
import org.junit.*;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueClient;
import org.sonar.wsclient.issue.IssueQuery;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class AntiPatternRulesIT {

    @ClassRule
    public static Orchestrator orchestrator = Orchestrator.builderEnv()
            .setOrchestratorProperty("javaVersion", "4.2.1")
            .setSonarVersion("6.3")
            .addPlugin("java")
            .addPlugin(FileLocation.byWildcardMavenFilename(new File("target"), "sonar-codesmellsantipatterns-plugin-*.jar"))
            .restoreProfileAtStartup(FileLocation.of(new File("src/test/antipattern-rules-profile.xml")))
            .build();

    private static IssueClient issueClient;
    private static final String PROJECT_KEY = "antipattern-rules";

    @BeforeClass
    public static void init() {
        orchestrator.resetData();
        orchestrator.getServer().provisionProject(PROJECT_KEY, PROJECT_KEY);

        SonarScanner build = SonarScanner.create();
        build
                .setProperty("javaVersion", "4.2.1")
                .setProjectVersion("1.0")
                .setSourceEncoding("UTF-8")
                .setSourceDirs(".")
                .setLanguage("java")
                .setProfile("custom-rules-profile")
                .setProjectDir(new File("src/test/projects/antipatterns"))
                .setProjectKey(PROJECT_KEY)
                .setProjectName(PROJECT_KEY);
        orchestrator.executeBuild(build);

        issueClient = orchestrator.getServer().wsClient().issueClient();
    }

    @Test
    public void classDataPrivate() {
        final List<Issue> issues = issueClient.find(IssueQuery.create().rules("code_smells:class_data_private")).list();
        Assert.assertEquals(1, issues.size());
    }

    @Test
    public void antiSingleton() {
        final List<Issue> issues  = issueClient.find(IssueQuery.create().rules("code_smells:antisingleton")).list();
        Assert.assertEquals(1, issues.size());
    }

    @AfterClass
    public static void tearDown() {
        if (orchestrator != null) {
            orchestrator.stop();
        }
    }

}