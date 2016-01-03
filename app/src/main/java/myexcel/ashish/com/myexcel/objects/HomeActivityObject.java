package myexcel.ashish.com.myexcel.objects;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class HomeActivityObject {

    int id;
    String toolNumber;
    String jwNumber, product, description, status, targetDate, startDate, doneBy, actualDateOfCompletion, cost, remarks;

    public String getTargetDate() {
        return targetDate;
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
}
