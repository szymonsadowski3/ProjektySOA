package org.jboss.as.quickstarts.jms;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.jms.JMSException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;

@ManagedBean
@ApplicationScoped
public class CompaniesManager implements Serializable {
    private QueueManager mngr = new QueueManager();

    private String name;
    private String address;
    private String phoneNum;

    public CompaniesManager() throws JMSException {
    }

    public ArrayList<String> getCompanies() throws JMSException {
        return mngr.fetchMessagesFromQueue();
    }

    public void addCompany(String name, String address, String phoneNum) {
        mngr.putMessageInQueue(MessageFormat.format("Name: {0} | Address: {1} | Phone Number: {2}", name, address, phoneNum));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}