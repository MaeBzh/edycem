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
import com.tactfactory.harmony.annotation.ManyToOne;
import com.tactfactory.harmony.annotation.OneToMany;
import com.tactfactory.harmony.annotation.Table;
import com.tactfactory.harmony.bundles.rest.annotation.Rest;
import com.tactfactory.harmony.bundles.rest.annotation.RestField;

import org.joda.time.DateTime;

@Rest
@Entity
@Table
public class User implements Serializable, Parcelable {

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
    private String firstname;
    @Column(type = Column.Type.TEXT)
    private String lastname;
    @Column(type = Column.Type.EMAIL)
    private String email;
    @Column(type = Column.Type.BOOLEAN)
    private boolean isEligible;
    @RestField(name = "smartphone_id")
    @Column(type = Column.Type.TEXT)
    private String idSmartphone;
    @RestField(name = "date_rgpd")
    @Column(type = Column.Type.DATETIME, nullable = true)
    private DateTime dateRgpd;
    @RestField(name = "api_token")
    @Column(type = Column.Type.STRING, nullable = true)
    private String token;

    @ManyToOne(targetEntity = "Job", inversedBy = "users")
    private Job job;
    @OneToMany(targetEntity = "WorkingTime", mappedBy = "user")
    private ArrayList<WorkingTime> userWorkingTimes;
    @OneToMany(targetEntity = "Project", mappedBy = "creator")
    private ArrayList<Project> createdProjects;


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
        dest.writeInt(this.getIdServer());
        if (this.getFirstname() != null) {
            dest.writeInt(1);
            dest.writeString(this.getFirstname());
        } else {
            dest.writeInt(0);
        }
        if (this.getLastname() != null) {
            dest.writeInt(1);
            dest.writeString(this.getLastname());
        } else {
            dest.writeInt(0);
        }
        if (this.getEmail() != null) {
            dest.writeInt(1);
            dest.writeString(this.getEmail());
        } else {
            dest.writeInt(0);
        }
        if (this.isIsEligible()) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (this.getIdSmartphone() != null) {
            dest.writeInt(1);
            dest.writeString(this.getIdSmartphone());
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
        if (this.getToken() != null) {
            dest.writeInt(1);
            dest.writeString(this.getToken());
        } else {
            dest.writeInt(0);
        }
        if (this.getJob() != null
                    && !this.parcelableParents.contains(this.getJob())) {
            this.getJob().writeToParcel(this.parcelableParents, dest, flags);
        } else {
            dest.writeParcelable(null, flags);
        }

        if (this.getUserWorkingTimes() != null) {
            dest.writeInt(this.getUserWorkingTimes().size());
            for (WorkingTime item : this.getUserWorkingTimes()) {
                if (!this.parcelableParents.contains(item)) {
                    item.writeToParcel(this.parcelableParents, dest, flags);
                } else {
                    dest.writeParcelable(null, flags);
                }
            }
        } else {
            dest.writeInt(-1);
        }

        if (this.getCreatedProjects() != null) {
            dest.writeInt(this.getCreatedProjects().size());
            for (Project item : this.getCreatedProjects()) {
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
        int firstnameBool = parc.readInt();
        if (firstnameBool == 1) {
            this.setFirstname(parc.readString());
        }
        int lastnameBool = parc.readInt();
        if (lastnameBool == 1) {
            this.setLastname(parc.readString());
        }
        int emailBool = parc.readInt();
        if (emailBool == 1) {
            this.setEmail(parc.readString());
        }
        this.setIsEligible(parc.readInt() == 1);
        int idSmartphoneBool = parc.readInt();
        if (idSmartphoneBool == 1) {
            this.setIdSmartphone(parc.readString());
        }
        if (parc.readInt() == 1) {
            this.setDateRgpd(
                    ISODateTimeFormat.dateTimeParser()
                            .withOffsetParsed().parseDateTime(
                                    parc.readString()));
        }
        int tokenBool = parc.readInt();
        if (tokenBool == 1) {
            this.setToken(parc.readString());
        }
        this.setJob((Job) parc.readParcelable(Job.class.getClassLoader()));

        int nbUserWorkingTimes = parc.readInt();
        if (nbUserWorkingTimes > -1) {
            ArrayList<WorkingTime> items =
                new ArrayList<WorkingTime>();
            for (int i = 0; i < nbUserWorkingTimes; i++) {
                items.add((WorkingTime) parc.readParcelable(
                        WorkingTime.class.getClassLoader()));
            }
            this.setUserWorkingTimes(items);
        }

        int nbCreatedProjects = parc.readInt();
        if (nbCreatedProjects > -1) {
            ArrayList<Project> items =
                new ArrayList<Project>();
            for (int i = 0; i < nbCreatedProjects; i++) {
                items.add((Project) parc.readParcelable(
                        Project.class.getClassLoader()));
            }
            this.setCreatedProjects(items);
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

     /**
     * Get the Job.
     * @return the job
     */
    public Job getJob() {
         return this.job;
    }
     /**
     * Set the Job.
     * @param value the job to set
     */
    public void setJob(final Job value) {
         this.job = value;
    }

     /**
     * Get the UserWorkingTimes.
     * @return the userWorkingTimes
     */
    public ArrayList<WorkingTime> getUserWorkingTimes() {
         return this.userWorkingTimes;
    }
     /**
     * Set the UserWorkingTimes.
     * @param value the userWorkingTimes to set
     */
    public void setUserWorkingTimes(final ArrayList<WorkingTime> value) {
         this.userWorkingTimes = value;
    }
     /**
     * Get the Firstname.
     * @return the firstname
     */
    public String getFirstname() {
         return this.firstname;
    }
     /**
     * Set the Firstname.
     * @param value the firstname to set
     */
    public void setFirstname(final String value) {
         this.firstname = value;
    }
     /**
     * Get the Lastname.
     * @return the lastname
     */
    public String getLastname() {
         return this.lastname;
    }
     /**
     * Set the Lastname.
     * @param value the lastname to set
     */
    public void setLastname(final String value) {
         this.lastname = value;
    }
     /**
     * Get the Email.
     * @return the email
     */
    public String getEmail() {
         return this.email;
    }
     /**
     * Set the Email.
     * @param value the email to set
     */
    public void setEmail(final String value) {
         this.email = value;
    }
     /**
     * Get the IsEligible.
     * @return the isEligible
     */
    public boolean isIsEligible() {
         return this.isEligible;
    }
     /**
     * Set the IsEligible.
     * @param value the isEligible to set
     */
    public void setIsEligible(final boolean value) {
         this.isEligible = value;
    }
     /**
     * Get the CreatedProjects.
     * @return the createdProjects
     */
    public ArrayList<Project> getCreatedProjects() {
         return this.createdProjects;
    }
     /**
     * Set the CreatedProjects.
     * @param value the createdProjects to set
     */
    public void setCreatedProjects(final ArrayList<Project> value) {
         this.createdProjects = value;
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
     * Get the Token.
     * @return the token
     */
    public String getToken() {
         return this.token;
    }
     /**
     * Set the Token.
     * @param value the token to set
     */
    public void setToken(final String value) {
         this.token = value;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", idServer=" + idServer +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", isEligible=" + isEligible +
                ", idSmartphone='" + idSmartphone + '\'' +
                ", dateRgpd=" + dateRgpd +
                ", token='" + token + '\'' +
                ", job=" + job +
                ", userWorkingTimes=" + userWorkingTimes +
                ", createdProjects=" + createdProjects +
                '}';
    }
}
