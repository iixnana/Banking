package io.bankbridge.TestUtil.SparkManager;

import io.bankbridge.Main;

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
public class BankbridgeSparkManager extends SparkManager {
    private static BankbridgeSparkManager starter;
    private final String host;
    private final String heartBeatPath;

    private BankbridgeSparkManager(String host, String heartBeatPath) {
        this.host = host;
        this.heartBeatPath = heartBeatPath;
    }

    public static BankbridgeSparkManager get(String host, String heartBeatPath) {
        if (BankbridgeSparkManager.starter == null) {
            BankbridgeSparkManager.starter = new BankbridgeSparkManager(host, heartBeatPath);
        }
        return BankbridgeSparkManager.starter;
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
        Main.main(null);
    }
}
