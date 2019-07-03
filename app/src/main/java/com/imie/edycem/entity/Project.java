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
import com.tactfactory.harmony.annotation.OneToMany;
import com.tactfactory.harmony.annotation.Table;

import org.joda.time.DateTime;

@Entity
@Table
public class Project implements Serializable, Parcelable {

    /** Parent parcelable for parcellisation purposes. */
    protected List<Parcelable> parcelableParents;

    @Id
    @Column(type = Column.Type.INTEGER, hidden = true)
    @GeneratedValue(strategy = GeneratedValue.Strategy.MODE_IDENTITY)
    private int id;
    @Column(type = Column.Type.TEXT)
    private String name;
    @Column(type = Column.Type.TEXT)
    private  String description;
    @Column(type = Column.Type.TEXT)
    private String company;
    @Column(type = Column.Type.TEXT)
    private String claimantName;
    @Column(type = Column.Type.TEXT, nullable = true)
    private String relevantSite;
    @Column(type = Column.Type.BOOLEAN, nullable = true)
    private boolean isEligibleCir;
    @Column(type = Column.Type.BOOLEAN, nullable = true)
    private boolean asPartOfPulpit;
    @Column(type = Column.Type.DATETIME, nullable = true)
    private DateTime deadline;
    @Column(type = Column.Type.TEXT, nullable = true)
    private String documents;
    @Column(type = Column.Type.TEXT, nullable = true)
    private String activityType;

    @OneToMany(targetEntity = "WorkingTime", mappedBy = "project")
    private ArrayList<WorkingTime> projectWorkingTimes;


    /**
     * Default constructor.
     */
    public Project() {

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
     * Get the Company.
     * @return the company
     */
    public String getCompany() {
         return this.company;
    }
     /**
     * Set the Company.
     * @param value the company to set
     */
    public void setCompany(final String value) {
         this.company = value;
    }
     /**
     * Get the ClaimantName.
     * @return the claimantName
     */
    public String getClaimantName() {
         return this.claimantName;
    }
     /**
     * Set the ClaimantName.
     * @param value the claimantName to set
     */
    public void setClaimantName(final String value) {
         this.claimantName = value;
    }
     /**
     * Get the RelevantSite.
     * @return the relevantSite
     */
    public String getRelevantSite() {
         return this.relevantSite;
    }
     /**
     * Set the RelevantSite.
     * @param value the relevantSite to set
     */
    public void setRelevantSite(final String value) {
         this.relevantSite = value;
    }
     /**
     * Get the IsEligibleCir.
     * @return the isEligibleCir
     */
    public boolean isIsEligibleCir() {
         return this.isEligibleCir;
    }
     /**
     * Set the IsEligibleCir.
     * @param value the isEligibleCir to set
     */
    public void setIsEligibleCir(final boolean value) {
         this.isEligibleCir = value;
    }
     /**
     * Get the AsPartOfPulpit.
     * @return the asPartOfPulpit
     */
    public boolean isAsPartOfPulpit() {
         return this.asPartOfPulpit;
    }
     /**
     * Set the AsPartOfPulpit.
     * @param value the asPartOfPulpit to set
     */
    public void setAsPartOfPulpit(final boolean value) {
         this.asPartOfPulpit = value;
    }
     /**
     * Get the Deadline.
     * @return the deadline
     */
    public DateTime getDeadline() {
         return this.deadline;
    }
     /**
     * Set the Deadline.
     * @param value the deadline to set
     */
    public void setDeadline(final DateTime value) {
         this.deadline = value;
    }
     /**
     * Get the Documents.
     * @return the documents
     */
    public String getDocuments() {
         return this.documents;
    }
     /**
     * Set the Documents.
     * @param value the documents to set
     */
    public void setDocuments(final String value) {
         this.documents = value;
    }
     /**
     * Get the ActivityType.
     * @return the activityType
     */
    public String getActivityType() {
         return this.activityType;
    }
     /**
     * Set the ActivityType.
     * @param value the activityType to set
     */
    public void setActivityType(final String value) {
         this.activityType = value;
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
        if (this.getDescription() != null) {
            dest.writeInt(1);
            dest.writeString(this.getDescription());
        } else {
            dest.writeInt(0);
        }
        if (this.getCompany() != null) {
            dest.writeInt(1);
            dest.writeString(this.getCompany());
        } else {
            dest.writeInt(0);
        }
        if (this.getClaimantName() != null) {
            dest.writeInt(1);
            dest.writeString(this.getClaimantName());
        } else {
            dest.writeInt(0);
        }
        if (this.getRelevantSite() != null) {
            dest.writeInt(1);
            dest.writeString(this.getRelevantSite());
        } else {
            dest.writeInt(0);
        }
        if (this.isIsEligibleCir()) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (this.isAsPartOfPulpit()) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (this.getDeadline() != null) {
            dest.writeInt(1);
            dest.writeString(ISODateTimeFormat.dateTime().print(
                    this.getDeadline()));
        } else {
            dest.writeInt(0);
        }
        if (this.getDocuments() != null) {
            dest.writeInt(1);
            dest.writeString(this.getDocuments());
        } else {
            dest.writeInt(0);
        }
        if (this.getActivityType() != null) {
            dest.writeInt(1);
            dest.writeString(this.getActivityType());
        } else {
            dest.writeInt(0);
        }

        if (this.getProjectWorkingTimes() != null) {
            dest.writeInt(this.getProjectWorkingTimes().size());
            for (WorkingTime item : this.getProjectWorkingTimes()) {
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
        int descriptionBool = parc.readInt();
        if (descriptionBool == 1) {
            this.setDescription(parc.readString());
        }
        int companyBool = parc.readInt();
        if (companyBool == 1) {
            this.setCompany(parc.readString());
        }
        int claimantNameBool = parc.readInt();
        if (claimantNameBool == 1) {
            this.setClaimantName(parc.readString());
        }
        int relevantSiteBool = parc.readInt();
        if (relevantSiteBool == 1) {
            this.setRelevantSite(parc.readString());
        }
        this.setIsEligibleCir(parc.readInt() == 1);
        this.setAsPartOfPulpit(parc.readInt() == 1);
        if (parc.readInt() == 1) {
            this.setDeadline(
                    ISODateTimeFormat.dateTimeParser()
                            .withOffsetParsed().parseDateTime(
                                    parc.readString()));
        }
        int documentsBool = parc.readInt();
        if (documentsBool == 1) {
            this.setDocuments(parc.readString());
        }
        int activityTypeBool = parc.readInt();
        if (activityTypeBool == 1) {
            this.setActivityType(parc.readString());
        }

        int nbProjectWorkingTimes = parc.readInt();
        if (nbProjectWorkingTimes > -1) {
            ArrayList<WorkingTime> items =
                new ArrayList<WorkingTime>();
            for (int i = 0; i < nbProjectWorkingTimes; i++) {
                items.add((WorkingTime) parc.readParcelable(
                        WorkingTime.class.getClassLoader()));
            }
            this.setProjectWorkingTimes(items);
        }
    }









    /**
     * Parcel Constructor.
     *
     * @param parc The parcel to read from
     */
    public Project(Parcel parc) {
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
    public static final Parcelable.Creator<Project> CREATOR
        = new Parcelable.Creator<Project>() {
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }
        
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

     /**
     * Get the ProjectWorkingTimes.
     * @return the projectWorkingTimes
     */
    public ArrayList<WorkingTime> getProjectWorkingTimes() {
         return this.projectWorkingTimes;
    }
     /**
     * Set the ProjectWorkingTimes.
     * @param value the projectWorkingTimes to set
     */
    public void setProjectWorkingTimes(final ArrayList<WorkingTime> value) {
         this.projectWorkingTimes = value;
    }
}
