package com.espertech.esper.ohlc;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

public class OHLCUpdateListener implements StatementAwareUpdateListener
{
    private static Log log = LogFactory.getLog(OHLCUpdateListener.class);

    public void update(EventBean[] newData, EventBean[] oldData, EPStatement epStatement, EPServiceProvider epServiceProvider)
    {
        for (EventBean element : newData) {
            if (log.isInfoEnabled())
            {
                log.info("Statement " + String.format("%s", epStatement.getName()) + " produced: " + getProperties(element));
            }
        }
    }

    private String getProperties(EventBean event)
    {
        StringBuilder buf = new StringBuilder();

        for (String name : event.getEventType().getPropertyNames())
        {
            Object value = event.get(name);
            buf.append(name);
            buf.append("=");

			if (name.contains("minuteValue"))
            {
                buf.append(new Date((Long) value));
            }
            else
            {
                buf.append(value);
            }
            buf.append(" ");
        }
        return buf.toString();
    }
}
