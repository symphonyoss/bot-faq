/*
 *
 *
 * Copyright 2016 Symphony Communication Services, LLC
 *
 * Licensed to Symphony Communication Services, LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.ppollack.symphony.faqbot.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Component
public class PropertiesFileConfigurationProvider implements IConfigurationProvider {

  private static final Logger LOG = LoggerFactory.getLogger(PropertiesFileConfigurationProvider.class);

  private static final String PROP_FILE = "/faqbot.properties";
  private static final String HELP_TEXT_FILE = "/help.txt";

  private Properties properties = new Properties();
  private String helpText;

  public PropertiesFileConfigurationProvider() {
    try {
      properties.load(getClass().getResourceAsStream(PROP_FILE));
    } catch (IOException e) {
      throw new RuntimeException("failed to load configuration from properties file: " + PROP_FILE, e);
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
        HELP_TEXT_FILE)))) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
      helpText = sb.toString();
    } catch (IOException e) {
      throw new RuntimeException("failed to load help text from file: " + HELP_TEXT_FILE, e);
    }
  }

  @Override
  public int getNumWorkerThreads() {
    return Integer.parseInt(properties.getProperty("numWorkerThreads"));
  }

  @Override
  public long getRequestProcessingTimeout() {
    return Long.parseLong(properties.getProperty("requestProcessingTimeout"));
  }

  @Override
  public long getBotUserId() {
    return Long.parseLong(properties.getProperty("myUserId"));
  }

  @Override
  public File getCertificateFile() {
    String classpathResource = properties.getProperty("certificateResource");
    LOG.info("attempting to load certificate file as classpath resource at " + classpathResource);
    File certificate = new File(getClass().getResource(classpathResource).getFile());
    if(!certificate.exists()) {
      throw new RuntimeException("no certificate found at " + certificate.getAbsolutePath());
    }
    return certificate;
  }

  @Override
  public String getSymphonyKeystorePassword() {
    return properties.getProperty("keystorePassword");
  }

  @Override
  public String getSymphonyKeystoreType() {
    return properties.getProperty("keystoreType");
  }

  @Override
  public String getSymphonyWebControllerUrl() {
    return properties.getProperty("symphonyWebControllerUrl");
  }

  @Override
  public String getSymphonyBaseUrl() {
    return properties.getProperty("symphonyBaseUrl");
  }

  @Override
  public String getSymphonyUserInfoPath() {
    return getSymphonyWebControllerUrl() + properties.getProperty("pathUserInfo");
  }

  @Override
  public String getSymphonyPodPath() {
    return getSymphonyBaseUrl() + properties.getProperty("pathPod");
  }

  @Override
  public String getSymphonyAgentPath() {
    return getSymphonyBaseUrl() + properties.getProperty("pathAgent");
  }

  @Override
  public String getSymphonySbePath() {
    return getSymphonyBaseUrl() + properties.getProperty("pathSessionAuth");
  }

  @Override
  public String getSymphonyKeyManagerPath() {
    return getSymphonyBaseUrl() + properties.getProperty("pathKeyAuth");
  }

  @Override
  public String getHelpText() {
    return helpText;
  }

  @Override
  public String getJiraConsumerPrivateKey() {
    return "-----BEGIN RSA PRIVATE KEY-----\n"
        + "MIICXAIBAAKBgQDWUx733WLaKEMPC/dzdlho0EaeFjY8WfV9ug8PkRQkT/414mTt\n"
        + "N+I7yq8vVBRlI1/1KH9g9aeCO1Drb0O3Euglexctsj+gkv88F6Og29JGRdMA6Wlt\n"
        + "urErK3IvC0g8F/5/Y5b7bKNXoEBKyxvBVEZPnY2yLxCU4fQLl+FhPg1SjQIDAQAB\n"
        + "AoGAJws/cgIntvxssvoIG0Ws93Mx2izLtpTgzwWtJrXUSIU2F1Tl8/0hPqk+3s1f\n"
        + "Zcla+stk4SH/YQ8zP6CmYlyUY6IIRhtfXtHG9kCLsdsWCJEQZ7+f5CcwmUBuoNsS\n"
        + "iN6X1yQd5eiHLC8fuF0xg7O1/BbnCJgaD6Sh2xp59DeelqECQQD7inXkHqDkn1m5\n"
        + "DaNCQMhQ+CQ+yoXBZS+p9lsVWvY7V4xwJRUUJhf8C9enX7LsFjYnrNcXMTexc44+\n"
        + "eiE42sdZAkEA2h/EOlaOvcDeBvlCkChTJ81xbshsW2aatuYs/uGhhnBR1Y2wQktx\n"
        + "uW9R5+RDTOYFPiWnVByF5MHECZf+vM/yVQJAKfhdWVW+9Mad2uGqpuhWRCRTL+Ls\n"
        + "1GsEu/AuHG8T/KzL8v5M+RKuF9EGB5hRK1E9cftF3EnLCCHGzyfjmS/v6QJBAIYu\n"
        + "nIw2y0C38N3hYK1F4UHPBETeTqo7iFmTZ4K0UqVdWzpAG3ns51znuj8pEK2xovAE\n"
        + "QddgZI3BFVPBiRel5LECQEU+lh5VeaKmB1CtBKXHLOPng9Uw6V9fRhqpJfy2Y+XQ\n"
        + "eMr7H2smcfKxDKZ6ghPdFMGi903yy9UvZOq0Sk0G5sg=\n"
        + "-----END RSA PRIVATE KEY-----";
  }

  @Override
  public String getJiraBaseURL() {
    return "https://perzoinc.atlassian.net";
  }

  @Override
  public String getJiraCallback() {
    return "https://placeholder";
  }

  @Override
  public String getJiraCallbackURI() {
    return "http://consumer/callback";
  }

  @Override
  public String getJiraConsumerKey() {
    return "sUper1337";
  }

  @Override
  public String getJiraAccessToken() {
    return "To3e4eNrc9mlvQR4cCR6lVBMG86U0caz";
  }

}