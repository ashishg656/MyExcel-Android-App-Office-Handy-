package myexcel.ashish.com.myexcel.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashish Goel on 1/4/2016.
 */
public class WorkDetailObject implements Parcelable {

    int id;
    String date, expectedDate, actualDate, trialDate, qcDate, actionTaken, status, cost, remarks;

    public WorkDetailObject(Parcel in) {
        id = in.readInt();
        date = in.readString();
        expectedDate = in.readString();
        actualDate = in.readString();
        trialDate = in.readString();
        qcDate = in.readString();
        actionTaken = in.readString();
        status = in.readString();
        cost = in.readString();
        remarks = in.readString();
    }

    public static final Creator<WorkDetailObject> CREATOR = new Creator<WorkDetailObject>() {
        @Override
        public WorkDetailObject createFromParcel(Parcel in) {
            return new WorkDetailObject(in);
        }

        @Override
        public WorkDetailObject[] newArray(int size) {
            return new WorkDetailObject[size];
        }
    };

    public WorkDetailObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(String trialDate) {
        this.trialDate = trialDate;
    }

    public String getQcDate() {
        return qcDate;
    }

    public void setQcDate(String qcDate) {
        this.qcDate = qcDate;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(expectedDate);
        dest.writeString(actualDate);
        dest.writeString(trialDate);
        dest.writeString(qcDate);
        dest.writeString(actionTaken);
        dest.writeString(status);
        dest.writeString(cost);
        dest.writeString(remarks);
    }
}
