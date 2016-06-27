package bit.beelin.browser.util.logging;

import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * Default runtime configuration for JDK logging
 * Use -Djava.util.logging.config.class=bit.beelin.browser.util.logging.DefaultLoggingConfig
 * on the command line to select this configuration
 */
public class DefaultLoggingConfig {
    public DefaultLoggingConfig() {
        try {
            final LogManager logManager = LogManager.getLogManager();
            try (final InputStream is = getClass().getResourceAsStream("/logging.properties")) {
                logManager.readConfiguration(is);
            }
        } catch (Exception e) {
            // The runtime won't show stack traces if the exception is thrown
            e.printStackTrace();
        }
    }
}
