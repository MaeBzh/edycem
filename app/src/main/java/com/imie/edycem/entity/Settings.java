package com.imie.edycem.entity;

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
import com.tactfactory.harmony.bundles.rest.annotation.Rest;
import com.tactfactory.harmony.bundles.rest.annotation.RestField;

@Rest
@Entity
@Table
public class Settings implements Serializable, Parcelable {

    /** Parent parcelable for parcellisation purposes. */
    protected List<Parcelable> parcelableParents;

    @Id
    @Column(type = Column.Type.INTEGER, hidden = true)
    @GeneratedValue(strategy = GeneratedValue.Strategy.MODE_IDENTITY)
    private int id;
    @RestField(name = "id")
    @Column(type = Column.Type.INTEGER, nullable = true)
    private int idServer;
    @Column(type = Column.Type.TEXT)
    private String rgpd;

    /**
     * Default constructor.
     */
    public Settings() {

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
     * Get the Rgpd.
     * @return the rgpd
     */
    public String getRgpd() {
         return this.rgpd;
    }
     /**
     * Set the Rgpd.
     * @param value the rgpd to set
     */
    public void setRgpd(final String value) {
         this.rgpd = value;
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
        dest.writeInt(this.getIdServer());
        if (this.getRgpd() != null) {
            dest.writeInt(1);
            dest.writeString(this.getRgpd());
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
        this.setIdServer(parc.readInt());
        int rgpdBool = parc.readInt();
        if (rgpdBool == 1) {
            this.setRgpd(parc.readString());
        }
    }

















    /**
     * Parcel Constructor.
     *
     * @param parc The parcel to read from
     */
    public Settings(Parcel parc) {
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
    public static final Parcelable.Creator<Settings> CREATOR
        = new Parcelable.Creator<Settings>() {
        public Settings createFromParcel(Parcel in) {
            return new Settings(in);
        }
        
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

     /**
     * Get the IdServer.
     * @return the idServer
     */
    public int getIdServer() {
         return this.idServer;
    }
     /**
     * Set the IdServer.
     * @param value the idServer to set
     */
    public void setIdServer(final int value) {
         this.idServer = value;
    }
}
