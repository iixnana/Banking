package io.bankbridge.TestUtil.SparkManager;

import io.bankbridge.TestUtil.Mock.MockRemotes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This code is used to simplify starting and stopping of Spark server: it keeps track of the status and helps
 * to avoid starting another server in another test class before the previous server was stopped because
 * Spark functions are async.
 * Code from a tutorial: https://www.eviltester.com/2018/04/overview-of-spark-and-testing.html
 * Code repository: https://github.com/eviltester/TestingApp
 */
public class MockRemotesSparkManager extends SparkManager {
    private static MockRemotesSparkManager starter;
    private final String host;
    private final String heartBeatPath;

    private MockRemotesSparkManager(String host, String heartBeatPath) {
        this.host = host;
        this.heartBeatPath = heartBeatPath;
    }

    public static MockRemotesSparkManager get(String host, String heartBeatPath) {
        if (MockRemotesSparkManager.starter == null) {
            MockRemotesSparkManager.starter = new MockRemotesSparkManager(host, heartBeatPath);
        }
        return MockRemotesSparkManager.starter;
    }

    public boolean isRunning() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http",
                    host,
                    port,
                    heartBeatPath
            ).openConnection();
            return con.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void startServer() {
        MockRemotes.main(null);
    }
}
