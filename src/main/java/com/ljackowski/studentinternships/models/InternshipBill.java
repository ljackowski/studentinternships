package com.ljackowski.studentinternships.models;

public class InternshipBill {
    private String mothersName;
    private String fathersName;
    private String birthLocation;
    private String birthDate;
    private String PESEL;
    private String bankAccountNumber;
    private String taxOffice;
    private Long internId;
    private int hoursSum;

    public InternshipBill() {
    }

    public InternshipBill(String mothersName, String fathersName, String birthLocation, String birthDate, String PESEL, String bankAccountNumber, String taxOffice, Long internId, int hoursSum) {
        this.mothersName = mothersName;
        this.fathersName = fathersName;
        this.birthLocation = birthLocation;
        this.birthDate = birthDate;
        this.PESEL = PESEL;
        this.bankAccountNumber = bankAccountNumber;
        this.taxOffice = taxOffice;
        this.internId = internId;
        this.hoursSum = hoursSum;
    }

    public int getHoursSum() {
        return hoursSum;
    }

    public void setHoursSum(int hoursSum) {
        this.hoursSum = hoursSum;
    }

    public Long getInternId() {
        return internId;
    }

    public void setInternId(Long internId) {
        this.internId = internId;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getBirthLocation() {
        return birthLocation;
    }

    public void setBirthLocation(String birthLocation) {
        this.birthLocation = birthLocation;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }
}
