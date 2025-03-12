package lk.ijse.dep13.interthreadcommunication2.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class MyCP {
    private final HashMap<Integer, Connection> MAIN_POOL = new HashMap<>();
    private final HashMap<Integer, Connection> CONSUMER_POOL = new HashMap<>();
    private final int poolSize;

    private int loadDefaultPoolSize() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        return Integer.parseInt(properties.getProperty("app.pool-size", "4"));
    }

    public MyCP() throws IOException {
        this.poolSize = loadDefaultPoolSize();
    }

    public MyCP(int poolSize) throws IOException {
        this.poolSize = poolSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    private void initializePool() throws SQLException, IOException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        String host = properties.getProperty("app.db.host");
        String port = properties.getProperty("app.db.port");
        String user = properties.getProperty("app.db.user");
        String password = properties.getProperty("app.db.password");
        String database = properties.getProperty("app.db.database-name");

        Class.forName("com.mysql.cj.jdbc.Driver");

        for (int i = 0; i < poolSize; i++) {
            Connection connection = null;
            connection = DriverManager.getConnection(
                    "jdbc:mysql://%s:%s/%s".formatted(host, port, database),
                    user, password
            );
            MAIN_POOL.put((i + 1) * 10, connection);
        }
    }
    public void releaseConnection(Integer id) {
        if (!CONSUMER_POOL.containsKey(id)) throw new RuntimeException("Invalid connection ID");
        Connection connection = CONSUMER_POOL.get(id);
        CONSUMER_POOL.remove(id);
        MAIN_POOL.put(id, connection);
        notify();
    }

    public void releaseAllConnections() {
        CONSUMER_POOL.forEach(MAIN_POOL::put);
        CONSUMER_POOL.clear();
        notifyAll();
    }

    public ConnectionWrapper getConnection() {
        while (MAIN_POOL.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Integer key = MAIN_POOL.keySet().stream().findFirst().get();
        Connection connection = MAIN_POOL.get(key);
        MAIN_POOL.remove(key);
        CONSUMER_POOL.put(key, connection);
        return new ConnectionWrapper(key, connection);
    }

    public record ConnectionWrapper(Integer id, Connection connection) {
    }
}
