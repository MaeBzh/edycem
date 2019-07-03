package com.imie.edycem.entity;

import org.joda.time.format.ISODateTimeFormat;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import com.tactfactory.harmony.annotation.Column;
import com.tactfactory.harmony.annotation.Entity;
import com.tactfactory.harmony.annotation.GeneratedValue;
import com.tactfactory.harmony.annotation.Id;
import com.tactfactory.harmony.annotation.Table;

import org.joda.time.DateTime;

@Entity
@Table
public class User implements Serializable, Parcelable {

    /** Parent parcelable for parcellisation purposes. */
    protected List<Parcelable> parcelableParents;

    @Id
    @Column(type = Column.Type.INTEGER, hidden = true)
    @GeneratedValue(strategy = GeneratedValue.Strategy.MODE_IDENTITY)
    private int id;
    @Column(type = Column.Type.TEXT)
    private String idSmartphone;
    @Column(type = Column.Type.PASSWORD)
    private String password;
    @Column(type = Column.Type.DATETIME)
    private DateTime dateRgpd;


    /**
     * Default constructor.
     */
    public User() {

    }

     /**
     * Get the Id.
     * @return the id
     */
    public int getId() {
         return this.id;
    }
     /**
     * Set the Id.
     * @param value the id to set
     */
    public void setId(final int value) {
         this.id = value;
    }
     /**
     * Get the IdSmartphone.
     * @return the idSmartphone
     */
    public String getIdSmartphone() {
         return this.idSmartphone;
    }
     /**
     * Set the IdSmartphone.
     * @param value the idSmartphone to set
     */
    public void setIdSmartphone(final String value) {
         this.idSmartphone = value;
    }
     /**
     * Get the Password.
     * @return the password
     */
    public String getPassword() {
         return this.password;
    }
     /**
     * Set the Password.
     * @param value the password to set
     */
    public void setPassword(final String value) {
         this.password = value;
    }
     /**
     * Get the DateRgpd.
     * @return the dateRgpd
     */
    public DateTime getDateRgpd() {
         return this.dateRgpd;
    }
     /**
     * Set the DateRgpd.
     * @param value the dateRgpd to set
     */
    public void setDateRgpd(final DateTime value) {
         this.dateRgpd = value;
    }
    /**
     * This stub of code is regenerated. DO NOT MODIFY.
     * 
     * @param dest Destination parcel
     * @param flags flags
     */
    public void writeToParcelRegen(Parcel dest, int flags) {
        if (this.parcelableParents == null) {
            this.parcelableParents = new ArrayList<Parcelable>();
        }
        if (!this.parcelableParents.contains(this)) {
            this.parcelableParents.add(this);
        }
        dest.writeInt(this.getId());
        if (this.getIdSmartphone() != null) {
            dest.writeInt(1);
            dest.writeString(this.getIdSmartphone());
        } else {
            dest.writeInt(0);
        }
        if (this.getPassword() != null) {
            dest.writeInt(1);
            dest.writeString(this.getPassword());
        } else {
            dest.writeInt(0);
        }
        if (this.getDateRgpd() != null) {
            dest.writeInt(1);
            dest.writeString(ISODateTimeFormat.dateTime().print(
                    this.getDateRgpd()));
        } else {
            dest.writeInt(0);
        }
    }

    /**
     * Regenerated Parcel Constructor. 
     *
     * This stub of code is regenerated. DO NOT MODIFY THIS METHOD.
     *
     * @param parc The parcel to read from
     */
    public void readFromParcel(Parcel parc) {
        this.setId(parc.readInt());
        int idSmartphoneBool = parc.readInt();
        if (idSmartphoneBool == 1) {
            this.setIdSmartphone(parc.readString());
        }
        int passwordBool = parc.readInt();
        if (passwordBool == 1) {
            this.setPassword(parc.readString());
        }
        if (parc.readInt() == 1) {
            this.setDateRgpd(
                    ISODateTimeFormat.dateTimeParser()
                            .withOffsetParsed().parseDateTime(
                                    parc.readString()));
        }
    }

    /**
     * Parcel Constructor.
     *
     * @param parc The parcel to read from
     */
    public User(Parcel parc) {
        // You can chose not to use harmony's generated parcel.
        // To do this, remove this line.
        this.readFromParcel(parc);

        // You can  implement your own parcel mechanics here.

    }

    /* This method is not regenerated. You can implement your own parcel mechanics here. */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // You can chose not to use harmony's generated parcel.
        // To do this, remove this line.
        this.writeToParcelRegen(dest, flags);
        // You can  implement your own parcel mechanics here.
    }

    /**
     * Use this method to write this entity to a parcel from another entity. (Useful for relations).
     *
     * @param parent The entity being parcelled that need to parcel this one
     * @param dest The destination parcel
     * @param flags The flags
     */
    public synchronized void writeToParcel(List<Parcelable> parent, Parcel dest, int flags) {
        this.parcelableParents = new ArrayList<Parcelable>(parent);
        dest.writeParcelable(this, flags);
        this.parcelableParents = null;
    }

    @Override
    public int describeContents() {
        // This should return 0 
        // or CONTENTS_FILE_DESCRIPTOR if your entity is a FileDescriptor.
        return 0;
    }

    /**
     * Parcelable creator.
     */
    public static final Parcelable.Creator<User> CREATOR
        = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
