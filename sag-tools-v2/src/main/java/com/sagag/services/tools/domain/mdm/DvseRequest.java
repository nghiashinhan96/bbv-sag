package com.sagag.services.tools.domain.mdm;

import javax.xml.bind.annotation.XmlElement;

/**
 * Class to provide base DVSE XML request.
 *
 */
public abstract class DvseRequest {
    private String timestamp;

    @XmlElement(name = "TimeStamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
