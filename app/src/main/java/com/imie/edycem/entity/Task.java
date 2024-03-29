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
import com.tactfactory.harmony.annotation.ManyToOne;
import com.tactfactory.harmony.annotation.OneToMany;
import com.tactfactory.harmony.annotation.Table;
import com.tactfactory.harmony.bundles.rest.annotation.Rest;
import com.tactfactory.harmony.bundles.rest.annotation.RestField;

@Rest
@Entity
@Table
public class Task implements Serializable, Parcelable {

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
    private String name;
    @Column(type = Column.Type.INTEGER)
    private int defaultTime;
    
    @ManyToOne(targetEntity = "Activity", inversedBy = "tasks")
    private Activity activity;
    @OneToMany(targetEntity = "WorkingTime", mappedBy = "task")
    private ArrayList<WorkingTime> taskWorkingTimes;

    /**
     * Default constructor.
     */
    public Task() {

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
     * Get the Name.
     * @return the name
     */
    public String getName() {
         return this.name;
    }
     /**
     * Set the Name.
     * @param value the name to set
     */
    public void setName(final String value) {
         this.name = value;
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
        if (this.getName() != null) {
            dest.writeInt(1);
            dest.writeString(this.getName());
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.getDefaultTime());
        if (this.getActivity() != null
                    && !this.parcelableParents.contains(this.getActivity())) {
            this.getActivity().writeToParcel(this.parcelableParents, dest, flags);
        } else {
            dest.writeParcelable(null, flags);
        }

        if (this.getTaskWorkingTimes() != null) {
            dest.writeInt(this.getTaskWorkingTimes().size());
            for (WorkingTime item : this.getTaskWorkingTimes()) {
                if (!this.parcelableParents.contains(item)) {
                    item.writeToParcel(this.parcelableParents, dest, flags);
                } else {
                    dest.writeParcelable(null, flags);
                }
            }
        } else {
            dest.writeInt(-1);
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
        int nameBool = parc.readInt();
        if (nameBool == 1) {
            this.setName(parc.readString());
        }
        this.setDefaultTime(parc.readInt());
        this.setActivity((Activity) parc.readParcelable(Activity.class.getClassLoader()));

        int nbTaskWorkingTimes = parc.readInt();
        if (nbTaskWorkingTimes > -1) {
            ArrayList<WorkingTime> items =
                new ArrayList<WorkingTime>();
            for (int i = 0; i < nbTaskWorkingTimes; i++) {
                items.add((WorkingTime) parc.readParcelable(
                        WorkingTime.class.getClassLoader()));
            }
            this.setTaskWorkingTimes(items);
        }
    }













    /**
     * Parcel Constructor.
     *
     * @param parc The parcel to read from
     */
    public Task(Parcel parc) {
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
    public static final Parcelable.Creator<Task> CREATOR
        = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }
        
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

     /**
     * Get the Activity.
     * @return the activity
     */
    public Activity getActivity() {
         return this.activity;
    }
     /**
     * Set the Activity.
     * @param value the activity to set
     */
    public void setActivity(final Activity value) {
         this.activity = value;
    }
     /**
     * Get the TaskWorkingTimes.
     * @return the taskWorkingTimes
     */
    public ArrayList<WorkingTime> getTaskWorkingTimes() {
         return this.taskWorkingTimes;
    }
     /**
     * Set the TaskWorkingTimes.
     * @param value the taskWorkingTimes to set
     */
    public void setTaskWorkingTimes(final ArrayList<WorkingTime> value) {
         this.taskWorkingTimes = value;
    }
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
     /**
     * Get the DefaultTime.
     * @return the defaultTime
     */
    public int getDefaultTime() {
         return this.defaultTime;
    }
     /**
     * Set the DefaultTime.
     * @param value the defaultTime to set
     */
    public void setDefaultTime(final int value) {
         this.defaultTime = value;
    }
}
