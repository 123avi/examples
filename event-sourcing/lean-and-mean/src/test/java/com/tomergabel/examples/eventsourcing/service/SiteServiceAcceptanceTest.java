package com.tomergabel.examples.eventsourcing.service;

import com.tomergabel.examples.eventsourcing.persistence.MysqlEventStore;
import com.tomergabel.examples.eventsourcing.persistence.MysqlSnapshotStore;
import com.tomergabel.examples.eventsourcing.resources.SiteResourceSpec;
import com.tomergabel.examples.eventsourcing.server.SiteServiceApplication;
import com.tomergabel.examples.eventsourcing.server.SiteServiceConfiguration;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.Sources;
import com.wix.mysql.config.MysqldConfig;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import javax.ws.rs.client.WebTarget;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

public class SiteServiceAcceptanceTest extends SiteResourceSpec {

    static EmbeddedMysql embeddedMysql;

    @Rule
    public DropwizardAppRule<SiteServiceConfiguration> applicationRule =
            new DropwizardAppRule<>(SiteServiceApplication.class, resourceFilePath("test-config.yaml"));

    @BeforeClass
    public static void setup() {
        MysqldConfig config = aMysqldConfig(v5_7_latest)
                .withUser("test", "test")
                .withPort(3310)
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("events", Sources.fromString(MysqlEventStore.SCHEMA_DDL))
                .addSchema("snapshots", Sources.fromString(MysqlSnapshotStore.SCHEMA_DDL))
                .start();
    }

    @AfterClass
    public static void tearDown() {
        embeddedMysql.stop();
    }

    @Override
    protected WebTarget sites() {
        // TODO gotta be a cleaner way to do this, but client() returns relative URI?! --TG
        return applicationRule
                .client()
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)     // OMG I hate this
                .target("http://localhost:" + applicationRule.getLocalPort() + "/sites");
    }
}
