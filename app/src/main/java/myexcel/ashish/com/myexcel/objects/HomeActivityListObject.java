package myexcel.ashish.com.myexcel.objects;

import java.util.List;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class HomeActivityListObject {

    List<HomeActivityObject> works;
    Integer next_page;

    public Integer getNext_page() {
        return next_page;
    }

    public void setNext_page(Integer next_page) {
        this.next_page = next_page;
    }

    public List<HomeActivityObject> getWorks() {
        return works;
    }

    public void setWorks(List<HomeActivityObject> works) {
        this.works = works;
    }
}
