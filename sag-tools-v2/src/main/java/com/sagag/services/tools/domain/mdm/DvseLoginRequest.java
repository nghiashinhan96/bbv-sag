package com.sagag.services.tools.domain.mdm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML create login request.
 *
 */
@XmlRootElement(name = "LoginCustomerUser_V2")
public class DvseLoginRequest extends DvseRequest {
    private String username;
    private String password;

    // These are constant.
    @XmlElement(name = "Guid")
    private String guid = "bf49c4de-7396-4a5f-8f36-5c114e4abafa";
    @XmlElement(name = "CatalogID")
    private String catalogId = "9997";
    @XmlElement(name = "ModuleMain")
    private String module = "5";
    @XmlElement(name = "Language")
    private String language = "1";
    @XmlElement(name = "ReUseSession")
    private String reuseSession = "false";
    @XmlElement(name = "GetCustomerUser")
    private String getCustomerUser = "false";

    @XmlElement(name = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
