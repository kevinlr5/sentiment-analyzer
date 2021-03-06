package org.sentiment.analyzer.services.properties;

public interface AnalyzerProperties {

  String getName();

  String getVersion();

  String getCommitHash();

  int getPort();

  String getDbHostname();

  String getDbUser();

  String getDbPassword();

  String getDbSchema();

}
