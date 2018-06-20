package ca.turbobutterfly.wachadoin.core.logfile;

import java.util.Date;

public interface ILogFileBuilder
{
    String BuildLogFile(Date startTime, Date endTime, boolean groupByDate, String logOrder, int roundTime);
}
