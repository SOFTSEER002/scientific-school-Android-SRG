package com.jeannypr.scientificstudy.Registration.model;

import com.jeannypr.scientificstudy.Base.Constants;

public class TakeRegModel {
    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    private int SchoolId;
    private int AcademicYearId;
    private int MasterClassId;
    private String RegistrationFee;
    private String FirstName;
    private String LastName;
    private String Gender;
    private String RegistrationDate;
    private String FathersFirstName;
    private String FatherMobile;
    private String MothersFirstName;
    private int PaymentMode;
    private String RegistrationSource;
    private String PaymentNote;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    private int UserId;

    public int getAcademicYearId() {
        return AcademicYearId == -1 ? 0 : AcademicYearId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }

    public int getMasterClassId() {
        return MasterClassId == -1 ? 0 : MasterClassId;
    }

    public void setMasterClassId(int masterClassId) {
        MasterClassId = masterClassId;
    }

    public String getRegistrationFee() {
        return RegistrationFee == null || RegistrationFee.equals("") ? Constants.DEFAULT_REGISTRATION_FEE : RegistrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        RegistrationFee = registrationFee;
    }

    public String getFirstName() {
        return FirstName == null ? "" : FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName == null ? "" : LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGenderName() {
        return Gender == null ? "" : Gender;
    }

    public void setGenderName(String genderName) {
        Gender = genderName;
    }

    public String getRegistrationDate() {
        return RegistrationDate == null ? "" : RegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }

    public String getFathersFirstName() {
        return FathersFirstName == null ? "" : FathersFirstName;
    }

    public void setFathersFirstName(String fathersFirstName) {
        FathersFirstName = fathersFirstName;
    }

    public String getFatherMobile() {
        return FatherMobile == null ? "" : FatherMobile;
    }

    public void setFatherMobile(String fatherMobile) {
        FatherMobile = fatherMobile;
    }

    public String getMothersFirstName() {
        return MothersFirstName == null ? "" : MothersFirstName;
    }

    public void setMothersFirstName(String mothersFirstName) {
        MothersFirstName = mothersFirstName;
    }

    public int getPaymentMode() {
        return PaymentMode == -1 ? 0 : PaymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getRegistrationSource() {
        return RegistrationSource == null ? "" : RegistrationSource;
    }

    public void setRegistrationSource(String registrationSource) {
        RegistrationSource = registrationSource;
    }

    public String getPaymentNote() {
        return PaymentNote == null ? "" : PaymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        PaymentNote = paymentNote;
    }
}
