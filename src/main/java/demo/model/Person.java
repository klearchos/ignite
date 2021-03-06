package demo.model;

import java.io.Serializable;

/**
 * Person definition.
 * 
 * This file was generated by Ignite Web Console (‎11‎/‎14‎/‎2017‎ ‎16‎:‎50)
 **/
public class Person implements Serializable {
    /** */
    private static final long serialVersionUID = 0L;

    public Long id;

    /** Value for firstname. */
    private String firstname;

    /** Value for lastname. */
    private String lastname;

    /** Value for orgid. */
    private Long orgid;

    /** Value for resume. */
    private String resume;

    /** Value for salary. */
    private Double salary;

    /**
     * Gets firstname
     * 
     * @return Value for firstname.
     **/
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets firstname
     * 
     * @param firstname New value for firstname.
     **/
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gets lastname
     * 
     * @return Value for lastname.
     **/
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets lastname
     * 
     * @param lastname New value for lastname.
     **/
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gets orgid
     * 
     * @return Value for orgid.
     **/
    public Long getOrgid() {
        return orgid;
    }

    /**
     * Sets orgid
     * 
     * @param orgid New value for orgid.
     **/
    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }

    /**
     * Gets resume
     * 
     * @return Value for resume.
     **/
    public String getResume() {
        return resume;
    }

    /**
     * Sets resume
     * 
     * @param resume New value for resume.
     **/
    public void setResume(String resume) {
        this.resume = resume;
    }

    /**
     * Gets salary
     * 
     * @return Value for salary.
     **/
    public Double getSalary() {
        return salary;
    }

    /**
     * Sets salary
     * 
     * @param salary New value for salary.
     **/
    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person) o;

        if (id != null ? !id.equals(person.id) : person.id != null)
            return false;
        if (firstname != null ? !firstname.equals(person.firstname) : person.firstname != null)
            return false;
        if (lastname != null ? !lastname.equals(person.lastname) : person.lastname != null)
            return false;
        if (orgid != null ? !orgid.equals(person.orgid) : person.orgid != null)
            return false;
        if (resume != null ? !resume.equals(person.resume) : person.resume != null)
            return false;
        return salary != null ? salary.equals(person.salary) : person.salary == null;
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (orgid != null ? orgid.hashCode() : 0);
        result = 31 * result + (resume != null ? resume.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        return result;
    }

    /** {@inheritDoc} **/
    @Override public String toString() {
        return "Person [" + 
            "firstname=" + firstname + ", " + 
            "lastname=" + lastname + ", " + 
            "orgid=" + orgid + ", " + 
            "resume=" + resume + ", " + 
            "salary=" + salary +
        "]";
    }
}