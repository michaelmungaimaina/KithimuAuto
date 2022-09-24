package mich.gwan.kithimuauto.model;

public class DbManagement {
    String databasename;
    int dbVersion;
    String dbN;

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getDatabasename() {
        return databasename;
    }

    public void setDatabasename(String databasename) {
        this.databasename = databasename;
    }

}
