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
import com.tactfactory.harmony.annotation.OneToMany;
import com.tactfactory.harmony.annotation.Table;

@Entity
@Table
public class Job implements Serializable, Parcelable {

    /** Parent parcelable for parcellisation purposes. */
    protected List<Parcelable> parcelableParents;

    @Id
    @Column(type = Column.Type.INTEGER, hidden = true)
    @GeneratedValue(strategy = GeneratedValue.Strategy.MODE_IDENTITY)
    private int id;
    @Column(type = Column.Type.TEXT)
    private String name;

    @OneToMany(targetEntity = "User", mappedBy = "job")
    private ArrayList<User> users;
    @OneToMany(targetEntity = "Project", mappedBy = "job")
    private ArrayList<Project> projects;

    /**
     * Default constructor.
     */
    public Job() {

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
        if (this.getName() != null) {
            dest.writeInt(1);
            dest.writeString(this.getName());
        } else {
            dest.writeInt(0);
        }

        if (this.getUsers() != null) {
            dest.writeInt(this.getUsers().size());
            for (User item : this.getUsers()) {
                if (!this.parcelableParents.contains(item)) {
                    item.writeToParcel(this.parcelableParents, dest, flags);
                } else {
                    dest.writeParcelable(null, flags);
                }
            }
        } else {
            dest.writeInt(-1);
        }

        if (this.getProjects() != null) {
            dest.writeInt(this.getProjects().size());
            for (Project item : this.getProjects()) {
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
        int nameBool = parc.readInt();
        if (nameBool == 1) {
            this.setName(parc.readString());
        }

        int nbUsers = parc.readInt();
        if (nbUsers > -1) {
            ArrayList<User> items =
                new ArrayList<User>();
            for (int i = 0; i < nbUsers; i++) {
                items.add((User) parc.readParcelable(
                        User.class.getClassLoader()));
            }
            this.setUsers(items);
        }

        int nbProjects = parc.readInt();
        if (nbProjects > -1) {
            ArrayList<Project> items =
                new ArrayList<Project>();
            for (int i = 0; i < nbProjects; i++) {
                items.add((Project) parc.readParcelable(
                        Project.class.getClassLoader()));
            }
            this.setProjects(items);
        }
    }











    /**
     * Parcel Constructor.
     *
     * @param parc The parcel to read from
     */
    public Job(Parcel parc) {
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
    public static final Parcelable.Creator<Job> CREATOR
        = new Parcelable.Creator<Job>() {
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }
        
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

     /**
     * Get the Users.
     * @return the users
     */
    public ArrayList<User> getUsers() {
         return this.users;
    }
     /**
     * Set the Users.
     * @param value the users to set
     */
    public void setUsers(final ArrayList<User> value) {
         this.users = value;
    }
     /**
     * Get the Projects.
     * @return the projects
     */
    public ArrayList<Project> getProjects() {
         return this.projects;
    }
     /**
     * Set the Projects.
     * @param value the projects to set
     */
    public void setProjects(final ArrayList<Project> value) {
         this.projects = value;
    }
}
