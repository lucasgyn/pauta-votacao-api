package br.com.sicredi.votacao.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@UtilityClass
public class LoggerUtils {

    private Log getLogger() {
        return LogFactory.getLog(getClassName());
    }

    private StackTraceElement getStackTraceElement() {
        return Thread.currentThread().getStackTrace()[2];
    }

    private String getClassName() {
        return getStackTraceElement().getClassName();
    }

    private String getMethodName() {
        return getStackTraceElement().getMethodName();
    }

    private int getLineNumber() {
        return getStackTraceElement().getLineNumber();
    }

    public void debug(final Object object) {
        getLogger().debug(getMethodName() + ':' + getLineNumber() + " ### " + object);
    }

    public static void error(final Object object) {
        getLogger().error(getMethodName() + ':' + getLineNumber() + " ### " + object);
    }
}
