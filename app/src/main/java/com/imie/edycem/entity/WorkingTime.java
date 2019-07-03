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
public class WorkingTime implements Serializable, Parcelable {

    /** Parent parcelable for parcellisation purposes. */
    protected List<Parcelable> parcelableParents;

    @Id
    @Column(type = Column.Type.INTEGER, hidden = true)
    @GeneratedValue(strategy = GeneratedValue.Strategy.MODE_IDENTITY)
    private int id;
    @Column(type = Column.Type.DATE)
    private DateTime date;
    @Column(type = Column.Type.TEXT)
    private String spentTime;
    @Column(type = Column.Type.TEXT)
    private String description;

    /**
     * Default constructor.
     */
    public WorkingTime() {

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
     * Get the Date.
     * @return the date
     */
    public DateTime getDate() {
         return this.date;
    }
     /**
     * Set the Date.
     * @param value the date to set
     */
    public void setDate(final DateTime value) {
         this.date = value;
    }
     /**
     * Get the SpentTime.
     * @return the spentTime
     */
    public String getSpentTime() {
         return this.spentTime;
    }
     /**
     * Set the SpentTime.
     * @param value the spentTime to set
     */
    public void setSpentTime(final String value) {
         this.spentTime = value;
    }
     /**
     * Get the Description.
     * @return the description
     */
    public String getDescription() {
         return this.description;
    }
     /**
     * Set the Description.
     * @param value the description to set
     */
    public void setDescription(final String value) {
         this.description = value;
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
        if (this.getDate() != null) {
            dest.writeInt(1);
            dest.writeString(ISODateTimeFormat.dateTime().print(
                    this.getDate()));
        } else {
            dest.writeInt(0);
        }
        if (this.getSpentTime() != null) {
            dest.writeInt(1);
            dest.writeString(this.getSpentTime());
        } else {
            dest.writeInt(0);
        }
        if (this.getDescription() != null) {
            dest.writeInt(1);
            dest.writeString(this.getDescription());
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
        if (parc.readInt() == 1) {
            this.setDate(
                    ISODateTimeFormat.dateTimeParser()
                            .withOffsetParsed().parseDateTime(
                                    parc.readString()));
        }
        int spentTimeBool = parc.readInt();
        if (spentTimeBool == 1) {
            this.setSpentTime(parc.readString());
        }
        int descriptionBool = parc.readInt();
        if (descriptionBool == 1) {
            this.setDescription(parc.readString());
        }
    }

    /**
     * Parcel Constructor.
     *
     * @param parc The parcel to read from
     */
    public WorkingTime(Parcel parc) {
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
    public static final Parcelable.Creator<WorkingTime> CREATOR
        = new Parcelable.Creator<WorkingTime>() {
        public WorkingTime createFromParcel(Parcel in) {
            return new WorkingTime(in);
        }
        
        public WorkingTime[] newArray(int size) {
            return new WorkingTime[size];
        }
    };

}