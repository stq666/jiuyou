package com.drink.model;

import java.io.Serializable;

public class PublicRow implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public_row.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public_row.serialnumber
     *
     * @mbggenerated
     */
    private String serialnumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public_row.pserialnumber
     *
     * @mbggenerated
     */
    private String pserialnumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public_row.bord
     *
     * @mbggenerated
     */
    private Byte bord;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table public_row
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public_row.id
     *
     * @return the value of public_row.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public_row.id
     *
     * @param id the value for public_row.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public_row.serialnumber
     *
     * @return the value of public_row.serialnumber
     *
     * @mbggenerated
     */
    public String getSerialnumber() {
        return serialnumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public_row.serialnumber
     *
     * @param serialnumber the value for public_row.serialnumber
     *
     * @mbggenerated
     */
    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public_row.pserialnumber
     *
     * @return the value of public_row.pserialnumber
     *
     * @mbggenerated
     */
    public String getPserialnumber() {
        return pserialnumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public_row.pserialnumber
     *
     * @param pserialnumber the value for public_row.pserialnumber
     *
     * @mbggenerated
     */
    public void setPserialnumber(String pserialnumber) {
        this.pserialnumber = pserialnumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public_row.bord
     *
     * @return the value of public_row.bord
     *
     * @mbggenerated
     */
    public Byte getBord() {
        return bord;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public_row.bord
     *
     * @param bord the value for public_row.bord
     *
     * @mbggenerated
     */
    public void setBord(Byte bord) {
        this.bord = bord;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public_row
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PublicRow other = (PublicRow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSerialnumber() == null ? other.getSerialnumber() == null : this.getSerialnumber().equals(other.getSerialnumber()))
            && (this.getPserialnumber() == null ? other.getPserialnumber() == null : this.getPserialnumber().equals(other.getPserialnumber()))
            && (this.getBord() == null ? other.getBord() == null : this.getBord().equals(other.getBord()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public_row
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSerialnumber() == null) ? 0 : getSerialnumber().hashCode());
        result = prime * result + ((getPserialnumber() == null) ? 0 : getPserialnumber().hashCode());
        result = prime * result + ((getBord() == null) ? 0 : getBord().hashCode());
        return result;
    }
}