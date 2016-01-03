package myexcel.ashish.com.myexcel.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class HomeActivityObject implements Parcelable {

    int id;
    String toolNumber;
    String date;
    String jwNumber, product, description, status, targetDate, startDate, doneBy, actualDateOfCompletion, cost, remarks;

    public HomeActivityObject(Parcel in) {
        id = in.readInt();
        toolNumber = in.readString();
        date = in.readString();
        jwNumber = in.readString();
        product = in.readString();
        description = in.readString();
        status = in.readString();
        targetDate = in.readString();
        startDate = in.readString();
        doneBy = in.readString();
        actualDateOfCompletion = in.readString();
        cost = in.readString();
        remarks = in.readString();
    }

    public HomeActivityObject() {

    }

    public static final Creator<HomeActivityObject> CREATOR = new Creator<HomeActivityObject>() {
        @Override
        public HomeActivityObject createFromParcel(Parcel in) {
            return new HomeActivityObject(in);
        }

        @Override
        public HomeActivityObject[] newArray(int size) {
            return new HomeActivityObject[size];
        }
    };

    public String getTargetDate() {
        return targetDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    public String getActualDateOfCompletion() {
        return actualDateOfCompletion;
    }

    public void setActualDateOfCompletion(String actualDateOfCompletion) {
        this.actualDateOfCompletion = actualDateOfCompletion;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToolNumber() {
        return toolNumber;
    }

    public void setToolNumber(String toolNumber) {
        this.toolNumber = toolNumber;
    }

    public String getJwNumber() {
        return jwNumber;
    }

    public void setJwNumber(String jwNumber) {
        this.jwNumber = jwNumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(toolNumber);
        dest.writeString(date);
        dest.writeString(jwNumber);
        dest.writeString(product);
        dest.writeString(description);
        dest.writeString(status);
        dest.writeString(targetDate);
        dest.writeString(startDate);
        dest.writeString(doneBy);
        dest.writeString(actualDateOfCompletion);
        dest.writeString(cost);
        dest.writeString(remarks);
    }
}
