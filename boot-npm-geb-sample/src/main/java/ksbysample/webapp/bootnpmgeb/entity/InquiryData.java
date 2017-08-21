package ksbysample.webapp.bootnpmgeb.entity;

import java.sql.Clob;
import java.time.LocalDateTime;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * 
 */
@Entity
@Table(name = "INQUIRY_DATA")
public class InquiryData {

    /**  */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Integer id;

    /**  */
    @Column(name = "LASTNAME")
    String lastname;

    /**  */
    @Column(name = "FIRSTNAME")
    String firstname;

    /**  */
    @Column(name = "LASTKANA")
    String lastkana;

    /**  */
    @Column(name = "FIRSTKANA")
    String firstkana;

    /**  */
    @Column(name = "SEX")
    String sex;

    /**  */
    @Column(name = "AGE")
    Integer age;

    /**  */
    @Column(name = "JOB")
    String job;

    /**  */
    @Column(name = "ZIPCODE1")
    String zipcode1;

    /**  */
    @Column(name = "ZIPCODE2")
    String zipcode2;

    /**  */
    @Column(name = "ADDRESS")
    String address;

    /**  */
    @Column(name = "TEL1")
    String tel1;

    /**  */
    @Column(name = "TEL2")
    String tel2;

    /**  */
    @Column(name = "TEL3")
    String tel3;

    /**  */
    @Column(name = "EMAIL")
    String email;

    /**  */
    @Column(name = "TYPE1")
    String type1;

    /**  */
    @Column(name = "TYPE2")
    String type2;

    /**  */
    @Column(name = "INQUIRY")
    Clob inquiry;

    /**  */
    @Column(name = "SURVEY")
    String survey;

    /**  */
    @Column(name = "UPDATE_DATE")
    LocalDateTime updateDate;

    /** 
     * Returns the id.
     * 
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /** 
     * Sets the id.
     * 
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /** 
     * Returns the lastname.
     * 
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /** 
     * Sets the lastname.
     * 
     * @param lastname the lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /** 
     * Returns the firstname.
     * 
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /** 
     * Sets the firstname.
     * 
     * @param firstname the firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /** 
     * Returns the lastkana.
     * 
     * @return the lastkana
     */
    public String getLastkana() {
        return lastkana;
    }

    /** 
     * Sets the lastkana.
     * 
     * @param lastkana the lastkana
     */
    public void setLastkana(String lastkana) {
        this.lastkana = lastkana;
    }

    /** 
     * Returns the firstkana.
     * 
     * @return the firstkana
     */
    public String getFirstkana() {
        return firstkana;
    }

    /** 
     * Sets the firstkana.
     * 
     * @param firstkana the firstkana
     */
    public void setFirstkana(String firstkana) {
        this.firstkana = firstkana;
    }

    /** 
     * Returns the sex.
     * 
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /** 
     * Sets the sex.
     * 
     * @param sex the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /** 
     * Returns the age.
     * 
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /** 
     * Sets the age.
     * 
     * @param age the age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /** 
     * Returns the job.
     * 
     * @return the job
     */
    public String getJob() {
        return job;
    }

    /** 
     * Sets the job.
     * 
     * @param job the job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /** 
     * Returns the zipcode1.
     * 
     * @return the zipcode1
     */
    public String getZipcode1() {
        return zipcode1;
    }

    /** 
     * Sets the zipcode1.
     * 
     * @param zipcode1 the zipcode1
     */
    public void setZipcode1(String zipcode1) {
        this.zipcode1 = zipcode1;
    }

    /** 
     * Returns the zipcode2.
     * 
     * @return the zipcode2
     */
    public String getZipcode2() {
        return zipcode2;
    }

    /** 
     * Sets the zipcode2.
     * 
     * @param zipcode2 the zipcode2
     */
    public void setZipcode2(String zipcode2) {
        this.zipcode2 = zipcode2;
    }

    /** 
     * Returns the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /** 
     * Sets the address.
     * 
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /** 
     * Returns the tel1.
     * 
     * @return the tel1
     */
    public String getTel1() {
        return tel1;
    }

    /** 
     * Sets the tel1.
     * 
     * @param tel1 the tel1
     */
    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    /** 
     * Returns the tel2.
     * 
     * @return the tel2
     */
    public String getTel2() {
        return tel2;
    }

    /** 
     * Sets the tel2.
     * 
     * @param tel2 the tel2
     */
    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    /** 
     * Returns the tel3.
     * 
     * @return the tel3
     */
    public String getTel3() {
        return tel3;
    }

    /** 
     * Sets the tel3.
     * 
     * @param tel3 the tel3
     */
    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    /** 
     * Returns the email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /** 
     * Sets the email.
     * 
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** 
     * Returns the type1.
     * 
     * @return the type1
     */
    public String getType1() {
        return type1;
    }

    /** 
     * Sets the type1.
     * 
     * @param type1 the type1
     */
    public void setType1(String type1) {
        this.type1 = type1;
    }

    /** 
     * Returns the type2.
     * 
     * @return the type2
     */
    public String getType2() {
        return type2;
    }

    /** 
     * Sets the type2.
     * 
     * @param type2 the type2
     */
    public void setType2(String type2) {
        this.type2 = type2;
    }

    /** 
     * Returns the inquiry.
     * 
     * @return the inquiry
     */
    public Clob getInquiry() {
        return inquiry;
    }

    /** 
     * Sets the inquiry.
     * 
     * @param inquiry the inquiry
     */
    public void setInquiry(Clob inquiry) {
        this.inquiry = inquiry;
    }

    /** 
     * Returns the survey.
     * 
     * @return the survey
     */
    public String getSurvey() {
        return survey;
    }

    /** 
     * Sets the survey.
     * 
     * @param survey the survey
     */
    public void setSurvey(String survey) {
        this.survey = survey;
    }

    /** 
     * Returns the updateDate.
     * 
     * @return the updateDate
     */
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** 
     * Sets the updateDate.
     * 
     * @param updateDate the updateDate
     */
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}